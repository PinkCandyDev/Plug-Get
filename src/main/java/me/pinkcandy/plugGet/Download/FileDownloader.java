package me.pinkcandy.plugGet.Download;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import me.pinkcandy.plugGet.PlugGet;
import org.bukkit.plugin.java.JavaPlugin;

public class FileDownloader {

    public static boolean downloadFile(String urlString, String fileName) {

        Path tmpFolder = PlugGet.instance.getDataFolder().toPath().resolve("tmp");
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
}
