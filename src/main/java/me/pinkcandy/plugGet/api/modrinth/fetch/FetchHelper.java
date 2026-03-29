package me.pinkcandy.plugGet.api.modrinth.fetch;

import me.pinkcandy.plugGet.api.modrinth.map.ProjectMapper;
import me.pinkcandy.plugGet.db.CacheMapper;
import me.pinkcandy.plugGet.model.ProjectMeta;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static me.pinkcandy.plugGet.PlugGet.projectCacheFolder;

public class FetchHelper {
    public static List<ProjectMeta> searchProjects(String slug) {
        try {
            String result = FetchProjects.fetchSearch(slug);

            if (result == null) {
                return List.of();
            }

            List<ProjectMeta> metaList = new ArrayList<>();
            JSONObject root = new JSONObject(result);
            JSONArray hits = root.getJSONArray("hits");

            for (int i = hits.length() - 1; i >= 0; i--) {
                JSONObject obj = hits.getJSONObject(i);
                ProjectMeta projectMeta = ProjectMapper.fromJsonSearch(obj);
                metaList.add(projectMeta);
                CacheMapper.CacheMeta(projectMeta);
            }

            return metaList;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static ProjectMeta getProject(String slug) {
        try {
            Path dir = projectCacheFolder;

            try (Stream<Path> files = Files.list(dir)) {
                Optional<Path> match = files
                        .filter(Files::isRegularFile)
                        .filter(p -> p.getFileName().toString().startsWith(slug + "_"))
                        .findFirst();

                if (match.isPresent()) {
                    Path file = match.get();
                    String content = Files.readString(file);
                    JSONObject obj = new JSONObject(content);

                    return new ProjectMeta(
                            obj.optString("slug"),
                            obj.optString("projectId"),
                            obj.optString("author"),
                            obj.optInt("downloads"),
                            obj.optString("description"),
                            obj.optJSONArray("loaders").toList().stream()
                                    .map(Object::toString)
                                    .toList(),
                            obj.optJSONArray("versions").toList().stream()
                                    .map(Object::toString)
                                    .toList(),
                            obj.optString("versionRange")
                    );
                }
            }

            JSONObject result = FetchProjects.fetchProject(slug);

            if (result == null) {
                return null;
            }

            ProjectMeta meta = ProjectMapper.fromJsonProject(result);
            CacheMapper.CacheMeta(meta);

            return meta;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String projectIDToSlug(String projectID){
        try {
            try (Stream<Path> files = Files.list(projectCacheFolder)) {
                Optional<Path> match = files
                        .filter(Files::isRegularFile)
                        .filter(p -> p.getFileName().toString()
                                .endsWith("_" + projectID + ".json"))
                        .findFirst();

                if (match.isPresent()) {
                    Path file = match.get();
                    String content = Files.readString(file);
                    JSONObject obj = new JSONObject(content);
                    String slug = obj.optString("slug");
                    if (slug!=null && !slug.isEmpty()) {
                        return slug;
                    }
                }
            }

            JSONObject result = FetchProjects.fetchProject(projectID);

            if (result == null) {
                return null;
            }

            ProjectMeta meta = ProjectMapper.fromJsonProject(result);
            CacheMapper.CacheMeta(meta);

            return meta.getSlug();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String slugToProjectID(String slug){
        try {
            try (Stream<Path> files = Files.list(projectCacheFolder)) {
                Optional<Path> match = files
                        .filter(Files::isRegularFile)
                        .filter(p -> p.getFileName().toString().startsWith(slug + "_"))
                        .findFirst();

                if (match.isPresent()) {
                    Path file = match.get();
                    String content = Files.readString(file);
                    JSONObject obj = new JSONObject(content);
                    String projectID = obj.optString("projectID");
                    if (projectID!=null && !projectID.isEmpty()) {
                        return projectID;
                    }
                }
            }

            JSONObject result = FetchProjects.fetchProject(slug);

            if (result == null) {
                return null;
            }

            ProjectMeta meta = ProjectMapper.fromJsonProject(result);
            CacheMapper.CacheMeta(meta);

            return meta.getProjectId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}