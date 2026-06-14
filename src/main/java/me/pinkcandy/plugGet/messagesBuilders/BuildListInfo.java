package me.pinkcandy.plugGet.messagesBuilders;

import me.pinkcandy.plugGet.Tools.SetColor;
import me.pinkcandy.plugGet.model.PluginData;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.List;

public class BuildListInfo {
    public static List<BaseComponent[]> buildListInfo(List<PluginData> plugins, List<String> files, String type)
    {
        List<String> installedPlugins = new java.util.ArrayList<>();
        for (int i = 0; i < plugins.size(); i++)
        {
            installedPlugins.add(plugins.get(i).getVersionInfo().getFileName());
        }
        List<BaseComponent[]> lines = new java.util.ArrayList<>();
        ComponentBuilder header = new ComponentBuilder("");
        if (type.equals("all"))
        {
            header.append("§2Installed plugins (§a" + files.size() + "§2)");
        }
        else if (type.equals("excluded"))
        {
            header.append("§2Excluded plugins (§a" + plugins.size() + "§2)");
        }
        lines.add(header.create());

        int iterationCount = type.equals("excluded") ? plugins.size() : files.size();
        for (int i = 0; i < iterationCount; i++) {
            ComponentBuilder line = new ComponentBuilder("");
            if (type.equals("excluded")) {
                line.append("§2Modrinth/");
                line.append("§a" + plugins.get(i).getInstallInfo().getSlug()).event(BuildTools.versionHover(plugins.get(i).getVersionInfo()));
                String color = SetColor.setColor(plugins.get(i).getVersionInfo().getBranch());
                line.append("  " + color + plugins.get(i).getVersionInfo().getVersionNumber()).event(BuildTools.versionHover(plugins.get(i).getVersionInfo()));
                lines.add(line.create());
            }
            else if (installedPlugins.contains(files.get(i)))
            {
                int j = installedPlugins.indexOf(files.get(i));
                line.append("§2Modrinth/");
                line.append("§a" + plugins.get(j).getInstallInfo().getSlug()).event(BuildTools.versionHover(plugins.get(j).getVersionInfo()));
                String color = SetColor.setColor(plugins.get(j).getVersionInfo().getBranch());
                line.append("  " + color + plugins.get(j).getVersionInfo().getVersionNumber()).event(BuildTools.versionHover(plugins.get(j).getVersionInfo()));
                lines.add(line.create());
            }
            else
            {
                line.append("§8Local/§7" + files.get(i));
                lines.add(line.create());
            }
        }
        return lines;
    }
}
