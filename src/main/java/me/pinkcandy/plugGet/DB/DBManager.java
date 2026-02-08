package me.pinkcandy.plugGet.DB;

import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static me.pinkcandy.plugGet.PlugGet.*;

public class DBManager {

    public static JSONObject db;

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
