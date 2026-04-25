package me.pinkcandy.plugGet.messagesBuilders;

import me.pinkcandy.plugGet.Tools.SetColor;
import me.pinkcandy.plugGet.Tools.TextTools;
import me.pinkcandy.plugGet.model.ProjectMeta;
import me.pinkcandy.plugGet.model.VersionInfo;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class BuildVersionsInfo {

    public static BaseComponent[] buildHeader(ProjectMeta meta, int totalVersions, int page, int totalPages, boolean showAll) {
        ComponentBuilder builder = new ComponentBuilder();
        builder.append("§2modrinth/")
                .event(BuildTools.modrinthHover(meta))
                .event(BuildTools.modrinthClick(meta));
        builder.append("§a" + meta.getSlug())
                .event(BuildTools.projetHover(meta, false));
        builder.append(" §8< §2Versions: §a" + totalVersions + " §8| §7Page " + page + "/" + totalPages + " §8>");
        if (showAll) {
            builder.append(" §8<§6--all§8>");
        }
        return builder.create();
    }

    public static BaseComponent[] buildVersionLine(ProjectMeta meta, VersionInfo versionInfo) {
        String branchColor = SetColor.setColor(versionInfo.getBranch());

        ComponentBuilder builder = new ComponentBuilder();
        builder.append("  §8- ");
        builder.append(branchColor + versionInfo.getVersionNumber())
                .event(BuildTools.versionHover(versionInfo))
                .event(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/plugget -S " + meta.getSlug() + " --vl " + versionInfo.getVersionNumber()
                ));

        builder.append(" §8| §7" + TextTools.formatDate(versionInfo.getDatePublished()))
                .event(BuildTools.versionHover(versionInfo))
                .event(new ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://modrinth.com/plugin/" + meta.getSlug() + "/version/" + versionInfo.getVersionId()
                ));

        return builder.create();
    }

    public static BaseComponent[] buildNavigation(String slug, int page, int totalPages, boolean showAll) {
        String flag = showAll ? " --all" : "";
        ComponentBuilder nav = new ComponentBuilder();
        nav.append(" ");

        if (page > 1) {
            nav.append("§8<-- ")
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pg versions " + slug + " " + (page - 1) + flag))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Previous page").create()));
        } else {
            nav.append("§8<-- ").event((HoverEvent) null);
        }

        nav.append("§8Page " + page + "/" + totalPages);

        if (page < totalPages) {
            nav.append(" §8-->")
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pg versions " + slug + " " + (page + 1) + flag))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Next page").create()));
        } else {
            nav.append(" §8-->").event((HoverEvent) null);
        }
        return nav.create();
    }
}
