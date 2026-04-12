package me.pinkcandy.plugGet;

import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.List;

import static me.pinkcandy.plugGet.Tools.TextTools.normalizeVersion;

public class ServerInfo {

    public static List<String> loaders = new ArrayList<>();
    public static List<String> version = new ArrayList<>();

    public ServerInfo(PlugGet plugin) {
        boolean isFolia = false, isPurpur = false, isPaper = false, isSpigot = false;

        try { Class.forName("io.papermc.paper.threadedregions.RegionizedServer"); isFolia = true; } catch (ClassNotFoundException ignored) {}
        try { Class.forName("org.purpurmc.purpur.PurpurConfig"); isPurpur = true; } catch (ClassNotFoundException ignored) {}
        try { Class.forName("com.destroystokyo.paper.PaperConfig"); isPaper = true; } catch (ClassNotFoundException ignored) {}
        try { Class.forName("org.spigotmc.SpigotConfig"); isSpigot = true; } catch (ClassNotFoundException ignored) {}
        if (isFolia) {
            loaders.add("folia");
        } else if (isPurpur) {
            loaders.add("bukkit");
            loaders.add("spigot");
            loaders.add("paper");
            loaders.add("purpur");
        } else if (isPaper) {
            loaders.add("bukkit");
            loaders.add("spigot");
            loaders.add("paper");
        } else if (isSpigot) {
            loaders.add("bukkit");
            loaders.add("spigot");
        } else {
            loaders.add("bukkit");
        }

        version.add(normalizeVersion(Bukkit.getBukkitVersion()));

        List<String> overwriteLoaders = plugin.getConfig().getStringList("overwrite.loaders");
        if (!overwriteLoaders.isEmpty()) {
            Bukkit.getLogger().warning("[Plug-Get] Loaders auto-detection overwritten by config: " + overwriteLoaders);
            loaders.clear();
            loaders.addAll(overwriteLoaders);
        }
        else {
            Bukkit.getLogger().info("[Plug-Get] Loaders: " + loaders);
        }

        String overwriteVersion = plugin.getConfig().getString("overwrite.version");
        if (overwriteVersion != null && !overwriteVersion.isBlank()) {
            Bukkit.getLogger().warning("[Plug-Get] Version auto-detection overwritten by config: " + overwriteVersion);
            version.clear();
            version.add(overwriteVersion);
        }
        else {
            Bukkit.getLogger().info("[Plug-Get] Version: " + version);
        }
    }
}