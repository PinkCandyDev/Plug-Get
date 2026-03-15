package me.pinkcandy.plugGet;

import me.pinkcandy.plugGet.model.PluginData;

public class ConfigManager {
    public static TabMode tabMode;

    public static void load(PlugGet plugin) {
        plugin.reloadConfig();
        tabMode = TabMode.fromString(plugin.getConfig().getString("tab-mode", "apt"));
    }

    public static TabMode getTabMode() {
        return tabMode;
    }

    public enum TabMode {
        APT, PACMAN;

        public static TabMode fromString(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return APT;
            }
        }
    }
}
