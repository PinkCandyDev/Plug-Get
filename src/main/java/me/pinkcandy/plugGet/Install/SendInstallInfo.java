package me.pinkcandy.plugGet.Install;

import me.pinkcandy.plugGet.ActionLock;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import me.pinkcandy.plugGet.TextTools;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SendInstallInfo {

    public void sendInstallInfo(CommandSender sender, List<String[]> plugins, List<String[]> versions) {

        List<Integer> pLetters = new ArrayList<>();
        for (int i = 0; i < plugins.size(); i++) {
            pLetters.add(TextTools.countLetters(plugins.get(i)[0]));
        }

        int maxPLetters = pLetters.stream().max(Integer::compareTo).orElse(0);

        String pSpace = TextTools.generateSpaces(maxPLetters + 2);

        ComponentBuilder header = new ComponentBuilder();
        header.append("§2Plugins (" + plugins.size() + ")" + pSpace + "§8< §2Version §8| §3Size§8 >");
        sender.spigot().sendMessage(header.create());
        sender.sendMessage("");

        for (int i = 0; i < plugins.size(); i++) {
            ComponentBuilder lineBuilder = new ComponentBuilder();

            int spacesNeeded = maxPLetters - pLetters.get(i) + 2;
            String maxPSpace = TextTools.generateSpaces(spacesNeeded);

            String versionColor;
            switch (versions.get(i)[8]) {
                case "release" -> versionColor = "§a";
                case "beta" -> versionColor = "§e";
                case "alpha" -> versionColor = "§c";
                default -> versionColor = "§f";
            }

            String line = "§2modrinth/§a" + plugins.get(i)[0] + maxPSpace
                    + "§8< " + versionColor + versions.get(i)[0] + " §8| §3" + versions.get(i)[4] + " §8>";

            lineBuilder.append(line);
            sender.spigot().sendMessage(lineBuilder.create());
        }

        sender.sendMessage("");
        ComponentBuilder footer = new ComponentBuilder();
        footer.append("§8:: §7Continue installation? §8[");
        footer.append("§8Y").event(new net.md_5.bungee.api.chat.ClickEvent(
                net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND,
                "/plugget y"
        ));
        footer.append("§8/").append("§8N").event(new net.md_5.bungee.api.chat.ClickEvent(
                net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND,
                "/plugget n"
        ));
        footer.append("§8]");
        ActionLock.isConfirming = true;
        sender.spigot().sendMessage(footer.create());
    }
}
