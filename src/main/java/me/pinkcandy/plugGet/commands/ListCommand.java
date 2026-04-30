package me.pinkcandy.plugGet.commands;

import me.pinkcandy.plugGet.PlugGet;
import me.pinkcandy.plugGet.db.DBManager;
import me.pinkcandy.plugGet.messagesBuilders.BuildListInfo;
import me.pinkcandy.plugGet.model.PluginData;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListCommand {
    public static void execute(CommandSender sender)
    {
        List<PluginData> installedPlugins = DBManager.getInstalledPlugins();

        File folder = new File("plugins");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".jar"));

        List<String> installedFiles = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                installedFiles.add(file.getName());
            }
        }
        List<BaseComponent[]> messages = BuildListInfo.buildListInfo(installedPlugins, installedFiles);
        for (int i = 0; i < messages.size(); i++) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.spigot().sendMessage(messages.get(i));
            } else {
                sender.sendMessage(BaseComponent.toLegacyText(messages.get(i)));
            }
        }
    }
}
