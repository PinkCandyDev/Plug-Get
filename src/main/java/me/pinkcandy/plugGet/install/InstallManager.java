package me.pinkcandy.plugGet.install;

import me.pinkcandy.plugGet.api.modrinth.fetch.FetchProjects;
import me.pinkcandy.plugGet.api.modrinth.map.ProjectMapper;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.ProjectMeta;
import me.pinkcandy.plugGet.model.VersionInfo;
import me.pinkcandy.plugGet.version.GetNewestVersion;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class InstallManager {

    public static void manageInstall(List<InstallInfo> pluginsToInstall, CommandSender sender) {
        List<ProjectMeta> project = new ArrayList<>();
        for (int i = 0; i < pluginsToInstall.size(); i++)
        {
            ProjectMeta meta = ProjectMapper.fromJson(FetchProjects.fetchProject(pluginsToInstall.get(i).getSlug()));
            if (meta == null) {
                sender.sendMessage("§cPlugin " + pluginsToInstall.get(i).getSlug() + " not found. Is the slug correct?");
                return;
            }
            project.add(meta);
        }
        List<VersionInfo> versionsToInstall = new ArrayList<>();
        for (int i = 0; i < pluginsToInstall.size(); i++) {
            VersionInfo versionInfo = GetNewestVersion.getNewestVersionForInstallType(pluginsToInstall.get(i));
            if (versionInfo == null) {
                if (pluginsToInstall.get(i).getInstallType().equals(""))
                {
                    sender.sendMessage("§cNo compatible versions found for " + pluginsToInstall.get(i).getSlug());
                }
                else if (pluginsToInstall.get(i).getInstallType().equals("version-latest") || pluginsToInstall.get(i).getInstallType().equals("version"))
                {
                    sender.sendMessage("§cVersion " + pluginsToInstall.get(i).getVersion() + " not found for " + pluginsToInstall.get(i).getSlug());
                }
                else {
                    sender.sendMessage("§cNo " + pluginsToInstall.get(i).getInstallType() + " version found for " + pluginsToInstall.get(i).getSlug());
                }
                return;
            }
            else
            {
                versionsToInstall.add(versionInfo);
            }
        }



    }



    public boolean installPlugins(List<String[]> pluginsToInstall, List<String[]> versionsToInstall, CommandSender sender) {
        InstallHelper installHelper = new InstallHelper();

        boolean continueInstall = true;

        sender.sendMessage("§8:: §7Downloading files...");
        continueInstall = installHelper.manageDownload(pluginsToInstall, versionsToInstall, sender);
        if (!continueInstall) {return false;}

        sender.sendMessage("§8:: §7Verifying files...");
        continueInstall = installHelper.manageVerification(pluginsToInstall, versionsToInstall, sender);
        if (!continueInstall) {return false;}

        sender.sendMessage("§8:: §7Copying Files...");
        continueInstall = installHelper.manageCopy(pluginsToInstall, versionsToInstall, sender);
        if (!continueInstall) {return false;}

        sender.sendMessage("§8:: §7Registering in db...");
        continueInstall = installHelper.manageRegisteringDB(pluginsToInstall, versionsToInstall, sender);
        if (!continueInstall) {return false;}

        return continueInstall;
    }
}
