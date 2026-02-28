package me.pinkcandy.plugGet.commands;

import me.pinkcandy.plugGet.db.DBManager;
import me.pinkcandy.plugGet.versionControll.BranchSelector;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class UpdateCommand {
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("§8:: §7Checking for updates...");
        sender.sendMessage("§8:: §7Fetching installed plugins from database...");
        List<String[][]> installedPlugins = DBManager.getInstalledPlugins();
        List<String[]> plugins = new ArrayList<>();
        List<String[]> versions = new ArrayList<>();
        for (int i = 0; i < installedPlugins.size(); i++) {
            plugins.add(installedPlugins.get(i)[0]);
            versions.add(installedPlugins.get(i)[1]);
        }

        BranchSelector bSelector = new BranchSelector();
        sender.sendMessage("§8:: §3Fetching plugins and versions...");
        for (int i = 0; i < plugins.size(); i++)
        {
            List<String[]> newestVersion = bSelector.selectBranch(sender, plugins);
            if (newestVersion == null) {
                sender.sendMessage("§cFailed to fetch versions for " + plugins.get(i)[0]);
                continue;
            }
            else if (newestVersion.get(i)[0].equals(versions.get(i)[0])) {
                sender.sendMessage("§8:: §7" + plugins.get(i)[0] + " is up to date.");
                continue;
            }
            else {
                sender.sendMessage("§8:: §7New version found for " + plugins.get(i)[0] + ": " + newestVersion.get(i)[0]);
                sender.sendMessage("§8:: §7Updating " + plugins.get(i)[0] + "...");
            }

        }

        return true;
    }
}
