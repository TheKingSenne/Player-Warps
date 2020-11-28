package me.tks.playerwarp;

import com.google.gson.Gson;
import me.tks.messages.Messages;
import me.tks.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PluginConfiguration {

    /**
     * Variables for plugin configuration
     */
    private int standardLimit; // Shown in info command
    private int teleportDelay; // Shown in info command
    private int refreshRateInMinutes; // Shown in info command
    private double warpPrice; // not implemented
    private ItemStack guiItem;
    private ItemStack separatorItem;
    private ItemStack warpItemPrice;
    private final ArrayList<World> blacklistedWorlds;
    private boolean w2w; // Shown in info command
    private boolean warpSafety; // Shown in info command

    /**
     * Constructor for plugin configuration.
     * @param standardLimit standard warp limit
     * @param teleportDelay teleport delay
     * @param refreshRateInMinutes refresh rate for the GUI
     * @param warpPrice virtual money price of warp
     * @param guiItem top item of GUI
     * @param separatorItem top and bottom row of items in GUI
     * @param warpSafety true if warp safety is enabled
     * @param w2w true if world to world teleport is enabled
     * @param worlds list of blacklisted worlds
     * @param warpItemPrice price to set warp in items
     */
    public PluginConfiguration(int standardLimit, int teleportDelay, int refreshRateInMinutes, double warpPrice,
                               ItemStack guiItem, ItemStack separatorItem, boolean warpSafety, boolean w2w, ArrayList<World> worlds,
                               ItemStack warpItemPrice) {

        this.standardLimit = standardLimit;
        this.teleportDelay = teleportDelay;
        this.refreshRateInMinutes = refreshRateInMinutes;
        this.warpPrice = warpPrice;
        this.guiItem = guiItem;
        this.separatorItem = separatorItem;
        this.warpSafety = warpSafety;
        this.w2w = w2w;
        this.blacklistedWorlds = worlds;
        this.warpItemPrice = warpItemPrice;
    }

    /**
     * Getter for the warp price.
     * @return Double containing warp price
     */
    public double getWarpPrice() {
        return this.warpPrice;
    }

    /**
     * Sets the warp price with message.
     * @param player player that requested
     * @param arg String provided by player
     */
    public void setWarpPrice(Player player, String arg) {

        double price;

        try {
            price = PlayerUtils.getDoubleFromUser(player, arg);
        }
        catch (NumberFormatException e) {
            return;
        }

        this.warpPrice = price;
        player.sendMessage(ChatColor.GREEN + Messages.SET_PRICE.getMessage());
    }

    /**
     * Setter for the warp item price with message.
     * @param player player that requested
     * @param arg String provided by the player
     */
    public void setWarpItemPrice(Player player, String arg) {

        int amount;

        try {
            amount = Integer.parseInt(arg);
        }
        catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
            return;
        }

        if (amount == 0) {
            this.warpItemPrice = null;
            player.sendMessage(ChatColor.GREEN + Messages.SET_PRICE.getMessage());
            return;
        }

        ItemStack price = PlayerUtils.getNotNullInMainHand(player);

        if (price == null) return;

        price.setAmount(amount);
        warpItemPrice = price;

        player.sendMessage(ChatColor.GREEN + Messages.SET_PRICE.getMessage());
    }

    /**
     * Getter for the warp item price.
     * @return ItemStack of the current warp item price
     */
    public ItemStack getWarpItemPrice() {
        return warpItemPrice;
    }

    /**
     * Checks if a player can afford the item price.
     * @param player player to check for
     * @return Boolean true if can afford
     */
    public boolean canAffordItemPrice(Player player) {

        Inventory inv = player.getInventory();

        if (warpItemPrice == null) return true;

        int amount = 0;

        for (ItemStack item : inv.getContents()) {

            if (item != null && item.getType().equals(warpItemPrice.getType()) && Objects.equals(item.getItemMeta(), warpItemPrice.getItemMeta())) {
                amount += item.getAmount();
            }
        }

        return amount >= warpItemPrice.getAmount();
    }

    /**
     * Makes a player pay the item price.
     * @param player player that has to pay
     */
    public void payItemPrice(Player player) {

        player.getInventory().removeItem(warpItemPrice);

    }

    /**
     * Getter for warp safety state.
     * @return Boolean true if warp safety is enabled
     */
    public boolean getWarpSafety() {
        return this.warpSafety;
    }

    /**
     * Setter for warp safety with message.
     * @param player player that requested
     * @param safety String provided by player
     */
    public void setWarpSafety(Player player, String safety) {

        boolean isOn;

        try {
            isOn = PlayerUtils.getBooleanFromUser(player, safety);
        }
        catch (Exception e) {
            return;
        }

        this.warpSafety = isOn;
        player.sendMessage(ChatColor.GREEN + Messages.WARP_SAFETY_UPDATED.getMessage());
    }

    /**
     * Getter for world to world state.
     * @return Boolean true if world to world is enabled
     */
    public boolean isWorldToWorld() {
        return w2w;
    }

    /**
     * Setter for world to world with message.
     * @param player player that requested
     * @param bool String provided by player
     */
    public void setWorldToWorld(Player player, String bool) {

        boolean state;

        try {
            state = PlayerUtils.getBooleanFromUser(player, bool);
        }
        catch (Exception e) {
            return;
        }

        this.w2w = state;

        if (state) {
            player.sendMessage(ChatColor.GREEN + Messages.ENABLED_W2W.getMessage());
        }
        else {
            player.sendMessage(ChatColor.GREEN + Messages.DISABLED_W2W.getMessage());
        }
    }

    /**
     * Checks if a world is blacklisted.
     * @param world world to check
     * @return Boolean true if world is blacklisted
     */
    public boolean isBlacklisted(World world) {
        return blacklistedWorlds.contains(world);
    }

    /**
     * Adds a blacklisted world with messages.
     * @param player player that requested
     * @param worldName world name given by player
     */
    public void addBlacklistedWorld(Player player, String worldName) {

        World world = PlayerUtils.getWorldFromString(player, worldName);

        if (world == null) return;

        if (blacklistedWorlds.contains(world)) {
            player.sendMessage(ChatColor.RED + Messages.ALREADY_BLACKLISTED.getMessage().replaceAll("PWORLDP", world.getName()));
            return;
        }

        blacklistedWorlds.add(world);
        player.sendMessage(ChatColor.GREEN + Messages.ADDED_BLACKLIST.getMessage().replaceAll("PWORLDP", world.getName()));
    }

    /**
     * Removes a blacklisted world with message.
     * @param player player that requested
     * @param worldName world name given by player
     */
    public void removeBlacklistedWorld(Player player, String worldName) {

        World world = PlayerUtils.getWorldFromString(player, worldName);

        if (world == null) return;

        if (!blacklistedWorlds.contains(world)) {
            player.sendMessage(ChatColor.RED + Messages.WORLD_NOT_BLACKLISTED.getMessage().replaceAll("PWORLDP", world.getName()));
            return;
        }

        blacklistedWorlds.remove(world);
        player.sendMessage(ChatColor.GREEN + Messages.REMOVED_BLACKLIST.getMessage().replaceAll("PWORLDP", world.getName()));
    }

    /**
     * Getter for blacklisted worlds as ArrayList of Strings.
     * @return ArrayList of world names
     */
    public ArrayList<String> getBlacklistedWorlds() {

        ArrayList<String> temp = new ArrayList<>();

        for (World world : this.blacklistedWorlds) {
            temp.add(world.getName());
        }


        return temp;
    }

    /**
     * Changes the top GUI item with message.
     * @param player player that requested
     */
    public void setGuiItem(Player player) {

        ItemStack guiItem = PlayerUtils.getNotNullInMainHand(player);

        if (guiItem == null) return;

        guiItem.setAmount(1);

        ItemMeta meta = guiItem.getItemMeta();

        // Colour codes
        meta.setDisplayName(meta.getDisplayName().replaceAll("&", "§"));
        guiItem.setItemMeta(meta);

        this.guiItem = guiItem;
        PWarp.gC.updateGuiItem();
        player.sendMessage(ChatColor.GREEN + Messages.GUI_ITEM_CHANGED.getMessage());
    }

    /**
     * Changes the top and bottom row of items with message.
     * @param player player that requested
     */
    public void setSeparatorItem(Player player) {

        ItemStack separatorItem = PlayerUtils.getNotNullInMainHand(player);

        if (separatorItem == null) return;

        separatorItem.setAmount(1);

        // Make display name empty
        ItemMeta meta = separatorItem.getItemMeta();
        meta.setDisplayName(" ");
        separatorItem.setItemMeta(meta);

        this.separatorItem = separatorItem;
        PWarp.gC.updateSeparatorItem();
        player.sendMessage(ChatColor.GREEN + Messages.CHANGED_SEPARATOR.getMessage());
    }

    /**
     * Getter for the top GUI item.
     * @return the top GUI ItemStack
     */
    public ItemStack getGuiItem() {
        return this.guiItem;
    }

    /**
     * Getter for the top and bottom row of items.
     * @return ItemStack
     */
    public ItemStack getSeparatorItem() {
        return this.separatorItem;
    }

    /**
     * Sets the standard limit with message.
     * @param sender sender that requested
     * @param arg String provided by sender
     */
    public void setStandardLimit(CommandSender sender, String arg) {

        int standardLimit = 0;

        try {
            standardLimit = Integer.parseInt(arg);
        }
        catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
        }

        if (standardLimit < 0) {
            sender.sendMessage(ChatColor.RED + Messages.LIMIT_ABOVE_0.getMessage());
            return;
        }

        this.standardLimit = standardLimit;
        sender.sendMessage(ChatColor.GREEN + Messages.LIMIT_CHANGED.getMessage());
    }

    /**
     * Sets the teleport delay.
     * @param teleportDelay new teleport delay
     */
    public void setTeleportDelay(int teleportDelay) {
        this.teleportDelay = teleportDelay;
    }

    /**
     * Sets the GUI refresh rate with message.
     * @param sender sender that requested
     * @param arg String passed by sender
     */
    public void setRefreshRateInMinutes(CommandSender sender, String arg) {

        int newRefreshRate;

        try {
            newRefreshRate = Integer.parseInt(arg);
        }
        catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
            return;
        }

        this.refreshRateInMinutes = newRefreshRate;
    }

    /**
     * Getter for default limit.
     * @return Integer default warp limit
     */
    public int getStandardLimit() {
        return standardLimit;
    }

    /**
     * Getter for teleport delay.
     * @return Integer teleport delay
     */
    public int getTeleportDelay() {
        return teleportDelay;
    }

    /**
     * Getter for GUI refresh rate.
     * @return Integer GUI refresh rate
     */
    public int getRefreshRateInMinutes() {
        return refreshRateInMinutes;
    }

    /**
     * Shows the current configuration to a player.
     * @param player player that requested
     */
    public void showConfigurationInfo(Player player) {
        player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "----------" + ChatColor.RESET + ChatColor.YELLOW + "[Info]" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "----------");

        // Limit
        if (this.standardLimit == 0) {
            player.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Default warp limit" + ChatColor.GRAY + " - Unlimited");

        }
        else {
            player.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Default warp limit" + ChatColor.GRAY + " - " + standardLimit);
        }

        // Teleport delay
        if (this.teleportDelay == 0) {
            player.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Teleport delay" + ChatColor.GRAY + " - Disabled");

        }
        else {
            player.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Teleport delay" + ChatColor.GRAY + " - " + teleportDelay + " Seconds");
        }

        // Gui refresh rate
        player.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " GUI refresh rate" + ChatColor.GRAY + " - Every " + refreshRateInMinutes + " minutes");

        // World to world teleport
        if (w2w) {
            player.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " World to world teleport" + ChatColor.GRAY + " - Enabled");

        }
        else {
            player.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " World to world teleport" + ChatColor.GRAY + " - Disabled");
        }

        // Warp safety
        if (warpSafety) {
            player.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Warp safety" + ChatColor.GRAY + " - Enabled");
        }
        else {
            player.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Warp safety" + ChatColor.GRAY + " - Disabled");
        }

        // Total amount of warps
        player.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Total amount of warps" + ChatColor.GRAY + " - " + PWarp.wL.getWarps().size() + " Warps");

        // Plugin author :)
        player.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Plugin author: " + ChatColor.GRAY + "The_King_Senne");

    }

    /**
     * Lists the currently blacklisted worlds to a player.
     * @param player player that requested
     */
    public void listBlacklistedWorlds(Player player) {

        player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.RESET + ChatColor.YELLOW + "[Blacklist]"
            + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----");

        player.sendMessage(ChatColor.GOLD + "» " + ChatColor.YELLOW + String.join(ChatColor.GOLD + "\n » " + ChatColor.YELLOW, getBlacklistedWorlds()));
    }

    /**
     * Reads the current plugin configuration from a file.
     * @return a new PluginConfiguration
     */
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
            pC = new PluginConfiguration(0, 0, 15, 0, null, null, true, true, new ArrayList<>(), null);
        }

        return pC;
    }

    /**
     * Writes the current plugin configuration to file.
     */
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

    /**
     * Converts the current plugin configuration to json format.
     * @return a String containing the json format
     */
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
        if (this.warpItemPrice != null) {
            properties.put("warpItemPrice", gson.toJson(this.warpItemPrice.serialize()));
            properties.put("warpItemPriceAmount", warpItemPrice.getAmount());
        }

        properties.put("warpSafety", warpSafety);
        properties.put("worldToWorld", w2w);

        properties.put("worldSize", blacklistedWorlds.size());

        for (int i = 0; i < blacklistedWorlds.size(); i++) {
            properties.put("world" + i, blacklistedWorlds.get(i).getName());
        }

        return gson.toJson(properties);
    }

    /**
     * Convertss a json formatted string to a PluginConfiguration.
     * @param properties the json String
     * @return a new PluginConfiguration
     */
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

        ItemStack warpItemPrice = null;

        if (map.get("warpItemPrice") != null) {
            warpItemPrice = ItemStack.deserialize( gson.fromJson((String) map.get("warpItemPrice"), Map.class));

            double amount = (double) map.get("warpItemPriceAmount");
            warpItemPrice.setAmount((int) amount);
        }

        boolean warpSafety = true;

        if (map.get("warpSafety") != null) warpSafety = (boolean) map.get("warpSafety");

        boolean w2w = true;

        if (map.get("worldToWorld") != null) w2w = (boolean) map.get("worldToWorld");

        int size = 0;

        if (map.get("worldSize") != null) {
            double sizeDouble = (double) map.get("worldSize");
            size = (int) sizeDouble;
        }

        ArrayList<String> worldNames = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            worldNames.add((String) map.get("world" + i));
        }

        ArrayList<World> worlds = new ArrayList<>();

        for (String name : worldNames) {
            worlds.add(Bukkit.getWorld(name));
        }

        return new PluginConfiguration(limit, delay, refreshRate, warpPrice,guiItem, separatorItem, warpSafety, w2w, worlds, warpItemPrice);

    }

}
