package bspkrs.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;

public class ModVersionChecker
{
    private URL          versionURL;
    private final String modName;
    private final String newVer;
    private final String oldVer;
    private String       updateURL;
    private String[]     loadMsg;
    private String[]     inGameMsg;
    
    @Deprecated
    public ModVersionChecker(String modName, String oldVer, String versionURL, String updateURL, String[] loadMsg, String[] inGameMsg, Logger logger)
    {
        this(modName, oldVer, versionURL, updateURL, loadMsg, inGameMsg);
    }
    
    public ModVersionChecker(String modName, String oldVer, String versionURL, String updateURL, String[] loadMsg, String[] inGameMsg)
    {
        this(modName, oldVer, versionURL, updateURL, loadMsg, inGameMsg, bspkrsCoreMod.instance.updateTimeoutMilliseconds);
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
        if (!isCurrentVersion())
            for (String msg : loadMsg)
                BSLog.info(msg);
    }
    
    @Deprecated
    public void checkVersionWithLoggingBySubStringAsFloat(int beginIndex, int endIndex)
    {
        if (!isCurrentVersionBySubStringAsFloatNewer(beginIndex, endIndex))
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
