package me.pinkcandy.plugGet.model;

import java.util.List;

public class ProjectMeta {

    private final String slug;
    private final String projectId;
    private final String author;
    private final int downloads;
    private final String description;
    private final List<String> loaders;
    private final List<String> versions;
    private final String versionRange;

    public ProjectMeta(String slug, String project_id, String author, int downloads, String description, List<String> loaders, List<String> versions, String versionRange) {
        this.slug = slug;
        this.projectId = project_id;
        this.author = author;
        this.downloads = downloads;
        this.description = description;
        this.loaders = loaders;
        this.versions = versions;
        this.versionRange = versionRange;
    }

    public String getSlug() { return slug; }
    public String getProjectId() { return projectId; }
    public String getAuthor() { return author; }
    public int getDownloads() { return downloads; }
    public String getDescription() { return description; }
    public List<String> getLoaders() { return loaders; }
    public List<String> getVersions() { return versions; }
    public String getVersionRange() { return versionRange; }
}
