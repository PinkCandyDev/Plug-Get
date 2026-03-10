package me.pinkcandy.plugGet.db;

import me.pinkcandy.plugGet.model.InstallInfo;
import me.pinkcandy.plugGet.model.PluginData;
import me.pinkcandy.plugGet.model.VersionInfo;
import org.json.JSONArray;
import org.json.JSONObject;

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

        pluginDataJson.put("installType", installInfo.getInstallType());

        if (versionInfo != null) {
            pluginDataJson.put("versionNumber", versionInfo.getVersionNumber());
            pluginDataJson.put("versionId", versionInfo.getVersionId());
            pluginDataJson.put("branch", versionInfo.getBranch());
            pluginDataJson.put("datePublished", versionInfo.getDatePublished());
            pluginDataJson.put("fileName", versionInfo.getFileName());
            pluginDataJson.put("sha512", versionInfo.getSha512());
            pluginDataJson.put("fileSize", versionInfo.getFileSize());
        }

        rootJson.put(slug, pluginDataJson);

        return rootJson;
    }

    public static PluginData jsonToPlugin(JSONObject rootJson) {
        if (rootJson == null || rootJson.isEmpty()) {
            return null;
        }

        PluginData plugin;

        String slug = rootJson.keys().next();
        JSONObject pluginDataJson = rootJson.getJSONObject(slug);

        VersionInfo vI;
        InstallInfo iI;

        String installType = pluginDataJson.getString("branch");

        String versionNumber = pluginDataJson.getString("versionNumber");
        String versionId = pluginDataJson.getString("versionId");
        String branch = pluginDataJson.getString("branch");
        String datePublished = pluginDataJson.getString("datePublished");
        String fileName = pluginDataJson.getString("fileName");
        String sha512 = pluginDataJson.getString("sha512");
        int fileSize = pluginDataJson.getInt("fileSize");

        iI = new InstallInfo(slug, installType, null);
        vI = new VersionInfo(versionNumber,
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
                fileSize
        );

        plugin = new PluginData(iI, vI);

        return plugin;

    }

}
