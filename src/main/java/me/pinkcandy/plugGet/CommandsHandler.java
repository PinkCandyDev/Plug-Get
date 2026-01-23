package me.pinkcandy.plugGet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommandsHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("Usage: /plugget -Ss <plugin-slug> or /plugget search <plugin-slug>");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("-ss") || subCommand.equals("search")) {

            StringBuilder slug = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                slug.append(args[i]);
                if (i < args.length - 1) slug.append(" ");
            }

            sender.sendMessage("Results for: " + slug.toString());
            new SearchProjects().searchRaw(slug.toString(), result -> {
                JSONObject root = new JSONObject(result);
                JSONArray hits = root.getJSONArray("hits");

                CatchProjectInfo parser = new CatchProjectInfo();

                VersionFetcher fetcher = new VersionFetcher();
                VersionSelector2 selector = new VersionSelector2();
                ProjectInfoSender sendInfo = new ProjectInfoSender();

                for (int i = hits.length() - 1; i >= 0; i--) {
                    JSONObject obj = hits.getJSONObject(i);
                    List<Object> projectData = parser.parseToList(obj);
                    String projectId = (String) projectData.get(1);
                    JSONArray versionsList = fetcher.fetchAll(projectId);
                    sendInfo.sendProjectInfo(sender, projectData, selector.selectVersion(versionsList));
                }
            });
        }

        if (subCommand.equals("-s")|| subCommand.equals("install")) {
            SearchProjects searchProjects = new SearchProjects();
            VersionFetcher versionFetcher = new VersionFetcher();
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

        }
        return true;
    }
}
