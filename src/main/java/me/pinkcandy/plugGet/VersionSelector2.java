package me.pinkcandy.plugGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VersionSelector2 {

    public List<String[]> selectVersion(JSONArray versions) {
        ServerInfo serverInfo = new ServerInfo();
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
                    for (String sLoader : serverInfo.loaders) {
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
                    if (gameVersions.getString(g).equals(serverInfo.version)) {
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

    public String[] buildBranchData(List<JSONObject> versionList) {
        versionList.sort((a, b) -> b.optString("date_published", "").compareTo(a.optString("date_published", "")));

        JSONObject newest = versionList.get(0);

        String versionNumber = newest.optString("version_number", "§cUnknown");
        String formattedDate = newest.has("date_published") ?
                OffsetDateTime.parse(newest.getString("date_published")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : "§cUnknown";
        String gameVersionsJson = newest.optJSONArray("game_versions") != null ? newest.getJSONArray("game_versions").toString() : "[]";
        String loadersJson = newest.optJSONArray("loaders") != null ? newest.getJSONArray("loaders").toString() : "[]";
        String formattedSize = "§cUnknown";
        JSONArray files = newest.optJSONArray("files");
        if (files != null && files.length() > 0) {
            JSONObject firstFile = files.getJSONObject(0);
            if (firstFile.has("size")) {
                formattedSize = String.format("%.1f MiB", firstFile.getLong("size") / 1024.0 / 1024.0);
            }
        }

        return new String[]{versionNumber, formattedDate, gameVersionsJson, loadersJson, formattedSize};
    }
}
