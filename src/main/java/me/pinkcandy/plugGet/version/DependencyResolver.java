package me.pinkcandy.plugGet.version;

import me.pinkcandy.plugGet.ConfigManager;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchHelper;
import me.pinkcandy.plugGet.install.InstallationPreparer;
import me.pinkcandy.plugGet.model.DependencyInfo;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.PluginData;
import me.pinkcandy.plugGet.model.VersionInfo;
import org.bukkit.command.CommandSender;

public class DependencyResolver {
    public static void resolveDependencies(PluginData pluginData, CommandSender sender) {
        InstallInfo installInfo = pluginData.getInstallInfo();
        VersionInfo versionInfo = pluginData.getVersionInfo();
        if (!versionInfo.getDependencies().isEmpty()) {
            sender.sendMessage("§8:: §7Resolving Dependencies for " + installInfo.getSlug() + "...");
            for (int i = 0; i < versionInfo.getDependencies().size(); i++) {
                DependencyInfo dependencyInfo = versionInfo.getDependencies().get(i);
                if (dependencyInfo.getType().equals("required") || ConfigManager.requiredDependencies) {
                    String depVersionId = dependencyInfo.getVersionID();
                    String displayDepVersion = (depVersionId == null || depVersionId.trim().isEmpty()) ? "<unspecified>" : depVersionId;
                    if (depVersionId != null && !depVersionId.trim().isEmpty()) {
                        String dpSlug = FetchHelper.projectIDToSlug(dependencyInfo.getProjectID());
                        InstallInfo dpInstallInfo = new InstallInfo(
                                dpSlug,
                                "version",
                                depVersionId
                        );
                        VersionInfo dpVersionInfo = GetNewestVersion.getNewestVersionForInstallType(dpInstallInfo);
                        if (dpVersionInfo != null) {
                            boolean ok = InstallationPreparer.prepareInstall(dpInstallInfo, sender);
                            dependencyInfo.setSlug(dpSlug);
                        }
                        sender.sendMessage("§cRequired dependency version " + displayDepVersion +
                                " not found for " + dpInstallInfo.getSlug());
                    } else {
                        String dpSlug = FetchHelper.projectIDToSlug(dependencyInfo.getProjectID());
                        InstallInfo dpInstallInfo = new InstallInfo(
                                dpSlug,
                                "latest",
                                null
                        );
                        VersionInfo dpVersionInfo = GetNewestVersion.getNewestVersionForInstallType(dpInstallInfo);
                        if (dpVersionInfo != null) {
                            boolean ok = InstallationPreparer.prepareInstall(dpInstallInfo, sender);
                            dependencyInfo.setSlug(dpSlug);
                            continue;
                        }
                        sender.sendMessage("§cNo compatible versions found for dependency " +
                                dpInstallInfo.getSlug());
                    }
                } else if (dependencyInfo.getType().equals("optional") || ConfigManager.optionalDependencies) {
                    String depVersionId = dependencyInfo.getVersionID();
                    String displayDepVersion = (depVersionId == null || depVersionId.trim().isEmpty()) ? "<unspecified>" : depVersionId;
                    if (depVersionId != null && !depVersionId.trim().isEmpty()) {
                        String dpSlug = FetchHelper.projectIDToSlug(dependencyInfo.getProjectID());
                        InstallInfo dpInstallInfo = new InstallInfo(
                                dpSlug,
                                "version",
                                depVersionId
                        );
                        VersionInfo dpVersionInfo = GetNewestVersion.getNewestVersionForInstallType(dpInstallInfo);
                        if (dpVersionInfo != null) {
                            boolean ok = InstallationPreparer.prepareInstall(dpInstallInfo, sender);
                            dependencyInfo.setSlug(dpSlug);
                        }
                        sender.sendMessage("§cRequired dependency version " + displayDepVersion +
                                " not found for " + dpInstallInfo.getSlug());
                    } else {
                        String dpSlug = FetchHelper.projectIDToSlug(dependencyInfo.getProjectID());
                        InstallInfo dpInstallInfo = new InstallInfo(
                                dpSlug,
                                "latest",
                                null
                        );
                        VersionInfo dpVersionInfo = GetNewestVersion.getNewestVersionForInstallType(dpInstallInfo);
                        if (dpVersionInfo != null) {
                            boolean ok = InstallationPreparer.prepareInstall(dpInstallInfo, sender);
                            dependencyInfo.setSlug(dpSlug);
                            continue;
                        }
                        sender.sendMessage("§cNo compatible versions found for dependency " +
                                dpInstallInfo.getSlug());
                    }
                }
            }
        }
    }
}
