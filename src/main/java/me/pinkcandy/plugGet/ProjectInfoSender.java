package me.pinkcandy.plugGet;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;

import java.util.List;

public class ProjectInfoSender {

    public void sendProjectInfo(CommandSender sender,List<Object> projectData, String[] versionInfo) {

        ComponentBuilder builder = new ComponentBuilder();

        String slug = (String) projectData.get(0);
        String author = (String) projectData.get(2);
        int downloads = (int) projectData.get(3);
        String description = (String) projectData.get(4);
        String loaders = (String) projectData.get(5);
        String versionRange = (String) projectData.get(7);

        String[] release = (String[]) projectData.get(0);
        String[] beta = (String[]) projectData.get(1);
        String[] alpha = (String[]) projectData.get(2);

        projectData.forEach(branch -> {

        });


        String versionLoaders = LoaderSet.FromJsonArray(new JSONArray(versionInfo[2])).toString();
        JSONArray VersionVersionsArray = new JSONArray(versionInfo[3]);
        String VersionVersionsRange = VersionVersionsArray != null ? VersionRange.buildRange(VersionVersionsArray) : "?";

                builder.append("§2modrinth/")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§9Downloads: §f" + downloads + "\n" +
                                "§fClick to open project page").create()))
                .event(new ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://modrinth.com/mod/" + slug))
                .append("§a" + slug)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§9Made by: §f" + author + "\n" +
                                "§9Loaders: §f" + loaders + "\n" +
                                "§9Game versions: §f" + versionRange + "\n" + "Click to install").create()))
                .event(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/plugget -S " + slug))
                .append("   §e");


        List<String> wrapped = DescriptionWrapper.wrap(description, 60);
        for (String line : wrapped) {
            builder.append("\n  §7" + line);
        }

        BaseComponent[] comp = builder.create();
        sender.spigot().sendMessage(comp);
    }

    public HoverEvent versionHover(String[] versionInfo)
    {
        HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§9Date: §f" + versionInfo[1] + "\n" +
                        "§9Game versions: §f" + versionInfo[2] + "\n" +
                        "§9Loaders: §f" + versionInfo[3] + "\n" +
                        "§9Size: §f" + versionInfo[4]).create());
        return hover;
    }
}
