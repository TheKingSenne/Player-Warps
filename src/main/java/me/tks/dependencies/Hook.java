package me.tks.dependencies;

import me.tks.playerwarp.PWarp;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Hook {

    /**
     * Displays all hooks to a player.
     * @param player player that requested
     */
    public static void displayHooks(Player player) {

        ArrayList<String> hooks = (ArrayList<String>) PWarp.hooks;

        player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.RESET + ChatColor.YELLOW + "[Hooks]"
            + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----");

        player.sendMessage(ChatColor.GOLD + "» " + ChatColor.YELLOW + String.join(ChatColor.GOLD + "\n » " + ChatColor.YELLOW, hooks));

    }

}
