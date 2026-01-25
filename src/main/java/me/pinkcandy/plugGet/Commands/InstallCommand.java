package me.pinkcandy.plugGet.Commands;

import me.pinkcandy.plugGet.Install.InstallHelper;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class InstallCommand {

    public boolean execute(CommandSender sender, String[] args) {
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

        List<String[]> versionsToInstall = new InstallHelper().BranchSelector(sender, plugins);


        return true;
    }
}
