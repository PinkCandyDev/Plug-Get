package me.pinkcandy.plugGet;

import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VersionSelector2 {

/*
    List<release, beta, alpha>
        <String[]>
            [0] = version number
            [1] = date published
            [2] = game versions (json array string)
            [3] = loaders (json array string)
            [4] = file size (formatted string)
            [5] = download URL
            [6] = file name
            [7] = sha512 hash
            [8] = branch
*/

    public List<String[]> selectVersion(JSONArray versions) {
        List<JSONObject> release = new ArrayList<>();
        List<JSONObject> beta = new ArrayList<>();
        List<JSONObject> alpha = new ArrayList<>();
        try {
            for (int i = 0; i < versions.length(); i++) {
                JSONObject v = versions.getJSONObject(i);

                String branch = v.optString("version_type", "unknown");

                JSONArray loadersArray = v.optJSONArray("loaders");
                if (loadersArray == null) continue;
                boolean loaderCompatible = false;
                for (int l = 0; l < loadersArray.length(); l++) {
                    String loader = loadersArray.getString(l).toLowerCase();
                    for (String sLoader : ServerInfo.loaders) {
                        if (loader.equals(sLoader.toLowerCase())) {
                            loaderCompatible = true;
                            break;
                        }
                    }
                    if (loaderCompatible) break;
                }
                if (!loaderCompatible) continue;

                JSONArray gameVersions = v.optJSONArray("game_versions");
                if (gameVersions == null) continue;
                boolean versionCompatible = false;
                for (int g = 0; g < gameVersions.length(); g++) {
                    if (normalizeVersion(gameVersions.getString(g))
                            .equals(normalizeVersion(ServerInfo.version))) {
                        versionCompatible = true;
                        break;
                    }
                }
                if (!versionCompatible) continue;

                if ("release".equals(branch)) release.add(v);
                else if ("beta".equals(branch)) beta.add(v);
                else if ("alpha".equals(branch)) alpha.add(v);

            }

            List<String[]> result = new ArrayList<>(3);
            result.add(!release.isEmpty() ? buildBranchData(release) : null);
            result.add(!beta.isEmpty() ? buildBranchData(beta) : null);
            result.add(!alpha.isEmpty() ? buildBranchData(alpha) : null);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] selectSpecific(JSONArray versions, String targetVersion) {
        List<JSONObject> matchedVersions = new ArrayList<>();
        try {
            for (int i = 0; i < versions.length(); i++) {
                JSONObject v = versions.getJSONObject(i);

                JSONArray loadersArray = v.optJSONArray("loaders");
                if (loadersArray == null) continue;
                boolean loaderCompatible = false;
                for (int l = 0; l < loadersArray.length(); l++) {
                    String loader = loadersArray.getString(l).toLowerCase();
                    for (String sLoader : ServerInfo.loaders) {
                        if (loader.equals(sLoader.toLowerCase())) {
                            loaderCompatible = true;
                            break;
                        }
                    }
                    if (loaderCompatible) break;
                }
                if (!loaderCompatible) continue;

                JSONArray gameVersions = v.optJSONArray("game_versions");
                if (gameVersions == null) continue;
                boolean versionCompatible = false;
                for (int g = 0; g < gameVersions.length(); g++) {
                    if (normalizeVersion(gameVersions.getString(g))
                            .equals(normalizeVersion(ServerInfo.version))) {
                        versionCompatible = true;
                        break;
                    }
                }
                if (!versionCompatible) continue;

                if (v.optString("version_number", "").equals(targetVersion)) {
                    matchedVersions.add(v);
                }
            }

            if (!matchedVersions.isEmpty()) {
                return buildBranchData(matchedVersions);
            }
            else
            {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] buildBranchData(List<JSONObject> versionList) {
        versionList = new ArrayList<>(versionList);
        versionList.sort((a, b) -> b.optString("date_published", "")
                .compareTo(a.optString("date_published", "")));

        JSONObject newest = versionList.get(0);

        String versionNumber = newest.optString("version_number", "§cUnknown");
        String formattedDate = newest.has("date_published") ?
                OffsetDateTime.parse(newest.getString("date_published")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : "§cUnknown";
        String gameVersionsJson = newest.optJSONArray("game_versions") != null ? newest.getJSONArray("game_versions").toString() : "[]";
        String loadersJson = newest.optJSONArray("loaders") != null ? newest.getJSONArray("loaders").toString() : "[]";
        String formattedSize = "§cUnknown";
        String downloadURL = null;
        String fileName = null;
        String sha512 = null;
        String branch = newest.optString("version_type", "unknown");

        JSONArray files = newest.optJSONArray("files");
        if (files != null && files.length() > 0) {
            JSONObject firstFile = files.getJSONObject(0);
            if (firstFile.has("size")) {
                formattedSize = String.format(
                        "%.1f MiB",
                        firstFile.getLong("size") / 1024.0 / 1024.0
                );
            }
            if (firstFile.has("url")) {
                downloadURL = firstFile.getString("url");
            }
            if (firstFile.has("filename")) {
                fileName = firstFile.getString("filename");
            }
            if (firstFile.has("hashes")) {
                JSONObject hashes = firstFile.getJSONObject("hashes");
                if (hashes.has("sha512")) {
                    sha512 = hashes.getString("sha512");
                }
            }
        }
        return new String[]{versionNumber, formattedDate, gameVersionsJson, loadersJson, formattedSize, downloadURL, fileName, sha512, branch};
    }

    private String normalizeVersion(String v) {
        if (v == null) return "";
        return v.replaceAll("[^0-9.]", "");
    }
}
