package me.pinkcandy.plugGet.messagesBuilders;

import me.pinkcandy.plugGet.Tools.TextTools;
import me.pinkcandy.plugGet.model.ProjectMeta;
import me.pinkcandy.plugGet.model.VersionInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.w3c.dom.Text;

import static me.pinkcandy.plugGet.Tools.TextTools.capitalize;
import static me.pinkcandy.plugGet.Tools.TextTools.formatDate;

public class BuildTools {
    public static HoverEvent versionHover(VersionInfo versionInfo){
        String gameVersions = "";
        String loaders = "";
        String onClickMessage = "§fClick to view on Modrinth";
        if (versionInfo.getGameVersionRange() != null && versionInfo.getLoaders() != null)
        {
            gameVersions = "§9Game versions: §f" + versionInfo.getGameVersionRange() + "\n";
            loaders = "§9Loaders: §f" + String.join(", ", versionInfo.getLoaders()) + "\n";
            onClickMessage = "§fClick to install this version";
        }
        HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§9Channel : §f"  + capitalize(versionInfo.getBranch()) + "\n" +
                        "§9Date: §f" + formatDate(versionInfo.getDatePublished()) + "\n" +
                        gameVersions +
                        loaders +
                        "§9Size: §f" + TextTools.formatSize(versionInfo.getFileSize()) + "\n" +
                        onClickMessage).create());
        return hover;
    }

    public static HoverEvent modrinthHover(ProjectMeta meta)
    {
        return new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(
                        "§9Downloads: §f" + meta.getDownloads() + "\n" +
                                "§fClick to open project page"
                ).create()
        );
    }

    public static ClickEvent modrinthClick(ProjectMeta meta)
    {
        return new ClickEvent(
                ClickEvent.Action.OPEN_URL,
                "https://modrinth.com/mod/" + meta.getSlug()
        );
    }

    public static HoverEvent projetHover(ProjectMeta meta, Boolean install)
    {
        String installMessage = "";
        if (install) {
            installMessage = "\n§9Click to install prefered version";
        }
        return new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(
                        "§9Made by: §f" + meta.getAuthor() + "\n" +
                                "§9Loaders: §f" + String.join(", ", meta.getLoaders()) + "\n" +
                                "§9Game versions: §f" + meta.getVersionRange() +
                                installMessage
                ).create()
        );
    }
    public static ClickEvent projectClick(ProjectMeta meta)
    {
        return new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/plugget -S " + meta.getSlug()
        );
    }
}
