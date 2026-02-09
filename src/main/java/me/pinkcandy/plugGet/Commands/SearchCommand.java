package me.pinkcandy.plugGet.Commands;

import me.pinkcandy.plugGet.*;
import me.pinkcandy.plugGet.VersionControll.VersionSelector;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class SearchCommand {

    public boolean execute(CommandSender sender, String[] args) {
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
                JSONArray versionsList = fetcher.fetchAll(projectId);
                sendInfo.sendProjectInfo(sender, projectData, selector.selectVersion(versionsList));
            }
        });
        return true;
    }
}
