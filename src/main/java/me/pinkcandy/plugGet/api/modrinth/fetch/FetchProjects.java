package me.pinkcandy.plugGet.api.modrinth.fetch;

import me.pinkcandy.plugGet.ConfigManager;
import me.pinkcandy.plugGet.ServerInfo;
import me.pinkcandy.plugGet.api.HttpUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
public class FetchProjects {

    public static String fetchSearch(String slug) {
        try {
            String query = URLEncoder.encode(slug, "UTF-8");

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
                facetsParam = "&facets=" + URLEncoder.encode(facets.toString(), "UTF-8");
            }

            String url = "https://api.modrinth.com/v2/search?query=" + query
                    + facetsParam
                    + "&limit=" + ConfigManager.searchMaxResults;

            return HttpUtils.get(url);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject fetchProject(String slug) {
        try {
            String url = "https://api.modrinth.com/v2/project/" + slug;

            String response = HttpUtils.get(url);
            if (response == null) return null;

            return new JSONObject(response);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}