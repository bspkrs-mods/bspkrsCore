/**
 * Copyright © 2014 bspkrs <bspkrs@gmail.com>
 * 
 * This Java package (bspkrs.helpers.**.*) is free software. It comes
 * without any warranty, to the extent permitted by applicable law.
 * You can redistribute it and/or modify it under the terms of the
 * Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package bspkrs.helpers.entity.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

import com.mojang.authlib.GameProfile;

public class EntityPlayerHelper
{
    public static void addChatMessage(EntityPlayer player, IChatComponent message)
    {
        player.func_146105_b(message);
    }
    
    public static GameProfile getGameProfile(EntityPlayer player)
    {
        return player.func_146103_bH();
    }
}
