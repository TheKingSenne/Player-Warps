package me.tks.playerwarp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.tks.dependencies.GriefPreventionPlugin;
import me.tks.dependencies.VaultPlugin;
import me.tks.messages.Messages;
import me.tks.utils.ItemUtils;
import me.tks.utils.PlayerUtils;
import org.bukkit.*;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;

public class Warp implements Serializable {

    /**
     * Warp variables.
     */
    private String name;
    private Location loc;
    private boolean isPrivate;
    private final List<String> trustedPlayers;
    private ItemStack guiItem;
    private final String owner;
    private ArrayList<String> lore;
    private int visitors;
    private boolean isHidden;


    public Warp(String name, Location loc, Player owner) {

        this.name = name;
        this.loc = loc;
        this.isPrivate = PWarp.pC.getDefaultPrivacy();
        this.trustedPlayers = new ArrayList<>();
        this.owner = owner.getUniqueId().toString();
        this.lore = new ArrayList<>();
        this.visitors = 0;
        this.guiItem = new ItemStack(Material.CONDUIT, 1);
        this.isHidden = false;

        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        ItemMeta meta = guiItem.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + name);

        lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------");
        lore.add(ChatColor.GOLD + "");
        lore.add(ChatColor.GOLD + "");
        lore.add(ChatColor.GOLD + "");
        lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------");
        lore.add(ChatColor.AQUA + Messages.GUI_VISITORS.getMessage() + " " + this.visitors);
        lore.add(ChatColor.AQUA + Messages.GUI_WARP_OWNER.getMessage() + " " + owner.getName());
        lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------");

        meta.setLore(lore);
        guiItem.setItemMeta(meta);



    }

    public Warp(String name, Location loc, boolean isPrivate, List<String> trustedPlayers, ItemStack guiItem, String owner, ArrayList<String> lore, int visitors, boolean isHidden) {
        this.name = name;
        this.loc = loc;
        this.isPrivate = isPrivate;
        this.trustedPlayers = trustedPlayers;
        this.guiItem = guiItem;
        this.owner = owner;
        this.lore = lore;
        this.visitors = visitors;
        this.isHidden = isHidden;

        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        ItemMeta meta = guiItem.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + name);
        this.lore.set(0, ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------");
        this.lore.set(4, ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------");
        this.lore.set(5, ChatColor.AQUA + Messages.GUI_VISITORS.getMessage() + " " + visitors);
        this.lore.set(6,ChatColor.AQUA + Messages.GUI_WARP_OWNER.getMessage() + " " + Bukkit.getOfflinePlayer(UUID.fromString(owner)).getName());
        this.lore.set(7, ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------");
        meta.setLore(this.lore);

        this.guiItem.setItemMeta(meta);
    }

    /**
     * Creates a new warp when a player requests to set a warp.
     * @param player player that requested
     * @param name name of the warp
     * @param wL current WarpList
     */
    public static void setWarp(Player player, String name, WarpList wL) {

        // Check if player has access to claim
        if (Bukkit.getPluginManager().getPlugin("GriefPreventionPlugin") != null) {
            if (!GriefPreventionPlugin.hasAccess(player)) return;
        }
        double warpPrice = PWarp.pC.getWarpPrice();

        ItemStack warpItemPrice = PWarp.pC.getWarpItemPrice();

        boolean moneyPrice = warpPrice != 0;
        boolean itemPrice = warpItemPrice != null;

        // Economy managing
        if (itemPrice || moneyPrice) {

            if (itemPrice && !PWarp.pC.canAffordItemPrice(player)) {

                if (!moneyPrice) {
                    player.sendMessage(ChatColor.RED + Messages.CANT_AFFORD_ITEM.getMessage()
                        .replaceAll("PITEMAMOUNTP", String.valueOf(warpItemPrice.getAmount()))
                        .replaceAll("PITEMP", warpItemPrice.getType().name().toLowerCase().replaceAll("_", " ") + "(s)"));
                }
                else {
                    player.sendMessage(ChatColor.RED + Messages.CANT_AFFORD_BOTH.getMessage()
                        .replaceAll("PMONEYP", String.valueOf(warpPrice))
                        .replaceAll("PITEMAMOUNTP", String.valueOf(warpItemPrice.getAmount()))
                        .replaceAll("PITEMP", warpItemPrice.getType().name().toLowerCase().replaceAll("_", " ") + "(s)"));
                }
                return;
            }

            if (moneyPrice && !VaultPlugin.hasEnoughMoney(player, warpPrice)) {

                if (!itemPrice) {
                    player.sendMessage(ChatColor.RED + Messages.CANT_AFFORD_MONEY.getMessage().replaceAll("PMONEYP", String.valueOf(warpPrice)));
                }
                else {
                    player.sendMessage(ChatColor.RED + Messages.CANT_AFFORD_BOTH.getMessage()
                        .replaceAll("PMONEYP", String.valueOf(warpPrice))
                        .replaceAll("PITEMAMOUNTP", String.valueOf(warpItemPrice.getAmount()))
                        .replaceAll("PITEMP", warpItemPrice.getType().name().toLowerCase().replaceAll("_", " ") + "(s)"));
                }
                return;
            }

            if (itemPrice)
            PWarp.pC.payItemPrice(player);

            if (moneyPrice)
            VaultPlugin.getEconomy().withdrawPlayer(player, warpPrice);
        }

        if (PWarp.pC.isBlacklisted(player.getLocation().getWorld())) {
            player.sendMessage(ChatColor.RED + Messages.BLACKLISTED_WORLD.getMessage());
            return;
        }

        if (PWarp.pC.getWarpSafety() && !PlayerUtils.isInSafeLocation(player)) return;

        if (wL.warpExists(name)) {
            player.sendMessage(ChatColor.RED + Messages.NAME_IN_USE.getMessage().replaceAll("PWARPNAMEP", name));
            return;
        }

        if (wL.ownsTooMany(player)) {
            player.sendMessage(ChatColor.RED + Messages.LIMIT_REACHED.getMessage().replaceAll("PLIMITP", "" + WarpList.getPersonalLimit(player)));
            return;
        }

        name = name.toLowerCase();

        wL.addWarp(new Warp(name, player.getLocation(), player));

        // Message manager
        if (moneyPrice || itemPrice) {

            // Only paid items
            if (!moneyPrice) {
                player.sendMessage(ChatColor.GREEN + Messages.CREATED_ITEM_PAID_WARP.getMessage()
                    .replaceAll("PITEMAMOUNTP", String.valueOf(warpItemPrice.getAmount()))
                    .replaceAll("PITEMP", warpItemPrice.getType().name().toLowerCase().replaceAll("_", " ") + "(s)"));
                return;
            }
            // Only paid money
            else if (!itemPrice) {
                player.sendMessage(ChatColor.GREEN + Messages.CREATED_MONEY_PAID_WARP.getMessage()
                    .replaceAll("PMONEYP", String.valueOf(warpPrice)));

                return;
            }
            // Paid both
            else {
                player.sendMessage(ChatColor.GREEN + Messages.CREATED_BOTH_PAID_WARP.getMessage()
                    .replaceAll("PMONEYP", String.valueOf(warpPrice))
                    .replaceAll("PITEMAMOUNTP", String.valueOf(warpItemPrice.getAmount()))
                    .replaceAll("PITEMP", warpItemPrice.getType().name().toLowerCase().replaceAll("_", " ") + "(s)"));
                return;
            }
        }

        // Didn't pay anything
        player.sendMessage(ChatColor.GREEN + Messages.CREATED_FREE_WARP.getMessage());
    }

    /**
     * Getter for the warp name.
     *
     * @return Warp name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Checks if the warp is hidden.
     * @return Boolean true if hidden
     */
    public boolean isHidden() {
        return this.isHidden;
    }

    /**
     * Sets the hidden state of a warp with message.
     * @param player player that requested
     * @param state Boolean true if hidden
     */
    public void setHidden(Player player, String state) {

        if (!isOwnerWithMessage(player)) return;

        boolean hiddenState;

        try {
            hiddenState = PlayerUtils.getBooleanFromUser(player, state);
        }
        catch (Exception e) {
            return;
        }

        this.isHidden = hiddenState;

        if (isHidden) {
            PWarp.gC.removeItem(this);
        }

        player.sendMessage(ChatColor.GREEN + Messages.HIDDEN_UNHIDDEN.getMessage());
    }

    /**
     * Changes the name of a warp.
     *
     * @param name the new name
     */
    public void changeName(Player player, String name) {

        if (!isOwnerWithMessage(player)) return;

        String oldName = this.name;

        this.name = name;
        updateItemStack();
        PWarp.gC.updateItem(this, oldName);
        player.sendMessage(ChatColor.GREEN + Messages.RENAMED_WARP.getMessage());
    }

    /**
     * Checks if a warp is safe
     * @param player player that requested
     * @return Boolean true if warp is safe
     */
    public boolean isSafe(Player player) {

        Location loc = this.loc.clone();

        loc.setY(loc.getY() - 1);

        Material material = loc.getBlock().getType();

        if (material.equals(Material.AIR) || material.equals(Material.LAVA) || material.equals(Material.FIRE)) {
            player.sendMessage(ChatColor.RED + Messages.IS_UNSAFE.getMessage());
            return false;
        }

        for (int i = 1; i < 3; i++) {

            loc.setY(loc.getY() + i);
            material = loc.getBlock().getType();

            if ((!material.equals(Material.AIR) && material.isSolid()) || material.equals(Material.FIRE) || material.equals(Material.COBWEB) || (material.equals(Material.LAVA) && !(loc.getBlock().getBlockData() instanceof Slab))) {
                player.sendMessage(ChatColor.RED + Messages.IS_OBSTRUCTED.getMessage());
                return false;
            }
        }

        return true;
    }

    /**
     * Updates the ItemStack in the GUI.
     */
    private void updateItemStack() {

        ItemMeta meta = this.guiItem.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + this.name.substring(0,1).toUpperCase() + this.name.substring(1));
        meta.setLore(lore);

        guiItem.setItemMeta(meta);
    }

    /**
     * Getter for visitors.
     *
     * @return the current amount of visitors
     */
    public int getVisitors() {
        return this.visitors;
    }

    /**
     * Moves a warp to a new location.
     * @param player player that requested
     */
    public void move(Player player) {

        if (!isOwnerWithMessage(player)) return;

        this.loc = player.getLocation();
        player.sendMessage(ChatColor.GREEN + Messages.MOVED_WARP.getMessage());
    }

    /**
     * Sets the privacy state of a warp.
     *
     * @param isPrivate True private, false public
     */
    public void setPrivacyState(Player owner, boolean isPrivate) {

        if (!isOwnerWithMessage(owner)) return;

        this.isPrivate = isPrivate;

        if (isPrivate) {
            owner.sendMessage(ChatColor.GREEN + Messages.MADE_PRIVATE.getMessage());
        }
        else {
            owner.sendMessage(ChatColor.GREEN + Messages.MADE_PUBLIC.getMessage());
        }
    }

    /**
     * Adds a trusted player.
     * @param owner Player who wants to trust
     * @param trusted Player to be trusted
     */
    public void addTrustedPlayer(Player owner, OfflinePlayer trusted) {

        if (!isOwnerWithMessage(owner)) return;

        if (trustedPlayers.contains(trusted.getUniqueId().toString())) {
            owner.sendMessage(ChatColor.RED + Messages.PLAYER_ALREADY_TRUSTED.getMessage());
            return;
        }

        this.trustedPlayers.add(trusted.getUniqueId().toString());
        owner.sendMessage(ChatColor.GREEN + Messages.PLAYER_TRUSTED.getMessage().replaceAll("PPLAYERP", trusted.getName()));
    }

    /**
     * Removes a trusted player.
     * @param owner Player who wants to untrust
     * @param untrusted Player to be untrusted
     */
    public void removeTrustedPlayer(Player owner, OfflinePlayer untrusted) {

        if (!isOwnerWithMessage(owner)) return;

        if (!trustedPlayers.contains(untrusted.getUniqueId().toString())) {
            owner.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_TRUSTED.getMessage());
            return;
        }

        this.trustedPlayers.remove(untrusted.getUniqueId().toString());
        owner.sendMessage(ChatColor.GREEN + Messages.PLAYER_UNTRUSTED.getMessage().replaceAll("PPLAYERP", untrusted.getName()));
    }

    /**
     * Changes the lore of the current warp.
     *
     * @param line New line for the lore
     * @param row  Row of the line (1/3)
     */
    public void setLore(Player player, String line, int row) {

        if (!isOwnerWithMessage(player)) return;

        if (row > 4 || row <= 0) {
            player.sendMessage(ChatColor.RED + Messages.LORE_LIMIT.getMessage());
            return;
        }

        this.lore.set(row, ChatColor.GOLD + line.replaceAll("&", "§"));

        updateLore();
        PWarp.gC.updateItem(this);
        player.sendMessage(ChatColor.GREEN + Messages.UPDATED_LORE.getMessage());
    }

    /**
     * Updates the warp's lore.
     */
    public void updateLore() {

        ItemMeta meta = this.guiItem.getItemMeta();
        this.lore.set(0, ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------");
        this.lore.set(4, ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------");
        this.lore.set(5, ChatColor.AQUA + Messages.GUI_VISITORS.getMessage() + " " + this.visitors);
        this.lore.set(6,ChatColor.AQUA + Messages.GUI_WARP_OWNER.getMessage() + " " + Bukkit.getOfflinePlayer(UUID.fromString(this.owner)).getName());
        this.lore.set(7, ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------");

        meta.setLore(lore);
        this.guiItem.setItemMeta(meta);

    }

    /**
     * Teleport a player to the current warp.
     *
     * @param player the player
     */
    public void goTo(Player player) {

        if (!PWarp.pC.isWorldToWorld()) {

            if (!player.getLocation().getWorld().equals(this.getWorld())) {
                player.sendMessage(ChatColor.RED + Messages.W2WTELEPORT.getMessage());
                return;
            }
        }

        if (isPrivate && !isTrusted(player)) {
            player.sendMessage(ChatColor.RED + Messages.NOT_TRUSTED.getMessage());
            return;
        }

        if (PWarp.pC.getWarpSafety() && !this.isSafe(player)) return;

        PWarp.events.addPlayer(player);

        if (PWarp.pC.getTeleportDelay() != 0) {
            player.sendMessage(ChatColor.GREEN + Messages.DONT_MOVE.getMessage().replaceAll("PSECONDSP", PWarp.pC.getTeleportDelay() + ""));

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PWarp.getProvidingPlugin(PWarp.class), () -> {
                teleportTo(player, true);
            }, 20 * PWarp.pC.getTeleportDelay());

        }
        else {
            teleportTo(player, false);
        }
    }

    /**
     * Teleports a play to a warp with a delay.
     * @param player player to teleport
     * @param delay delay
     */
    public void teleportTo(Player player, boolean delay) {

        if (delay && !PWarp.events.isTeleporting(player)) return;

        player.teleport(this.loc);

        // Only increase visitors if it's not the warp owner
        if (!this.owner.equals(player.getUniqueId().toString())) {
            this.visitors++;
            updateLore();
            PWarp.gC.updateItem(this);
        }
        player.sendMessage(ChatColor.GREEN + Messages.TELEPORTED.getMessage());
        PWarp.events.removePlayer(player);

    }

    /**
     * Check if a player is trusted to a warp.
     * @param player the player
     * @return Boolean true if the player is trusted
     */
    public boolean isTrusted(Player player) {
        return this.trustedPlayers.contains(player.getUniqueId().toString()) || isOwner(player);
    }

    /**
     * Check if player is owner or has an override permission.
     * @param player the player
     * @return True if owner/permission, false if not
     */
    public boolean isOwner(Player player) {
        // Player is owner or has an override permission
        return this.owner.equals(player.getUniqueId().toString()) || player.hasPermission("pwarp.manage");
    }

    /**
     * Check if player is owner or has an override permission. Sends a message if player is not an owner.
     *
     * @param player the player
     * @return True if owner/permission, false if not
     */
    public boolean isOwnerWithMessage(Player player) {
        // Player is owner or has override permission
        if (this.owner.equals(player.getUniqueId().toString()) || player.hasPermission("pwarp.manage")) {
            return true;
        }

        // Notify player
        player.sendMessage(ChatColor.RED + Messages.NOT_AN_OWNER.getMessage());
        return false;
    }

    /**
     * Getter for warp ItemStack.
     *
     * @return the ItemStack
     */
    public ItemStack getItemStack() {
        return this.guiItem;
    }

    /**
     * Changes the ItemStack of the current warp to the one held by the player.
     * @param player the player
     */
    public void setItemStack(Player player) {

        if (!isOwnerWithMessage(player)) return;


        ItemStack newItem = PlayerUtils.getNotNullInMainHand(player);

        if (newItem == null) return;

//        this.getItemStack().setType(newItem.getType());
        guiItem = newItem;
        ItemMeta meta = guiItem.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.YELLOW + name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
        guiItem.setItemMeta(meta);

        PWarp.gC.updateItem(this);

        player.sendMessage(ChatColor.GREEN + Messages.CHANGED_WARP_ICON.getMessage());
    }

    /**
     * Getter for the world of the warp.
     * @return the world
     */
    public World getWorld() {
        return this.loc.getWorld();
    }

    /**
     * Converts the Warp object into json format to save.
     * @return A string containing the formatted object
     */
    public String toJson() {
        Type collectionType = new TypeToken<ArrayList<String>>(){}.getType();
        Map<String, Object> properties = new HashMap<>();

        Gson gson = new Gson();

        properties.put("name", name);
        properties.put("location", "" + gson.toJson(loc.serialize()));
        properties.put("isPrivate", "" + isPrivate);
        properties.put("trustedPlayers", gson.toJson(trustedPlayers, collectionType));
        properties.put("guiItem", gson.toJson(ItemUtils.serialize(guiItem)));

        if (guiItem.getType().equals(Material.PLAYER_HEAD)) {
            SkullMeta meta = (SkullMeta) guiItem.getItemMeta();
            if (meta != null && meta.hasOwner()) {
                properties.put("skullOwner", meta.getOwningPlayer().getUniqueId().toString());
            }
        }

        properties.put("owner", owner);
        properties.put("lore", gson.toJson(lore, collectionType));
        properties.put("visitors", "" + visitors);
        properties.put("isHidden", "" + isHidden);

        return gson.toJson(properties);
    }

    /**
     * Gets a Warp object from a json format.
     * @param properties The string with json format
     * @return A new Warp object
     */
    public static Warp fromJson(String properties) {

        Gson gson = new Gson();
        try {
            HashMap<String, Object> objectMap = gson.fromJson(properties, HashMap.class);

            String name = (String) objectMap.get("name");
            Location loc = Location.deserialize(gson.fromJson((String) objectMap.get("location"), Map.class));
            boolean isPrivate = (Boolean) objectMap.get("isPrivate");
            List<String> trustedPlayers = (List<String>) objectMap.get("trustedPlayers");
            ItemStack guiItem = ItemStack.deserialize(gson.fromJson((String) objectMap.get("guiItem"), Map.class));
            String owner = (String) objectMap.get("owner");
            ArrayList<String> lore = (ArrayList<String>) objectMap.get("lore");
            double visitors = (double) objectMap.get("visitors");

            boolean isHidden = false;

            if (objectMap.get("isHidden") != null) {
                isHidden = (boolean) objectMap.get("isHidden");
            }
            return new Warp(name, loc, isPrivate, trustedPlayers, guiItem, owner, lore, (int) visitors, isHidden);
        }
        catch (Exception e) {
            // do nothing
        }

        Map<String, String> map = gson.fromJson(properties, Map.class);

        String name = map.get("name");
        Location loc = Location.deserialize(gson.fromJson(map.get("location"), Map.class));
        boolean isPrivate = Boolean.parseBoolean(map.get("isPrivate"));

        Type collectionType = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> trustedPlayers = gson.fromJson(map.get("trustedPlayers"), collectionType);

        ItemStack guiItem;

        if (map.get("guiItem") != null) {
            try {
                guiItem = ItemStack.deserialize(gson.fromJson(map.get("guiItem"), Map.class));
            }
            catch (Exception e) {
                guiItem = ItemUtils.deserialize(gson.fromJson(map.get("guiItem"), String.class));
            }
        }
        else {
            guiItem = new ItemStack(Material.CONDUIT, 1);
        }

        // letsgo struggling with some skulls *insert sad noises here* :D
//        if (guiItem.getType().equals(Material.PLAYER_HEAD)) {
//
//            if (map.get("skullOwner") != null) {
//
//                SkullMeta meta = (SkullMeta) guiItem.getItemMeta();
//                meta.setOwningPlayer(Bukkit.getOfflinePlayer(map.get("skullOwner")));
//                guiItem.setItemMeta(meta);
//            }
//
//        }


        String owner = map.get("owner");
        ArrayList<String> lore = gson.fromJson(map.get("lore"), collectionType);
        int visitors = Integer.parseInt(map.get("visitors"));

        boolean isHidden = false;

        if (map.get("isHidden") != null) {
            isHidden = Boolean.parseBoolean(map.get("isHidden"));
        }

        return new Warp(name, loc, isPrivate, trustedPlayers, guiItem, owner, lore, visitors, isHidden);
    }

}
