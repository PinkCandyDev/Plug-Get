package me.pinkcandy.plugGet.commands;

import me.pinkcandy.plugGet.*;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchProjects;
import me.pinkcandy.plugGet.api.modrinth.map.ProjectMapper;
import me.pinkcandy.plugGet.model.ProjectMeta;
import me.pinkcandy.plugGet.model.VersionInfo;
import me.pinkcandy.plugGet.version.GetNewestVersion;
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
        new FetchProjects().SeatchProjects(slug.toString(), result -> {
            JSONObject root = new JSONObject(result);
            JSONArray hits = root.getJSONArray("hits");

            for (int i = hits.length() - 1; i >= 0; i--) {
                JSONObject obj = hits.getJSONObject(i);
                ProjectMeta projectMeta = ProjectMapper.fromJson(obj);
                List<VersionInfo> branches = GetNewestVersion.getBranchesFromSlug(projectMeta.getProjectId());
                sender.sendMessage(BuildSearchInfo.sendProjectInfo(projectMeta, branches));
            }
        });
        return true;
    }
}
