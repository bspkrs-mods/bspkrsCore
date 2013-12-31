package bspkrs.helpers.entity.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

public class EntityPlayerHelper
{
    public static void addChatMessage(EntityPlayer player, IChatComponent message)
    {
        player.func_146105_b(message);
    }
}
