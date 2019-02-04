package bspkrs.bspkrscore.fml;

import bspkrs.util.ModVersionChecker;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
    public String getName() {
        return "bs";
    }


    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.bs.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!bspkrsCoreMod.instance.allowUpdateCheck)
            throw new WrongUsageException("commands.bs.disabled");

        if (args.length != 2)
            throw new WrongUsageException("commands.bs.usage");

        if (!args[0].equalsIgnoreCase("version"))
            throw new WrongUsageException("commands.bs.usage");

        String[] message = ModVersionChecker.checkVersionForMod(args[1]);

        for (String s : message)
            sender.sendMessage(new TextComponentString(s));
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 1;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 2 ? getListOfStringsMatchingLastWord(args, ModVersionChecker.getVersionCheckerMap().keySet().toArray(new String[] {})) : args.length == 1 ? version : null;
    }

    @Override
    public int compareTo(ICommand p_compareTo_1_) {
        if (p_compareTo_1_ instanceof CommandBase)
            return this.getName().compareTo(p_compareTo_1_.getName());

        return 0;
    }
}
