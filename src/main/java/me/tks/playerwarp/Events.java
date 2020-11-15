package me.tks.playerwarp;

import me.tks.messages.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

public class Events implements Listener {

    // Arraylist containing all players that are currently teleporting
    private final ArrayList<Player> teleportingPlayers;

    /**
     * Constructor for EVents.
     */
    public Events() {
        teleportingPlayers = new ArrayList<>();
    }

    /**
     * Adds a player to the teleporting players.
     * @param player player to add
     */
    public void addPlayer(Player player) {
        teleportingPlayers.add(player);
    }

    /**
     * Removes a player from the teleporting players.
     * @param player player to remove
     */
    public void removePlayer(Player player) {
        teleportingPlayers.remove(player);
    }

    /**
     * Checks if a player is teleporting.
     * @param player player to check
     * @return Boolean true if player is currently teleporting
     */
    public boolean isTeleporting(Player player) {
        return teleportingPlayers.contains(player);
    }

    /**
     * Checks if a player moves to cancel teleportation.
     * @param e PlayerMoveEvent
     */
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (!isTeleporting(player)) return;

        Location from = e.getFrom();
        Location to = e.getTo();

        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) return;

        teleportingPlayers.remove(player);
        player.sendMessage(ChatColor.RED + Messages.MOVED.getMessage());

    }


    /**
     * This method handles the gui clicks.
     * @param e Click event
     */
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {

        Inventory playerInv = e.getWhoClicked().getInventory();
        Inventory clickedInv = e.getClickedInventory();

        if (playerInv.equals(clickedInv)) return;

        if (!PWarp.gC.contains(clickedInv)) return;

        e.setCancelled(true);

        ItemStack clicked = e.getCurrentItem();

        if (clicked == null) return;

        Material type = clicked.getType();

        // If is button
        if (type.equals(Material.STONE_BUTTON)) {

            String name = clicked.getItemMeta().getDisplayName();

            if (name.equals(ChatColor.GRAY + Messages.NEXT_BUTTON.getMessage())) {
                PWarp.gC.openNextGui(e.getClickedInventory() , (Player) e.getWhoClicked());
                return;
            }
            else if (name.equals(ChatColor.GRAY + Messages.BACK_BUTTON.getMessage())) {
                PWarp.gC.openPreviousGui(e.getClickedInventory() , (Player) e.getWhoClicked());
                return;
            }

        }

        // If is top/bottom slots
        if (e.getSlot() < 9 || e.getSlot() > 44) {
            return;
        }

        // Is warp
        String name = Objects.requireNonNull(clicked.getItemMeta()).getDisplayName().substring(2);

        Warp warp = PWarp.wL.getWarp(name);

        if (warp != null) {
            warp.goTo((Player) e.getWhoClicked());
        }

    }

}
