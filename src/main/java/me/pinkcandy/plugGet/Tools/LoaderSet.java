package me.pinkcandy.plugGet.Tools;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LoaderSet {

    private static final List<String> VALID_LOADERS =
            Arrays.asList("paper", "spigot", "bukkit", "folia");

    public static List<String> filterLoaders(List<String> input) {
        if (input == null) return List.of();

        return input.stream()
                .map(String::toLowerCase)
                .filter(VALID_LOADERS::contains)
                .collect(Collectors.toList());
    }

    public static String joinLoaders(List<String> loaders) {
        return (loaders == null || loaders.isEmpty())
                ? "unknown"
                : String.join(", ", loaders);
    }
}