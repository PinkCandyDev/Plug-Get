package me.pinkcandy.plugGet.api.modrinth.fetch;

import me.pinkcandy.plugGet.ConfigManager;
import me.pinkcandy.plugGet.api.HttpUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static me.pinkcandy.plugGet.PlugGet.tmpFolder;

public class FetchVersions {

    public static JSONArray fetchAll(String slug) {
        try {
            File file = tmpFolder.resolve(slug + "_versions.json").toFile();

            if (ConfigManager.tmpVersions && file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                return new JSONArray(content);
            }

            String url = "https://api.modrinth.com/v2/project/" + slug + "/version";

            String body = HttpUtils.get(url);
            if (body == null) return null;

            Files.write(file.toPath(), body.getBytes(StandardCharsets.UTF_8));

            return new JSONArray(body);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject fetchExact(String slug, String version) {
        try {
            String url = "https://api.modrinth.com/v2/project/" + slug + "/version/" + version;

            String response = HttpUtils.get(url);
            if (response == null) return null;

            return new JSONObject(response);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}