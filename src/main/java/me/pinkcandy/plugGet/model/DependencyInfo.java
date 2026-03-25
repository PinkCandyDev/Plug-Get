package me.pinkcandy.plugGet.model;

public class DependencyInfo {
    private final String projectID;
    private String slug;
    private final String versionID;
    private final String fileName;
    private final String type;

    public DependencyInfo(String projectID, String slug, String versionID, String fileName, String type) {
        this.projectID = projectID;
        this.slug = slug;
        this.versionID = versionID;
        this.fileName = fileName;
        this.type = type;
    }

    public String getProjectID() { return projectID; }
    public String getSlug() {return slug;}
    public String getVersionID() { return versionID; }
    public void setSlug(String slug){ this.slug = slug;}
    public String getFileName() { return fileName; }
    public String getType() { return type; }
}
