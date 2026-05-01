package me.pinkcandy.plugGet.commands;

import me.pinkcandy.plugGet.ThreadManager;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchHelper;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchVersions;
import me.pinkcandy.plugGet.api.modrinth.map.VersionMapper;
import me.pinkcandy.plugGet.messagesBuilders.BuildVersionsInfo;
import me.pinkcandy.plugGet.model.ProjectMeta;
import me.pinkcandy.plugGet.model.VersionInfo;
import me.pinkcandy.plugGet.version.CompatibilityFilter;
import me.pinkcandy.plugGet.version.VersionValidator;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class VersionsCommand {
    private static final int PAGE_SIZE = 15;

    public static boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cPlease provide a slug. Example: §7/pg -Vs plug-get");
            return true;
        }

        String slug = args[1];
        int page = 1;
        boolean showAll = false;
        if (args.length > 2) {
            for (int i = 2; i < args.length; i++) {
                if (args[i].equalsIgnoreCase("--all")) {
                    showAll = true;
                } else if (args[i].matches("\\d+")) {
                    page = Integer.parseInt(args[i]);
                } else {
                    sender.sendMessage("§4Wrong argument: " + args[i]);
                    return true;
                }
            }
        }

        int requestedPage = page;
        boolean finalShowAll = showAll;
        ThreadManager.runAsync(() -> {

            ProjectMeta projectMeta = FetchHelper.getProject(slug);
            if (projectMeta == null) {
                sender.sendMessage("§cPlugin " + slug + " not found. Is the slug correct?");
                return;
            }
            JSONArray versionsJson = FetchVersions.fetchAll(projectMeta.getProjectId());
            if (versionsJson == null) {
                sender.sendMessage("§cCould not fetch versions for " + slug);
                return;
            }

            List<VersionInfo> versions = new ArrayList<>();
            for (int i = 0; i < versionsJson.length(); i++) {
                VersionInfo versionInfo = VersionMapper.fromJson(versionsJson.getJSONObject(i));
                if (finalShowAll || (VersionValidator.isValid(versionInfo) && CompatibilityFilter.isCompatible(versionInfo))) {
                    versions.add(versionInfo);
                }
            }

            if (versions.isEmpty()) {
                if (finalShowAll) {
                    sender.sendMessage("§cNo versions found for " + slug);
                } else {
                    sender.sendMessage("§cNo compatible versions found for " + slug + ". Try §7--all");
                }
                return;
            }

            int totalPages = (int) Math.ceil((double) versions.size() / PAGE_SIZE);
            int currentPage = Math.max(1, Math.min(requestedPage, totalPages));
            int fromIndex = (currentPage - 1) * PAGE_SIZE;
            int toIndex = Math.min(fromIndex + PAGE_SIZE, versions.size());


            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.spigot().sendMessage(BuildVersionsInfo.buildHeader(projectMeta, versions.size(), currentPage, totalPages, finalShowAll));
            } else {
                sender.sendMessage(TextComponent.toLegacyText(BuildVersionsInfo.buildHeader(projectMeta, versions.size(), currentPage, totalPages, finalShowAll)));
            }
            for (int i = fromIndex; i < toIndex; i++) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.spigot().sendMessage(BuildVersionsInfo.buildVersionLine(projectMeta, versions.get(i)));
                } else {
                    sender.sendMessage(TextComponent.toLegacyText(BuildVersionsInfo.buildVersionLine(projectMeta, versions.get(i))));
                }
            }

            BaseComponent[] navigation = BuildVersionsInfo.buildNavigation(projectMeta.getSlug(), currentPage, totalPages, finalShowAll);
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.spigot().sendMessage(navigation);
            } else {
                sender.sendMessage(TextComponent.toLegacyText(navigation));
            }
        });

        return true;
    }
}
