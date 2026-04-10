package me.pinkcandy.plugGet.messagesBuilders;

import me.pinkcandy.plugGet.Tools.SetColor;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchHelper;
import me.pinkcandy.plugGet.db.DBManager;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.PluginData;
import me.pinkcandy.plugGet.model.ProjectMeta;
import me.pinkcandy.plugGet.model.VersionInfo;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import me.pinkcandy.plugGet.Tools.TextTools;
import org.apache.maven.model.Build;

import java.util.ArrayList;
import java.util.List;

public class BuildInstallInfo {

    public static List<BaseComponent[]> buildInstallInfo(List<PluginData> pluginsToInstall) {
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
            PluginData pluginData = pluginsToInstall.get(i);
            if (pluginData.getStatus().equals("not"))
            {
                lines.add(buildInstallInfoLine(pluginData, FetchHelper.getProject(pluginData.getInstallInfo().getSlug()), maxPLetters, pLetters, i));
            }
            else if (pluginData.getStatus().equals("dif"))
            {
                lines.addAll(
                        BuildUpdateInfo.buildUpdateInfoLine(DBManager.getPluginData(pluginData.getInstallInfo().getSlug()),
                                pluginData,
                                FetchHelper.getProject(pluginData.getInstallInfo().getSlug()),
                                false,
                                0 ));
            }
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
        lines.add(footer.create());
        return  lines;
    }
    public static BaseComponent[] buildInstallInfoLine(PluginData pluginData, ProjectMeta projetMeta, int maxPLetters, List<Integer> pLetters, int i)
    {
        ComponentBuilder lineBuilder = new ComponentBuilder();

        int spacesNeeded = maxPLetters - pLetters.get(i) + 2;
        String maxPSpace = TextTools.generateSpaces(spacesNeeded);
        InstallInfo installInfo = pluginData.getInstallInfo();
        VersionInfo versionInfo = pluginData.getVersionInfo();

        String versionColor = SetColor.setColor(versionInfo != null ? versionInfo.getBranch() : "");
        String versionNumber = versionInfo != null && versionInfo.getVersionNumber() != null ? versionInfo.getVersionNumber() : "";
        int fileSize = versionInfo != null ? versionInfo.getFileSize() : 0;
        lineBuilder.append("§2modrinth/§a")
                .event(BuildTools.modrinthHover(projetMeta))
                .event(BuildTools.modrinthClick(projetMeta));
        lineBuilder.append("§a" + installInfo.getSlug())
                .event(BuildTools.projetHover(projetMeta, false));
        lineBuilder.append(maxPSpace + "§8< " + versionColor + versionNumber + " §8| §3" + TextTools.formatSize(fileSize) + " §8>");
        return lineBuilder.create();
    }
}
