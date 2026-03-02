package me.pinkcandy.plugGet.model;

import java.util.List;

public class InstallInfo {
    private final String slug;
    private final String installType;
    private final String version;

    public InstallInfo(String slug, String installType, String version) {
        this.slug = slug;
        this.installType = installType;
        this.version = version;
    }

    public String getSlug() {return slug;}
    public String getInstallType() {return installType;}
    public String getVersion() {return version;}


}
