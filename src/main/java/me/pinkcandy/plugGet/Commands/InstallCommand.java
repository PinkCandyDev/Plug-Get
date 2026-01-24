package me.pinkcandy.plugGet.Commands;

import me.pinkcandy.plugGet.SearchProjects;
import me.pinkcandy.plugGet.VersionFetcher;
import me.pinkcandy.plugGet.VersionSelector2;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InstallCommand {

    public boolean execute(CommandSender sender, String[] args) {
        SearchProjects searchProjects = new SearchProjects();
        VersionFetcher versionFetcher = new VersionFetcher();
        VersionSelector2 selector = new VersionSelector2();
        List<String[]> plugins = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            String slug = args[i];

            boolean invalidArg = false;
            String modifier = null;
            String version = null;

            if (slug.startsWith("--")) {
                sender.sendMessage("Wrong argument: " + slug);
                invalidArg = true;
                return true;
            }

            if (i + 1 < args.length && invalidArg==false) {
                String next = args[i + 1].toLowerCase();

                if (next.equals("--beta")) {
                    modifier = "beta";
                    i++;
                } else if (next.equals("--alpha")) {
                    modifier = "alpha";
                    i++;
                } else if (next.equals("--v")) {
                    if (i + 2 >= args.length) {
                        sender.sendMessage("No version provided after --v " + slug);
                        return true;
                    }
                    modifier = "version";
                    version = args[i + 2];
                    i += 2;
                } else if (next.equals("--vl")) {
                    if (i + 2 >= args.length) {
                        sender.sendMessage("No version provided after --v " + slug);
                        return true;
                    }
                    modifier = "version-latest";
                    version = args[i + 2];
                    i += 2;
                }

            }

            plugins.add (new String[] {slug, modifier, version});

        }

        for (int i = 0; i < plugins.size(); i++) {

            if (searchProjects.fetchProject(plugins.get(i)[0]) == null) {
                sender.sendMessage("Plugin " + plugins.get(i)[0] + " not found.");
                return true;
            }

            JSONArray versionsList = versionFetcher.fetchAll(plugins.get(i)[0]);
            List<String[]> branches = selector.selectVersion(versionsList);


            if (plugins.get(i)[1] == null) {

            } else if (plugins.get(i)[1].equals("beta")) {

            } else if (plugins.get(i)[1].equals("alpha")) {

            } else if (plugins.get(i)[1].equals("version")) {

                JSONObject versionInfo = versionFetcher.fetchSpecific(plugins.get(i)[0], plugins.get(i)[2]);
                if (versionInfo == null) {
                    sender.sendMessage("Version " + plugins.get(i)[2] + " not found for plugin " + plugins.get(i)[0]);
                } else {

                }
            } else if (plugins.get(i)[1].equals("version-latest")) {

            }
        }
        return true;
    }
}
