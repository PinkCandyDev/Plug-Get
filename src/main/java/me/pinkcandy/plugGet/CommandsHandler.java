package me.pinkcandy.plugGet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

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

            StringBuilder slug = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                slug.append(args[i]);
                if (i < args.length - 1) slug.append(" ");
            }

            sender.sendMessage("Results for: " + slug.toString());
            new SearchProjects().searchRaw(slug.toString(), result -> {
                JSONObject root = new JSONObject(result);
                JSONArray hits = root.getJSONArray("hits");

                CatchProjectInfo parser = new CatchProjectInfo();

                VersionFetcher fetcher = new VersionFetcher();
                VersionSelector selector = new VersionSelector();
                ProjectInfoSender sendInfo = new ProjectInfoSender();

                for (int i = hits.length() - 1; i >= 0; i--) {
                    JSONObject obj = hits.getJSONObject(i);
                    List<Object> projectData = parser.parseToList(obj);
                    String projectId = (String) projectData.get(1);
                    JSONArray versionsList = fetcher.fetchVersions(projectId);
                    String[] versionInfo = selector.selectVersion(versionsList);
                    sendInfo.sendProjectInfo(sender, projectData, versionInfo);
                }
            });
        }

        if (subCommand.equals("-S")|| subCommand.equals("install")) {

            return true;
        }

        return true;
    }
}
