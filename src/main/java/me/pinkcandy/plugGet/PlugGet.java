package me.pinkcandy.plugGet;

import me.pinkcandy.plugGet.api.modrinth.fetch.FetchProjects;
import me.pinkcandy.plugGet.commands.CommandsHandler;
import me.pinkcandy.plugGet.commands.Tab;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static me.pinkcandy.plugGet.db.DBManager.loadBackupDB;
import static me.pinkcandy.plugGet.db.DBManager.loadDB;

public final class PlugGet extends JavaPlugin {

    public static PlugGet instance;
    public static Path dbFolder;
    public static Path dbFile;
    public static Path dbBackupFile;
    public static Path tmpFolder;
    public static Path plugincCacheFolder;
    public static Path projectCacheFolder;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ThreadManager.init(instance);
        ConfigManager.load(this);
        dbFolder = PlugGet.instance.getDataFolder().toPath().resolve("db/");
        dbFile = dbFolder.resolve("plugins.json");
        dbBackupFile = dbFolder.resolve("plugins_backup.json");
        try {
            Files.createDirectories(dbFolder);

            if (!Files.exists(dbFile)) {
                JSONObject dbJson = new JSONObject();
                dbJson.put("plugins", new JSONObject());

                Files.writeString(dbFile, dbJson.toString(4), StandardOpenOption.CREATE);
            }
            if (!Files.exists(dbBackupFile)) {
                JSONObject dbJson = new JSONObject();
                dbJson.put("plugins", new JSONObject());

                Files.writeString(dbBackupFile, dbJson.toString(4), StandardOpenOption.CREATE);
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
        plugincCacheFolder = PlugGet.instance.getDataFolder().toPath().resolve("cache/plugins/");
        try {
            Files.createDirectories(plugincCacheFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        projectCacheFolder = PlugGet.instance.getDataFolder().toPath().resolve("cache/project/");
        try {
            Files.createDirectories(projectCacheFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ServerInfo();
        loadDB();
        loadBackupDB();
        this.getCommand("plugget").setExecutor(new CommandsHandler());
        getCommand("plugget").setTabCompleter(new Tab());
    }

    @Override
    public void onDisable() {
        ThreadManager.shutdown();
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
