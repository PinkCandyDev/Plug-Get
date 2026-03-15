package me.pinkcandy.plugGet.db;

import me.pinkcandy.plugGet.model.PluginData;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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

    public static String getPluginBranch(String slug) {
        JSONObject plugins = db.optJSONObject("plugins");
        if (plugins.has(slug)) {
            JSONObject branchObj = plugins.getJSONObject(slug);
            return branchObj.optString("branch", null);
        }
        else {
            return null;
        }
    }

    public static PluginData getPluginData(String slug){
        JSONObject plugins = db.optJSONObject("plugins");
        if (plugins.has(slug)) {
            JSONObject pluginObj = plugins.getJSONObject(slug);
            JSONObject wrapper = new JSONObject();
            wrapper.put(slug, pluginObj);
            return DBMapper.jsonToPlugin(wrapper);
        }
        return null;
    }

    public static boolean isPluginInstalled(String slug){
        JSONObject plugins = db.optJSONObject("plugins");
        if (plugins.has(slug)) {
            return true;
        }
        return false;
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

    public static List<PluginData> getInstalledPlugins() {
        JSONObject plugins = db.optJSONObject("plugins");
        List<PluginData> installedPlugins = new ArrayList<>();

        for (String slug : plugins.keySet()) {
            JSONObject pluginObj = plugins.getJSONObject(slug);
            JSONObject wrapper = new JSONObject();
            wrapper.put(slug, pluginObj);
            installedPlugins.add(DBMapper.jsonToPlugin(wrapper));
        }

        return installedPlugins;
    }

    public static boolean replaceDB()
    {
        try {
            saveDB();
            saveBackupDB();
            loadDB();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static List<String> getAllInstalledSlugs()
    {
        JSONObject plugins = db.optJSONObject("plugins");
        List<String> installedPlugins = new ArrayList<>();

        for (String slug : plugins.keySet()) {
            installedPlugins.add(slug);
        }
        return installedPlugins;
    }

    public static void deletePlugin(String slug){
        JSONObject plugins = db.optJSONObject("plugins");
        plugins.remove(slug);
    }
}
