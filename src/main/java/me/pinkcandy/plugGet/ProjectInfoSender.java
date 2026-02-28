package me.pinkcandy.plugGet;

import me.pinkcandy.plugGet.Tools.LoaderSet;
import me.pinkcandy.plugGet.Tools.TextTools;
import me.pinkcandy.plugGet.Tools.VersionRange;
import me.pinkcandy.plugGet.model.ProjectMeta;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;

import java.util.List;
import java.util.Objects;

import static me.pinkcandy.plugGet.db.DBManager.pluginExists;

public class ProjectInfoSender {

    public void sendProjectInfo(CommandSender sender, ProjectMeta projectMeta) {

        ComponentBuilder builder = new ComponentBuilder();

        versionInfo.forEach(branch -> {
            if (branch == null) return;
            branch[3] = LoaderSet.joinLoaders(LoaderSet.FromJsonArray(new JSONArray(branch[3])));
            JSONArray versionVersionsArray = new JSONArray(branch[2]);
            branch[2] = versionVersionsArray != null ? VersionRange.buildRange(versionVersionsArray) : "?";
        });

        boolean foundVersions = versionInfo != null && versionInfo.stream().anyMatch(Objects::nonNull);

                builder.append("§2modrinth/")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§9Downloads: §f" + projectMeta.getDownloads() + "\n" +
                                "§fClick to open project page").create()))
                .event(new ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://modrinth.com/mod/" + projectMeta.getSlug()))
                .append("§a" + projectMeta.getSlug())
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§9Made by: §f" + projectMeta.getAuthor() + "\n" +
                                "§9Loaders: §f" + String.join(", ", projectMeta.getLoaders()) + "\n" +
                                "§9Game versions: §f" + projectMeta.getVersionRange() + "\n" + "Click to install prefered version").create()))
                .event(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/plugget -S " + projectMeta.getSlug()))
                        .append("    ").event((HoverEvent) null);

        if (!foundVersions) {
            builder.append("§8< §cUnknown §8>");
        } else {
            builder.append("§8< ");

            boolean first = true;

            for (int i = 0; i <= 2; i++) {
                if (versionInfo.size() > i &&
                        versionInfo.get(i) != null &&
                        versionInfo.get(i)[0] != null) {

                    if (!first) {
                        builder.append(" §8| ").event((HoverEvent) null);
                    }

                    String color;
                    switch (i) {
                        case 0 -> color = "§a";
                        case 1 -> color = "§e";
                        case 2 -> color = "§c";
                        default -> color = "§f";
                    }

                    builder.append(color + versionInfo.get(i)[0])
                            .event(versionHover(versionInfo.get(i), i))
                            .event(new  ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                            "/plugget -S " + projectMeta.getSlug() + " --vl " + versionInfo.get(i)[0]));


                    first = false;
                }
            }

            builder.append(" §8>").event((HoverEvent) null);
        }

        String pluginInDB = pluginExists(projectMeta.getSlug());
        if (pluginInDB != null)
        {
            switch (pluginInDB) {
                case "release" -> pluginInDB = "§a";
                case "beta" -> pluginInDB = "§e";
                case "alpha" -> pluginInDB = "§c";
                default -> pluginInDB = "§f";
            }
            builder.append("  §8<" + pluginInDB + "Installed§8>");
        }



        List<String> wrapped = TextTools.wrapText(projectMeta.getDescription(), 60);
        for (String line : wrapped) {
            builder.append("\n  §7" + line).event((HoverEvent) null);
        }

        BaseComponent[] comp = builder.create();
        sender.spigot().sendMessage(comp);
    }

    public HoverEvent versionHover(String[] versionInfo, int branch)
    {
        HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§9Channel : §f" + (branch == 0 ? "Release" : branch == 1 ? "Beta" : "Alpha") + "\n" +
                        "§9Date: §f" + versionInfo[1] + "\n" +
                        "§9Game versions: §f" + versionInfo[2] + "\n" +
                        "§9Loaders: §f" + versionInfo[3] + "\n" +
                        "§9Size: §f" + versionInfo[4] + "\n" +
                        "§fClick to install this version").create());
        return hover;
    }
}
