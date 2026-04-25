package me.pinkcandy.plugGet;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {

    private static Plugin plugin;

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(1, r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("plugget-helper-" + t.getId());
        return t;
    });

    private static final ExecutorService VERSION_FETCH_EXECUTOR = Executors.newFixedThreadPool(6, r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("plugget-version-fetch-" + t.getId());
        return t;
    });

    public static void init(Plugin pl) {
        plugin = pl;
    }

    public static void runAsync(Runnable task) {
        EXECUTOR.submit(task);
    }

    public static ExecutorService getVersionFetchExecutor() {
        return VERSION_FETCH_EXECUTOR;
    }

    public static void shutdown() {
        EXECUTOR.shutdown();
        VERSION_FETCH_EXECUTOR.shutdown();
    }
}