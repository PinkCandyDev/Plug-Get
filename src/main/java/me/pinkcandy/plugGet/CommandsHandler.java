package me.pinkcandy.plugGet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

public class CommandsHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("Usage: /plugget -Ss <plugin-slug> or /plugget search <plugin-slug>");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("-ss") || subCommand.equals("search")) {

            StringBuilder slug = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                slug.append(args[i]);
                if (i < args.length - 1) slug.append(" ");
            }

            sender.sendMessage("Results for: " + slug.toString());
            new SearchProjects().searchRaw(slug.toString(), result -> {
                ServerInfo serverInfo = new ServerInfo();
                JSONObject root = new JSONObject(result);
                JSONArray hits = root.getJSONArray("hits");

                BuildProject formatter = new BuildProject();
                for (int i = hits.length() - 1; i >= 0; i--) {
                    JSONObject obj = hits.getJSONObject(i);
                    formatter.sendFormatted(obj, serverInfo, sender);
                }
            });
        }

        if (subCommand.equals("-S")|| subCommand.equals("install")) {

            return true;
        }

        return true;
    }
}
