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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;

import static me.pinkcandy.plugGet.PlugGet.plugincCacheFolder;
import static me.pinkcandy.plugGet.PlugGet.tmpFolder;

public class InstallHelper {

        public static boolean manageDownload(VersionInfo versionInfo, String slug, int i, int size, CommandSender sender) {
            sender.sendMessage("§8:: §7Downloading: " + versionInfo.getFileName() + " §8(" + (1 + i) + "/" + size + ")");
            boolean success = FileDownloader.downloadFile(versionInfo.getDownloadUrl(), versionInfo.getFileName(),versionInfo.getVersionNumber(), slug, sender);
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
            try (InputStream is = Files.newInputStream(targetFile)) {
                String fileHash = sha512Hex(is);
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
            Path cache = plugincCacheFolder.resolve(slug + "/" + versionInfo.getVersionNumber() + "/" + versionInfo.getFileName());
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

        public static String sha512Hex(InputStream is) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-512");

                byte[] buffer = new byte[8192];
                int read;

                while ((read = is.read(buffer)) != -1) {
                    digest.update(buffer, 0, read);
                }

                byte[] hash = digest.digest();

                StringBuilder hex = new StringBuilder();
                for (byte b : hash) {
                    hex.append(String.format("%02x", b));
                }

                return hex.toString();

            } catch (Exception e) {
                throw new RuntimeException("Failed to compute SHA-512", e);
            }
        }
}
