package bspkrs.helpers.client.entity;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.IChatComponent;

public class EntityPlayerSPHelper
{
    public static void addChatMessage(EntityPlayerSP player, IChatComponent message)
    {
        player.func_146105_b(message);
    }
}
