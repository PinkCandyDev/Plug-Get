package me.pinkcandy.plugGet;

import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class PlugGet extends JavaPlugin {

    public static PlugGet instance;
    public static Path dbFolder;
    public static Path dbFile;
    public static Path tmpFolder;
    public static Path cacheFolder;

    @Override
    public void onEnable() {
        instance = this;
        dbFolder = PlugGet.instance.getDataFolder().toPath().resolve("db/");
        dbFile = dbFolder.resolve("plugins.json");
        try {
            Files.createDirectories(dbFolder);

            if (!Files.exists(dbFile)) {
                JSONObject dbJson = new JSONObject();
                dbJson.put("plugins", new JSONObject());

                Files.writeString(dbFile, dbJson.toString(4), StandardOpenOption.CREATE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tmpFolder = PlugGet.instance.getDataFolder().toPath().resolve("tmp/");
        try {
            Files.createDirectories(tmpFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cacheFolder = PlugGet.instance.getDataFolder().toPath().resolve("cache/plugins/");
        try {
            Files.createDirectories(cacheFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ServerInfo();
        this.getCommand("plugget").setExecutor(new CommandsHandler());
    }

    @Override
    public void onDisable() {
        try {
            if (Files.exists(tmpFolder)) {
                Files.walk(tmpFolder)
                        .filter(Files::isRegularFile)
                        .forEach(path -> {
                            try { Files.delete(path); }
                            catch (Exception e) { e.printStackTrace(); }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
