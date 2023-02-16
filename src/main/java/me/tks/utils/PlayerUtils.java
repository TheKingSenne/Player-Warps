package me.tks.utils;

import me.tks.messages.Messages;
import org.bukkit.*;
import org.bukkit.block.data.type.Slab;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PlayerUtils {

    /**
     * Creates an OfflinePlayer from player name with message.
     * @param sender sender that requested
     * @param name name provided by sender
     * @return a new OfflinePlayer or null if it doesn't exist
     */
    public static OfflinePlayer getOfflinePlayerFromName(Player sender, String name) {
        OfflinePlayer p;

        try {
            //noinspection deprecation
            p = Bukkit.getOfflinePlayer(name);

            if (p.hasPlayedBefore()) {
                return p;
            }

        } catch (NullPointerException ignored) {
        }
        sender.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_EXISTING.getMessage());
        return null;
    }

    /**
     * Checks if a player has a permission and sends a message if not.
     * @param player player to check
     * @param permission permission to check for
     * @return Boolean true if player has permission
     */
    public static boolean hasPermissionWithMessage(Player player, String permission) {

        if (player.hasPermission(permission)) return true;

        player.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
        return false;
    }

    /**
     * Converts an array of player input into a single string.
     * @param string Input by player
     * @param index Starting index in array
     * @return a String containing all strings starting from the index separated with a space
     */
    public static String playerArrayInputToString(String[] string, int index) {

        ArrayList<String> newStrings = new ArrayList<>();

        for (int i = 0; i < string.length; i++) {

            if (i > index) {
                newStrings.add(string[i]);
            }

        }

        return String.join(" ", newStrings);
    }

    /**
     * Gets the item from a player's main hand and checks if it's null with message.
     * @param player player that requested
     * @return a new ItemStack or null if it's air.
     */
    public static ItemStack getNotNullInMainHand(Player player) {

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().equals(Material.AIR)) {
            player.sendMessage(ChatColor.RED + Messages.HOLD_ITEM.getMessage());
            return null;
        }

        player.getInventory().setItemInMainHand(item);
        item.setAmount(1);

        return item;
    }

    /**
     * Converts a string given by a player into a new world with message.
     * @param player player that requested
     * @param worldName world name provided by player
     * @return a new world or null if it doesn't exist
     */
    public static World getWorldFromString(Player player, String worldName) {

        World world = Bukkit.getWorld(worldName);

        if (world != null) {
            return world;
        }
        player.sendMessage(ChatColor.RED + Messages.WORLD_NOT_EXISTING.getMessage());
        return null;
    }

    /**
     * Checks if a player is standing in a safe location.
     * @param player player to check for
     * @return Boolean true if player is standing in a safe location
     */
    public static boolean isInSafeLocation(Player player) {
        Location loc = player.getLocation().clone();

        loc.setY(loc.getY() - 1);

        Material material = loc.getBlock().getType();

        if (material.equals(Material.AIR) || material.equals(Material.LAVA) || material.equals(Material.FIRE)) {
            player.sendMessage(ChatColor.RED + Messages.SET_UNSAFE.getMessage());
            return false;
        }

        for (int i = 1; i < 3; i++) {

            loc.setY(loc.getY() + i);
            material = loc.getBlock().getType();

            if ((!material.equals(Material.AIR) && material.isSolid()) || material.equals(Material.FIRE) || material.equals(Material.COBWEB) || (material.equals(Material.LAVA) && !(loc.getBlock().getBlockData() instanceof Slab))) {
                player.sendMessage(ChatColor.RED + Messages.SET_UNSAFE.getMessage());
                return false;
            }
        }

        return true;
    }

    /**
     * Converts user input into a boolean with message.
     * @param player player that requested
     * @param bool string provided by player
     * @return Boolean provided by player
     * @throws IllegalArgumentException when player didn't give a boolean
     */
    public static boolean getBooleanFromUser(CommandSender player, String bool) throws IllegalArgumentException {
        if (bool.equalsIgnoreCase("true")) {
            return true;
        }
        if (bool.equalsIgnoreCase("false")) {
            return false;
        }
        player.sendMessage(ChatColor.RED + Messages.TRUE_OR_FALSE.getMessage());
        throw new IllegalArgumentException();
    }

    public static double getDoubleFromUser(Player player, String arg) {

        double price;

        try {
            price = Double.parseDouble(arg);
        }
        catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
            throw new NumberFormatException();
        }

        return price;
    }

}
