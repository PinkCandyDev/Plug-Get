package me.pinkcandy.plugGet.Install;

import me.pinkcandy.plugGet.SearchProjects;
import me.pinkcandy.plugGet.VersionFetcher;
import me.pinkcandy.plugGet.VersionSelector2;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InstallHelper {

    public List<String[]> BranchSelector(CommandSender sender, List<String[]> plugins){

        SearchProjects searchProjects = new SearchProjects();
        VersionFetcher fetcher = new VersionFetcher();
        VersionSelector2 selector = new VersionSelector2();

        List<String[]> versionsInfo = new ArrayList<>();
        boolean continueInstall = true;

        sender.sendMessage("§3Fetching plugins and versions...");
        for (int i = 0; i < plugins.size(); i++) {

            if (searchProjects.fetchProject(plugins.get(i)[0]) == null) {
                sender.sendMessage("§cPlugin " + plugins.get(i)[0] + " not found.");
                continueInstall = false;
            }

            JSONArray versionsList = fetcher.fetchAll(plugins.get(i)[0]);
            List<String[]> branches = selector.selectVersion(versionsList);

            if (plugins.get(i)[1].equals("version") || plugins.get(i)[1].equals("version-latest")) {
                String[] versionInfo = selector.selectSpecific(versionsList, plugins.get(i)[2]);
                if (versionInfo != null) {
                    versionsInfo.add(versionInfo);
                }
                else {
                    sender.sendMessage("§cVersion " + plugins.get(i)[2] + " not found for " + plugins.get(i)[0]);
                    continueInstall = false;
                }
            }

            if (plugins.get(i)[1].equals("")) {
                if (branches.get(0) != null) {
                    versionsInfo.add(branches.get(0));
                }
                else if (branches.get(1) != null) {
                    versionsInfo.add(branches.get(1));
                }
                else if (branches.get(2) != null) {
                    versionsInfo.add(branches.get(2));
                }
                else {
                    sender.sendMessage("§cNo versions found for " + plugins.get(i)[0]);
                    continueInstall = false;
                }
            } else if (plugins.get(i)[1].equals("beta")) {
                if (branches.get(1) != null) {
                    versionsInfo.add(branches.get(1));
                }
                else
                {
                    sender.sendMessage("§cNo beta version found for " + plugins.get(i)[0]);
                    continueInstall = false;
                }
            } else if (plugins.get(i)[1].equals("alpha")) {
                if (branches.get(2) != null) {
                    versionsInfo.add(branches.get(2));
                } else {
                    sender.sendMessage("§cNo alpha version found for " + plugins.get(i)[0]);
                    continueInstall = false;
                }
            }
        }

        if (continueInstall==false) {
            return null;
        }
        else {
            return versionsInfo;
        }
    }
}
