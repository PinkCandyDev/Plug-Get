package me.pinkcandy.plugGet.remove;

import me.pinkcandy.plugGet.PlugGet;
import me.pinkcandy.plugGet.db.DBManager;
import me.pinkcandy.plugGet.model.PluginData;

import java.io.IOException;
import java.nio.file.Files;

public class RemovePlugin {
    public static boolean deletePlugin(PluginData pluginData)
    {
        try {
            Files.deleteIfExists(PlugGet.instance.getDataFolder().getParentFile().toPath().resolve(pluginData.getVersionInfo().getFileName()));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        DBManager.deletePlugin(pluginData.getInstallInfo().getSlug());
        return true;
    }

    public static boolean removeCurrentVer(String slug)
    {
        PluginData current = DBManager.getPluginData(slug);
        try {
            Files.deleteIfExists(PlugGet.instance.getDataFolder().getParentFile().toPath().resolve(current.getVersionInfo().getFileName()));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
