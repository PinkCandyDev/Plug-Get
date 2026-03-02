package me.pinkcandy.plugGet;

import me.pinkcandy.plugGet.Tools.SetColor;
import me.pinkcandy.plugGet.Tools.TextTools;
import me.pinkcandy.plugGet.model.ProjectMeta;
import me.pinkcandy.plugGet.model.VersionInfo;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import java.util.List;
import java.util.Objects;

import static me.pinkcandy.plugGet.Tools.TextTools.*;

public class BuildSearchInfo {

    public static BaseComponent[] sendProjectInfo(ProjectMeta projectMeta, List<VersionInfo> versionsList) {
        ComponentBuilder builder = new ComponentBuilder();

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

        if (versionsList == null || versionsList.stream().allMatch(Objects::isNull)) {
            builder.append("§8< §cUnknown §8>");
        } else {
            builder.append("§8< ");

            boolean first = true;

            for (int i = 0; i <= 2; i++) {
                if (versionsList.size() > i && versionsList.get(i) != null) {
                    if (!first) {
                        builder.append(" §8| ").event((HoverEvent) null);
                    }

                    builder.append(SetColor.setColor(versionsList.get(i).getBranch())
                                    + versionsList.get(i).getVersionNumber())
                            .event(versionHover(versionsList.get(i)))
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                    "/plugget -S " + projectMeta.getSlug() + " --vl " + versionsList.get(i).getVersionNumber()));


                    first = false;
                }
            }

            builder.append(" §8>").event((HoverEvent) null);
        }

//        String pluginInDB = pluginExists(projectMeta.getSlug());
//        if (pluginInDB != null) {
//            switch (pluginInDB) {
//                case "release" -> pluginInDB = "§a";
//                case "beta" -> pluginInDB = "§e";
//                case "alpha" -> pluginInDB = "§c";
//                default -> pluginInDB = "§f";
//            }
//            builder.append("  §8<" + pluginInDB + "Installed§8>");
//        }

        List<String> wrapped = TextTools.wrapText(projectMeta.getDescription(), 60);
        for (String line : wrapped) {
            builder.append("\n  §7" + line).event((HoverEvent) null);
        }

        BaseComponent[] comp = builder.create();
        return comp;
    }

    public static HoverEvent versionHover(VersionInfo versionInfo){
        HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§9Channel : §f"  + capitalize(versionInfo.getBranch()) + "\n" +
                        "§9Date: §f" + formatDate(versionInfo.getDatePublished()) + "\n" +
                        "§9Game versions: §f" + versionInfo.getGameVersions() + "\n" +
                        "§9Loaders: §f" + String.join(", ", versionInfo.getLoaders()) + "\n" +
                        "§9Size: §f" + versionInfo.getFileSize() + "\n" +
                        "§fClick to install this version").create());
        return hover;
    }
}