package me.pinkcandy.plugGet.DB;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static me.pinkcandy.plugGet.PlugGet.dbFolder;
import static me.pinkcandy.plugGet.PlugGet.mainDb;

public class DBManager {

    public static JSONObject db;

    public static void addPlugin() {
        JSONObject plugins = db.optJSONObject("plugins");

    }

    public static void save() {
        try {
            Files.writeString(dbFolder, new GsonBuilder().setPrettyPrinting().create().toJson(db));
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void load() {
        try {
            db = new JSONObject(Files.readString(mainDb));
        } catch (IOException e) { e.printStackTrace(); }
    }
}
