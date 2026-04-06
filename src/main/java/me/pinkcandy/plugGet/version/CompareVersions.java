package me.pinkcandy.plugGet.version;

import me.pinkcandy.plugGet.db.DBManager;
import me.pinkcandy.plugGet.model.PluginData;
import me.pinkcandy.plugGet.model.VersionInfo;

import static org.bukkit.Bukkit.getLogger;

public class CompareVersions {
    public static String getStatus(VersionInfo versionInfo, String slug){
        PluginData installed = DBManager.getPluginData(slug);;
        if (installed == null) {
            return "not";
        }
        else if (installed.getVersionInfo().getVersionNumber().equals(versionInfo.getVersionNumber()))
        {
            return "same";
        }
        else if (!installed.getVersionInfo().getVersionNumber().equals(versionInfo.getVersionNumber()))
        {
            return "dif";
        }
        return null;
    }
}
