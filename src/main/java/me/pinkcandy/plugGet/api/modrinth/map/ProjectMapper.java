package me.pinkcandy.plugGet.api.modrinth.map;

import me.pinkcandy.plugGet.Tools.LoaderList;
import me.pinkcandy.plugGet.Tools.VersionRange;
import me.pinkcandy.plugGet.model.ProjectMeta;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class ProjectMapper {

    public static ProjectMeta fromJsonSearch(JSONObject obj) {

        String slug = obj.getString("slug");
        String projectId = obj.getString("project_id");
        String author = obj.getString("author");
        int downloads = obj.optInt("downloads", 0);
        String description = obj.optString("description", "No description provided.");

        List<String> loaders = jsonArrayToList(obj.optJSONArray("categories"));
        loaders = LoaderList.filterLoaders(loaders);
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
    public static ProjectMeta fromJsonProject(JSONObject obj) {

        String slug = obj.getString("slug");
        String projectId = obj.getString("id");
        int downloads = obj.optInt("downloads", 0);
        String description = obj.optString("description", "No description provided.");

        List<String> loaders = jsonArrayToList(obj.optJSONArray("loaders"));
        loaders = LoaderList.filterLoaders(loaders);
        List<String> versions = jsonArrayToList(obj.optJSONArray("game_versions"));
        String versionRange = versions.isEmpty() ? "?" : VersionRange.buildRange(obj.getJSONArray("game_versions"));

        return new ProjectMeta(
                slug,
                projectId,
                null,
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