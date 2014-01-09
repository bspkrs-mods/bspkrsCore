package bspkrs.helpers.client.settings;

import net.minecraft.client.settings.KeyBinding;

public class KeyBindingHelper
{
    public static int getKeyCode(KeyBinding kb)
    {
        return kb.func_151463_i();
    }
}
