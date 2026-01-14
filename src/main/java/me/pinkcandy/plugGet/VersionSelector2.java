package me.pinkcandy.plugGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VersionSelector2 {

    public List<Object> selectVersion(JSONArray versions) {
        ServerInfo serverInfo = new ServerInfo();
        try{
            List<JSONObject> release = new ArrayList<>();
            List<JSONObject> beta = new ArrayList<>();
            List<JSONObject> alpha = new ArrayList<>();

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

                if (branch.equals("release"))
                {
                    release.add(v);
                }
                if (branch.equals("beta"))
                {
                    beta.add(v);
                }
                if (branch.equals("alpha"))
                {
                    alpha.add(v);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
    }
}
