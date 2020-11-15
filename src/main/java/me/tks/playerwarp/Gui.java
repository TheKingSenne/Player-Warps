package me.tks.playerwarp;

import me.tks.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Gui {

    private final Inventory guiInv;
    private final int page;

    /**
     * Constructor for a GUI inventory.
     *
     * @param page  Page of the GUI list
     * @param warps ArrayList containing all 36 warps in a sorted way
     */
    public Gui(int page, ArrayList<Warp> warps) {
        this.page = page;
        //this.warps = warps;

        // Create new inventory
        guiInv = Bukkit.createInventory(null, 54, ChatColor.YELLOW + Messages.GUI_INVENTORY_NAME.getMessage());

        // Create pane item
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);

        if (PWarp.pC.getSeparatorItem() != null) {
            pane = PWarp.pC.getSeparatorItem();
        }

        ItemMeta paneMeta = Bukkit.getItemFactory().getItemMeta(Material.GRAY_STAINED_GLASS_PANE);
        if (paneMeta != null) { paneMeta.setDisplayName(" "); }
        pane.setItemMeta(paneMeta);

        // Create back/next button
        ItemStack backButton = new ItemStack(Material.STONE_BUTTON, 1);
        ItemStack nextButton = new ItemStack(Material.STONE_BUTTON, 1);

        ItemMeta bbim = backButton.getItemMeta();
        ItemMeta nbim = nextButton.getItemMeta();
        if (bbim != null) { bbim.setDisplayName(ChatColor.GRAY + Messages.BACK_BUTTON.getMessage()); }
        if (nbim != null) { nbim.setDisplayName(ChatColor.GRAY + Messages.NEXT_BUTTON.getMessage()); }
        backButton.setItemMeta(bbim);
        nextButton.setItemMeta(nbim);

        // Create top gui item
        ItemStack guiItem = null;

        if (PWarp.pC.getGuiItem() != null) {
            guiItem = PWarp.pC.getGuiItem();
        }
        else {
            guiItem = new ItemStack(Material.SUNFLOWER);

            ItemMeta guiItemMeta = Bukkit.getItemFactory().getItemMeta(Material.SUNFLOWER);

            if (guiItemMeta != null) { guiItemMeta.setDisplayName(ChatColor.YELLOW + Messages.SERVER_NAME.getMessage()); }
            guiItem.setItemMeta(guiItemMeta);
        }

        // Create page item
        ItemStack pageItem = new ItemStack(Material.PAPER, 1);
        ItemMeta pageMeta = Bukkit.getItemFactory().getItemMeta(Material.PAPER);

        if (pageMeta != null) { pageMeta.setDisplayName(ChatColor.AQUA + Messages.GUI_PAGE.getMessage() + page); }

        pageItem.setItemMeta(pageMeta);

        // Place items in gui
        for (int i = 0; i < 9; ++i) {
            guiInv.setItem(i, pane);
            guiInv.setItem(i + 45, pane);
        }

        guiInv.setItem(4, guiItem);
        guiInv.setItem(48, backButton);
        guiInv.setItem(50, nextButton);
        guiInv.setItem(49, pageItem);

        for (Warp warp : warps) {
            guiInv.addItem(warp.getItemStack());
        }
    }

    /**
     * Getter for the page number.
     *
     * @return the page number
     */
    public int getPage() {
        return this.page;
    }

    /**
     * Opens the GUI for a player.
     *
     * @param player the player
     */
    public void openGui(Player player) {
        player.openInventory(this.guiInv);
    }

    /**
     * Updates the warp item in the gui.
     *
     * @param warp the warp
     * @return Boolean true if the item has been updated
     */
    public boolean updateItem(Warp warp) {

        for (int i = 0; i < guiInv.getSize(); i++) {
            if (guiInv.getItem(i)!= null && guiInv.getItem(i).getItemMeta().getDisplayName().toLowerCase().equals(ChatColor.YELLOW + warp.getName())) {

                guiInv.setItem(i, warp.getItemStack());
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the warp item in the GUI if the name has changed.
     * @param warp the warp
     * @param oldName old name of the warp
     * @return Boolean true if the warp has been updated.
     */
    public boolean updateItem(Warp warp, String oldName) {

        for (int i = 0; i < guiInv.getSize(); i++) {
            if (guiInv.getItem(i)!= null && guiInv.getItem(i).getItemMeta().getDisplayName().toLowerCase().equals(ChatColor.YELLOW + oldName)) {

                guiInv.setItem(i, warp.getItemStack());
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if this GUI has a free slot.
     *
     * @return Boolean true if the warp has a free slot
     */
    public boolean hasFreeSlot() {

        for (int i = 0; i < guiInv.getSize(); i++) {
            if (this.guiInv.getItem(i) == null) {
                return true;
            }
        }
        return false;

    }

    /**
     * Adds an item to the GUI.
     *
     * @param item item to add
     */
    public void addItem(ItemStack item) {
        this.guiInv.addItem(item);
    }

    /**
     * Removes an item from the GUI.
     *
     * @param item item to remove
     */
    public void removeItem(ItemStack item) {
        this.guiInv.removeItem(item);
    }

    /**
     * Getter for the GUI inventory.
     *
     * @return the GUI inventory
     */
    public Inventory getInventory() {
        return this.guiInv;
    }

    /**
     * Updates the top and bottom row items of the GUI.
     */
    public void updateSeparatorItem() {

        // Place items in gui
        for (int i = 0; i < 9; ++i) {

            if (i != 4)
            guiInv.setItem(i, PWarp.pC.getSeparatorItem());

            if (i+45 != 48 && i+45 != 50 && i+45 != 49)
            guiInv.setItem(i + 45, PWarp.pC.getSeparatorItem());
        }
    }

    /**
     * Updates the top center item of the GUI.
     */
    public void updateGuiItem() {
        guiInv.setItem(4, PWarp.pC.getGuiItem());
    }
}
