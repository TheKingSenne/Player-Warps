package me.tks.dependencies;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.tks.messages.Messages;
import me.tks.playerwarp.PWarp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GriefPreventionPlugin extends Hook{

    /**
     * Sets up the GriefPreventionPlugin hook.
     */
    public static void setUp() {

        if (Bukkit.getPluginManager().getPlugin("GriefPrevention") != null) {
            Bukkit.getConsoleSender().sendMessage("[PWarp] Successfully hooked into GriefPrevention!");
            PWarp.hooks.add("GriefPrevention" + ChatColor.GREEN + " \u2714");
        }
        else {
            PWarp.hooks.add("GriefPrevention" + ChatColor.RED + " \u2718");
        }

    }

    /**
     * Checks if a player has access to a GP claim.
     * @param player player to check for
     * @return Boolean true if player has access
     */
    public static boolean hasAccess(Player player) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null);

        if (claim != null && claim.allowAccess(player) != null) {
            player.sendMessage(ChatColor.RED + Messages.NO_ACCESS_GP.getMessage());
            return false;
        }

        return true;
    }

}
