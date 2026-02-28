package me.pinkcandy.plugGet.api.modrinth.map;

import me.pinkcandy.plugGet.Tools.LoaderSet;
import me.pinkcandy.plugGet.Tools.VersionRange;
import me.pinkcandy.plugGet.model.ProjectMeta;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectMapper {

    public static ProjectMeta fromJson(JSONObject obj) {

        String slug = obj.getString("slug");
        String projectId = obj.getString("project_id");
        String author = obj.getString("author");
        int downloads = obj.optInt("downloads", 0);
        String description = obj.optString("description", "No description provided.");

        List<String> loaders = jsonArrayToList(obj.optJSONArray("categories"));
        loaders = LoaderSet.filterLoaders(loaders);
        List<String> versions = jsonArrayToList(obj.optJSONArray("versions"));
        String versionRange = versions.isEmpty() ? "?" : VersionRange.buildRange(obj.getJSONArray("versions"));

        return new ProjectMeta(
                slug,
                projectId,
                author,
                downloads,
                description,
                loaders,
                versions,
                versionRange
        );
    }

    private static List<String> jsonArrayToList(JSONArray array) {
        List<String> list = new ArrayList<>();

        if (array == null) return list;

        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }

        return list;
    }
}