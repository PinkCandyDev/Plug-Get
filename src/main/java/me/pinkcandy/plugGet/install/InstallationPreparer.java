package me.pinkcandy.plugGet.install;

import me.pinkcandy.plugGet.commands.ActionLock;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchProjects;
import me.pinkcandy.plugGet.messagesBuilders.BuildInstallInfo;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.PluginData;
import me.pinkcandy.plugGet.model.VersionInfo;
import me.pinkcandy.plugGet.version.GetNewestVersion;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static me.pinkcandy.plugGet.install.InstallPlugins.installPlugins;

public class InstallationPreparer {

    public static void prepareInstall(List<InstallInfo> pluginsToInstall, CommandSender sender) {
        for (int i = 0; i < pluginsToInstall.size(); i++)
        {
            JSONObject obj = FetchProjects.fetchProject(pluginsToInstall.get(i).getSlug());
            if (obj == null) {
                sender.sendMessage("§cPlugin " + pluginsToInstall.get(i).getSlug() + " not found. Is the slug correct?");
                ActionLock.release();
                return;
            }
        }
        List<VersionInfo> versionsToInstall = new ArrayList<>();
        for (int i = 0; i < pluginsToInstall.size(); i++) {
            VersionInfo versionInfo = GetNewestVersion.getNewestVersionForInstallType(pluginsToInstall.get(i));
            if (versionInfo == null) {
                if (pluginsToInstall.get(i).getInstallType().equals("latest"))
                {
                    sender.sendMessage("§cNo compatible versions found for " + pluginsToInstall.get(i).getSlug());
                    ActionLock.release();
                }
                else if (pluginsToInstall.get(i).getInstallType().equals("version-latest") || pluginsToInstall.get(i).getInstallType().equals("version") || pluginsToInstall.get(i).getInstallType().equals("version-rolling"))
                {
                    sender.sendMessage("§cVersion " + pluginsToInstall.get(i).getVersion() + " not found for " + pluginsToInstall.get(i).getSlug());
                    ActionLock.release();
                }
                else {
                    sender.sendMessage("§cNo " + pluginsToInstall.get(i).getInstallType() + " version found for " + pluginsToInstall.get(i).getSlug());
                    ActionLock.release();
                }
                return;
            }
            else
            {
                versionsToInstall.add(versionInfo);
            }
        }

        List<PluginData> plugins = new ArrayList<>();
        for (int i = 0; i < pluginsToInstall.size(); i++) {
            plugins.add(new PluginData(pluginsToInstall.get(i), versionsToInstall.get(i)));
        }

        List<BaseComponent[]> messages = BuildInstallInfo.buildInstallInfo(plugins);
        ActionLock.isConfirming = true;
        for (int i = 0; i < messages.size(); i++)
        {
            sender.spigot().sendMessage(messages.get(i));
        }
        ActionLock.confirm = () -> {
            boolean completed = installPlugins(plugins, sender);
            ActionLock.release();

        };
        ActionLock.deny = () -> {
            sender.sendMessage("§cInstallation cancelled.");
            ActionLock.release();
        };
    }
}