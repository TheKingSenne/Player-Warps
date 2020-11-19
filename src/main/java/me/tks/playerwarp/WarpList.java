package me.tks.playerwarp;

import com.google.gson.Gson;
import me.tks.messages.Messages;
import me.tks.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;


public class WarpList implements Serializable {

    private ArrayList<Warp> warps;

    /**
     * Constructor for warp list.
     */
    public WarpList() {
        warps = new ArrayList<>();
    }

    /**
     * Constructor for warp list.
     * @param warps ArrayList of warps
     */
    public WarpList(ArrayList<Warp> warps) {
        this.warps = warps;
    }

    /**
     * Reads a warp list from a json formatted file.
     * @return a new WarpList
     */
    public static WarpList read() {

        WarpList warpList = null;

        try {
            File file = new File(PWarp.getPlugin(PWarp.class).getDataFolder(), "warps.json");

            if (file.createNewFile())
                Bukkit.getLogger().info("[PWarp] A new warps.json file has been created.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fileIn = new FileInputStream(new File(PWarp.getPlugin(PWarp.class).getDataFolder(), "warps.json"));

            Scanner sc = new Scanner(fileIn);

            if (sc.hasNextLine()) {

                String json = sc.nextLine();

                if (!json.isEmpty()) {
                    warpList = WarpList.fromJson(json);
                }


            }

            fileIn.close();
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (warpList == null) {
            warpList = new WarpList();
        }

        return warpList;
    }

    /**
     * Writes a warp list to file in json fromat.
     */
    public void write() {
        try {

            FileWriter fW = new FileWriter(new File(PWarp.getPlugin(PWarp.class).getDataFolder(), "warps.json"));

            fW.write(this.toJson());

            fW.close();
        }
        catch (Exception e) {
            Bukkit.getLogger().info("Error: warps.json could not be accessed");
            e.printStackTrace();
        }
    }

    /**
     * Adds a warp to the warp list.
     * @param warp warp to add
     */
    public void addWarp(Warp warp) {
        this.warps.add(warp);

        PWarp.gC.addItem(warp);
    }

    /**
     * Removes a warp from the warp list with message.
     * @param player player that requested
     * @param name name of the warp provided by player
     */
    public void removeWarp(Player player, String name) {

        // Check if warp exists
        if (!warpExistsWithMessage(player, name)) {
            return;
        }

        // Get Warp object from name
        Warp warp = this.getWarp(name);

        if (!warp.isOwnerWithMessage(player)) {
            return;
        }

        // Remove warp from list and GUI
        warps.remove(warp);
        PWarp.gC.removeItem(warp);

        // Notify player
        player.sendMessage(ChatColor.GREEN + Messages.REMOVED_WARP.getMessage());

    }

    /**
     * Removes all warps from the warp list with message.
     * @param player player that requested
     */
    public void removeAllWarps(Player player) {
        this.warps = new ArrayList<>();

        PWarp.gC.createGuis(this);
        player.sendMessage(ChatColor.GREEN + Messages.REMOVED_ALL.getMessage());
    }

    /**
     * Getter for the warps.
     * @return ArrayList of warps
     */
    public ArrayList<Warp> getWarps() {
        return this.warps;
    }

    /**
     * Getter for all warps that are currently not hidden.
     * @return ArrayList of warps
     */
    public ArrayList<Warp> getUnhiddenWarps() {

        return (ArrayList<Warp>) this.warps.stream()
            .filter(x -> !x.isHidden())
            .collect(Collectors.toList());
    }

    /**
     * Sorts the warps based on visitors.
     */
    public void sortWarps() {
        this.warps = (ArrayList<Warp>) this.warps.stream()
            .sorted((x, y) -> y.getVisitors() - x.getVisitors())
            .collect(Collectors.toList());
    }

    /**
     * Gets a warp from a string.
     * @param name name of the warp
     * @return a Warp or null if it doesn't exist.
     */
    public Warp getWarp(String name) {
        for (Warp warp : this.warps) {
            if (warp.getName().equalsIgnoreCase(name)) {
                return warp;
            }
        }
        return null;
    }

    /**
     * Checks if a warp exists with message.
     * @param sender sender that requested
     * @param name name provided by sender
     * @return Boolean true if the warp exists
     */
    public boolean warpExistsWithMessage(CommandSender sender, String name) {

        if (warpExists(name)) return true;

        sender.sendMessage(ChatColor.RED + Messages.WARP_NOT_EXISTING.getMessage());
        return false;
    }

    /**
     * Checks if warp exists.
     * @param name name of the warp
     * @return Boolean true if warp exists
     */
    public boolean warpExists(String name) {
        for (Warp warp : this.warps) {

            if (warp.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Converts a warplist to json format.
     * @return a String containing json format
     */
    public String toJson() {
        HashMap<String, Object> properties = new HashMap<>();
        Gson gson = new Gson();

        properties.put("size", warps.size());

        for (int i = 0; i < warps.size(); i++) {
            properties.put(String.valueOf(i), warps.get(i).toJson());
        }

        return gson.toJson(properties);
    }

    /**
     * Converts a json format to a new WarpList.
     * @param properties String containing the json format
     * @return a new WarpList
     */
    public static WarpList fromJson(String properties) {

        Gson gson = new Gson();

        Map<String, Object> map = gson.fromJson(properties, Map.class);

        double sizeDouble = (double) map.get("size");
        int size = (int) sizeDouble;


        ArrayList<Warp> warps = new ArrayList<>();

        for (int i = 0; i < size; i++) {

            warps.add(Warp.fromJson((String) map.get(String.valueOf(i))));

        }

        return new WarpList(warps);

    }

    /**
     * Checks the amount of warps owned by a player.
     * @param player player to check for
     * @return Integer amount of warps owned by player
     */
    public int ownsHowMany(Player player) {

        int warpAmount = 0;

        for (Warp warp : warps) {
            if (warp.isOwner(player)) warpAmount++;
        }

        return warpAmount;
    }

    /**
     * Lists all warps owned by a player to another player.
     * @param player player that requested
     * @param arg String passed by player
     */
    public void listOtherOwnedWarps(Player player, String arg) {

        OfflinePlayer requested = PlayerUtils.getOfflinePlayerFromName(player, arg);

        if (requested == null) return;
        listOwnedWarps((Player) requested);
    }

    /**
     * List all warps owned by a player.
     * @param player player that requested
     */
    public void listOwnedWarps(Player player) {

        ArrayList<String> owned = (ArrayList<String>) this.warps.stream()
            .filter(x -> x.isOwner(player))
            .map(Warp::getName)
            .collect(Collectors.toList());

        if (owned.isEmpty()) {
            player.sendMessage(ChatColor.RED + Messages.NO_OWNED_WARPS.getMessage());
            return;
        }

        player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.RESET + ChatColor.YELLOW + Messages.OWNED_WARPS.getMessage().replaceAll("PPLAYERP", player.getName())
            + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----");

        player.sendMessage(ChatColor.GOLD + "» " + ChatColor.YELLOW + String.join(ChatColor.GOLD + "\n » " + ChatColor.YELLOW, owned));

    }

    /**
     * Checks how many warps a player is allowed to have.
     * @param player player to check for
     * @return Integer amount of warps player is allowed to have
     */
    public static int getPersonalLimit(Player player) {
        int limit = PWarp.pC.getStandardLimit();
        int newLimit = 0;

        if (limit == 0) return 0;

        for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
            int length = perm.getPermission().length();

            if (length >= 18) {

                String permission = perm.getPermission().substring(0, 16);

                if (!permission.equals("pwarp.otherlimit")) {
                    continue;
                }
                try {
                    newLimit = Integer.parseInt(perm.getPermission().substring(17));

                } catch (Exception e2) {
                    break;
                }
            }
        }

        if (newLimit != 0 && newLimit > limit) limit = newLimit;

        return limit;
    }

    /**
     * Checks if a player has reached his warp limit.
     * @param player player to check for
     * @return Boolean true if player has reached warp limit
     */
    public boolean ownsTooMany(Player player) {

        if (player.hasPermission("pwarp.nolimit")) return false;

        if (PWarp.pC.getStandardLimit() == 0) return false;

        return  (getPersonalLimit(player) != 0 && ownsHowMany(player) >= getPersonalLimit(player));

    }

}
