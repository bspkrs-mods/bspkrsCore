package bspkrs.bspkrscore.fml;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import bspkrs.util.ModVersionChecker;

@SuppressWarnings("unchecked")
public class CommandBS extends CommandBase
{
    @SuppressWarnings("rawtypes")
    private static List version = new ArrayList();

    static
    {
        version.add("version");
    }

    @Override
    public String getCommandName()
    {
        return "bs";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.bs.usage";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 1;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (!bspkrsCoreMod.instance.allowUpdateCheck)
            throw new WrongUsageException("commands.bs.disabled");

        if (args.length != 2)
            throw new WrongUsageException("commands.bs.usage");

        if (!args[0].equalsIgnoreCase("version"))
            throw new WrongUsageException("commands.bs.usage");

        String[] message = ModVersionChecker.checkVersionForMod(args[1]);

        for (String s : message)
            //sender.sendChatToPlayer(new ChatComponentText(s));
            sender.addChatMessage(new ChatComponentText(s));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 2 ? getListOfStringsMatchingLastWord(args, ModVersionChecker.getVersionCheckerMap().keySet().toArray(new String[] {})) : args.length == 1 ? version : null;
    }

    @Override
    public int compareTo(Object object)
    {
        if (object instanceof CommandBase)
            return this.getCommandName().compareTo(((CommandBase) object).getCommandName());

        return 0;
    }
}
