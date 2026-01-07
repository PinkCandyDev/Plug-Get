package me.pinkcandy.plugGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class CatchProjectInfo {

    public List<Object> parseToList(JSONObject obj) {
        try {
            String slug = obj.getString("slug");
            String projectId = obj.getString("project_id");
            String author = obj.getString("author");
            int downloads = obj.optInt("downloads", 0);
            String description = obj.optString("description", "No description provided.");
            JSONArray categories = obj.optJSONArray("categories");
            JSONArray versionsJson = obj.optJSONArray("versions");

            Set<String> loaderSet = LoaderSet.FromJsonArray(categories);

            String loaders = LoaderSet.joinLoaders(loaderSet);

            String versions = versionsJson != null ? VersionRange.buildRange(versionsJson) : "?";

            String versionRange = versionsJson != null ? VersionRange.buildRange(versionsJson) : "?";

            List<Object> data = new ArrayList<>();
            data.add(slug);           // 0
            data.add(projectId);      // 1
            data.add(author);         // 2
            data.add(downloads);      // 3
            data.add(description);    // 4
            data.add(loaders);      // 5
            data.add(versions);       // 6
            data.add(versionRange);   // 7

            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
