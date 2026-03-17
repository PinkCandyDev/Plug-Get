package me.pinkcandy.plugGet.model;

import java.util.List;

public class VersionInfo {
    private final String versionNumber;
    private final String versionId;
    private final String branch;
    private final String status;
    private final String datePublished;
    private final List<String> gameVersions;
    private final String gameVersionRange;
    private final List<String> loaders;
    private final String fileName;
    private final String downloadUrl;
    private final String sha512;
    private final int fileSize;
    private final List<DependencyInfo> dependencies;

    public VersionInfo(String versionNumber, String versionId, String branch, String status, String datePublished, List<String> gameVersions,
                       String gameVersionsRange, List<String> loaders, String fileName, String downloadUrl, String sha512, int fileSize, List<DependencyInfo> dependencies) {
        this.versionNumber = versionNumber;
        this.versionId = versionId;
        this.branch = branch;
        this.status = status;
        this.datePublished = datePublished;
        this.gameVersions = gameVersions;
        this.gameVersionRange = gameVersionsRange;
        this.loaders = loaders;
        this.fileName = fileName;
        this.downloadUrl = downloadUrl;
        this.sha512 = sha512;
        this.fileSize = fileSize;
        this.dependencies = dependencies;
    }

    public String getVersionNumber() { return versionNumber; }
    public String getVersionId() { return versionId; }
    public String getBranch() { return branch; }
    public String getStatus() { return status; }
    public String getDatePublished() { return datePublished; }
    public List<String> getGameVersions() { return gameVersions; }
    public String getGameVersionRange() { return gameVersionRange; }
    public List<String> getLoaders() { return loaders; }
    public String getFileName() { return fileName; }
    public String getDownloadUrl() { return downloadUrl; }
    public String getSha512() { return sha512; }
    public int getFileSize() { return fileSize; }
    public List<DependencyInfo> getDependencies() { return dependencies;}
}
