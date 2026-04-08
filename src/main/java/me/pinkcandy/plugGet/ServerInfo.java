package me.pinkcandy.plugGet;

import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.List;

import static me.pinkcandy.plugGet.Tools.TextTools.normalizeVersion;

public class ServerInfo {

    public static List<String> loaders = new ArrayList<>();
    public static List<String> version = new ArrayList<>();

    public ServerInfo(PlugGet plugin) {
        boolean isSpigot = false, isPaper = false, isFolia = false, isPurpur = false;

        try { Class.forName("io.papermc.paper.threadedregions.RegionizedServer"); isFolia = true; } catch (ClassNotFoundException ignored) {}
        try { Class.forName("org.purpurmc.purpur.PurpurConfig"); isPurpur = true; } catch (ClassNotFoundException ignored) {}
        try { Class.forName("com.destroystokyo.paper.PaperConfig"); isPaper = true; } catch (ClassNotFoundException ignored) {}
        try { Class.forName("org.spigotmc.SpigotConfig"); isSpigot = true; } catch (ClassNotFoundException ignored) {}

        loaders.add("bukkit");
        if (isSpigot || isPaper || isFolia || isPurpur) loaders.add("spigot");
        if (isPaper || isFolia || isPurpur) loaders.add("paper");
        if (isFolia) loaders.add("folia");
        if (isPurpur) loaders.add("purpur");

        version.add(normalizeVersion(Bukkit.getBukkitVersion()));

        List<String> overwriteLoaders = plugin.getConfig().getStringList("overwrite-loaders");
        if (!overwriteLoaders.isEmpty()) {
            Bukkit.getLogger().warning("[ServerInfo] Loaders auto-detection overwritten by config: " + overwriteLoaders);
            loaders.clear();
            loaders.addAll(overwriteLoaders);
        }

        String overwriteVersion = plugin.getConfig().getString("overwrite-version");
        if (overwriteVersion != null && !overwriteVersion.isBlank()) {
            Bukkit.getLogger().warning("[ServerInfo] Version auto-detection overwritten by config: " + overwriteVersion);
            version.clear();
            version.add(overwriteVersion);
        }

        Bukkit.getLogger().info("[ServerInfo] Loaders: " + loaders);
        Bukkit.getLogger().info("[ServerInfo] Version: " + version);
    }
}