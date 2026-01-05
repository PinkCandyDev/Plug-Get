package me.pinkcandy.plugGet;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class LoaderSet {

    private static final Set<String> VALID_LOADERS = new LinkedHashSet<>(Arrays.asList("paper", "spigot", "bukkit", "folia"));

    public static Set<String> fromCategories(JSONArray categories) {
        Set<String> set = new LinkedHashSet<>();
        if (categories == null) return set;
        for (int i = 0; i < categories.length(); i++) {
            String c = categories.optString(i, "").toLowerCase();
            if (VALID_LOADERS.contains(c)) {
                set.add(c);
            }
        }
        return set;
    }

    public static String joinLoaders(Set<String> loaders) {
        return loaders == null || loaders.isEmpty() ? "unknown" : String.join(",", loaders);
    }
}

