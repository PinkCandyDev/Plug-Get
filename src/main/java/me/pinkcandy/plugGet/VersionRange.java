package me.pinkcandy.plugGet;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public final class VersionRange {


    private VersionRange() { }

    public static String buildRange(JSONArray versions) {
        if (versions == null || versions.length() == 0) return "";

        List<String> list = new ArrayList<>();
        for (int i = 0; i < versions.length(); i++) {
            list.add(versions.getString(i));
        }

        list.sort((v1, v2) -> {
            String[] parts1 = v1.split("\\.");
            String[] parts2 = v2.split("\\.");
            int length = Math.max(parts1.length, parts2.length);

            for (int i = 0; i < length; i++) {
                int p1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
                int p2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;

                if (p1 < p2) return -1;
                if (p1 > p2) return 1;
            }
            return 0;
        });

        String min = list.get(0);
        String max = list.get(list.size() - 1);

        if (min.equals(max)) return min;
        return min + "-" + max;
    }
}
