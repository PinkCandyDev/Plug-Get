package me.pinkcandy.plugGet.install;

import me.pinkcandy.plugGet.ActionLock;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchProjects;
import me.pinkcandy.plugGet.api.modrinth.map.ProjectMapper;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.ProjectMeta;
import me.pinkcandy.plugGet.model.VersionInfo;
import me.pinkcandy.plugGet.version.GetNewestVersion;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InstallManager {

    public static void manageInstall(List<InstallInfo> pluginsToInstall, CommandSender sender) {
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
                if (pluginsToInstall.get(i).getInstallType().equals(""))
                {
                    sender.sendMessage("§cNo compatible versions found for " + pluginsToInstall.get(i).getSlug());
                    ActionLock.release();
                }
                else if (pluginsToInstall.get(i).getInstallType().equals("version-latest") || pluginsToInstall.get(i).getInstallType().equals("version"))
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
        List<BaseComponent[]> messages = BuildInstallInfo.buildInstallInfo(pluginsToInstall, versionsToInstall);
        for (int i = 0; i < messages.size(); i++)
        {
            sender.spigot().sendMessage(messages.get(i));
        }
        ActionLock.release();
        return;
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
