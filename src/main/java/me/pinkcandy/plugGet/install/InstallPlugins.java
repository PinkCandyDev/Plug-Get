package me.pinkcandy.plugGet.install;

import me.pinkcandy.plugGet.db.DBManager;
import me.pinkcandy.plugGet.db.DBMapper;
import me.pinkcandy.plugGet.model.PluginData;
import me.pinkcandy.plugGet.model.VersionInfo;
import org.bukkit.command.CommandSender;
import org.json.JSONObject;

import java.util.List;

import static me.pinkcandy.plugGet.db.DBManager.*;

public class InstallPlugins {

    public static boolean installPlugins(List<PluginData> pluginsToInstall, CommandSender sender) {
        InstallHelper installHelper = new InstallHelper();

        boolean continueInstall = true;

        sender.sendMessage("§8:: §7Downloading and verifying files...");
        int size = pluginsToInstall.size();
        for (int i = 0; i < pluginsToInstall.size(); i++) {
            PluginData pluginData = pluginsToInstall.get(i);
            String slug = pluginData.getInstallInfo().getSlug();

            VersionInfo versionInfo = pluginData.getVersionInfo();

            continueInstall = installHelper.manageDownload(versionInfo, slug, i, size, sender);
            if (!continueInstall) { return false; }

            continueInstall = installHelper.manageVerification(versionInfo, slug, i, size, sender);
            if (!continueInstall) { return false; }
        }

        sender.sendMessage("§8:: §7Copying Files...");
        for (int i = 0; i < pluginsToInstall.size(); i++) {
            PluginData pluginData = pluginsToInstall.get(i);
            String slug = pluginData.getInstallInfo().getSlug();

            VersionInfo versionInfo = pluginData.getVersionInfo();

            continueInstall = installHelper.manageCopy(versionInfo, slug);

            if (!continueInstall) {
                sender.sendMessage("§cFailed to move " + versionInfo.getFileName() + " to cache.");
                return false;
            }
        }

        sender.sendMessage("§8:: §7Registering in db...");
        loadDB();
        loadDBToBackoupDB();

        for (int i = 0; i < pluginsToInstall.size(); i++) {
            PluginData pluginData = pluginsToInstall.get(i);

            JSONObject pluginJson = DBMapper.pluginToJson(pluginData);

            AddPlugin(pluginJson);
        }
        DBManager.replaceDB();
        if (!continueInstall) {return false;}
        sender.sendMessage("§aAll plugins installed correctly");
        return continueInstall;
    }
}
