package me.pinkcandy.plugGet.version;

import me.pinkcandy.plugGet.model.VersionInfo;

public class VersionValidator {
    public static boolean isValid(VersionInfo v) {
        return v.getDownloadUrl() != null && !v.getDownloadUrl().trim().isEmpty()
                && v.getSha512() != null && v.getSha512().length() == 128
                && v.getFileName() != null && !v.getFileName().trim().isEmpty()
                && "listed".equals(v.getStatus())
                && v.getFileSize() > 0;
    }
}
