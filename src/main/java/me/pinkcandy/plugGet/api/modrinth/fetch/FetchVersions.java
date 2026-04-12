package me.pinkcandy.plugGet.api.modrinth.fetch;

import me.pinkcandy.plugGet.ConfigManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;

import static me.pinkcandy.plugGet.PlugGet.tmpFolder;

public class FetchVersions {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static JSONArray fetchAll(String slug) {
        try {
            File file = tmpFolder.resolve(slug + "_versions.json").toFile();
            if (ConfigManager.tmpVersions) {
                if (file.exists()) {
                    String content = Files.readString(file.toPath());
                    return new JSONArray(content);
                }
            }
            String url = "https://api.modrinth.com/v2/project/" + slug + "/version";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "plug-get")
                    .GET()
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) return null;

            String body = response.body();

            Files.writeString(file.toPath(), body);

            return new JSONArray(body);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject fetchExact(String slug, String version) {
        try {
            String url = "https://api.modrinth.com/v2/project/" + slug + "/version/" + version;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "plug-get")
                    .GET()
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) return null;
            if (response.statusCode() == 404) return null;

            return new JSONObject(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

