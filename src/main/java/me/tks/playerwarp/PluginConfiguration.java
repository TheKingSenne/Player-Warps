package me.tks.playerwarp;

import com.google.gson.Gson;
import me.tks.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PluginConfiguration {

    private int standardLimit;
    private int teleportDelay;
    private int refreshRateInMinutes;
    private double warpPrice;
    private ItemStack guiItem;
    private ItemStack separatorItem;

    public PluginConfiguration(int standardLimit, int teleportDelay, int refreshRateInMinutes, double warpPrice, ItemStack guiItem, ItemStack separatorItem) {
        this.standardLimit = standardLimit;
        this.teleportDelay = teleportDelay;
        this.refreshRateInMinutes = refreshRateInMinutes;
        this.warpPrice = warpPrice;
        this.guiItem = guiItem;
        this.separatorItem = separatorItem;
    }

    public void setGuiItem(Player player) {

        ItemStack guiItem = player.getInventory().getItemInMainHand();

        if (guiItem.getType().equals(Material.AIR)) {
            player.sendMessage(ChatColor.RED + Messages.HOLD_ITEM.getMessage());
            return;
        }

        player.getInventory().setItemInMainHand(guiItem);

        guiItem.setAmount(1);

        ItemMeta meta = guiItem.getItemMeta();

        meta.setDisplayName(meta.getDisplayName().replaceAll("&", "§"));
        guiItem.setItemMeta(meta);

        this.guiItem = guiItem;
        PWarp.gC.updateGuiItem();
        player.sendMessage(ChatColor.GREEN + Messages.GUI_ITEM_CHANGED.getMessage());
    }

    public void setSeparatorItem(Player player) {

        ItemStack separatorItem = player.getInventory().getItemInMainHand();

        if(separatorItem.getType().equals(Material.AIR)) {
            player.sendMessage(ChatColor.RED + Messages.HOLD_ITEM.getMessage());
            return;
        }

        player.getInventory().setItemInMainHand(separatorItem);
        separatorItem.setAmount(1);

        ItemMeta meta = separatorItem.getItemMeta();
        meta.setDisplayName(" ");
        separatorItem.setItemMeta(meta);

        this.separatorItem = separatorItem;
        PWarp.gC.updateSeparatorItem();
        player.sendMessage(ChatColor.GREEN + Messages.CHANGED_SEPARATOR.getMessage());
    }

    public ItemStack getGuiItem() {
        return this.guiItem;
    }

    public ItemStack getSeparatorItem() {
        return this.separatorItem;
    }

    public void setStandardLimit(CommandSender sender, String arg) {

        int standardLimit = 0;

        try {
            standardLimit = Integer.parseInt(arg);
        }
        catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
        }

        this.standardLimit = standardLimit;
        sender.sendMessage(ChatColor.GREEN + Messages.LIMIT_CHANGED.getMessage());
    }

    public void setTeleportDelay(int teleportDelay) {
        this.teleportDelay = teleportDelay;
    }

    public void setRefreshRateInMinutes(CommandSender sender, String arg) {

        int newRefreshRate = this.refreshRateInMinutes;

        try {
            newRefreshRate = Integer.parseInt(arg);
        }
        catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
            return;
        }

        this.refreshRateInMinutes = newRefreshRate;
    }

    public int getStandardLimit() {
        return standardLimit;
    }

    public int getTeleportDelay() {
        return teleportDelay;
    }

    public int getRefreshRateInMinutes() {
        return refreshRateInMinutes;
    }

    public static PluginConfiguration read() {

        PluginConfiguration pC = null;

        try {
            File file = new File(PWarp.getPlugin(PWarp.class).getDataFolder(), "configuration.json");

            if (file.createNewFile())
                Bukkit.getLogger().info("[PWarp] A new configuration.json file has been created.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fileIn = new FileInputStream(new File(PWarp.getPlugin(PWarp.class).getDataFolder(), "configuration.json"));

            Scanner sc = new Scanner(fileIn);

            if (sc.hasNextLine()) {

                String json = sc.nextLine();

                if (!json.isEmpty()) {
                    pC = PluginConfiguration.fromJson(json);
                }


            }

            fileIn.close();
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (pC == null) {
            pC = new PluginConfiguration(0, 0, 15, 0, null, null);
        }

        return pC;
    }

    public void write() {
        try {

            FileWriter fW = new FileWriter(new File(PWarp.getPlugin(PWarp.class).getDataFolder(), "configuration.json"));

            fW.write(this.toJson());

            fW.close();
        }
        catch (Exception e) {
            Bukkit.getLogger().info("Error: configuration.json could not be accessed");
            e.printStackTrace();
        }
    }

    public String toJson() {
        HashMap<String, Object> properties = new HashMap<>();
        Gson gson = new Gson();

        properties.put("limit", this.standardLimit);
        properties.put("delay", this.teleportDelay);
        properties.put("refreshRate", this.refreshRateInMinutes);
        properties.put("warpPrice", this.warpPrice);

        if (this.guiItem != null) {
            properties.put("guiItem", gson.toJson(this.guiItem.serialize()));
            properties.put("guiItemName", guiItem.getItemMeta().getDisplayName());
        }
        if (this.separatorItem != null) {
            properties.put("separatorItem", gson.toJson(this.separatorItem.serialize()));
        }

        return gson.toJson(properties);
    }

    public static PluginConfiguration fromJson(String properties) {

        Gson gson = new Gson();

        Map<String, Object> map = gson.fromJson(properties, Map.class);

        double limitDouble = (double) map.get("limit");
        int limit = (int) limitDouble;

        double delayDouble = (double) map.get("delay");
        int delay = (int) delayDouble;

        double refreshDouble = (double) map.get("refreshRate");
        int refreshRate = (int) refreshDouble;

        double warpPrice = 0;

        if (map.get("warpPrice") != null) {
            warpPrice = (double) map.get("warpPrice");
        }

        ItemStack guiItem = null;

        if (map.get("guiItem") != null) {
            guiItem = ItemStack.deserialize( gson.fromJson((String) map.get("guiItem"), Map.class));
            String name = (String) map.get("guiItemName");
            ItemMeta meta = guiItem.getItemMeta();
            meta.setDisplayName(name);
            guiItem.setItemMeta(meta);
        }

        ItemStack separatorItem = null;

        if (map.get("separatorItem") != null) {
            separatorItem = ItemStack.deserialize( gson.fromJson((String) map.get("separatorItem"), Map.class));
        }


        return new PluginConfiguration(limit, delay, refreshRate, warpPrice,guiItem, separatorItem);

    }

}
