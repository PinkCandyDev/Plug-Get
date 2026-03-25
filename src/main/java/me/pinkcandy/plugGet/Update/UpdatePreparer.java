package me.pinkcandy.plugGet.Update;

import me.pinkcandy.plugGet.api.modrinth.fetch.FetchProjects;
import me.pinkcandy.plugGet.commands.ActionLock;
import me.pinkcandy.plugGet.db.DBManager;
import me.pinkcandy.plugGet.messagesBuilders.BuildUpdateInfo;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.PluginData;
import me.pinkcandy.plugGet.model.VersionInfo;
import me.pinkcandy.plugGet.version.CompareDate;
import me.pinkcandy.plugGet.version.GetNewestVersion;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdatePreparer {
    public static boolean execute(CommandSender sender) {
        sender.sendMessage("§8:: §7Checking for updates...");
        sender.sendMessage("§8:: §7Fetching plugins and versions...");
        List<PluginData> pluginsInDB = DBManager.getInstalledPlugins();
        List<PluginData> installedPlugins = new ArrayList<>();
        List<PluginData> pluginsToUpdate = new ArrayList<>();
        for (int i = 0; i < pluginsInDB.size(); i++) {
            InstallInfo installInfo = pluginsInDB.get(i).getInstallInfo();
            VersionInfo currentV = pluginsInDB.get(i).getVersionInfo();
            String slug = installInfo.getSlug();
            JSONObject obj = FetchProjects.fetchProject(slug);
            if (obj == null) {
                sender.sendMessage("§cInstalled plugin " + slug + " haven't been found on Modrinth. Does it still exist or mabey chainged the name?");
                ActionLock.release();
                continue;
            }

            List<VersionInfo> versions = new ArrayList<>();
            VersionInfo newestV = GetNewestVersion.getNewestVersionForInstallType(installInfo);
            if (newestV == null) {continue;}
            versions.add(newestV);
            versions.add(currentV);
            newestV =  CompareDate.compare(versions);
            if (newestV.getVersionNumber().equals(currentV.getVersionNumber()))
            {
                continue;
            }
            pluginsToUpdate.add(new PluginData(pluginsInDB.get(i).getInstallInfo(), newestV));
            installedPlugins.add(new PluginData(pluginsInDB.get(i).getInstallInfo(), currentV));
        }
        if (!installedPlugins.isEmpty() && !pluginsToUpdate.isEmpty()) {
            List<BaseComponent[]> messages = BuildUpdateInfo.buildUpdateInfo(installedPlugins, pluginsToUpdate);
            ActionLock.isConfirming = true;
            for (int i = 0; i < messages.size(); i++) {
                sender.spigot().sendMessage(messages.get(i));
            }
        }
        else {
            sender.sendMessage("§8:: §7All plugins are up to date");
            ActionLock.release();
            return true;
        }
        ActionLock.confirm= () -> {
            List<PluginData> pluginsToDelete = new ArrayList<>();
            List<PluginData> pluginsToInstall = new ArrayList<>();
            for (int i = 0; i < pluginsToUpdate.size(); i++)
            {
                if (ActionLock.numberList.contains(i)) {
                    continue;
                }
                pluginsToDelete.add(installedPlugins.get(i));
                pluginsToInstall.add(pluginsToUpdate.get(i));
            }

            InstallUpdates.installUpdates(pluginsToDelete, pluginsToInstall, sender);

            ActionLock.release();
        };
        ActionLock.deny = () -> {
            ActionLock.release();
        };
        return true;
    }
}
