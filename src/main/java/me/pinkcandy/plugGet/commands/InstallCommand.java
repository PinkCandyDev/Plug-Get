package me.pinkcandy.plugGet.commands;

import me.pinkcandy.plugGet.ConfigManager;
import me.pinkcandy.plugGet.ThreadManager;
import me.pinkcandy.plugGet.install.InstallationPreparer;
import me.pinkcandy.plugGet.model.InstallInfo;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class InstallCommand {

    public static boolean execute(CommandSender sender, String[] args) {
        List<InstallInfo> pluginsToInstall = new ArrayList<>();
        if (args.length < 2) {
            sender.sendMessage("§cNo plugin slug provided. Usage: §7/pg install <slug> [options]");
            return true;
        }
        for (int i = 1; i < args.length; i++) {
            String slug = args[i];

            boolean invalidArg = false;
            String modifier = ConfigManager.installDefaultFlag.toString().toLowerCase();
            String version = null;

            if (slug.startsWith("--")) {
                sender.sendMessage("§4Wrong argument: " + slug);
                invalidArg = true;
                ActionLock.release();
                return true;
            }
            if (i + 1 < args.length && invalidArg == false) {
                String next = args[i + 1].toLowerCase();
                if (next.equals("--latest"))
                {
                    modifier = "latest";
                    i++;
                } else if (next.equals("--rolling")) {
                    modifier = "rolling";
                    i++;
                }
                if (next.equals("--stable")) {
                    modifier = "stable";
                    i++;
                } else if (next.equals("--beta")) {
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
                } else if (next.equals("--vr")) {
                    if (i + 2 >= args.length) {
                        sender.sendMessage("§4No version provided after --vr " + slug);
                        ActionLock.release();
                        return true;
                    }
                    modifier = "rolling";
                    version = args[i + 2];
                    i += 2;
                }


            }
            pluginsToInstall.add(new InstallInfo(slug, modifier, version));
        }

        ThreadManager.runAsync(() -> {
            InstallationPreparer.prepareInstall(pluginsToInstall, sender);
        });
        return true;
    }
}