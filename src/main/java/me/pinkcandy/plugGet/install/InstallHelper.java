package me.pinkcandy.plugGet.install;

import me.pinkcandy.plugGet.db.DBMapper;
import me.pinkcandy.plugGet.download.FileDownloader;
import me.pinkcandy.plugGet.PlugGet;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.PluginData;
import me.pinkcandy.plugGet.model.VersionInfo;
import org.bukkit.command.CommandSender;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static me.pinkcandy.plugGet.db.DBManager.*;
import static me.pinkcandy.plugGet.db.DBMapper.pluginToJson;
import static me.pinkcandy.plugGet.PlugGet.tmpFolder;
import static org.apache.commons.codec.digest.DigestUtils.sha512Hex;

public class InstallHelper {

        public static boolean manageDownload(VersionInfo versionInfo, String slug, int i, int size, CommandSender sender) {
            sender.sendMessage("§8:: §7Downloading: " + versionInfo.getFileName() + " §8(" + (1 + i) + "/" + size + ")");
            boolean success = FileDownloader.downloadFile(versionInfo.getDownloadUrl(), versionInfo.getFileName(), sender);
            if (!success) {
                sender.sendMessage("§cFailed to download " + versionInfo.getFileName() + " for " + slug);
                return false;
            }
            return true;
        }

        public static boolean manageVerification (VersionInfo versionInfo, String slug, int i, int size, CommandSender sender) {
            sender.sendMessage("§8:: §7Verifying: " + versionInfo.getFileName() + " §8(" + (i+1) + "/" + size + ")");
            boolean verified;
            Path targetFile = tmpFolder.resolve(versionInfo.getFileName());
            try {
                String fileHash = sha512Hex(Files.newInputStream(targetFile));
                verified = fileHash.equalsIgnoreCase(versionInfo.getSha512());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            if (!verified) {
                sender.sendMessage("§cFailed verification of file: " + versionInfo.getFileName() + "for " + slug);
                return false;
            }
            return true;
        }

        public static boolean manageCopy(VersionInfo versionInfo, String slug) {
            Path cache = PlugGet.instance.getDataFolder().toPath().resolve("cache/plugins/" + slug + "/" + versionInfo.getVersionNumber() + "/" + versionInfo.getFileName());
            try {
                Files.createDirectories(cache.getParent());
                Files.copy(tmpFolder.resolve(versionInfo.getFileName()), cache, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(
                        cache,
                        PlugGet.instance.getDataFolder().getParentFile()
                                .toPath()
                                .resolve(versionInfo.getFileName()),
                        StandardCopyOption.REPLACE_EXISTING
                );

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
}
