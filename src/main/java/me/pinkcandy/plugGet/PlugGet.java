package me.pinkcandy.plugGet;

import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public final class PlugGet extends JavaPlugin {

    public static PlugGet instance;
    public static Path tmpFolder;
    public static Path cacheFolder;

    @Override
    public void onEnable() {
        instance = this;
        tmpFolder = PlugGet.instance.getDataFolder().toPath().resolve("tmp/");
        cacheFolder = PlugGet.instance.getDataFolder().toPath().resolve("cache/plugins/");
        new ServerInfo();
        this.getCommand("plugget").setExecutor(new CommandsHandler());
    }

    @Override
    public void onDisable() {
    }
}
