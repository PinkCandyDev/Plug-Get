package me.pinkcandy.plugGet.install;

import me.pinkcandy.plugGet.ActionLock;
import me.pinkcandy.plugGet.Tools.SetColor;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.ProjectMeta;
import me.pinkcandy.plugGet.model.VersionInfo;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import me.pinkcandy.plugGet.Tools.TextTools;

import java.util.ArrayList;
import java.util.List;

public class BuildInstallInfo {

    public static List<BaseComponent[]> buildInstallInfo(List<InstallInfo> pluginsToInstall, List<VersionInfo> versions) {
        List<BaseComponent[]> lines = new ArrayList<>();
        List<Integer> pLetters = new ArrayList<>();
        for (int i = 0; i < pluginsToInstall.size(); i++) {
            pLetters.add(TextTools.countLetters(pluginsToInstall.get(i).getSlug()));
        }

        int maxPLetters = pLetters.stream().max(Integer::compareTo).orElse(0);

        String pSpace = TextTools.generateSpaces(maxPLetters + 2);

        ComponentBuilder header = new ComponentBuilder();
        header.append("§2Plugins (" + pluginsToInstall.size() + ")" + pSpace + "§8< §2Version §8| §3Size§8 >");
        lines.add(header.create());
        lines.add(new ComponentBuilder("").create());

        for (int i = 0; i < pluginsToInstall.size(); i++) {
            ComponentBuilder lineBuilder = new ComponentBuilder();

            int spacesNeeded = maxPLetters - pLetters.get(i) + 2;
            String maxPSpace = TextTools.generateSpaces(spacesNeeded);

            String versionColor = SetColor.setColor(versions.get(i).getBranch());

            String line = "§2modrinth/§a" + pluginsToInstall.get(i).getSlug() + maxPSpace
                    + "§8< " + versionColor + versions.get(i).getVersionNumber() + " §8| §3" + versions.get(i).getFileSize() + " §8>";

            lineBuilder.append(line);
            lines.add(lineBuilder.create());
        }

        lines.add(new ComponentBuilder("").create());
        ComponentBuilder footer = new ComponentBuilder();
        footer.append("§8:: §7Continue installation? §8[");
        footer.append("§8Y").event(new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/plugget y"
        ));
        footer.append("§8/").append("§8N").event(new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/plugget n"
        ));
        footer.append("§8]");
        ActionLock.isConfirming = true;
        lines.add(footer.create());
        return  lines;
    }
}
