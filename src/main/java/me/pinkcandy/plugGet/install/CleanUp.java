package me.pinkcandy.plugGet.install;

import me.pinkcandy.plugGet.PlugGet;

import java.nio.file.Files;
import java.util.List;

import static me.pinkcandy.plugGet.PlugGet.tmpFolder;

public class CleanUp {

        public void succesClean(String[] FilesToDelete) {
                try {
                        for (String fileName : FilesToDelete) {
                                Files.deleteIfExists(tmpFolder.resolve(fileName));
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        public void failureInstallCleanUp(List<String[]> plugins, List<String[]> versionsToInstall, boolean[] reinstall) {
                try {
                        for (int i = 0; i < plugins.size(); i++) {
                                Files.deleteIfExists(tmpFolder.resolve(versionsToInstall.get(i)[6]));
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

                for (int i = 0; i < plugins.size(); i++) {
                        try {
                                Files.deleteIfExists(PlugGet.instance.getDataFolder().toPath().resolve("cache/plugins/" + plugins.get(i)[0] + "/" + versionsToInstall.get(i)[0] + "/" + versionsToInstall.get(i)[6]));
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }

                for (int i = 0; i < plugins.size(); i++) {
                        if (!reinstall[i]) {
                                try {
                                        Files.deleteIfExists(PlugGet.instance.getDataFolder().getParentFile()
                                                .toPath()
                                                .resolve(versionsToInstall.get(i)[6]));
                                } catch (Exception e) {
                                        e.printStackTrace();
                                }
                        }
                }

//                try{
//                        loadBackupDB();
//                        db = backupDB;
//                        saveDB();
//                } catch (Exception e) {
//                        e.printStackTrace();
//                }

        }
}
