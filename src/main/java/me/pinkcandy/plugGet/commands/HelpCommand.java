package me.pinkcandy.plugGet.commands;

import org.bukkit.command.CommandSender;

public class HelpCommand {

    public static void execute(CommandSender sender) {
        sender.sendMessage("§2§l=== §aPlugGet Help (apt syntax) §2===");
        sender.sendMessage("§8:: §f/pg help §7- Displays this help message");
        sender.sendMessage("§8:: §f/pg search <slug> §7- Searches for a plugin");
        sender.sendMessage("§8:: §f/pg install <slug> §7- Installs a plugin (by default install as latest");
        sender.sendMessage("§8:: §f/pg update §7- Updates all plugins");
        sender.sendMessage("§8:: §f/pg remove <slug> §7- Removes a plugin");
        sender.sendMessage("§8:: §f/pg autoremove <slug> §7- Removes plugin and its dependencies if not used");
        sender.sendMessage("§8:: §f/pg purge <slug> §7- Removes plugin its config folder, dependencies");
        sender.sendMessage("§8:: §f/pg y §7- Confirms an action");
        sender.sendMessage("§8:: §f/pg n §7- Denies an action");
        sender.sendMessage("§2§l=== §aInstall arguments §2===");
        sender.sendMessage("§8:: 7Example command: §f/pg install <slug> <arg(optional)> <slug> <arg(optional)>");
        sender.sendMessage("§8:: §f--latest §7- Install best version and only upgrade to more stable channels");
        sender.sendMessage("§8:: §f--rolling §7- Install newest version, can change channel to beta and alpha on update");
        sender.sendMessage("§8:: §f--beta §7- Install beta version and stay on beta (wont update to release)");
        sender.sendMessage("§8:: §f--alpha §7- Install beta version and stay on alpha (wont update to release)");
        sender.sendMessage("§8:: §f--v <version number> §7- Install specific version and doesn't allow updating it");
        sender.sendMessage("§8:: §f--vl <version number> §7- Install specific version and set install type to latest");
        sender.sendMessage("§8:: §f--vr <version number> §7- Install specific version and set install type to rolling");
    }

}
