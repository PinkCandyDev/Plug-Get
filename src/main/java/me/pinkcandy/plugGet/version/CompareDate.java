package me.pinkcandy.plugGet.version;

import me.pinkcandy.plugGet.model.VersionInfo;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CompareDate {
    public static VersionInfo compare(List<VersionInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.stream()
                .filter(o -> o.getDatePublished() != null)
                .max(Comparator.comparing(o -> {
                    try {
                        return OffsetDateTime.parse(o.getDatePublished());
                    } catch (DateTimeParseException e) {
                        throw new IllegalArgumentException(
                                "Invalid datePublished value: " + o.getDatePublished(), e);
                    }
                }))
                .orElse(null);
    }

    public static List<VersionInfo> compareAll(List<VersionInfo> release, List<VersionInfo> beta, List<VersionInfo> alpha) {
        List<VersionInfo> branches = new ArrayList<>();
        branches.add(compare(release));
        branches.add(compare(beta));
        branches.add(compare(alpha));
        return branches;
    }
}