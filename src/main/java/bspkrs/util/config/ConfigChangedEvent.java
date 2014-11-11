package bspkrs.util.config;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.Event.HasResult;

@HasResult
@Deprecated
public class ConfigChangedEvent extends Event
{
    /**
     * The Mod ID of the mod whose configuration just changed.
     */
    public final String  modID;
    /**
     * Whether or not properties with isHotLoadable() == false were allowed to be modified.
     */
    public final boolean allowNonHotLoadConfigChanges;

    public ConfigChangedEvent(String modID, boolean allowNonHotLoadConfigChanges)
    {
        this.modID = modID;
        this.allowNonHotLoadConfigChanges = allowNonHotLoadConfigChanges;
    }

    public static class OnConfigChangedEvent extends ConfigChangedEvent
    {
        public OnConfigChangedEvent(String modID, boolean allowNonHotLoadConfigChanges)
        {
            super(modID, allowNonHotLoadConfigChanges);
        }
    }

    public static class PostConfigChangedEvent extends ConfigChangedEvent
    {
        public PostConfigChangedEvent(String modID, boolean allowNonHotLoadConfigChanges)
        {
            super(modID, allowNonHotLoadConfigChanges);
        }
    }
}
