package me.pinkcandy.plugGet.install;

import me.pinkcandy.plugGet.commands.ActionLock;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchProjects;
import me.pinkcandy.plugGet.messagesBuilders.BuildInstallInfo;
import me.pinkcandy.plugGet.model.DependencyInfo;
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

    public static List<PluginData> plugins;

    public static void prepareInstall(List<InstallInfo> pluginsToInstall, CommandSender sender) {
        plugins = new ArrayList<>();
        sender.sendMessage("§8:: §7Fetching plugins and versions...");
        for (int i = 0; i < pluginsToInstall.size(); i++)
        {
            JSONObject obj = FetchProjects.fetchProject(pluginsToInstall.get(i).getSlug());
            if (obj == null) {
                sender.sendMessage("§cPlugin " + pluginsToInstall.get(i).getSlug() + " not found. Is the slug correct?");
                ActionLock.release();
                return;
            }
            else {
                if (preparePluginData(pluginsToInstall.get(i), sender) != true)
                {
                    ActionLock.release();
                    return;
                }
            }
        }
        sender.sendMessage("");
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

    public static boolean preparePluginData(InstallInfo installInfo, CommandSender sender) {
        VersionInfo versionInfo = GetNewestVersion.getNewestVersionForInstallType(installInfo);
        if (versionInfo == null) {
            if (installInfo.getInstallType().equals("latest")) {
                sender.sendMessage("§cNo compatible versions found for " + installInfo.getSlug());
            } else if (installInfo.getInstallType().equals("version-latest") || installInfo.getInstallType().equals("version") || installInfo.getInstallType().equals("version-rolling")) {
                sender.sendMessage("§cVersion " + installInfo.getVersion() + " not found for " + installInfo.getSlug());
            } else {
                sender.sendMessage("§cNo " + installInfo.getInstallType() + " version found for " + installInfo.getSlug());
            }
            return false;
        } else {
            if (!versionInfo.getDependencies().isEmpty()) {
                sender.sendMessage("§8:: §7Resolving Dependencies for " + installInfo.getSlug() + "...");

                for (int i = 0; i < versionInfo.getDependencies().size(); i++) {
                    DependencyInfo dependencyInfo = versionInfo.getDependencies().get(i);
                    if (dependencyInfo.getType().equals("required")) {
                        String depVersionId = dependencyInfo.getVersionID();
                        String displayDepVersion = (depVersionId == null || depVersionId.trim().isEmpty()) ? "<unspecified>" : depVersionId;
                        if (depVersionId != null && !depVersionId.trim().isEmpty()) {
                            String dpSlug = FetchProjects.projectIDToSlug(dependencyInfo.getProjectID());
                            InstallInfo dpInstallInfo = new InstallInfo(
                                    dpSlug,
                                    "version",
                                    depVersionId
                            );
                            VersionInfo dpVersionInfo = GetNewestVersion.getNewestVersionForInstallType(dpInstallInfo);
                            if (dpVersionInfo != null) {
                                boolean ok = preparePluginData(dpInstallInfo, sender);
                                dependencyInfo.setSlug(dpSlug);
                                if (!ok) return false;
                                continue;
                            }
                            sender.sendMessage("§cRequired dependency version " + displayDepVersion +
                                    " not found for " + dpInstallInfo.getSlug());
                            return false;
                        } else {

                            String dpSlug = FetchProjects.projectIDToSlug(dependencyInfo.getProjectID());
                            InstallInfo dpInstallInfo = new InstallInfo(
                                    FetchProjects.projectIDToSlug(dependencyInfo.getProjectID()),
                                    "latest",
                                    null
                            );
                            VersionInfo dpVersionInfo = GetNewestVersion.getNewestVersionForInstallType(dpInstallInfo);
                            if (dpVersionInfo != null) {
                                boolean ok = preparePluginData(dpInstallInfo, sender);
                                dependencyInfo.setSlug(dpSlug);
                                if (!ok) return false;
                                continue;
                            }
                            sender.sendMessage("§cNo compatible versions found for dependency " +
                                    dpInstallInfo.getSlug());
                            return false;
                        }
                    } else if (dependencyInfo.getType().equals("optional")) {
                        sender.sendMessage("§8:: §7Plugin " +
                                FetchProjects.projectIDToSlug(dependencyInfo.getProjectID()) +
                                " is a optional dependency for " +
                                installInfo.getSlug() +
                                ".\n Would you like to add it? §8[Y/N]");
                    }
                }
            }
            plugins.add(new PluginData(installInfo, versionInfo));
            return true;
        }
    }
}