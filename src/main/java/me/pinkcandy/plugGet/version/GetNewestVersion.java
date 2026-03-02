package me.pinkcandy.plugGet.version;

import me.pinkcandy.plugGet.api.modrinth.fetch.FetchVersions;
import me.pinkcandy.plugGet.api.modrinth.map.VersionMapper;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.VersionInfo;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class GetNewestVersion {
    public static List<VersionInfo> getBranchesFromSlug(String slug){
        FetchVersions fetcher = new FetchVersions();
        JSONArray versions = fetcher.fetchAll(slug);
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

            versionInfo = VersionMapper.fromJson(FetchVersions.fetchExact(installInfo.getSlug(), installInfo.getVersion()));
            if (versionInfo != null) {
                return versionInfo;
            }
            else {
                return null;
            }
        }

        List<VersionInfo> branches = getBranchesFromSlug(installInfo.getSlug());
        if (installInfo.getInstallType().equals("")) {
            if (branches.get(0) != null) {
                versionInfo = branches.get(0);
            }
            else if (branches.get(1) != null) {
                versionInfo = branches.get(1);
            }
            else if (branches.get(2) != null) {
                versionInfo = branches.get(2);
            }
            else {
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
