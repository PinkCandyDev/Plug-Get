package me.pinkcandy.plugGet.commands;

import me.pinkcandy.plugGet.ConfigManager;
import me.pinkcandy.plugGet.ThreadManager;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchHelper;
import me.pinkcandy.plugGet.messagesBuilders.BuildSearchInfo;
import me.pinkcandy.plugGet.model.ProjectMeta;
import me.pinkcandy.plugGet.model.VersionInfo;
import me.pinkcandy.plugGet.version.GetNewestVersion;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

            List<ProjectMeta> orderedProjects = new ArrayList<>(metaList);
            if (!ConfigManager.searchReversedList) {
                Collections.reverse(orderedProjects);
            }

            if (ConfigManager.searchMode.equals(ConfigManager.SearchMode.FULL)) {
                List<CompletableFuture<BaseComponent[]>> futures = new ArrayList<>();
                for (ProjectMeta meta : orderedProjects) {
                    CompletableFuture<BaseComponent[]> future = CompletableFuture.supplyAsync(() -> {
                        List<VersionInfo> branches = GetNewestVersion.getBranchesFromSlug(meta.getProjectId());
                        return BuildSearchInfo.sendProjectInfo(meta, branches);
                    }, ThreadManager.getVersionFetchExecutor()).exceptionally(ex -> BuildSearchInfo.sendProjectInfo(meta, null));
                    futures.add(future);
                }

                for (CompletableFuture<BaseComponent[]> future : futures) {
                    sender.sendMessage(future.join());
                }
            } else {
                for (ProjectMeta meta : orderedProjects) {
                    sender.sendMessage(BuildSearchInfo.sendProjectInfo(meta, null));
                }
            }
        });
        return true;
    }
}
