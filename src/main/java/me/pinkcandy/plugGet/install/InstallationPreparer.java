package me.pinkcandy.plugGet.install;

import me.pinkcandy.plugGet.api.modrinth.fetch.FetchHelper;
import me.pinkcandy.plugGet.commands.ActionLock;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchProjects;
import me.pinkcandy.plugGet.messagesBuilders.BuildInstallInfo;
import me.pinkcandy.plugGet.model.*;
import me.pinkcandy.plugGet.version.CompareVersions;
import me.pinkcandy.plugGet.version.DependencyResolver;
import me.pinkcandy.plugGet.version.GetNewestVersion;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.pinkcandy.plugGet.install.InstallPlugins.installPlugins;

public class InstallationPreparer {
    public static List<PluginData> plugins;

    public static void prepareInstall(List<InstallInfo> pluginsToInstall, CommandSender sender) {
        plugins = new ArrayList<>();
        sender.sendMessage("§8:: §7Fetching plugins and versions...");
        for (int i = 0; i < pluginsToInstall.size(); i++) {
            ProjectMeta meta = FetchHelper.getProject(pluginsToInstall.get(i).getSlug());
            if (meta == null) {
                sender.sendMessage("§cPlugin " + pluginsToInstall.get(i).getSlug() + " not found. Is the slug correct?");
                ActionLock.release();
                return;
            } else {
                if (prepareInstall(pluginsToInstall.get(i), sender) != true) {
                    ActionLock.release();
                    return;
                }
            }
        }
        Collections.reverse(plugins);
        sender.sendMessage("test" + plugins.get(0).getStatus(), plugins.get(0).getVersionInfo().getVersionNumber());
        List<BaseComponent[]> messages = BuildInstallInfo.buildInstallInfo(plugins);
        ActionLock.isConfirming = true;
        for (int i = 0; i < messages.size(); i++) {
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

    public static boolean prepareInstall(InstallInfo installInfo, CommandSender sender) {
        VersionInfo versionInfo = GetNewestVersion.getNewestVersionForInstallType(installInfo);
        if (versionInfo == null) {
            if (installInfo.getInstallType().equals("latest")) {
                sender.sendMessage("§cNo compatible versions found for " + installInfo.getSlug());
            } else if (installInfo.getInstallType().equals("version-latest") || installInfo.getInstallType().equals("version") || installInfo.getInstallType().equals("version-rolling")) {
                sender.sendMessage("§cVersion " + installInfo.getVersion() + " not found for " + installInfo.getSlug());
            } else {
                sender.sendMessage("§cNo " + installInfo.getInstallType() + " version found for " + installInfo.getSlug());
            }
            return true;
        }
        String status = CompareVersions.getStatus(versionInfo, installInfo.getSlug());
        PluginData pluginData = new PluginData(
                installInfo,
                versionInfo,
                status
        );
        DependencyResolver.resolveDependencies(pluginData, sender);
        plugins.add(pluginData);
        return true;
    }
}