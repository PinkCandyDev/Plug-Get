package me.pinkcandy.plugGet.commands;

import me.pinkcandy.plugGet.ConfigManager;
import me.pinkcandy.plugGet.PlugGet;
import me.pinkcandy.plugGet.ThreadManager;
import me.pinkcandy.plugGet.Tools.TextTools;
import me.pinkcandy.plugGet.Update.UpdatePreparer;
import me.pinkcandy.plugGet.remove.RemovePreparer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandsHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("§cNo subcommand provided. Use §7/pg help §cto see available commands.");
            return true;
        }

        String subCommand = args[0];

        if (ActionLock.isConfirming && ActionLock.lockedBy == sender) {
            boolean allNumericArgs = true;
            for (String a : args) {
                if (!TextTools.isNonNegativeInteger(a)) {
                    allNumericArgs = false;
                    break;
                }
            }
            if (allNumericArgs) {
                List<Integer> numbers = TextTools.parseIntList(args);
                if (numbers == null) return true;
                ActionLock.numberList = numbers;
                return true;
            }
        }

        if (subCommand.equals("y") || subCommand.equals("-y")) {
            if (ActionLock.isConfirming && ActionLock.lockedBy == sender) {
                ActionLock.confirm.run();
                List<Integer> numbers = new ArrayList<>();
                ActionLock.numberList = numbers;
            } else {
                sender.sendMessage("§cNo action to confirm.");
            }
            return true;
        }

        if (subCommand.equals("n") || subCommand.equals("-n")) {
            if (ActionLock.isConfirming && ActionLock.lockedBy == sender) {
                ActionLock.deny.run();
            } else {
                sender.sendMessage("§cNo action to deny.");
            }
            return true;
        }

        if (subCommand.equals("release") || subCommand.equals("-Alr")) {
            if (!ActionLock.isLocked && ActionLock.lockedBy == null) {
                sender.sendMessage("§cNo action lock is active.");
            } else {
                ActionLock.release();
                sender.sendMessage("§7Action lock released.");
            }
            return true;
        }

        if (subCommand.equals("-R") || subCommand.equals("remove"))
        {
            if (!ActionLock.isLocked && ActionLock.lockedBy == null) {
                ActionLock.lock(sender);
                List<String> slugs = new ArrayList<>();
                for (int i = 1; i < args.length; i++) {
                    slugs.add(args[i]);
                }
                RemovePreparer.prepareDelete(slugs, "", sender);
            }
            else {
                sender.sendMessage("§cAnother action is currently in progress. Please wait until it is finished.");
            }
            return true;
        }

        if (subCommand.equals("-Rs") || subCommand.equals("autoremove"))
        {
            if (!ActionLock.isLocked && ActionLock.lockedBy == null) {
                ActionLock.lock(sender);
                List<String> slugs = new ArrayList<>();
                for (int i = 1; i < args.length; i++) {
                    slugs.add(args[i]);
                }
                RemovePreparer.prepareDelete(slugs, "auto", sender);
            }
            else {
                sender.sendMessage("§cAnother action is currently in progress. Please wait until it is finished.");
            }
            return true;
        }

        if ((subCommand.equals("-Ss") || subCommand.equals("search")) && ActionLock.lockedBy != sender) {
            new SearchCommand().execute(sender, args);
            return true;
        }

        if ((subCommand.equals("update") || subCommand.equals("-Syu"))){
            if (!ActionLock.isLocked && ActionLock.lockedBy == null) {
                ActionLock.lock(sender);
                ThreadManager.runAsync(() -> {
                    UpdatePreparer.execute(sender);
                });
            }
            else {
                sender.sendMessage("§cAnother action is currently in progress. Please wait until it is finished.");
            }
            return true;
        }

        if (subCommand.equals("help") || subCommand.equals("-h")) {
            HelpCommand.execute(sender, args);
            return true;
        }

        if (subCommand.equals("reload") || subCommand.equals("-rl"))
        {
            ConfigManager.reload(PlugGet.instance);
            sender.sendMessage("§7Configuration reloaded.");
            return true;
        }

        if ((subCommand.equals("-S") || subCommand.equals("install"))){
            if (!ActionLock.isLocked && ActionLock.lockedBy == null) {
                ActionLock.lock(sender);
                InstallCommand.execute(sender, args);
            }
            else
            {
                sender.sendMessage("§cAnother action is currently in progress. Please wait until it is finished.");
            }
            return true;
        }

        if (subCommand.equals("list") || subCommand.equals("-Qs")) {
            ThreadManager.runAsync(() -> {
                new ListCommand().execute(sender);
            });
            return true;
        }

        if (subCommand.equals("versions") || subCommand.equals("-Vs")) {
            VersionsCommand.execute(sender, args);
            return true;
        }

        sender.sendMessage("§cUnknown subcommand. Use §7/pg help §cto see available commands.");
        return true;
    }
}
