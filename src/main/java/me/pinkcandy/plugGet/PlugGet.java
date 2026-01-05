package me.pinkcandy.plugGet;

import org.bukkit.plugin.java.JavaPlugin;

public final class PlugGet extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("plugget").setExecutor(new CommandsHandler());
    }

    @Override
    public void onDisable() {
    }
}
