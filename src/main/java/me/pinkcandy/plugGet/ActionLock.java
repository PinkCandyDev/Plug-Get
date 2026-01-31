package me.pinkcandy.plugGet;

import org.bukkit.command.CommandSender;

public class ActionLock {
    public static boolean isLocked = false;
    public static CommandSender lockedBy = null;

    public static Runnable confirm = null;
    public static Runnable deny = null;

    public static boolean isConfirming = false;

    public static void lock(CommandSender sender) {
        isLocked = true;
        lockedBy = sender;
    }

    public static void release() {
        isLocked = false;
        lockedBy = null;
        isConfirming = false;
        confirm = null;
        deny = null;
    }

    public static void askConfirmation()
    {
        isConfirming = true;
    }
}
