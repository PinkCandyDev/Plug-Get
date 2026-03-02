package me.pinkcandy.plugGet.version;

import me.pinkcandy.plugGet.model.VersionInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CompareDate {
    public static VersionInfo compare(List<VersionInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.stream().max(Comparator.comparing(o ->
                java.time.OffsetDateTime.parse(o.getDatePublished()))).orElse(null);
    }

    public static List<VersionInfo> compareAll(List<VersionInfo> release, List<VersionInfo> beta, List<VersionInfo> alpha) {
        List<VersionInfo> branches = new ArrayList<>();
        branches.add(compare(release));
        branches.add(compare(beta));
        branches.add(compare(alpha));
        return branches;
    }
}