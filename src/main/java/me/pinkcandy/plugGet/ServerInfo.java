package me.pinkcandy.plugGet;

import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.List;

public class ServerInfo {

    public static List<String> loaders = new ArrayList<>();
    public static List<String> version = new ArrayList<>();

    public  ServerInfo() {

        String fullVersion = Bukkit.getBukkitVersion();
        Bukkit.getLogger().info("Detected server version: " + fullVersion);
        this.version.add(fullVersion.split("-")[0]);

        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            loaders.add("paper");
            loaders.add("spigot");
            loaders.add("bukkit");
        } catch (ClassNotFoundException ignored) {
            loaders.add("spigot");
            loaders.add("bukkit");
        }

    }
}
