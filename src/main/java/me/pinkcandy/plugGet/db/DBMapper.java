package me.pinkcandy.plugGet.db;

import me.pinkcandy.plugGet.model.DependencyInfo;
import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.PluginData;
import me.pinkcandy.plugGet.model.VersionInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DBMapper {

    public static JSONObject pluginToJson(PluginData pluginData) {
        JSONObject rootJson = new JSONObject();

        if (pluginData == null || pluginData.getInstallInfo() == null) {
            return rootJson;
        }

        InstallInfo installInfo = pluginData.getInstallInfo();
        VersionInfo versionInfo = pluginData.getVersionInfo();

        String slug = installInfo.getSlug();

        JSONObject pluginDataJson = new JSONObject();
        String instType = installInfo.getInstallType();
        switch (instType) {
            case "version-latest":
                pluginDataJson.put("installType", "latest");
                break;
            case "version-rolling":
                pluginDataJson.put("installType", "rolling");
                break;
            default:
                pluginDataJson.put("installType", instType);
                break;
        }

        if (versionInfo != null) {
            pluginDataJson.put("versionNumber", versionInfo.getVersionNumber());
            pluginDataJson.put("source", "modrinth");
            pluginDataJson.put("versionId", versionInfo.getVersionId());
            pluginDataJson.put("branch", versionInfo.getBranch());
            pluginDataJson.put("datePublished", versionInfo.getDatePublished());
            pluginDataJson.put("fileName", versionInfo.getFileName());
            pluginDataJson.put("sha512", versionInfo.getSha512());
            pluginDataJson.put("fileSize", versionInfo.getFileSize());

            if (versionInfo.getDependencies() != null) {
                JSONArray depsArray = new JSONArray();

                for (DependencyInfo dep : versionInfo.getDependencies()) {
                    JSONObject depJson = new JSONObject();
                    depJson.put("projectID", dep.getProjectID());
                    depJson.put("slug", dep.getSlug());
                    depJson.put("versionID", dep.getVersionID());
                    depJson.put("fileName", dep.getFileName());
                    depJson.put("type", dep.getType());

                    depsArray.put(depJson);
                }

                pluginDataJson.put("dependencies", depsArray);
            }
        }

        rootJson.put(slug, pluginDataJson);

        return rootJson;
    }

    public static PluginData jsonToPlugin(JSONObject rootJson) {
        if (rootJson == null || rootJson.isEmpty()) {
            return null;
        }

        String slug = rootJson.keys().next();
        JSONObject pluginDataJson = rootJson.getJSONObject(slug);

        String installType = pluginDataJson.optString("installType", "unknown");

        String versionNumber = pluginDataJson.optString("versionNumber", null);
        String versionId = pluginDataJson.optString("versionId", null);
        String branch = pluginDataJson.optString("branch", null);
        String datePublished = pluginDataJson.optString("datePublished", null);
        String fileName = pluginDataJson.optString("fileName", null);
        String sha512 = pluginDataJson.optString("sha512", null);
        int fileSize = pluginDataJson.optInt("fileSize", 0);

        JSONArray dependenciesArray = pluginDataJson.optJSONArray("dependencies");
        List<DependencyInfo> dependencies = new ArrayList<>();

        if (dependenciesArray != null) {
            for (int i = 0; i < dependenciesArray.length(); i++) {
                JSONObject depJson = dependenciesArray.getJSONObject(i);

                DependencyInfo dep = new DependencyInfo(
                        depJson.optString("projectID", null),
                        depJson.optString("slug", null),
                        depJson.optString("versionID", null),
                        depJson.optString("fileName", null),
                        depJson.optString("type", null)
                );

                dependencies.add(dep);
            }
        }

        InstallInfo iI = new InstallInfo(slug, installType, null);

        VersionInfo vI = new VersionInfo(
                versionNumber,
                versionId,
                branch,
                null,
                datePublished,
                null,
                null,
                null,
                fileName,
                null,
                sha512,
                fileSize,
                dependencies.isEmpty() ? null : dependencies
        );

        return new PluginData(iI, vI, null);
    }
}