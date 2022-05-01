package me.tks.dependencies;

import com.earth2me.essentials.Essentials;
import me.tks.playerwarp.PWarp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EssentialsPlugin extends Hook {

    private static Essentials essentials;

    /**
     * Sets up the EssentialsX hook.
     */
    public static void setUp() {
        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
            essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
            Bukkit.getConsoleSender().sendMessage("[PWarp] Successfully hooked into Essentials!");
            PWarp.hooks.add("Essentials" + ChatColor.GREEN + " \u2714");
        }
        else {
            PWarp.hooks.add("Essentials" + ChatColor.RED + " \u2718");
        }
    }

    /**
     * Registers the players current location as their last location
     * to be used with the /back command.
     * @param player the player to set the location for
     **/
    public static void registerPlayerLocation(Player player) {
        essentials.getUser(player).setLastLocation();
    }

    /**
     * Registers a specific location as a player's last location
     * to be used with the /back command.
     * @param player the player to set the location for
     * @param location the location to set as the player's last location.
     **/
    public static void registerPlayerLocation(Player player, Location location) {
        essentials.getUser(player).setLastLocation(location);
    }

}
