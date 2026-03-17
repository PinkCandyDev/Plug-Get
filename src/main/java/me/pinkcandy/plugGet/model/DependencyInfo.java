package me.pinkcandy.plugGet.model;

public class DependencyInfo {
    private final String projectID;
    private final String versionID;
    private final String fileName;
    private final String type;

    public DependencyInfo(String projectID, String slug, String versionID, String fileName, String type) {
        this.projectID = projectID;
        this.versionID = versionID;
        this.fileName = fileName;
        this.type = type;
    }

    public String getProjectID() { return projectID; }
    public String getVersionID() { return versionID; }
    public String getFileName() { return fileName; }
    public String getType() { return type; }
}
