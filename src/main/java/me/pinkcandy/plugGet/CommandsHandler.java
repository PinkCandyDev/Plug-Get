package me.pinkcandy.plugGet;

import me.pinkcandy.plugGet.Commands.InstallCommand;
import me.pinkcandy.plugGet.Commands.SearchCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommandsHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("Usage: /plugget -Ss <plugin-slug> or /plugget search <plugin-slug>");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("y")) {
            if (ActionLock.isConfirming && ActionLock.lockedBy == sender) {
                ActionLock.confirm.run();
            } else {
                sender.sendMessage("§cNo action to confirm.");
            }
        }

        if (subCommand.equals("n")) {
            if (ActionLock.isConfirming && ActionLock.lockedBy == sender) {
                ActionLock.deny.run();
            } else {
                sender.sendMessage("§cNo action to deny.");
            }
        }


        if (subCommand.equals("-ss") || subCommand.equals("search")) {
            new SearchCommand().execute(sender, args);
        }


        if ((subCommand.equals("-s") || subCommand.equals("install")) &&
                 ( !ActionLock.isLocked && ActionLock.lockedBy == null)){
            ActionLock.lock(sender);
            new InstallCommand().execute(sender, args);
            return true;
        }
        else if (ActionLock.isLocked)
        {
            sender.sendMessage("§cAnother action is currently in progress. Please wait until it is finished.");
        }
        return true;
    }
}
