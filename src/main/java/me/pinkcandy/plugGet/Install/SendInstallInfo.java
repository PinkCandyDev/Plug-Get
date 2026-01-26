package me.pinkcandy.plugGet.Install;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import me.pinkcandy.plugGet.TextTools;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SendInstallInfo {

    public void sendInstallInfo(CommandSender sender, List<String[]> plugins, List<String[]> versions) {
        ComponentBuilder builder = new ComponentBuilder();
        String pSpace = TextTools.makeSpaces(TextTools.countLetters(plugins, 0) - (10 + plugins.size())) + " ";
        String vSpace = TextTools.makeSpaces(TextTools.countLetters(versions, 0) - 8);
        builder.append("§2Plugins (" + plugins.size() + ")" + pSpace + "Versions" + vSpace + "To download");
    }
}
