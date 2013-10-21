package bspkrs.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.src.mod_bspkrsCore;

public class ModVersionChecker
{
    private URL                  versionURL;
    private final String         modName;
    private final String         newVer;
    private final String         oldVer;
    private String               updateURL;
    private String[]             loadMsg;
    private String[]             inGameMsg;
    private File                 trackerFile;
    private File                 trackerDir;
    private static Configuration versionCheckTracker;
    private final String         lastNewVersionFound;
    
    @Deprecated
    public ModVersionChecker(String modName, String oldVer, String versionURL, String updateURL, String[] loadMsg, String[] inGameMsg, Logger logger)
    {
        this(modName, oldVer, versionURL, updateURL, loadMsg, inGameMsg);
    }
    
    public ModVersionChecker(String modName, String oldVer, String versionURL, String updateURL, String[] loadMsg, String[] inGameMsg)
    {
        this(modName, oldVer, versionURL, updateURL, loadMsg, inGameMsg, mod_bspkrsCore.updateTimeoutMilliseconds);
    }
    
    public ModVersionChecker(String modName, String oldVer, String versionURL, String updateURL, String[] loadMsg, String[] inGameMsg, int timeoutMS)
    {
        this.modName = modName;
        this.oldVer = oldVer;
        this.updateURL = updateURL;
        this.loadMsg = loadMsg;
        this.inGameMsg = inGameMsg;
        
        try
        {
            this.versionURL = new URL(versionURL);
            BSLog.info("Initializing ModVersionChecker for mod %s", modName);
        }
        catch (Throwable e)
        {
            BSLog.warning("Error initializing ModVersionChecker for mod %s: %s", modName, e.getMessage());
        }
        
        String[] versionLines = CommonUtils.loadTextFromURL(this.versionURL, BSLog.INSTANCE.getLogger(), new String[] { oldVer }, timeoutMS);
        
        newVer = versionLines[0].trim();
        
        // Keep track of the versions we've seen to keep from nagging players with new version notifications beyond the first one
        if (trackerDir == null)
        {
            trackerDir = new File(CommonUtils.getConfigDir() + "/bspkrsCore/");
            if (trackerDir.exists() || trackerDir.mkdirs())
                trackerFile = new File(trackerDir, "ModVersionCheckerTracking.txt");
        }
        
        if (versionCheckTracker == null)
            versionCheckTracker = new Configuration(trackerFile);
        
        versionCheckTracker.load();
        ConfigCategory cc = versionCheckTracker.getCategory("version_check_tracker");
        
        if (!cc.containsKey(modName))
            versionCheckTracker.get("version_check_tracker", modName, oldVer);
        
        lastNewVersionFound = cc.get(modName).getString();
        
        if (!isCurrentVersion(lastNewVersionFound, newVer))
            cc.get(modName).set(newVer);
        
        versionCheckTracker.save();
        
        // Override instantiated updateURL with second line of version file if
        // it exists and is non-blank
        if (versionLines.length > 1 && versionLines[1].trim().length() != 0)
            this.updateURL = versionLines[1];
        
        setLoadMessage(loadMsg);
        setInGameMessage(inGameMsg);
    }
    
    @Deprecated
    public ModVersionChecker(String modName, String oldVer, String versionURL, String updateURL, Logger logger)
    {
        this(modName, oldVer, versionURL, updateURL);
    }
    
    public ModVersionChecker(String modName, String oldVer, String versionURL, String updateURL)
    {
        this(modName, oldVer, versionURL, updateURL, new String[] { "{modName} {oldVer} is out of date! Visit {updateURL} to download the latest release ({newVer})." }, new String[] { "\247c{modName} {newVer} \247ris out! Download the latest from \247a{updateURL}\247r" });
    }
    
    public void checkVersionWithLogging()
    {
        if (!isCurrentVersion(oldVer, newVer))
            for (String msg : loadMsg)
                BSLog.info(msg);
    }
    
    @Deprecated
    public void checkVersionWithLoggingBySubStringAsFloat(int beginIndex, int endIndex)
    {
        if (!isCurrentVersion(oldVer, newVer))
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
    
    public boolean isCurrentVersion()
    {
        return isCurrentVersion(lastNewVersionFound, newVer);
    }
    
    public static boolean isCurrentVersion(String oldVer, String newVer)
    {
        List<String> list = new ArrayList<String>();
        list.add(oldVer);
        list.add(newVer);
        Collections.sort(list, new NaturalOrderComparator());
        
        return list.get(1).equals(oldVer);
    }
    
    @Deprecated
    public boolean isCurrentVersionBySubStringAsFloatNewer(int beginIndex, int endIndex)
    {
        return isCurrentVersion();
    }
    
    private String replaceAllTags(String s)
    {
        return s.replace("{oldVer}", oldVer).replace("{newVer}", newVer).replace("{modName}", modName).replace("{updateURL}", updateURL);
    }
}
