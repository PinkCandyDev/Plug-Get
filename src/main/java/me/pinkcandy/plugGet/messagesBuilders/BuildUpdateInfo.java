package me.pinkcandy.plugGet.messagesBuilders;

import me.pinkcandy.plugGet.Tools.SetColor;
import me.pinkcandy.plugGet.model.PluginData;
import me.pinkcandy.plugGet.model.ProjectMeta;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import me.pinkcandy.plugGet.Tools.TextTools;
import org.apache.maven.model.Build;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class BuildUpdateInfo {

    public static List<BaseComponent[]> buildUpdateInfo(List<PluginData> installedPlugins, List<PluginData> pluginsToUpdate) {
        List<BaseComponent[]> lines = new ArrayList<>();

        ComponentBuilder header = new ComponentBuilder();
        header.append("§2Plugins to update (§a" + pluginsToUpdate.size() + "§2)");
        lines.add(header.create());
        lines.add(new ComponentBuilder("").create());
        for (int i = 0; i < pluginsToUpdate.size(); i++) {
            PluginData pI = installedPlugins.get(i);
            PluginData pU = pluginsToUpdate.get(i);
            int df = pU.getVersionInfo().getFileSize() - pI.getVersionInfo().getFileSize();
            String sizeDiffrance = TextTools.formatSize(df);
            if (df >= 0) {
                sizeDiffrance = "+ " + sizeDiffrance;
            }
            ComponentBuilder line = new ComponentBuilder();
            line.append("[" + i + "] §2modrinth/§a" + pI.getInstallInfo().getSlug());
            lines.add(line.create());
            ComponentBuilder vline = new ComponentBuilder();
            vline.append("     ");
            vline.append(pI.getVersionInfo().getVersionNumber())
                    .event(BuildTools.versionHover(pI.getVersionInfo()))
                    .event(new ClickEvent(ClickEvent.Action.OPEN_URL,
                            "https://modrinth.com/plugin/" + pI.getInstallInfo().getSlug() + "/version/" + pI.getVersionInfo().getVersionId()));
            vline.append(" ---> ");
            vline.append(pU.getVersionInfo().getVersionNumber())
                    .event(BuildTools.versionHover(pU.getVersionInfo()))
                    .event(new ClickEvent(ClickEvent.Action.OPEN_URL,
                            "https://modrinth.com/plugin/" + pU.getInstallInfo().getSlug() + "/version/" + pU.getVersionInfo().getVersionId()));
            vline.append("  §3" + sizeDiffrance);
            lines.add(vline.create());
        }

        lines.add(new ComponentBuilder("").create());
        ComponentBuilder footer = new ComponentBuilder();
        footer.append("§8:: §7Write §8/pg 0 1 2...§7 to exclude updates or §8[");
        footer.append("§8Y").event(new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/plugget y"
        ));
        footer.append("§8/" ).append("§8N").event(new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/plugget n"
        ));
        footer.append("§8]");
        lines.add(footer.create());

        return lines;
    }

    public static List<BaseComponent[]> buildUpdateInfoLine(PluginData pI, PluginData pU, ProjectMeta projetMeta, boolean addIndex, int index)
    {
        List<BaseComponent[]> info = new ArrayList<>();
        int df = pU.getVersionInfo().getFileSize() - pI.getVersionInfo().getFileSize();
        String sizeDiffrance = TextTools.formatSize(df);
        if (df >= 0) {
            sizeDiffrance = "+ " + sizeDiffrance;
        }
        ComponentBuilder line = new ComponentBuilder();
        if (addIndex) {
            line.append("[" + index + "]");
        }
        line.append("§2modrinth/")
                .event(BuildTools.modrinthHover(projetMeta))
                .event(BuildTools.modrinthClick(projetMeta));
        line.append("§a" + pI.getInstallInfo().getSlug())
                .event(BuildTools.projetHover(projetMeta, false));
        info.add(line.create());
        ComponentBuilder vline = new ComponentBuilder();
        vline.append("     ");
        vline.append(pI.getVersionInfo().getVersionNumber())
                .event(BuildTools.versionHover(pI.getVersionInfo()))
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL,
                        "https://modrinth.com/plugin/" + pI.getInstallInfo().getSlug() + "/version/" + pI.getVersionInfo().getVersionId()));
        vline.append(" ---> ");
        vline.append(pU.getVersionInfo().getVersionNumber())
                .event(BuildTools.versionHover(pU.getVersionInfo()))
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL,
                        "https://modrinth.com/plugin/" + pU.getInstallInfo().getSlug() + "/version/" + pU.getVersionInfo().getVersionId()));
        vline.append("  §3" + sizeDiffrance);
        info.add(vline.create());
        return info;
    }
}
