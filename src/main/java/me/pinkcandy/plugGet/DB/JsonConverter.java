package me.pinkcandy.plugGet.DB;

import org.json.JSONObject;

public class JsonConverter {

    public JSONObject pluginToJson(String[] pluginInfo, String[] versionInfo) {
        JSONObject object = new JSONObject();
        JSONObject plugin = new JSONObject();
        JSONObject version = new JSONObject();

        version.put("number", versionInfo[0]);
        version.put("releaseDate", versionInfo[1]);
        version.put("filename", versionInfo[6]);
        version.put("filesize", versionInfo[7]);

        plugin.put("version", version);
        plugin.put("branch", versionInfo[8]);
        plugin.put("installType", pluginInfo[1]);
        plugin.put("bestMatch", "release");

        object.put(pluginInfo[0], plugin);

        return object;
    }

    public String[][] jsonToPlugin(JSONObject object) {
        String slug = object.keySet().iterator().next();
        JSONObject plugin = object.getJSONObject(slug);
        JSONObject version = plugin.getJSONObject("version");

        String[] pluginInfo = new String[3];
        String[] versionInfo = new String[9];

        // pluginInfo
        pluginInfo[0] = slug;
        pluginInfo[1] = "";
        pluginInfo[2] = plugin.optString("installType", "");

        versionInfo[0] = version.optString("number", "");
        versionInfo[1] = version.optString("releaseDate", "");
        versionInfo[2] = "";
        versionInfo[3] = "";
        versionInfo[4] = "";
        versionInfo[5] = "";
        versionInfo[6] = version.optString("filename", "");
        versionInfo[7] = version.optString("filesize", "");
        versionInfo[8] = plugin.optString("branch", "");

        return new String[][] { pluginInfo, versionInfo };
    }

}
