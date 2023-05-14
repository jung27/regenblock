package io.github.jung27.regenblock.Command;

import io.github.jung27.regenblock.Command.SubCommands.AddBlockCommand;
import io.github.jung27.regenblock.Command.SubCommands.AppointCommand;
import io.github.jung27.regenblock.Command.SubCommands.BlockCommand;
import io.github.jung27.regenblock.Command.SubCommands.RemoveCommand;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RegenBlockCommand implements CommandExecutor, TabCompleter {
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public RegenBlockCommand(){
        subCommands.add(new AppointCommand());
        subCommands.add(new RemoveCommand());
        subCommands.add(new AddBlockCommand());
        subCommands.add(new BlockCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            System.out.println("플레이어만 사용할 수 있는 명령어입니다.");
            return false;
        }
        if(args.length >= 1) {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getName().equalsIgnoreCase(args[0])) {
                    subCommand.perform((Player) sender, args);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1) {
            ArrayList<String> subCommandNames = new ArrayList<>();
            for (SubCommand subCommand : subCommands) {
                subCommandNames.add(subCommand.getName());
            }
            return subCommandNames;
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("remove") ||
                    args[0].equalsIgnoreCase("addblock") ||
                    args[0].equalsIgnoreCase("removeblock")
            ) {
                ArrayList<String> regionIds = new ArrayList<>();
                for (Region region : Region.regions) {
                    regionIds.add(region.getId());
                }
                return regionIds;
            }
        }
        return null;
    }
}
