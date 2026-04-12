package me.pinkcandy.plugGet.db;

import me.pinkcandy.plugGet.ConfigManager;
import me.pinkcandy.plugGet.model.ProjectMeta;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static me.pinkcandy.plugGet.PlugGet.projectCacheFolder;


public class CacheMapper {
    public static void CacheMeta(ProjectMeta meta)
    {
        if (ConfigManager.cacheMetadata) {
            Path file = projectCacheFolder.resolve(
                    meta.getSlug() + "_" + meta.getProjectId() + ".json"
            );
            try {
                Files.createDirectories(file.getParent());

                ProjectMeta projectMeta = new ProjectMeta(
                        meta.getSlug(),
                        meta.getProjectId(),
                        meta.getAuthor(),
                        meta.getDownloads(),
                        meta.getDescription(),
                        meta.getLoaders(),
                        meta.getVersions(),
                        meta.getVersionRange()
                );

                JSONObject json = new JSONObject(projectMeta);
                Files.writeString(file, json.toString(2));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
