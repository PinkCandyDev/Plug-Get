package me.pinkcandy.plugGet;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class BuildProject {

    public void sendFormatted(JSONObject obj, ServerInfo serverInfo, CommandSender sender) {
        try {
            String slug = obj.getString("slug");
            String projectId = obj.getString("project_id");
            String author = obj.getString("author");
            int downloads = obj.optInt("downloads", 0);
            String description = obj.optString("description", "No description provided.");
            JSONArray categories = obj.optJSONArray("categories");
            JSONArray versions = obj.optJSONArray("versions");

            Set<String> loaderSet = LoaderSet.fromCategories(categories);
            String loaders = LoaderSet.joinLoaders(loaderSet);

            String versionRange = versions != null ? VersionRange.buildRange(versions) : "?";

            VersionFetcher fetcher = new VersionFetcher();
            VersionSelector selector = new VersionSelector();

            JSONArray versionsList = fetcher.fetchVersions(projectId);
            String[] versionInfo = selector.selectVersion(versionsList);
            JSONArray VersionVersionsArray = new JSONArray(versionInfo[3]);

            Set<String> versionLoaderSet = new LinkedHashSet<>();

            if (versionInfo[2] != null) {
                JSONArray loadersArray = new JSONArray(versionInfo[2]);

                for (int i = 0; i < loadersArray.length(); i++) {
                    String loader = loadersArray.getString(i).toLowerCase();
                    if (loader.equals("paper") || loader.equals("spigot") || loader.equals("bukkit") || loader.equals("folia")) {
                        versionLoaderSet.add(loader);
                    }
                }
            }

            String versionLoaders = versionLoaderSet.isEmpty() ? "unknown" : String.join(",", versionLoaderSet);

            String VersionVersionsRange = VersionVersionsArray != null ? VersionRange.buildRange(VersionVersionsArray) : "?";


            ComponentBuilder builder = new ComponentBuilder("§2modrinth/")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("§9Downloads: §f" + downloads + "\n" +
                                    "§fClick to open project page").create()))
                    .event(new ClickEvent(
                            ClickEvent.Action.OPEN_URL,
                            "https://modrinth.com/mod/" + slug
                    ))
                    .append("§a" + slug)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("§9Made by: §f" + author + "\n" +
                                    "§9Loaders: §f" + loaders + "\n" +
                                    "§9Game versions: §f" + versionRange + "\n" + "Click to install").create()))
                    .event(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/plugget -S " + slug
                    ))
                    .append("   §e" + versionInfo[0]);

            boolean unknownVersion = versionInfo[0] == null || versionInfo[0].toLowerCase().contains("unknown");
            if (!unknownVersion) {
                builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§9Loaders: §f" + versionLoaders + "\n" +
                                "§9Game versions: §f" + VersionVersionsRange + "\n" +
                                "§9VersionId: §f" + versionInfo[1]).create()));
            }

            List<String> wrapped = DescriptionWrapper.wrap(description, 60);
            for (String line : wrapped) {
                builder.append("\n  §7" + line);
            }

            BaseComponent[] comp = builder.create();
            sender.spigot().sendMessage(comp);
        } catch (Exception e) {
            sender.sendMessage("§cError formatting project: " + e.getMessage());
        }
    }
}
