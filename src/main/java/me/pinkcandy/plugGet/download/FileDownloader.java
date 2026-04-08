package me.pinkcandy.plugGet.download;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.bukkit.command.CommandSender;

import static me.pinkcandy.plugGet.PlugGet.plugincCacheFolder;
import static me.pinkcandy.plugGet.PlugGet.tmpFolder;
import static org.apache.commons.codec.digest.DigestUtils.sha512Hex;

public class FileDownloader {

    public static boolean downloadFile(String urlString, String fileName, String versionNumber, String slug, CommandSender sender) {


        Path targetFile = tmpFolder.resolve(fileName);

        try {
            Path cache = plugincCacheFolder.resolve(slug + "/" + versionNumber + "/" + fileName);

            if (Files.exists(cache))
            {
                Files.copy(cache, tmpFolder.resolve(fileName));
                return true;
            }

            Files.createDirectories(tmpFolder);

            if (Files.exists(targetFile)) {
                Files.delete(targetFile);
            }

            Files.copy(new URL(urlString).openStream(), targetFile);

            return true;

        } catch (UnknownHostException e) {

            sender.sendMessage("§cNo internet connection or DNS lookup failed.");

        } catch (SocketTimeoutException e) {

            sender.sendMessage("§cConnection timed out. The server did not respond.");

        } catch (MalformedURLException e) {

            sender.sendMessage("§cInvalid download URL.");

        } catch (FileNotFoundException e) {

            sender.sendMessage("§cFile not found (404).");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}



