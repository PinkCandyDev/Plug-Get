package me.pinkcandy.plugGet.commands;

import me.pinkcandy.plugGet.PlugGet;
import me.pinkcandy.plugGet.db.DBManager;
import me.pinkcandy.plugGet.messagesBuilders.BuildListInfo;
import me.pinkcandy.plugGet.model.PluginData;
import net.kyori.adventure.text.ComponentBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
            sender.spigot().sendMessage(messages.get(i));
        }
    }
}
