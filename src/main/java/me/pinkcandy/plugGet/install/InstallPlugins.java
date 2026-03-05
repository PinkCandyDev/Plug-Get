package me.pinkcandy.plugGet.install;

import me.pinkcandy.plugGet.db.DBMapper;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.PluginData;
import me.pinkcandy.plugGet.model.VersionInfo;
import org.bukkit.command.CommandSender;
import org.json.JSONObject;

import java.util.List;

import static me.pinkcandy.plugGet.db.DBManager.*;

public class InstallPlugins {

    public static boolean installPlugins(List<InstallInfo> pluginsToInstall, List<VersionInfo> versionsToInstall, CommandSender sender) {
        InstallHelper installHelper = new InstallHelper();

        boolean continueInstall = true;

        sender.sendMessage("§8:: §7Downloading and verifying files...");
        int size = versionsToInstall.size();
        for (int i = 0; i < versionsToInstall.size(); i++)
        {
            String slug = pluginsToInstall.get(i).getSlug();
            continueInstall = installHelper.manageDownload(versionsToInstall.get(i), slug, i, size, sender);
            if (!continueInstall) {return false;}
            continueInstall = installHelper.manageVerification(versionsToInstall.get(i), slug, i, size, sender);
            if (!continueInstall) {return false;}
        }
        sender.sendMessage("§8:: §7Copying Files...");
        for (int i = 0; i < versionsToInstall.size(); i++) {
            String slug = pluginsToInstall.get(i).getSlug();
            continueInstall = installHelper.manageCopy(versionsToInstall.get(i), slug);
            if (!continueInstall) {
                sender.sendMessage("§cFailed to move " + versionsToInstall.get(i).getFileName() + " to cache.");
                return false;
            }
        }

        sender.sendMessage("§8:: §7Registering in db...");
        loadDB();
        loadDBToBackoupDB();
        for (int i = 0; i < pluginsToInstall.size(); i ++)
        {
            JSONObject plugin = DBMapper.pluginToJson(new PluginData(pluginsToInstall.get(i), versionsToInstall.get(i)));
            AddPlugin(plugin);
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
        if (!continueInstall) {return false;}
        sender.sendMessage("§aAll plugins installed corectly ");
        return continueInstall;
    }
}
