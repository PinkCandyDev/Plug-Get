package me.pinkcandy.plugGet;

import org.json.JSONArray;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class SearchProjects {

    public String[] loaders;
    public String version;

    private final ServerInfo serverInfo;

    public SearchProjects() {
        this.serverInfo = new ServerInfo();
        this.loaders = serverInfo.loaders;
        this.version = serverInfo.version;
    }

    public void searchRaw(String slug, Consumer<String> callback) {
        new Thread(() -> {
            try {
                String query = URLEncoder.encode(slug, StandardCharsets.UTF_8);

                // facets JSON
                JSONArray facets = new JSONArray();
                facets.put(new JSONArray().put("categories:" + serverInfo.loaders[0].toLowerCase())); // np. "paper"
                facets.put(new JSONArray().put("versions:" + serverInfo.version));

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
}