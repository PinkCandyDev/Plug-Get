package me.pinkcandy.plugGet.commands;

import me.pinkcandy.plugGet.ActionLock;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchProjects;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchVersions;
import me.pinkcandy.plugGet.db.DBManager;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.PluginData;
import me.pinkcandy.plugGet.model.VersionInfo;
import me.pinkcandy.plugGet.version.CompareDate;
import me.pinkcandy.plugGet.version.GetNewestVersion;
import me.pinkcandy.plugGet.versionControll.BranchSelector;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdateCommand {
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("§8:: §7Checking for updates...");
        sender.sendMessage("§8:: §3Fetching plugins and versions...");
        List<PluginData> installedPlugins = DBManager.getInstalledPlugins();
        List<VersionInfo> newestVersions = new ArrayList<>();
        for (int i = 0; i < installedPlugins.size(); i++) {
            InstallInfo installInfo = installedPlugins.get(i).getInstallInfo();
            VersionInfo currentV = installedPlugins.get(i).getVersionInfo();
            String slug = installInfo.getSlug();
            JSONObject obj = FetchProjects.fetchProject(slug);
            if (obj == null) {
                sender.sendMessage("§cAlready installed plugin " + slug + " haven't been found on Modrinth. Does it still exist or mabey chainged the name?");
                ActionLock.release();
                continue;
            }

            List<VersionInfo> versions = new ArrayList<>();
            VersionInfo newestV = GetNewestVersion.getNewestVersionForInstallType(installInfo);
            if (newestV == null) {newestVersions.add(currentV); continue;}
            versions.add(newestV);
            versions.add(currentV);
            newestV =  CompareDate.compare(versions);
            newestVersions.add(newestV);
        }
        return  true;


    }
}
