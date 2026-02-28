package me.pinkcandy.plugGet.commands;

import me.pinkcandy.plugGet.*;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchProjects;
import me.pinkcandy.plugGet.api.modrinth.fetch.VersionFetcher;
import me.pinkcandy.plugGet.api.modrinth.map.ProjectMapper;
import me.pinkcandy.plugGet.api.modrinth.map.VersionMapper;
import me.pinkcandy.plugGet.model.ProjectMeta;
import me.pinkcandy.plugGet.model.VersionInfo;
import me.pinkcandy.plugGet.versionControll.VersionSelector;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchCommand {

    public boolean execute(CommandSender sender, String[] args) {
        StringBuilder slug = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            slug.append(args[i]);
            if (i < args.length - 1) slug.append(" ");
        }

        sender.sendMessage("Results for: " + slug.toString());
        new FetchProjects().SearchRaw(slug.toString(), result -> {
            JSONObject root = new JSONObject(result);
            JSONArray hits = root.getJSONArray("hits");

            VersionFetcher fetcher = new VersionFetcher();
            VersionSelector selector = new VersionSelector();
            ProjectInfoSender sendInfo = new ProjectInfoSender();

            for (int i = hits.length() - 1; i >= 0; i--) {
                JSONObject obj = hits.getJSONObject(i);
                ProjectMeta projectMeta = ProjectMapper.fromJson(obj);
                JSONArray versions = fetcher.fetchAll(projectMeta.getProjectId());
                List<VersionInfo> versionsList = new ArrayList<>();
                for (int j = 0; j < versions.length(); j++) {
                    VersionInfo versionInfo = VersionMapper.fromJson(versions.getJSONObject(i));
                    versionsList.add(versionInfo);
                }


                sendInfo.sendProjectInfo(sender, projectMeta,);
            }
        });
        return true;
    }
}
