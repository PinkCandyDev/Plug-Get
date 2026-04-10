package me.pinkcandy.plugGet.commands;

import me.pinkcandy.plugGet.ThreadManager;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchHelper;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchProjects;
import me.pinkcandy.plugGet.api.modrinth.map.ProjectMapper;
import me.pinkcandy.plugGet.messagesBuilders.BuildSearchInfo;
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

        ThreadManager.runAsync(() -> {
            List<ProjectMeta> metaList = FetchHelper.searchProjects(slug.toString());
            if (metaList.isEmpty()) {
                sender.sendMessage("§cNo results found for: §4" + slug);
                return;
            }
            else {
                sender.sendMessage("§8:: §7Found §8" + metaList.size() + "§7 results");
            }
            for (int i = 0; i < metaList.size(); i++) {
                List<VersionInfo> branches = GetNewestVersion.getBranchesFromSlug(metaList.get(i).getProjectId());
                sender.sendMessage(BuildSearchInfo.sendProjectInfo(metaList.get(i), branches));
            }
        });
        return true;
    }
}
