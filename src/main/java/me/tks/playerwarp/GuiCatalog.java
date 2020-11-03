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

    public GuiCatalog() {
        guis = new ArrayList<>();
    }

    /**
     * Adds a new item to the gui
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

    public void updateItem(Warp warp) {
        if (guis.isEmpty()) return;

        for (Gui gui : guis) {
            if (gui.updateItem(warp)) {
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

    public void openFirstGui(Player player) {

        if (guis.size() == 0) {
            player.sendMessage(ChatColor.RED + Messages.NO_WARPS.getMessage());
            return;
        }

        player.openInventory(guis.get(0).getInventory());
    }

    public boolean contains(Inventory inv) {

        for (Gui gui : guis) {

            if (gui.getInventory().equals(inv)) return true;

        }

        return false;
    }

    public void createGuis(WarpList wL) {

        Bukkit.getLogger().info("[PWarp] Refreshing GUI's.");

        wL.sortWarps();
        ArrayList<Warp> warps = wL.getWarps();

        ArrayList<Player> viewers = closeAllGuis();

        guis.clear();

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

    public void reloadGuis(PWarp p) {

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        scheduler.scheduleSyncRepeatingTask(p, () -> PWarp.gC.createGuis(PWarp.wL), 0L, 1200 * PWarp.pC.getRefreshRateInMinutes());

    }

}
