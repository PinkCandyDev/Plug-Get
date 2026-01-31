package me.pinkcandy.plugGet;

import org.bukkit.plugin.java.JavaPlugin;

public final class PlugGet extends JavaPlugin {

    public static PlugGet instance;

    @Override
    public void onEnable() {
        instance = this;
        new ServerInfo();
        this.getCommand("plugget").setExecutor(new CommandsHandler());
    }

    @Override
    public void onDisable() {
    }
}
