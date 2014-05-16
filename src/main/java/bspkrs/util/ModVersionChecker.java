package bspkrs.util;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.minecraft.util.StatCollector;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.util.config.ConfigCategory;
import bspkrs.util.config.Configuration;
import bspkrs.util.config.Property;

import com.google.common.collect.Ordering;

/**
 * This class retrieves a version string from a text file at a given URL and compares it against the locally provided version string. It
 * uses a natural order comparator to determine if the remote version is newer than our local version.
 * 
 * @author bspkrs
 */

public class ModVersionChecker
{
    private static final Map<String, ModVersionChecker> versionCheckerMap = new HashMap<String, ModVersionChecker>();
    private URL                                         versionURL;
    private final String                                modID;
    private String                                      newVersion;
    private final String                                currentVersion;
    private String                                      updateURL;
    private String[]                                    loadMsg;
    private String[]                                    inGameMsg;
    private File                                        trackerFile;
    private File                                        trackerDir;
    private static Configuration                        versionCheckTracker;
    private final String                                lastNewVersionFound;
    private final String                                CHECK_ERROR       = "check_error";
    private final boolean                               errorDetected;
    private final int                                   runsSinceLastMessage;
    
    public ModVersionChecker(String modID, String curVer, String versionURL, String updateURL, String[] loadMsg, String[] inGameMsg)
    {
        this(modID, curVer, versionURL, updateURL, loadMsg, inGameMsg, bspkrsCoreMod.instance.updateTimeoutMilliseconds);
    }
    
    public ModVersionChecker(String modID, String curVer, String versionURL, String updateURL, String[] loadMsg, String[] inGameMsg, int timeoutMS)
    {
        this.modID = modID;
        this.currentVersion = curVer;
        this.updateURL = updateURL;
        this.loadMsg = loadMsg;
        this.inGameMsg = inGameMsg;
        
        try
        {
            this.versionURL = new URL(versionURL);
            BSLog.info("Initializing ModVersionChecker for mod %s", modID);
        }
        catch (Throwable e)
        {
            BSLog.warning("Error initializing ModVersionChecker for mod %s: %s", modID, e.getMessage());
        }
        
        String[] versionLines = CommonUtils.loadTextFromURL(this.versionURL, BSLog.INSTANCE.getLogger(), new String[] { CHECK_ERROR }, timeoutMS);
        
        newVersion = versionLines[0].trim();
        
        // Keep track of the versions we've seen to keep from nagging players with new version notifications beyond the first one
        if (trackerDir == null)
        {
            trackerDir = new File(CommonUtils.getConfigDir() + "/bspkrsCore/");
            if (trackerDir.exists())
            {
                trackerFile = new File(trackerDir, "ModVersionCheckerTracking.txt");
                trackerFile.delete();
                trackerDir.delete();
            }
            
            trackerDir = new File(CommonUtils.getConfigDir());
            trackerFile = new File(trackerDir, "bspkrs_ModVersionCheckerTracking.txt");
        }
        
        if (versionCheckTracker == null)
            versionCheckTracker = new Configuration(trackerFile);
        
        versionCheckTracker.load();
        ConfigCategory cc = versionCheckTracker.getCategory("version_check_tracker");
        
        if (!cc.containsKey(modID))
            versionCheckTracker.get("version_check_tracker", modID, curVer);
        
        Property count = versionCheckTracker.get("version_check_tracker.runs_since_last_message", modID, 0);
        runsSinceLastMessage = count.getInt() % 10;
        count.set(runsSinceLastMessage + 1);
        
        if (!newVersion.equals(CHECK_ERROR) && isCurrentVersion(curVer, newVersion))
            lastNewVersionFound = newVersion;
        else
            lastNewVersionFound = cc.get(modID).getString();
        
        if (!newVersion.equals(CHECK_ERROR))
        {
            cc.get(modID).set(newVersion);
            errorDetected = false;
        }
        else
        {
            newVersion = lastNewVersionFound;
            errorDetected = true;
        }
        
        versionCheckTracker.save();
        
        // Override instantiated updateURL with second line of version file if
        // it exists and is non-blank
        if (versionLines.length > 1 && versionLines[1].trim().length() != 0)
            this.updateURL = versionLines[1];
        
        setLoadMessage(loadMsg);
        setInGameMessage(inGameMsg);
        
        versionCheckerMap.put(modID.toLowerCase(Locale.US), this);
    }
    
    public ModVersionChecker(String modName, String oldVer, String versionURL, String updateURL)
    {
        this(modName, oldVer, versionURL, updateURL,
                new String[] { "{modID} {oldVer} is out of date! Visit {updateURL} to download the latest release ({newVer})." },
                new String[] { "\247c{modID} {newVer} \247ris out! Download the latest from \247a{updateURL}\247r" });
    }
    
    public void checkVersionWithLogging()
    {
        if (!isCurrentVersion(currentVersion, newVersion))
            for (String msg : loadMsg)
                BSLog.info(msg);
    }
    
    public void setLoadMessage(String[] loadMsg)
    {
        this.loadMsg = loadMsg;
        
        for (int i = 0; i < this.loadMsg.length; i++)
            this.loadMsg[i] = replaceAllTags(this.loadMsg[i]);
    }
    
    public void setInGameMessage(String[] inGameMsg)
    {
        this.inGameMsg = inGameMsg;
        
        for (int i = 0; i < this.inGameMsg.length; i++)
            this.inGameMsg[i] = replaceAllTags(this.inGameMsg[i]);
        
    }
    
    public String[] getLoadMessage()
    {
        return loadMsg;
    }
    
    public String[] getInGameMessage()
    {
        return inGameMsg;
    }
    
    public URL getVersionURL()
    {
        return versionURL;
    }
    
    public String getNewVersion()
    {
        return newVersion;
    }
    
    public String getCurrentVersion()
    {
        return currentVersion;
    }
    
    public String getUpdateURL()
    {
        return updateURL;
    }
    
    public static Map<String, ModVersionChecker> getVersionCheckerMap()
    {
        return versionCheckerMap;
    }
    
    public boolean isCurrentVersion()
    {
        return isCurrentVersion(runsSinceLastMessage == 0 ? currentVersion : lastNewVersionFound, newVersion);
    }
    
    public static boolean isCurrentVersion(String oldVer, String newVer)
    {
        return Ordering.natural().compare(oldVer, newVer) >= 0;
    }
    
    private String replaceAllTags(String s)
    {
        return s.replace("{oldVer}", currentVersion).replace("{newVer}", newVersion).replace("{modID}", modID).replace("{updateURL}", updateURL);
    }
    
    public static String[] checkVersionForMod(String modID)
    {
        String[] r = { "" };
        
        if (versionCheckerMap.containsKey(modID.toLowerCase(Locale.US)))
        {
            ModVersionChecker versionChecker = versionCheckerMap.get(modID.toLowerCase(Locale.US));
            if (!versionChecker.errorDetected)
            {
                if (!isCurrentVersion(versionChecker.currentVersion, versionChecker.newVersion))
                    r = versionChecker.getInGameMessage();
                else
                    r = new String[] { StatCollector.translateToLocalFormatted("bspkrs.modversionchecker.uptodate", versionChecker.modID) };
            }
            else
                r = new String[] { StatCollector.translateToLocalFormatted("bspkrs.modversionchecker.error", versionChecker.modID) };
        }
        else
            r = new String[] { StatCollector.translateToLocal("bspkrs.modversionchecker.invalidmodid") };
        
        return r;
    }
}
