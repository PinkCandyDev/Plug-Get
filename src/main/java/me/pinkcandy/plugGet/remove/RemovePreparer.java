package me.pinkcandy.plugGet.remove;

import me.pinkcandy.plugGet.PlugGet;
import me.pinkcandy.plugGet.commands.ActionLock;
import me.pinkcandy.plugGet.db.DBManager;
import me.pinkcandy.plugGet.messagesBuilders.BuildDeleteInfo;
import me.pinkcandy.plugGet.model.DependencyInfo;
import me.pinkcandy.plugGet.model.PluginData;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RemovePreparer {
    public static void prepareDelete(List<String> slugs, String type, CommandSender sender)
    {
        List<PluginData> pluginsToDelete = new ArrayList<>();
        for (int i = 0; i < slugs.size(); i++)
        {
            PluginData pluginData = DBManager.getPluginData(slugs.get(i));
            if (pluginData == null){
                sender.sendMessage("§4Plugin " + slugs.get(i) + " is not installed");
                continue;
            }
            if (type.equals("auto"))
            {
                for (DependencyInfo dependency : pluginData.getVersionInfo().getDependencies()) {
                    if (dependency.getSlug() != null)
                    {
                        PluginData dependencyPD = DBManager.getPluginData(dependency.getSlug());
                        if (dependencyPD != null){
                            pluginsToDelete.add(dependencyPD);
                        }
                    }
                }
            }
            pluginsToDelete.add(pluginData);
        }

        if (!pluginsToDelete.isEmpty()) {
            List<BaseComponent[]> messages = BuildDeleteInfo.buildDeleteInfo(pluginsToDelete);
            ActionLock.isConfirming = true;
            for (int i = 0; i < messages.size(); i++) {
                sender.spigot().sendMessage(messages.get(i));
            }
        }
        else {
            ActionLock.release();
            return;
        }

        ActionLock.confirm = () -> {
            for (int i = 0; i < pluginsToDelete.size(); i++)
            {
                sender.sendMessage("§8:: §7Removing: " + pluginsToDelete.get(i).getVersionInfo().getFileName());
                RemovePlugin.deletePlugin(pluginsToDelete.get(i));
            }
            DBManager.replaceDB();
            ActionLock.release();
        };
        ActionLock.deny = () -> {
            sender.sendMessage("§cRemoval canceled");
            ActionLock.release();
        };
    }
}
