package bspkrs.util;
/*
import java.net.*;
import java.io.*;
import java.util.prefs.*;
import bspkrs.bspkrscore.fml.*;
import com.google.common.collect.*;
import net.minecraft.util.text.translation.I18n;

import java.util.*;

@SuppressWarnings("deprecation")
*/
public class ModVersionChecker
{
    /*
     * private static final Map<String, ModVersionChecker> versionCheckerMap;
     * private URL versionURL;
     * private final String modID;
     * private String newVersion;
     * private final String currentVersion;
     * private String updateURL;
     * private String[] loadMsg;
     * private String[] inGameMsg;
     * private File trackerFile;
     * private File trackerDir;
     * private static Preferences versionCheckTracker;
     * private String lastNewVersionFound;
     * @SuppressWarnings("unused")
     * private final String CHECK_ERROR = "check_error";
     * private final boolean errorDetected;
     * private int runsSinceLastMessage;
     * public ModVersionChecker(final String modID, final String curVer, final String versionURL, final String updateURL, final String[] loadMsg, final String[] inGameMsg)
     * {
     * this(modID, curVer, versionURL, updateURL, loadMsg, inGameMsg, bspkrsCoreMod.instance.updateTimeoutMilliseconds);
     * }
     * public ModVersionChecker(final String modID, final String curVer, String versionURL, final String updateURL, final String[] loadMsg, final String[] inGameMsg, final int timeoutMS)
     * {
     * this.modID = modID;
     * this.currentVersion = curVer;
     * this.updateURL = updateURL;
     * this.loadMsg = loadMsg;
     * this.inGameMsg = inGameMsg;
     * try
     * {
     * if(versionURL.startsWith("http://dl.dropboxusercontent.com"))
     * {
     * versionURL = versionURL.replaceFirst("http", "https");
     * }
     * this.versionURL = new URL(versionURL);
     * BSLog.info("Initializing ModVersionChecker for mod %s", modID);
     * }
     * catch(Throwable e)
     * {
     * BSLog.warning("Error initializing ModVersionChecker for mod %s: %s", modID, e.getMessage());
     * }
     * final String[] versionLines = CommonUtils.loadTextFromURL(this.versionURL, BSLog.INSTANCE.getLogger(), new String[] {"check_error"}, timeoutMS);
     * if(versionLines.length == 0 || versionLines[0].trim().equals("<html>"))
     * {
     * this.newVersion = "check_error";
     * }
     * else
     * {
     * this.newVersion = versionLines[0].trim();
     * }
     * this.errorDetected = this.newVersion.equals("check_error");
     * if(this.trackerDir == null)
     * {
     * this.trackerDir = new File(CommonUtils.getConfigDir() + "/bspkrsCore/");
     * if(this.trackerDir.exists())
     * {
     * (this.trackerFile = new File(this.trackerDir, "ModVersionCheckerTracking.txt")).delete();
     * this.trackerDir.delete();
     * }
     * this.trackerDir = new File(CommonUtils.getConfigDir());
     * this.trackerFile = new File(this.trackerDir, "bspkrs_ModVersionCheckerTracking.txt");
     * if(this.trackerFile.exists())
     * {
     * this.trackerFile.delete();
     * }
     * }
     * if(ModVersionChecker.versionCheckTracker == null)
     * {
     * ModVersionChecker.versionCheckTracker = Preferences.userNodeForPackage(this.getClass()).node("modversiontracker" + Const.MCVERSION);
     * }
     * if(!("@" + "MOD_VERSION@").equals(this.currentVersion) && !"${mod_version}".equals(this.currentVersion))
     * {
     * this.lastNewVersionFound = ModVersionChecker.versionCheckTracker.get(modID, this.currentVersion);
     * if(this.lastNewVersionFound.equals("<html>"))
     * {
     * this.lastNewVersionFound = this.currentVersion;
     * }
     * this.runsSinceLastMessage = ModVersionChecker.versionCheckTracker.node("runs_since_last_message").getInt(modID, 0);
     * if(this.errorDetected)
     * {
     * this.newVersion = this.lastNewVersionFound;
     * }
     * if(!this.errorDetected && !isCurrentVersion(this.lastNewVersionFound, this.newVersion))
     * {
     * this.runsSinceLastMessage = 0;
     * this.lastNewVersionFound = this.newVersion;
     * }
     * else
     * {
     * this.runsSinceLastMessage %= 10;
     * }
     * ModVersionChecker.versionCheckTracker.node("runs_since_last_message").putInt(modID, this.runsSinceLastMessage + 1);
     * ModVersionChecker.versionCheckTracker.put(modID, this.lastNewVersionFound);
     * }
     * if(versionLines.length > 1 && versionLines[1].trim().length() != 0)
     * {
     * this.updateURL = versionLines[1];
     * }
     * this.setLoadMessage(loadMsg);
     * this.setInGameMessage(inGameMsg);
     * ModVersionChecker.versionCheckerMap.put(modID.toLowerCase(Locale.US), this);
     * }
     * public ModVersionChecker(final String modName, final String oldVer, final String versionURL, final String updateURL)
     * {
     * this(modName, oldVer, versionURL, updateURL, new String[] {"{modID} {oldVer} is out of date! Visit {updateURL} to download the latest release ({newVer})."}, new String[]
     * {"�c{modID} {newVer} �ris out! Download the latest from �a{updateURL}�r"});
     * }
     * public void checkVersionWithLogging()
     * {
     * if(!isCurrentVersion(this.currentVersion, this.newVersion))
     * {
     * for(final String msg : this.loadMsg)
     * {
     * BSLog.info(msg, new Object[0]);
     * }
     * }
     * }
     * public void setLoadMessage(final String[] loadMsg)
     * {
     * this.loadMsg = loadMsg;
     * for(int i = 0; i < this.loadMsg.length; ++i)
     * {
     * this.loadMsg[i] = this.replaceAllTags(this.loadMsg[i]);
     * }
     * }
     * public void setInGameMessage(final String[] inGameMsg)
     * {
     * this.inGameMsg = inGameMsg;
     * for(int i = 0; i < this.inGameMsg.length; ++i)
     * {
     * this.inGameMsg[i] = this.replaceAllTags(this.inGameMsg[i]);
     * }
     * }
     * public String[] getLoadMessage()
     * {
     * return this.loadMsg;
     * }
     * public String[] getInGameMessage()
     * {
     * return this.inGameMsg;
     * }
     * public URL getVersionURL()
     * {
     * return this.versionURL;
     * }
     * public String getNewVersion()
     * {
     * return this.newVersion;
     * }
     * public String getCurrentVersion()
     * {
     * return this.currentVersion;
     * }
     * public String getUpdateURL()
     * {
     * return this.updateURL;
     * }
     * public static Map<String, ModVersionChecker> getVersionCheckerMap()
     * {
     * return ModVersionChecker.versionCheckerMap;
     * }
     * public boolean isCurrentVersion()
     * {
     * return this.errorDetected || isCurrentVersion((this.runsSinceLastMessage == 0) ? this.currentVersion : this.lastNewVersionFound, this.newVersion);
     * }
     * public static boolean isCurrentVersion(final String oldVer, final String newVer)
     * {
     * return Ordering.natural().compare((Comparable<String>)oldVer, (Comparable<String>)newVer) >= 0;
     * }
     * private String replaceAllTags(final String s)
     * {
     * return s.replace("{oldVer}", this.currentVersion).replace("{newVer}", this.newVersion).replace("{modID}", this.modID).replace("{updateURL}", this.updateURL);
     * }
     * public static String[] checkVersionForMod(final String modID)
     * {
     * String[] r = {""};
     * if(ModVersionChecker.versionCheckerMap.containsKey(modID.toLowerCase(Locale.US)))
     * {
     * final ModVersionChecker versionChecker = ModVersionChecker.versionCheckerMap.get(modID.toLowerCase(Locale.US));
     * if(!versionChecker.errorDetected)
     * {
     * if(!isCurrentVersion(versionChecker.currentVersion, versionChecker.newVersion))
     * {
     * r = versionChecker.getInGameMessage();
     * }
     * else
     * {
     * r = new String[] {I18n.translateToLocalFormatted("bspkrs.modversionchecker.uptodate", new Object[] {versionChecker.modID})};
     * }
     * }
     * else
     * {
     * r = new String[] {I18n.translateToLocalFormatted("bspkrs.modversionchecker.error", new Object[] {versionChecker.modID})};
     * }
     * }
     * else
     * {
     * r = new String[] {I18n.translateToLocal("bspkrs.modversionchecker.invalidmodid")};
     * }
     * return r;
     * }
     * static
     * {
     * versionCheckerMap = new HashMap<String, ModVersionChecker>();
     * }
     */
}
