package me.tks.utils;

import me.tks.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PlayerUtils {

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

    public static boolean hasPermissionWithMessage(Player player, String permission) {

        if (player.hasPermission(permission)) return true;

        player.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
        return false;
    }

    public static String playerArrayInputToString(String[] string, int index) {

        ArrayList<String> newStrings = new ArrayList<>();

        for (int i = 0; i < string.length; i++) {

            if (i > index) {
                newStrings.add(string[i]);
            }

        }

        return String.join(" ", newStrings);
    }

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

}
