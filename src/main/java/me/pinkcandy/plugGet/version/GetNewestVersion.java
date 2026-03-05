package me.pinkcandy.plugGet.version;

import com.sun.tools.classfile.FatalError;
import me.pinkcandy.plugGet.api.modrinth.fetch.FetchVersions;
import me.pinkcandy.plugGet.api.modrinth.map.VersionMapper;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.VersionInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetNewestVersion {
    public static List<VersionInfo> getBranchesFromSlug(String slug){
        JSONArray versions = FetchVersions.fetchAll(slug);
        if (versions == null) {
            return null;
        }
        List<VersionInfo> release = new ArrayList<>();
        List<VersionInfo> beta = new ArrayList<>();
        List<VersionInfo> alpha = new ArrayList<>();
        for (int j = 0; j < versions.length(); j++) {
            VersionInfo versionInfo = VersionMapper.fromJson(versions.getJSONObject(j));
            if (VersionValidator.isValid(versionInfo) && CompatibilityFilter.isCompatible(versionInfo)) {
                switch (versionInfo.getBranch())
                {
                    case "release":
                        release.add(versionInfo);
                        break;
                    case "beta":
                        beta.add(versionInfo);
                        break;
                    case "alpha":
                        alpha.add(versionInfo);
                        break;
                    default:
                }
            }
        }
        List<VersionInfo> branches = CompareDate.compareAll(release, beta, alpha);
        return branches;
    }

    public static VersionInfo getNewestVersionForInstallType(InstallInfo installInfo)
    {
        VersionInfo versionInfo = null;

        if (installInfo.getInstallType().equals("version") || installInfo.getInstallType().equals("version-latest")) {
            JSONArray versions = FetchVersions.fetchAll(installInfo.getSlug());
            if (versions==null){return null;}
            List<VersionInfo> validVs = new ArrayList<>();
            for (int i = 0; i < versions.length(); i++) {
                VersionInfo v = VersionMapper.fromJson(versions.getJSONObject(i));
                if (VersionValidator.isValid(v) && CompatibilityFilter.isCompatible(v)) {
                    validVs.add(v);
                }
            }
            if (validVs.isEmpty()) {
                return null;
            }
            else {
                for (int i = 0; i < validVs.size(); i++)
                {
                    if (validVs.get(i).getVersionNumber().equals(installInfo.getVersion()))
                    {
                        return validVs.get(i);
                    }
                }
                return null;
            }
        }

        List<VersionInfo> branches = getBranchesFromSlug(installInfo.getSlug());
        if (installInfo.getInstallType().equals("best")) {
            if (branches.get(0) != null) {
                versionInfo = branches.get(0);
            } else if (branches.get(1) != null) {
                versionInfo = branches.get(1);
            } else if (branches.get(2) != null) {
                versionInfo = branches.get(2);
            } else {
                return null;
            }
        } else if (installInfo.getInstallType().equals("release")) {
            if (branches.get(0) != null) {
                versionInfo = branches.get(0);
            } else {
                return null;
            }
        } else if (installInfo.getInstallType().equals("beta")) {
            if (branches.get(1) != null) {
                versionInfo = branches.get(1);
            }
            else
            {
                return null;
            }
        } else if (installInfo.getInstallType().equals("alpha")) {
            if (branches.get(2) != null) {
                versionInfo = branches.get(2);
            } else {
                return null;
            }
        }
        return versionInfo;
    }
}
