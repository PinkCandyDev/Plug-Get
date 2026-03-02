package me.pinkcandy.plugGet.api.modrinth.fetch;

import me.pinkcandy.plugGet.ServerInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class FetchProjects {

    public static void SeatchProjects(String slug, Consumer<String> callback) {
        new Thread(() -> {
            try {
                String query = URLEncoder.encode(slug, StandardCharsets.UTF_8);

                JSONArray facets = new JSONArray();

                if (!ServerInfo.loaders.isEmpty()) {
                    facets.put(new JSONArray().put("categories:" + ServerInfo.loaders.get(0).toLowerCase()));
                }

                if (!ServerInfo.version.isEmpty()) {
                    facets.put(new JSONArray().put("versions:" + ServerInfo.version.get(0)));
                }

                String facetsParam = URLEncoder.encode(facets.toString(), StandardCharsets.UTF_8);

                String url = "https://api.modrinth.com/v2/search?query=" + query
                        + "&facets=" + facetsParam
                        + "&limit=20";

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("User-Agent", "plug-get")
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    callback.accept(response.body());
                } else {
                    callback.accept("HTTP error: " + response.statusCode());
                }

            } catch (Exception e) {
                e.printStackTrace();
                callback.accept("Unexpected error: " + e.getMessage());
            }
        }).start();
    }

    public static JSONObject fetchProject(String slug) {
        try {
            String url = "https://api.modrinth.com/v2/project/" + slug;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "plug-get")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) return null;
            if (response.statusCode() == 404) return null;

            return new JSONObject(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}