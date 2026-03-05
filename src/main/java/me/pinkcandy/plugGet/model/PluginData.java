package me.pinkcandy.plugGet.model;

public class PluginData {
    private final InstallInfo installInfo;
    private final VersionInfo versionInfo;

    public PluginData(InstallInfo installInfo, VersionInfo versionInfo)
    {
        this.installInfo = installInfo;
        this.versionInfo = versionInfo;
    }

    public InstallInfo getInstallInfo(){return installInfo;}
    public VersionInfo getVersionInfo(){return versionInfo;}

}
