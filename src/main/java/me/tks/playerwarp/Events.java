package me.tks.playerwarp;

import me.tks.messages.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Events implements Listener {

    /**
     * This method handles the gui clicks
     * @param e Click event
     */
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {

        Inventory playerInv = e.getWhoClicked().getInventory();
        Inventory clickedInv = e.getClickedInventory();

        if (playerInv.equals(clickedInv)) return;

        if (!PWarp.getPlugin(PWarp.class).gC.contains(clickedInv)) return;

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

        // If is page item
        if (type.equals(Material.PAPER)) {
            return;
        }

        // If is pane
        if (type.equals(Material.GRAY_STAINED_GLASS_PANE)) {
            return;
        }

        // If top gui item
        if (e.getSlot() == 4) {
            return;
        }

        // Is warp
        String name = Objects.requireNonNull(clicked.getItemMeta()).getDisplayName()/*.substring(2) FIX colour:)*/;

        Warp warp = PWarp.getPlugin(PWarp.class).wL.getWarp(name);

        if (warp != null) {
            warp.goTo((Player) e.getWhoClicked());
        }

    }

}
