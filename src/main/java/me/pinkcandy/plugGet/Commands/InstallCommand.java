package me.pinkcandy.plugGet.Commands;

import me.pinkcandy.plugGet.ActionLock;
import me.pinkcandy.plugGet.Download.FileDownloader;
import me.pinkcandy.plugGet.Install.InstallHelper;
import me.pinkcandy.plugGet.Install.SendInstallInfo;
import me.pinkcandy.plugGet.PlugGet;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static me.pinkcandy.plugGet.PlugGet.tmpFolder;

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

        List<String[]> versionsToInstall = new InstallHelper().BranchSelector(sender, plugins);
        SendInstallInfo sendInstallInfo = new SendInstallInfo();
        sendInstallInfo.sendInstallInfo(sender, plugins,versionsToInstall);

        ActionLock.confirm = () -> {
            boolean continueInstall = true;
            sender.sendMessage("§8:: §7Downloading files...");
            for (int i = 0; i < plugins.size(); i++) {
                sender.sendMessage("§8:: §7Downloading: " + versionsToInstall.get(i)[6] + " §8(" + (1+i) +"/" + plugins.size() + ")");
                boolean success = FileDownloader.downloadFile(versionsToInstall.get(i)[5], versionsToInstall.get(i)[6]);
                if (!success) {
                    sender.sendMessage("§cFailed to download " + versionsToInstall.get(i)[6] + " for " + plugins.get(i)[0]);
                    continueInstall = false;
                    break;
                }
            }

            if (continueInstall){
                sender.sendMessage("§8:: §7Verifying files...");
                for (int i = 0; i < plugins.size(); i++) {
                    sender.sendMessage("§8:: §7Verifying: " + versionsToInstall.get(i)[6] + " §8(" + (i+1) + "/" + plugins.size() + ")");
                    boolean verified = FileDownloader.verifyFile(versionsToInstall.get(i)[6], versionsToInstall.get(i)[7]);
                    if (!verified) {
                        sender.sendMessage("§cFailed verification of file: " + versionsToInstall.get(i)[6] + "for " + plugins.get(i)[0]);
                        continueInstall = false;
                        break;
                    }
                }
            }
            if (continueInstall) {
                for (int i = 0; i < plugins.size(); i++) {
                    Path cache = PlugGet.instance.getDataFolder().toPath().resolve("cache/plugins/" + plugins.get(i)[0] + "/" + versionsToInstall.get(i)[0] + "/" + versionsToInstall.get(i)[6]);
                    try {
                        Files.createDirectories(cache.getParent());
                        Files.copy(tmpFolder.resolve(versionsToInstall.get(i)[6]), cache, StandardCopyOption.REPLACE_EXISTING);
                        Files.copy(
                                cache,
                                PlugGet.instance.getDataFolder().getParentFile()
                                        .toPath()
                                        .resolve(versionsToInstall.get(i)[6]),
                                StandardCopyOption.REPLACE_EXISTING
                        );

                    } catch (IOException e) {
                        sender.sendMessage("§cFailed to move file to cache.");
                        e.printStackTrace();
                        continueInstall = false;
                        break;
                    }
                }
            }
            if (continueInstall) {
                sender.sendMessage("§8:: §7Copying files...");
                sender.sendMessage("§8:: §7Cleaning up...");
                sender.sendMessage("§a All plugins installed successfully!");
            }
            else {
                sender.sendMessage("§4Installation aborted due to errors.");
                ActionLock.release();
            }
            ActionLock.release();
        };
        ActionLock.deny = () -> {

        };


        return true;
    }
}