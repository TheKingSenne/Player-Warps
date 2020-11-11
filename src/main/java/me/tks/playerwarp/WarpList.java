package me.tks.playerwarp;

import com.google.gson.Gson;
import me.tks.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    public WarpList() {
        warps = new ArrayList<>();
    }

    public WarpList(ArrayList<Warp> warps) {
        this.warps = warps;
    }

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

    public void addWarp(Warp warp) {
        this.warps.add(warp);

        PWarp.gC.addItem(warp);
    }

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

    public void removeAllWarps(Player player) {
        this.warps = new ArrayList<>();

        PWarp.gC.createGuis(this);
        player.sendMessage(ChatColor.GREEN + Messages.REMOVED_ALL.getMessage());
    }

    public ArrayList<Warp> getWarps() {
        return this.warps;
    }

    public void sortWarps() {
        this.warps = (ArrayList<Warp>) this.warps.stream()
            .sorted((x, y) -> y.getVisitors() - x.getVisitors())
            .collect(Collectors.toList());
    }

    public Warp getWarp(String name) {
        for (Warp warp : this.warps) {
            if (warp.getName().equalsIgnoreCase(name)) {
                return warp;
            }
        }
        return null;
    }

    public boolean warpExistsWithMessage(CommandSender sender, String name) {
        for (Warp warp : this.warps) {

            if (warp.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + Messages.WARP_NOT_EXISTING.getMessage());
        return false;
    }

    public boolean warpExists(String name) {
        for (Warp warp : this.warps) {

            if (warp.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    public String toJson() {
        HashMap<String, Object> properties = new HashMap<>();
        Gson gson = new Gson();

        properties.put("size", warps.size());

        for (int i = 0; i < warps.size(); i++) {
            properties.put(String.valueOf(i), warps.get(i).toJson());
        }

        return gson.toJson(properties);
    }

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

    public int ownsHowMany(Player player) {

        int warpAmount = 0;

        for (Warp warp : warps) {
            if (warp.isOwner(player)) warpAmount++;
        }

        return warpAmount;
    }

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

    public boolean ownsTooMany(Player player) {

        if (player.hasPermission("pwarp.nolimit")) return false;

        return  getPersonalLimit(player) != 0 || ownsHowMany(player) >= getPersonalLimit(player);

    }

}
