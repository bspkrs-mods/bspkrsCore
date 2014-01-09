/**
 * Copyright © 2014 bspkrs <bspkrs@gmail.com>
 * 
 * This Java package (bspkrs.helpers.**.*) is free software. It comes
 * without any warranty, to the extent permitted by applicable law.
 * You can redistribute it and/or modify it under the terms of the
 * Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package bspkrs.helpers.client.settings;

import net.minecraft.client.settings.KeyBinding;

public class KeyBindingHelper
{
    public static int getKeyCode(KeyBinding kb)
    {
        return kb.func_151463_i();
    }
}
