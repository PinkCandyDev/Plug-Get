package me.pinkcandy.plugGet.Download;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import me.pinkcandy.plugGet.PlugGet;
import org.bukkit.plugin.java.JavaPlugin;

import static me.pinkcandy.plugGet.PlugGet.tmpFolder;
import static org.apache.commons.codec.digest.DigestUtils.sha512Hex;

public class FileDownloader {

    public static boolean downloadFile(String urlString, String fileName) {
        Path targetFile = tmpFolder.resolve(fileName);
        try {
            Files.createDirectories(tmpFolder);

            if (Files.exists(targetFile)) {
                Files.delete(targetFile);
            }

            Files.copy(new URL(urlString).openStream(), targetFile);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean verifyFile(String fileName, String hash) {
        Path targetFile = tmpFolder.resolve(fileName);

        try {
            String fileHash = sha512Hex(Files.newInputStream(targetFile));
            return fileHash.equalsIgnoreCase(hash);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
