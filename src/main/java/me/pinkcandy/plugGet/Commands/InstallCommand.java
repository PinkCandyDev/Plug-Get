package me.pinkcandy.plugGet.Commands;

import me.pinkcandy.plugGet.ActionLock;
import me.pinkcandy.plugGet.Install.InstallManager;
import me.pinkcandy.plugGet.VersionControll.BranchSelector;
import me.pinkcandy.plugGet.Install.SendInstallInfo;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class InstallCommand {

    public boolean execute(CommandSender sender, String[] args) {
        List<String[]> plugins = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            String slug = args[i];

            boolean invalidArg = false;
            String modifier = "";
            String version = null;

            if (slug.startsWith("--")) {
                sender.sendMessage("§4Wrong argument: " + slug);
                invalidArg = true;
                return true;
            }

            if (i + 1 < args.length && invalidArg==false) {
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
                        return true;
                    }
                    modifier = "version";
                    version = args[i + 2];
                    i += 2;
                } else if (next.equals("--vl")) {
                    if (i + 2 >= args.length) {
                        sender.sendMessage("§4No version provided after --vl " + slug);
                        return true;
                    }
                    modifier = "version-latest";
                    version = args[i + 2];
                    i += 2;
                }

            }
            plugins.add (new String[] {slug, modifier, version});
        }

        List<String[]> versionsToInstall = new BranchSelector().selectBranch(sender, plugins);
        if (versionsToInstall == null)
        {
            sender.sendMessage("§4Installation aborted due to errors.");
            ActionLock.release();
            return true;
        }
        SendInstallInfo sendInstallInfo = new SendInstallInfo();
        sendInstallInfo.sendInstallInfo(sender, plugins,versionsToInstall);

        InstallManager installManager = new InstallManager();

        ActionLock.confirm = () -> {
            boolean continueInstall = installManager.installPlugins(plugins, versionsToInstall, sender);
            if (continueInstall) {
                sender.sendMessage("§8:: §7Cleaning up...");
                sender.sendMessage("§a All plugins installed successfully!");
                ActionLock.release();
            } else {
                sender.sendMessage("§8:: §7Cleaning up...");
                sender.sendMessage("§4Installation aborted due to errors.");
                ActionLock.release();
            }
        };
        ActionLock.deny = () -> {
            sender.sendMessage("§cInstallation cancelled.");
            ActionLock.release();
        };
        return true;
    }
}