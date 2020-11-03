package me.tks.utils;

import me.tks.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public static OfflinePlayer getOfflinePlayerFromName(Player sender, String name) {
        OfflinePlayer p = null;

        try {
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


}
