package me.pinkcandy.plugGet.Tools;

public class SetColor {
    public static String setColor(String branch) {
        switch (branch) {
            case "release":
                return "§a";
            case "beta":
                return "§e";
            case "alpha":
                return "§c";
            default:
                return "§f";
        }
    }
}

