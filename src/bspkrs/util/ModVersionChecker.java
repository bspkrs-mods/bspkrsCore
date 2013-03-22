package bspkrs.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModVersionChecker
{
    private final Logger logger;
    private URL          versionURL;
    private final String modName;
    private final String newVer;
    private final String oldVer;
    private String       updateURL;
    private String[]     loadMsg;
    private String[]     inGameMsg;
    
    public ModVersionChecker(String modName, String oldVer, String versionURL, String updateURL, String[] loadMsg, String[] inGameMsg, Logger logger)
    {
        this.modName = modName;
        this.oldVer = oldVer;
        this.updateURL = updateURL;
        this.logger = logger;
        this.loadMsg = loadMsg;
        this.inGameMsg = inGameMsg;
        
        try
        {
            this.versionURL = new URL(versionURL);
        }
        catch (Throwable e)
        {
            logger.log(Level.WARNING, "Error initializing ModVersionChecker: " + e.getMessage());
        }
        
        String[] versionLines = loadTextFromURL(this.versionURL);
        
        newVer = versionLines[0].trim();
        
        // Override instantiated updateURL with second line of version file if
        // it exists and is non-blank
        if (versionLines.length > 1 && !versionLines[1].trim().equals(""))
            this.updateURL = versionLines[1];
        
        setLoadMessage(loadMsg);
        setInGameMessage(inGameMsg);
    }
    
    public ModVersionChecker(String modName, String oldVer, String versionURL, String updateURL, Logger logger)
    {
        this(modName, oldVer, versionURL, updateURL, new String[] { "{modName} {oldVer} is out of date! Visit {updateURL} to download the latest release ({newVer})." }, new String[] { "\247c{modName} {newVer} \247ris out! Download the latest from \247a{updateURL}\247r" }, logger);
    }
    
    public void checkVersionWithLogging()
    {
        if (!isCurrentVersion())
            for (String msg : loadMsg)
                logger.log(Level.INFO, msg);
    }
    
    public void checkVersionWithLoggingBySubStringAsFloat(int beginIndex, int endIndex)
    {
        if (!isCurrentVersionBySubStringAsFloatNewer(beginIndex, endIndex))
            for (String msg : loadMsg)
                logger.log(Level.INFO, msg);
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
        return newVer.equalsIgnoreCase(oldVer);
    }
    
    public boolean isCurrentVersionBySubStringAsFloatNewer(int beginIndex, int endIndex)
    {
        return Float.valueOf(newVer.substring(beginIndex, endIndex)) <= Float.valueOf(oldVer.substring(beginIndex, endIndex));
    }
    
    private String replaceAllTags(String s)
    {
        return s.replace("{oldVer}", oldVer).replace("{newVer}", newVer).replace("{modName}", modName).replace("{updateURL}", updateURL);
    }
    
    private String[] loadTextFromURL(URL url)
    {
        ArrayList arraylist = new ArrayList();
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(url.openStream(), "UTF-8");
        }
        catch (Throwable e)
        {
            logger.log(Level.WARNING, "Error getting current version info: " + e.getMessage());
            //e.printStackTrace();
            return new String[] { oldVer };
        }
        
        while (scanner.hasNextLine())
        {
            arraylist.add(scanner.nextLine());
        }
        scanner.close();
        return (String[]) arraylist.toArray(new String[arraylist.size()]);
    }
}
