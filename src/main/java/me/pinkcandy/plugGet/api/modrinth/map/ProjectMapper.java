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

import static me.pinkcandy.plugGet.PlugGet.cacheFolder;

public class ProjectMapper {

    public static ProjectMeta fromJson(JSONObject obj) {

        String slug = obj.getString("slug");
        String projectId = obj.getString("project_id");
        String author = obj.getString("author");
        int downloads = obj.optInt("downloads", 0);
        String description = obj.optString("description", "No description provided.");

        List<String> loaders = jsonArrayToList(obj.optJSONArray("categories"));
        loaders = LoaderList.filterLoaders(loaders);
        List<String> versions = jsonArrayToList(obj.optJSONArray("versions"));
        String versionRange = versions.isEmpty() ? "?" : VersionRange.buildRange(obj.getJSONArray("versions"));
        Path file = cacheFolder.resolve(slug).resolve("project.json");
        if (!Files.exists(file)) {
            try
            {
                Files.createDirectories(file.getParent());
                ProjectMeta projectMeta = new ProjectMeta(
                        slug,
                        projectId,
                        author,
                        downloads,
                        description,
                        loaders,
                        versions,
                        versionRange
                );
                JSONObject json = new JSONObject(projectMeta);
                Files.writeString(file, json.toString(2));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

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