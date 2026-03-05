package me.pinkcandy.plugGet.messagesBuilders;

import me.pinkcandy.plugGet.model.VersionInfo;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import static me.pinkcandy.plugGet.Tools.TextTools.capitalize;
import static me.pinkcandy.plugGet.Tools.TextTools.formatDate;

public class BuildTools {
    public static HoverEvent versionHover(VersionInfo versionInfo){
        HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§9Channel : §f"  + capitalize(versionInfo.getBranch()) + "\n" +
                        "§9Date: §f" + formatDate(versionInfo.getDatePublished()) + "\n" +
                        "§9Game versions: §f" + versionInfo.getGameVersionRange() + "\n" +
                        "§9Loaders: §f" + String.join(", ", versionInfo.getLoaders()) + "\n" +
                        "§9Size: §f" + versionInfo.getFileSize() + "\n" +
                        "§fClick to install this version").create());
        return hover;
    }
}
