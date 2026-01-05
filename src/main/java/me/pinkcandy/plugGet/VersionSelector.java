package me.pinkcandy.plugGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VersionSelector {

    private final ServerInfo serverInfo;

    public VersionSelector() {
        this.serverInfo = new ServerInfo();
    }

    /**
     * Returns:
     * [0] version_number
     * [1] version id (id)
     * [2] loaders
     * [3] game_versions
     */
    public String[] selectVersion(JSONArray versions) {
        String[] defaults = new String[]{"§cUnknown", "", "[]", "[]"};
        if (versions == null) return defaults;

        try {
            List<JSONObject> compatible = new ArrayList<>();

            for (int i = 0; i < versions.length(); i++) {
                JSONObject v = versions.getJSONObject(i);

                String versionType = v.optString("version_type", "release").toLowerCase();
                String status = v.optString("status", "listed").toLowerCase();
                if (!status.equals("listed")) continue;
                if (!versionType.equals("release")) continue;

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

                compatible.add(v);
            }

            if (compatible.isEmpty()) return defaults;

            compatible.sort((a, b) -> b.optString("date_published", "").compareTo(a.optString("date_published", "")));

            JSONObject chosen = compatible.get(0);
            String versionNumber = chosen.optString("version_number", "§cUnknown");
            String versionId = chosen.optString("id", chosen.optString("version_id", ""));
            String loadersJson = chosen.optJSONArray("loaders") != null ? chosen.getJSONArray("loaders").toString() : "[]";
            String gameVersionsJson = chosen.optJSONArray("game_versions") != null ? chosen.getJSONArray("game_versions").toString() : "[]";

            return new String[]{versionNumber, versionId, loadersJson, gameVersionsJson};

        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"§4Error", "", "[]", "[]"};
        }
    }
}