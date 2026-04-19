package me.pinkcandy.plugGet.api.modrinth.fetch;

import me.pinkcandy.plugGet.ConfigManager;
import me.pinkcandy.plugGet.ServerInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;


public class FetchProjects {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static String fetchSearch(String slug) {
        try {
            String query = URLEncoder.encode(slug, StandardCharsets.UTF_8);

            JSONArray facets = new JSONArray();

            if (!ServerInfo.loaders.isEmpty()) {
                JSONArray loaderFacet = new JSONArray();
                for (String l : ServerInfo.loaders) {
                    loaderFacet.put("categories:" + l.toLowerCase(java.util.Locale.ROOT));
                }
                facets.put(loaderFacet);
            }

            if (!ServerInfo.version.isEmpty()) {
                facets.put(new JSONArray().put("versions:" + ServerInfo.version.get(0)));
            }

            String facetsParam = "";

            if (!ConfigManager.showIncompatible && facets.length() > 0) {
                facetsParam = "&facets=" + URLEncoder.encode(facets.toString(), StandardCharsets.UTF_8);
            }

            String url = "https://api.modrinth.com/v2/search?query=" + query
                    + facetsParam
                    + "&limit=" + ConfigManager.searchMaxResults;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "plug-get")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject fetchProject(String slug) {
        try {
            String url = "https://api.modrinth.com/v2/project/" + slug;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "plug-get")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) return null;

            return new JSONObject(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}