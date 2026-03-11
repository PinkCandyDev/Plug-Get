package me.pinkcandy.plugGet.commands;

import me.pinkcandy.plugGet.install.InstallationPreparer;
import me.pinkcandy.plugGet.model.InstallInfo;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class InstallCommand {

    public static boolean execute(CommandSender sender, String[] args) {
        List<InstallInfo> pluginsToInstall = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            String slug = args[i];

            boolean invalidArg = false;
            String modifier = "best";
            String version = null;

            if (slug.startsWith("--")) {
                sender.sendMessage("§4Wrong argument: " + slug);
                invalidArg = true;
                ActionLock.release();
                return true;
            }

            if (i + 1 < args.length && invalidArg == false) {
                String next = args[i + 1].toLowerCase();

                if (next.equals("--beta")) {
                    modifier = "beta";
                    i++;
                } else if (next.equals("--alpha")) {
                    modifier = "alpha";
                    i++;
                } else if (next.equals("--v")) {
                    if (i + 2 >= args.length) {
                        sender.sendMessage("§4No version provided after --v " + slug);
                        ActionLock.release();
                        return true;
                    }
                    modifier = "version";
                    version = args[i + 2];
                    i += 2;
                } else if (next.equals("--vl")) {
                    if (i + 2 >= args.length) {
                        sender.sendMessage("§4No version provided after --vl " + slug);
                        ActionLock.release();
                        return true;
                    }
                    modifier = "version-latest";
                    version = args[i + 2];
                    i += 2;
                }

            }
            pluginsToInstall.add(new InstallInfo(slug, modifier, version));
        }

        InstallationPreparer.prepareInstall(pluginsToInstall, sender);
        return true;
//        List<String[]> versionsToInstall = new BranchSelector().selectBranch(sender, pluginsToInstall);
//        if (versionsToInstall == null)
//        {
//            sender.sendMessage("§4Installation aborted due to errors.");
//            ActionLock.release();
//            return true;
//        }
//        BuildInstallInfo buildInstallInfo = new BuildInstallInfo();
//        buildInstallInfo.buildInstallInfo(sender, pluginsToInstall,versionsToInstall);
//        InstallManager installManager = new InstallManager();
//        CleanUp cleanUp = new CleanUp();
//        boolean[] reinstall = {false};
//
//        ActionLock.confirm = () -> {
//            boolean continueInstall = installManager.installPlugins(pluginsToInstall, versionsToInstall, sender);
//            if (continueInstall) {
//                sender.sendMessage("§8:: §7Cleaning up...");
//                cleanUp.succesClean(versionsToInstall.stream().map(a -> a[6]).toArray(String[]::new));
//                sender.sendMessage("§a All pluginsToInstall installed successfully!");
//                ActionLock.release();
//            } else {
//                sender.sendMessage("§8:: §7Cleaning up...");
//                cleanUp.failureInstallCleanUp(pluginsToInstall, versionsToInstall, reinstall);
//                sender.sendMessage("§4Installation aborted due to errors.");
//                ActionLock.release();
//            }
//        };
//        ActionLock.deny = () -> {
//            sender.sendMessage("§cInstallation cancelled.");
//            ActionLock.release();
//        };
//        return true;
//    }
    }
}