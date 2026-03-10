package me.pinkcandy.plugGet.Update;

import me.pinkcandy.plugGet.ServerInfo;
import me.pinkcandy.plugGet.commands.ActionLock;
import me.pinkcandy.plugGet.db.DBManager;
import me.pinkcandy.plugGet.delete.DeletePlugin;
import me.pinkcandy.plugGet.install.InstallPlugins;
import me.pinkcandy.plugGet.model.PluginData;
import org.bukkit.command.CommandSender;

import java.util.List;

public class InstallUpdates {
    public static boolean installUpdates(List<PluginData> pluginsToDelete, List<PluginData> pluginsToInstall, CommandSender sender){
        for (int i = 0; i < pluginsToDelete.size(); i++)
        {
            sender.sendMessage("§8:: §7Removing: " + pluginsToDelete.get(i).getVersionInfo().getFileName());
            DeletePlugin.deletePlugin(pluginsToDelete.get(i));
        }
        boolean succes = InstallPlugins.installPlugins(pluginsToInstall,sender);
        DBManager.replaceDB();
        return succes;
    }
}
