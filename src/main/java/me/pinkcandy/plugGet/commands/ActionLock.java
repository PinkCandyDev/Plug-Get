package me.pinkcandy.plugGet.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ActionLock {
    public static boolean isLocked = false;
    public static CommandSender lockedBy = null;

    public static Runnable confirm = null;
    public static Runnable deny = null;

    public static boolean isConfirming = false;

    public static List<Integer> numberList = new ArrayList<>();

    public static void lock(CommandSender sender) {
        List<Integer> numbers = new ArrayList<>();
        ActionLock.numberList = numbers;
        isLocked = true;
        lockedBy = sender;
    }

    public static void release() {
        List<Integer> numbers = new ArrayList<>();
        ActionLock.numberList = numbers;
        isLocked = false;
        lockedBy = null;
        isConfirming = false;
        confirm = null;
        deny = null;
    }
}
