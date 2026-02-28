package me.pinkcandy.plugGet.install;

import org.bukkit.command.CommandSender;

import java.util.List;

public class InstallManager {

    public boolean installPlugins(List<String[]> plugins, List<String[]> versionsToInstall, CommandSender sender) {
        InstallHelper installHelper = new InstallHelper();

        boolean continueInstall = true;

        sender.sendMessage("§8:: §7Downloading files...");
        continueInstall = installHelper.manageDownload(plugins, versionsToInstall, sender);
        if (!continueInstall) {return false;}

        sender.sendMessage("§8:: §7Verifying files...");
        continueInstall = installHelper.manageVerification(plugins, versionsToInstall, sender);
        if (!continueInstall) {return false;}

        sender.sendMessage("§8:: §7Copying Files...");
        continueInstall = installHelper.manageCopy(plugins, versionsToInstall, sender);
        if (!continueInstall) {return false;}

        sender.sendMessage("§8:: §7Registering in db...");
        continueInstall = installHelper.manageRegisteringDB(plugins, versionsToInstall, sender);
        if (!continueInstall) {return false;}

        return continueInstall;
    }
}
