package me.pinkcandy.plugGet.db;

import me.pinkcandy.plugGet.model.PluginData;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static me.pinkcandy.plugGet.PlugGet.*;

public class DBManager {

    public static JSONObject db;
    public static JSONObject backupDB;

    private static JSONObject getPlugins(JSONObject root) {
        JSONObject plugins = root.optJSONObject("plugins");
        if (plugins == null) {
            plugins = new JSONObject();
            root.put("plugins", plugins);
        }
        return plugins;
    }

    public static void AddPlugin(JSONObject pluginData) {
        JSONObject plugins = getPlugins(db);

        for (String slug : pluginData.keySet()) {
            JSONObject pluginObj = pluginData.getJSONObject(slug);
            plugins.put(slug, pluginObj);
        }
    }

    public static String getPluginBranch(String slug) {
        JSONObject plugins = getPlugins(db);

        if (plugins.has(slug)) {
            JSONObject branchObj = plugins.getJSONObject(slug);
            return branchObj.optString("branch", null);
        }
        return null;
    }

    public static PluginData getPluginData(String slug) {
        JSONObject plugins = getPlugins(db);

        if (plugins.has(slug)) {
            JSONObject pluginObj = plugins.getJSONObject(slug);
            JSONObject wrapper = new JSONObject();
            wrapper.put(slug, pluginObj);
            return DBMapper.jsonToPlugin(wrapper);
        }
        return null;
    }

    public static boolean isPluginInstalled(String slug) {
        JSONObject plugins = getPlugins(db);
        return plugins.has(slug);
    }

    public static void deletePlugin(String slug) {
        JSONObject plugins = getPlugins(db);
        plugins.remove(slug);
    }

    public static List<PluginData> getInstalledPlugins() {
        JSONObject plugins = getPlugins(db);
        List<PluginData> installed = new ArrayList<>();

        for (String slug : plugins.keySet()) {
            JSONObject pluginObj = plugins.getJSONObject(slug);
            JSONObject wrapper = new JSONObject();
            wrapper.put(slug, pluginObj);
            installed.add(DBMapper.jsonToPlugin(wrapper));
        }

        return installed;
    }

    public static List<String> getAllInstalledSlugs() {
        JSONObject plugins = getPlugins(db);
        List<String> list = new ArrayList<>(plugins.keySet());
        return list;
    }

    public static void saveDB() {
        try {
            String jsonOutput = db.toString(4);
            Files.write(dbFile, jsonOutput.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBackupDB() {
        try {
            String jsonOutput = backupDB.toString(4);
            Files.write(dbBackupFile, jsonOutput.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBackupDB() {
        try {
            if (Files.exists(dbBackupFile)) {
                String content = new String(Files.readAllBytes(dbBackupFile), StandardCharsets.UTF_8);
                backupDB = new JSONObject(content);
            } else {
                backupDB = new JSONObject();
                backupDB.put("plugins", new JSONObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadDBToBackoupDB() {
        try {
            if (Files.exists(dbFile)) {
                String content = new String(Files.readAllBytes(dbFile), StandardCharsets.UTF_8);
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
                String content = new String(Files.readAllBytes(dbFile), StandardCharsets.UTF_8);
                db = new JSONObject(content);
            } else {
                db = new JSONObject();
                db.put("plugins", new JSONObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean replaceDB() {
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
}