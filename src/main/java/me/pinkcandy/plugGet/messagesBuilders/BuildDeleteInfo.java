package me.pinkcandy.plugGet.messagesBuilders;

import me.pinkcandy.plugGet.Tools.SetColor;
import me.pinkcandy.plugGet.Tools.TextTools;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.PluginData;
import me.pinkcandy.plugGet.model.VersionInfo;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.ArrayList;
import java.util.List;

public class BuildDeleteInfo {
    public static List<BaseComponent[]> buildDeleteInfo(List<PluginData> pluginsToInstall) {
        List<BaseComponent[]> lines = new ArrayList<>();
        List<Integer> pLetters = new ArrayList<>();
        for (int i = 0; i < pluginsToInstall.size(); i++) {
            InstallInfo installInfo = pluginsToInstall.get(i).getInstallInfo();
            pLetters.add(TextTools.countLetters(installInfo.getSlug()));
        }

        int maxPLetters = pLetters.stream().max(Integer::compareTo).orElse(0);

        String pSpace = TextTools.generateSpaces(maxPLetters + 2);

        ComponentBuilder header = new ComponentBuilder();
        header.append("§2Plugins (§a" + pluginsToInstall.size() + "§2)" + pSpace + "§8< §2Version §8| §3Size§8 >");
        lines.add(header.create());
        lines.add(new ComponentBuilder("").create());

        for (int i = 0; i < pluginsToInstall.size(); i++) {
            ComponentBuilder lineBuilder = new ComponentBuilder();

            int spacesNeeded = maxPLetters - pLetters.get(i) + 2;
            String maxPSpace = TextTools.generateSpaces(spacesNeeded);

            PluginData pluginData = pluginsToInstall.get(i);
            InstallInfo installInfo = pluginData.getInstallInfo();
            VersionInfo versionInfo = pluginData.getVersionInfo();

            String versionColor = SetColor.setColor(versionInfo != null ? versionInfo.getBranch() : "");
            String versionNumber = versionInfo != null && versionInfo.getVersionNumber() != null ? versionInfo.getVersionNumber() : "";
            int fileSize = versionInfo != null ? versionInfo.getFileSize() : 0;

            String line = "§2modrinth/§a" + installInfo.getSlug() + maxPSpace
                    + "§8< " + versionColor + versionNumber + " §8| §3" + TextTools.formatSize(fileSize) + " §8>";

            lineBuilder.append(line);
            lines.add(lineBuilder.create());
        }

        lines.add(new ComponentBuilder("").create());
        ComponentBuilder footer = new ComponentBuilder();
        footer.append("§8:: §7Continue removal? §8[");
        footer.append("§8Y").event(new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/plugget y"
        ));
        footer.append("§8/").append("§8N").event(new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/plugget n"
        ));
        footer.append("§8]");
        lines.add(footer.create());
        return  lines;
    }
}
