package me.pinkcandy.plugGet;

import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.List;

public class ServerInfo {

    public String[] loaders; // zmieniono na tablicę
    public String version;

    public ServerInfo() {

        String fullVersion = Bukkit.getBukkitVersion();
        this.version = fullVersion.split("-")[0];

        List<String> loaderList = new ArrayList<>();

        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            loaderList.add("paper");
            loaderList.add("spigot");
            loaderList.add("bukkit");
        } catch (ClassNotFoundException ignored) {
            loaderList.add("spigot");
            loaderList.add("bukkit");
        }

        this.loaders = loaderList.toArray(new String[0]);
    }
}
