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

        if (subCommand.equals("-ss") || subCommand.equals("search")) {
            new SearchCommand().execute(sender, args);
        }

        if (subCommand.equals("-s")|| subCommand.equals("install")) {
            new InstallCommand().execute(sender, args);
            return true;
        }
        return true;
    }
}
