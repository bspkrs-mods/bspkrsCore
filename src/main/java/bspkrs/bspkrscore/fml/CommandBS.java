package bspkrs.bspkrscore.fml;

import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import bspkrs.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;

import java.util.*;

@SuppressWarnings("unused")
public class CommandBS extends CommandBase
{
    private static List<String> version;

    @Override
    public String getName()
    {
        return "bs";
    }

    @Override
    public String getUsage(final ICommandSender sender)
    {
        return "commands.bs.usage";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender par1ICommandSender)
    {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 1;
    }

    @Override
    public void execute(MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException
    {
        /*
         * if(!bspkrsCoreMod.INSTANCE.allowUpdateCheck)
         * {
         * throw new WrongUsageException("commands.bs.disabled", new Object[0]);
         * }
         */
        if(args.length != 2)
        {
            throw new WrongUsageException("commands.bs.usage", new Object[0]);
        }
        if(!args[0].equalsIgnoreCase("version"))
        {
            throw new WrongUsageException("commands.bs.usage", new Object[0]);
        }
        /*
         * final String[] message = ModVersionChecker.checkVersionForMod(args[1]);
         * for(final String s : message)
         * {
         * sender.sendMessage(new TextComponentString(s));
         * }
         */
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, final ICommandSender sender, final String[] args, final BlockPos pos)
    {
        /*
         * return (args.length == 2) ? getListOfStringsMatchingLastWord(args, (String[])ModVersionChecker.getVersionCheckerMap().keySet().toArray(new String[0])) : ((args.length == 1) ?
         * CommandBS.version : null);
         */
        return null;
    }

    @Override
    public int compareTo(final ICommand compareTo)
    {
        if(compareTo instanceof CommandBase)
        {
            return this.getName().compareTo(((CommandBase)compareTo).getName());
        }
        return 0;
    }

    static
    {
        (CommandBS.version = new ArrayList<String>()).add("version");
    }
}
