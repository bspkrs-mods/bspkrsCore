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
