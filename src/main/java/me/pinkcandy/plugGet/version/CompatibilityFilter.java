package me.pinkcandy.plugGet.version;

import me.pinkcandy.plugGet.ServerInfo;
import me.pinkcandy.plugGet.model.VersionInfo;

public class CompatibilityFilter {
    public static boolean isCompatible(VersionInfo v){
        boolean compatible = false;
        compatible = v.getLoaders().stream().anyMatch(ServerInfo.loaders::contains);
        if (!compatible){return false;}
        compatible = v.getGameVersions().stream().anyMatch(ServerInfo.version::contains);
        return compatible;
    }
}
