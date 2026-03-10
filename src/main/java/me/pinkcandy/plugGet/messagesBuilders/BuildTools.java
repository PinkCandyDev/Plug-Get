package me.pinkcandy.plugGet.messagesBuilders;

import me.pinkcandy.plugGet.Tools.TextTools;
import me.pinkcandy.plugGet.model.VersionInfo;
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
}
