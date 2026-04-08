package me.pinkcandy.plugGet.api.modrinth.map;

import me.pinkcandy.plugGet.Tools.VersionRange;
import me.pinkcandy.plugGet.model.DependencyInfo;
import me.pinkcandy.plugGet.model.VersionInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static me.pinkcandy.plugGet.Tools.TextTools.normalizeVersion;

public class VersionMapper {

    public static VersionInfo fromJson(JSONObject json) {

        String versionNumber = json.optString("version_number");
        String versionId = json.optString("id");
        String branch = json.optString("version_type");
        String status = json.optString("status", null);
        String datePublished = json.optString("date_published");

        List<String> gameVersions = jsonArrayToList(json.optJSONArray("game_versions"));
        List<String> normalizedGameVersions = new ArrayList<>();
        for (int i = 0; i < gameVersions.size(); i++) {
            normalizedGameVersions.add(normalizeVersion(gameVersions.get(i)));
        }
        List<String> loaders = jsonArrayToList(json.optJSONArray("loaders"));

        String gameVersionRange = gameVersions.isEmpty() ? "?" : VersionRange.buildRange(json.getJSONArray("game_versions"));

        String fileName = null;
        String downloadUrl = null;
        String sha512 = null;
        int fileSize = 0;

        JSONArray files = json.optJSONArray("files");
        if (files != null) {
            for (int i = 0; i < files.length(); i++) {
                JSONObject file = files.getJSONObject(i);

                if (file.optBoolean("primary", false)) {

                    fileName = file.optString("filename");
                    downloadUrl = file.optString("url");
                    fileSize = file.optInt("size");

                    JSONObject hashes = file.optJSONObject("hashes");
                    if (hashes != null) {
                        sha512 = hashes.optString("sha512");
                    }

                    break;
                }
            }
        }
        JSONArray dependencies = json.optJSONArray("dependencies");
        List<DependencyInfo> dependenciesInfo = new ArrayList<>();
        if (dependencies != null) {
            for (int i = 0; i < dependencies.length(); i++) {
                JSONObject dependency = dependencies.getJSONObject(i);
                String projectId = dependency.optString("project_id");
                String dpVersionID = dependency.optString("version_id");
                String dbFileName = dependency.optString("file_name");
                String dependencyType = dependency.optString("dependency_type");
                DependencyInfo dependencyInfo = new DependencyInfo(projectId, null, dpVersionID, dbFileName, dependencyType);
                dependenciesInfo.add(dependencyInfo);
            }
        }


        return new VersionInfo(
                versionNumber,
                versionId,
                branch,
                status,
                datePublished,
                normalizedGameVersions,
                gameVersionRange,
                loaders,
                fileName,
                downloadUrl,
                sha512,
                fileSize,
                dependenciesInfo
        );
    }

    private static List<String> jsonArrayToList(JSONArray array) {
        List<String> list = new ArrayList<>();
        if (array == null) return list;

        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }

        return list;
    }
}