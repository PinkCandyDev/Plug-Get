package me.pinkcandy.plugGet.Install;

import me.pinkcandy.plugGet.Download.FileDownloader;
import me.pinkcandy.plugGet.PlugGet;
import org.bukkit.command.CommandSender;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static me.pinkcandy.plugGet.DB.DBManager.*;
import static me.pinkcandy.plugGet.DB.JsonConverter.pluginToJson;
import static me.pinkcandy.plugGet.PlugGet.tmpFolder;

public class InstallHelper {

        public boolean manageDownload(List<String[]> plugins, List<String[]>versionsToInstall, CommandSender sender) {
            for (int i = 0; i < plugins.size(); i++) {
                sender.sendMessage("§8:: §7Downloading: " + versionsToInstall.get(i)[6] + " §8(" + (1 + i) + "/" + plugins.size() + ")");
                boolean success = FileDownloader.downloadFile(versionsToInstall.get(i)[5], versionsToInstall.get(i)[6]);
                if (!success) {
                    sender.sendMessage("§cFailed to download " + versionsToInstall.get(i)[6] + " for " + plugins.get(i)[0]);
                    return true;
                }
            }
            return true;
        }

        public boolean manageVerification (List<String[]> plugins, List<String[]> versionsToInstall, CommandSender sender) {
            for (int i = 0; i < plugins.size(); i++) {
                sender.sendMessage("§8:: §7Verifying: " + versionsToInstall.get(i)[6] + " §8(" + (i+1) + "/" + plugins.size() + ")");
                boolean verified = FileDownloader.verifyFile(versionsToInstall.get(i)[6], versionsToInstall.get(i)[7]);
                if (!verified) {
                    sender.sendMessage("§cFailed verification of file: " + versionsToInstall.get(i)[6] + "for " + plugins.get(i)[0]);
                    return false;
                }
            }
            return true;
        }

        public boolean manageCopy(List<String[]> plugins, List<String[]> versionsToInstall, CommandSender sender) {
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
                    return  false;
                }
            }
            return true;
        }

        public boolean manageRegisteringDB(List<String[]> plugins, List<String[]> versionsToInstall, CommandSender sender) {
            loadDB();
            loadDBToBackoupDB();
            for (int i = 0; i < plugins.size(); i++) {
                try {
                    JSONObject jP =  pluginToJson(plugins.get(i), versionsToInstall.get(i));
                    AddPlugin(jP);
                }
                catch (Exception e) {
                    sender.sendMessage("§cFailed to register plugin " + plugins.get(i)[0] + " in database.");
                    e.printStackTrace();
                    return false;
                }
            }
            try {
                saveDB();
                saveBackupDB();
                loadDB();
            }
            catch (Exception e) {
                sender.sendMessage("§cFailed to save database.");
                e.printStackTrace();
                return false;
            }
            return true;
        }
}
