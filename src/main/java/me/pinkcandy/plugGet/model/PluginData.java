package me.pinkcandy.plugGet.model;

public class PluginData {
    private final InstallInfo installInfo;
    private final VersionInfo versionInfo;
    private final String status;

    public PluginData(InstallInfo installInfo, VersionInfo versionInfo, String status)
    {
        this.installInfo = installInfo;
        this.versionInfo = versionInfo;
        this.status = status;
    }

    public InstallInfo getInstallInfo(){return installInfo;}
    public VersionInfo getVersionInfo(){return versionInfo;}
    public String getStatus(){return status;}

}
