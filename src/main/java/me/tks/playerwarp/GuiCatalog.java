package me.tks.playerwarp;

import me.tks.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

public class GuiCatalog {

    private ArrayList<Gui> guis;

    /**
     * Constructor for the GuiCatalog.
     */
    public GuiCatalog() {
        guis = new ArrayList<>();
    }

    /**
     * Adds a new item to the first empty gui.
     * @param warp item to add to the gui
     */
    public void addItem(Warp warp) {

        boolean added = false;

        if (guis.isEmpty()) {
            ArrayList<Warp> warps = new ArrayList<>();
            warps.add(warp);
            guis.add(new Gui(1, warps));
            added = true;
        }
        else {
            for (Gui gui : guis) {
                if (gui.hasFreeSlot()) {
                    gui.addItem(warp.getItemStack());
                    added = true;
                    break;
                }
            }
        }

        if (!added) {
            ArrayList<Warp> warps = new ArrayList<>();
            warps.add(warp);
            Gui gui = new Gui(this.guis.size() + 1, warps);
            guis.add(gui);
        }

    }

    /**
     * Removes an item from the GUI's.
     * @param warp warp to remove
     */
    public void removeItem(Warp warp) {

        if (guis.isEmpty()) {
            return;
        }

        for (Gui gui : guis) {
            if (gui.getInventory().contains(warp.getItemStack())) {
                gui.removeItem(warp.getItemStack());
                break;
            }
        }
    }

    /**
     * Updates an item in the GUI's.
     * @param warp warp to update
     */
    public void updateItem(Warp warp) {
        if (guis.isEmpty()) return;

        for (Gui gui : guis) {
            if (gui.updateItem(warp)) {
                break;
            }
        }
    }

    /**
     * Updates an item in the GUI's taht has changed name.
     * @param warp warp that has to be updated
     * @param oldName old name of the warp
     */
    public void updateItem(Warp warp, String oldName) {
        if (guis.isEmpty()) return;

        for (Gui gui : guis) {
            if (gui.updateItem(warp, oldName)) {
                break;
            }
        }
    }

    /**
     * Opens the next gui from the list
     * @param inv current inv
     * @param player player that's in the gui
     */
    public void openNextGui(Inventory inv, Player player) {

        int oldIndex = 0;

        for (Gui gui : guis) {
            if (gui.getInventory().equals(inv)) {
                oldIndex = guis.indexOf(gui);
            }
        }

        int newIndex = oldIndex + 1;

        if (this.guis.size() > newIndex) {
            player.closeInventory();
            this.guis.get(newIndex).openGui(player);
        }
    }

    /**
     * Open the previous GUI.
     * @param inv current opened inventory
     * @param player player to open it for
     */
    public void openPreviousGui(Inventory inv, Player player) {

        int oldIndex = 0;

        for (Gui gui : guis) {
            if (gui.getInventory().equals(inv)) {
                oldIndex = guis.indexOf(gui);
            }
        }

        int newIndex = oldIndex - 1;

        if (newIndex >= 0) {
            this.guis.get(newIndex).openGui(player);
        }
    }

    /**
     * Opens the first GUI for a player if it exists.
     * @param player player to open the GUI for
     */
    public void openFirstGui(Player player) {

        if (guis.isEmpty() || PWarp.wL.getWarps().isEmpty()) {
            player.sendMessage(ChatColor.RED + Messages.NO_WARPS.getMessage());
            return;
        }

        player.openInventory(guis.get(0).getInventory());
    }

    /**
     * Check if the catalog contains a specific inventory.
     * @param inv inventory to check
     * @return Boolean true if the catalog contains the inventory
     */
    public boolean contains(Inventory inv) {

        for (Gui gui : guis) {

            if (gui.getInventory().equals(inv)) return true;

        }

        return false;
    }

    /**
     * Creates all the GUI's for a warp list.
     * @param wL warp list
     */
    public void createGuis(WarpList wL) {

        if (wL.getWarps().isEmpty()) {
            return;
        }

        Bukkit.getLogger().info("[PWarp] Refreshing GUI's.");

        wL.sortWarps();
        ArrayList<Warp> warps = wL.getUnhiddenWarps();

        ArrayList<Player> viewers = closeAllGuis();

        guis = new ArrayList<>();

        final int invAmount = (int) Math.ceil(warps.size() / 36.0);

        for (int a = 1; a <= invAmount; a++) {

            ArrayList<Warp> subList;

            if (36 * a >= warps.size()) {
                subList = new ArrayList<>(warps.subList((a-1) * 35, warps.size()));
            }
            else {
                subList = new ArrayList<>(warps.subList((a - 1) * 35, 36 * a));
            }

            guis.add(new Gui(a, subList));
        }

        for (Player player : viewers) {
            openFirstGui(player);
        }

        Bukkit.getLogger().info("[PWarp] GUI's have been refreshed.");
    }

    /**
     * Closes all GUI's that are currently opened.
     * @return ArrayList of all players who had the GUI closed
     */
    public ArrayList<Player> closeAllGuis() {

        ArrayList<Player> players = new ArrayList<>();

        for (Gui gui : guis) {
            for (HumanEntity viewer : gui.getInventory().getViewers()) {
                players.add((Player) viewer);
            }
        }

        for (Player player : players) {

            player.closeInventory();
        }

        return players;
    }

    /**
     * Reloads the GUI's.
     * @param p reference to main
     */
    public void reloadGuis(PWarp p) {

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        scheduler.scheduleSyncRepeatingTask(p, () -> PWarp.gC.createGuis(PWarp.wL), 0L, 1200 * PWarp.pC.getRefreshRateInMinutes());

    }

    /**
     * Updates the center top item for all GUI's.
     */
    public void updateGuiItem() {

        for (Gui gui : guis) {
            gui.updateGuiItem();
        }

    }

    /**
     * UPdates the top and bottom row of items for all GUI's.
     */
    public void updateSeparatorItem() {

        for (Gui gui : guis) {
            gui.updateSeparatorItem();
        }

    }
}
