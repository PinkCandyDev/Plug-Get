package me.pinkcandy.plugGet.commands;

import me.pinkcandy.plugGet.ConfigManager;
import org.bukkit.command.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand {

    public static void execute(CommandSender sender, String[] args) {
        int page = 1;
        if (args != null && args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
                page = 1;
            }
        }

        List<BaseComponent[]> pages = buildPages();
        if (page < 1) page = 1;
        if (page > pages.size()) page = pages.size();

        sender.sendMessage(pages.get(page - 1));

        ComponentBuilder nav = new ComponentBuilder();
        nav.append(" ");

        if (page > 1) {
            nav.append("§8<-- ")
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pg help " + (page - 1)))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Previous page").create()));
        } else {
            nav.append("§8<-- ")
                    .event((HoverEvent) null);
        }

        nav.append("§8Page " + page + "/" + pages.size());

        if (page < pages.size()) {
            nav.append(" §8-->")
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pg help " + (page + 1)))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Next page").create()));
        } else {
            nav.append(" §8-->")
                    .event((HoverEvent) null);
        }
        sender.sendMessage(nav.create());
    }

    private static List<BaseComponent[]> buildPages() {
        List<BaseComponent[]> pages = new ArrayList<>();

        boolean isApt = ConfigManager.getTabMode() == ConfigManager.TabMode.APT;

        ComponentBuilder p1 = new ComponentBuilder();
        if (isApt) {
            p1.append("§2§l=== §aPlugGet Help (apt) §2===\n");
            p1.append("§a/pg help §2<§apage§2> §7- Show help\n");
            p1.append("§a/pg search §2<§aslug§2> §7- Search for plugin\n");
            p1.append("§a/pg install §2<§aslug§2> §7- Install plugin\n");
            p1.append("§a/pg update §7- Update installed plugins\n");
            p1.append("§a/pg remove §2<§aslug§2> §7- Remove plugin\n");
            p1.append("§a/pg autoremove §2<§aslug§2> §7- Auto-remove unused deps\n");
            p1.append("§a/pg purge §2<§aslug§2> §7- Remove plugin & config\n");
            p1.append("§a/pg y §7- Confirm action\n");
            p1.append("§a/pg n §7- Deny action");
        } else {
            p1.append("§2§l=== §aPlugGet Help (pacman) §2===\n");
            p1.append("§a-h §7- Show help\n");
            p1.append("§a-Ss §2<§aslug§2> §7- Search for plugin\n");
            p1.append("§a-S §2<§apkg§2> §7- Install plugin\n");
            p1.append("§a-Syu §7- Update installed plugins\n");
            p1.append("§a-R §2<§apkg§2> §7- Remove plugin\n");
            p1.append("§a-Rs §2<§apkg§2> §7- Auto-remove unused deps\n");
            p1.append("§a--purge §2<§apkg§2> §7- Remove plugin & config\n");
            p1.append("§a-y §7- Confirm action\n");
            p1.append("§a-n §7- Deny action");
        }
        pages.add(p1.create());

        ComponentBuilder p2 = new ComponentBuilder();
        p2.append("§2§l=== §aInstall arguments §2===\n");
        p2.append("§7Example: §a/pg install §2<§aslug§2> §2<§aarg(optional)§2>\n");
        p2.append("§a--latest §7- Prefer stable channels (default)\n");
        p2.append("§a--rolling §7- Always newest\n");
        p2.append("§a--beta §7- Stay on beta channel\n");
        p2.append("§a--alpha §7- Stay on alpha channel\n");
        p2.append("§a--v §2<§aver§2> §7- Install specific version\n");
        p2.append("§a--vl §2<§aver§2> §7- Install specific as latest type\n");
        p2.append("§a--vr §2<§aver§2> §7- Install specific as rolling type");
        pages.add(p2.create());

        return pages;
    }

}
