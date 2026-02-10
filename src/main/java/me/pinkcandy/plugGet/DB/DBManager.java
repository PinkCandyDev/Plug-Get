package me.pinkcandy.plugGet.DB;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;

import static me.pinkcandy.plugGet.PlugGet.*;

public class DBManager {

    public static JSONObject db;
    public static JSONObject backupDB;

    public static void AddPlugin(JSONObject pluginData) {
        JSONObject plugins = db.optJSONObject("plugins");
        for (String slug : pluginData.keySet()) {
            JSONObject pluginObj = pluginData.getJSONObject(slug);
            plugins.put(slug, pluginObj);
        }
    }

    public static void saveDB() {
        try {
            String jsonOutput = db.toString(4);
            Files.writeString(dbFile, jsonOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBackupDB() {
        try {
            String jsonOutput = backupDB.toString(4);
            Files.writeString(dbBackupFile, jsonOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBackupDB() {
        try {
            if (Files.exists(dbBackupFile)) {
                String content = Files.readString(dbBackupFile);
                backupDB = new JSONObject(content);
            } else {
                backupDB = new JSONObject();
                backupDB.put("plugins", new JSONObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadDBToBackoupDB(){
        try {
            if (Files.exists(dbFile)) {
                String content = Files.readString(dbFile);
                backupDB = new JSONObject(content);
            } else {
                backupDB = new JSONObject();
                backupDB.put("plugins", new JSONObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadDB() {
        try {
            if (Files.exists(dbFile)) {
                String content = Files.readString(dbFile);
                db = new JSONObject(content);
            } else {
                db = new JSONObject();
                db.put("plugins", new JSONObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
