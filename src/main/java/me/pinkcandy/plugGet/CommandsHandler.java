package me.pinkcandy.plugGet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

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
                    JSONArray versionsList = fetcher.fetchVersions(projectId);
                    sendInfo.sendProjectInfo(sender, projectData, selector.selectVersion(versionsList));
                }
            });
        }

        if (subCommand.equals("-s")|| subCommand.equals("install")) {
            for (int i = 1; i < args.length; i++) {
                String slug = args[i];

                if (slug.startsWith("--")) {
                    sender.sendMessage("Wrong argument: " + slug);
                    return true;
                }

                String modifier = null;
                String version = null;

                if (i + 1 < args.length) {
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
                    }
                }

                if (modifier == null) {
                    sender.sendMessage(slug);
                } else if (modifier.equals("beta")) {
                    sender.sendMessage(slug + " with beta modifier");
                } else if (modifier.equals("alpha")) {
                    sender.sendMessage(slug + " with alpha modifier");
                } else if (modifier.equals("version")) {
                    sender.sendMessage(slug + " with version modifier: " + version);
                }
            }
            return true;
        }
        return true;
    }
}
