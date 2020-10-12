package com.elixiumnetwork.playerwarp;

import com.elixiumnetwork.messages.Messages;
import io.papermc.lib.PaperLib;
import org.bukkit.inventory.*;
import com.elixiumnetwork.gui.*;
import org.bukkit.configuration.file.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import me.ryanhamshire.GriefPrevention.*;
import org.bukkit.permissions.*;
import java.io.*;
import net.milkbowl.vault.economy.*;
import org.bukkit.plugin.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import java.util.*;

public class Warp
{
    private static List<String> warps;
    private static ItemStack price;
    private static PWarpPlugin p;
    private final GUI gui;
    private int taskId;
    public static final List<String> teleportingPlayers;
    public FileConfiguration wC;

    public Warp() {
        this.gui = new GUI();
        this.wC = new YamlConfiguration();
    }

    public void setWarp(String name, final Location loc, final CommandSender sender, final PWarpPlugin p) {
        Warp.p = p;
        this.wC = p.wF.getWarpFile();
        name = name.toLowerCase();
        final Player owner = (Player)sender;
        if (Bukkit.getPluginManager().getPlugin("GriefPrevention") != null) {
            final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(owner.getLocation(), true, (Claim)null);
            if (claim != null && claim.allowAccess(owner) != null) {
                sender.sendMessage(ChatColor.RED + Messages.NO_ACCESS_GP.getMessage());
                return;
            }
        }
        Warp.warps = (List<String>)this.wC.getStringList("warpList");
        if (p.getConfig().getStringList("blacklist") != null && !sender.hasPermission("pwarp.blacklist.bypass")) {
            for (int i = 0; i < p.getConfig().getStringList("blacklist").size(); ++i) {
                if (p.getConfig().getStringList("blacklist").get(i).equalsIgnoreCase(owner.getWorld().getName())) {
                    sender.sendMessage(ChatColor.RED + Messages.BLACKLISTED_WORLD.getMessage());
                    return;
                }
            }
        }
        if (p.getConfig().get("warpLimit") != null && this.wC.get("warps") != null && !sender.isOp() && !sender.hasPermission("pwarp.nolimit")) {
            int warpAmount = 0;
            final int warpLimit = p.getConfig().getInt("warpLimit");
            for (final String warp : this.wC.getConfigurationSection("warps").getKeys(false)) {
                if (this.wC.getString("warps." + warp + ".owner-UUID") != null) {
                    final String configOwner = this.wC.getString("warps." + warp + ".owner-UUID");
                    if (configOwner.equals(owner.getUniqueId().toString())) {
                        ++warpAmount;
                    }
                    int newLimit = warpLimit;
                    final List<Integer> limits = new ArrayList<Integer>();
                    limits.add(warpLimit);
                    for (final PermissionAttachmentInfo perm : owner.getEffectivePermissions()) {
                        final int length = perm.getPermission().length();
                        if (length >= 18) {
                            final String permission = perm.getPermission().substring(0, 16);
                            if (!permission.equals("pwarp.otherlimit")) {
                                continue;
                            }
                            try {
                                newLimit = Integer.parseInt(perm.getPermission().substring(17));
                                limits.add(newLimit);
                            }
                            catch (Exception e2) {
                                break;
                            }
                        }
                    }
                    Collections.sort(limits, Collections.reverseOrder());
                    newLimit = limits.get(0);
                    if (newLimit < warpLimit && warpAmount >= newLimit) {
                        sender.sendMessage(ChatColor.RED + Messages.LIMIT_REACHED.getMessage().replaceAll("PLIMITP", newLimit + ""));
                        return;
                    }
                    if (warpAmount < warpLimit) {
                        continue;
                    }
                    if (warpLimit >= newLimit) {
                        if (newLimit < warpLimit) {
                            sender.sendMessage(ChatColor.RED + Messages.LIMIT_REACHED.getMessage().replaceAll("PLIMITP", newLimit + ""));
                            return;
                        }
                        sender.sendMessage(ChatColor.RED + Messages.LIMIT_REACHED.getMessage().replaceAll("PLIMITP", warpLimit + ""));
                        return;
                    }
                    else {
                        if (warpAmount >= newLimit) {
                            sender.sendMessage(ChatColor.RED + Messages.LIMIT_REACHED.getMessage().replaceAll("PLIMITP", newLimit + ""));
                            return;
                        }
                    }
                }
            }
        }
        if (this.wC.get("warps." + name) != null) {
            sender.sendMessage(ChatColor.RED + Messages.NAME_IN_USE.getMessage().replaceAll("PWARPNAMEP", name));
            return;
        }
        if (p.getConfig().get("warpPrice.enable") == null) {
            p.getConfig().set("warpPrice.enable", (Object)false);
        }
        if (p.getConfig().get("warpMoneyPrice.enable") == null) {
            p.getConfig().set("warpMoneyPrice.enable", (Object)false);
        }
        boolean canAfford = false;
        boolean useEco = false;
        boolean useItem = false;
        double moneyAmount = 0.0;
        if (p.getConfig().get("warpMoneyPrice.price") != null && p.getConfig().getBoolean("warpMoneyPrice.enable") && !sender.hasPermission("pwarp.noprice")) {
            final OfflinePlayer offlineWarpOwner = (OfflinePlayer)owner;
            useEco = true;
            moneyAmount = p.getConfig().getDouble("warpMoneyPrice.price");
            final Economy eco = p.v.getEconomy();
            canAfford = (moneyAmount <= eco.getBalance(offlineWarpOwner));
        }
        ItemStack itemStack = null;
        int priceAmount = 0;
        if (p.getConfig().get("warpPrice.item") != null && p.getConfig().getBoolean("warpPrice.enable") && !sender.hasPermission("pwarp.noprice")) {
            useItem = true;
            Warp.price = p.getConfig().getItemStack("warpPrice.item");
            itemStack = new ItemStack(Warp.price.getType(), Warp.price.getAmount());
            priceAmount = Warp.price.getAmount();
            itemStack.setAmount(0);
            for (int j = 0; j < owner.getInventory().getSize(); ++j) {
                if (owner.getInventory().getItem(j) != null) {
                    if (Warp.price.hasItemMeta()) {
                        if (owner.getInventory().getItem(j).isSimilar(Warp.price)) {
                            itemStack.setAmount(itemStack.getAmount() + owner.getInventory().getItem(j).getAmount());
                        }
                    }
                    else if (owner.getInventory().getItem(j).getType() == Warp.price.getType()) {
                        itemStack.setAmount(itemStack.getAmount() + owner.getInventory().getItem(j).getAmount());
                    }
                }
            }
        }
        if (!useEco && useItem) {
            if (itemStack.getAmount() < priceAmount) {
                if (!Warp.price.hasItemMeta()) {
                    owner.sendMessage(ChatColor.RED + Messages.CANT_AFFORD_ITEM.getMessage().replaceAll("PITEMAMOUNTP", priceAmount + "").replaceAll("PITEMP", itemStack.getType().name().toLowerCase().replaceAll("_", " ") + "(s)"));
                }
                else if (Warp.price.getItemMeta().hasDisplayName()) {
                    owner.sendMessage(ChatColor.RED + Messages.CANT_AFFORD_ITEM.getMessage().replaceAll("PITEMAMOUNTP", priceAmount + "").replaceAll("PITEMP", Warp.price.getItemMeta().getDisplayName() + "(s)"));
                }
                else {
                    owner.sendMessage(ChatColor.RED + Messages.CANT_AFFORD_ITEM.getMessage().replaceAll("PITEMAMOUNTP", priceAmount + "").replaceAll("PITEMP", itemStack.getType().name().toLowerCase().replaceAll("_", " ") + "(s)"));
                }
                return;
            }
            owner.getInventory().removeItem(new ItemStack[] { p.getConfig().getItemStack("warpPrice.item") });
        }
        else if (!useItem && useEco) {
            if (!canAfford) {
                owner.sendMessage(ChatColor.RED + Messages.CANT_AFFORD_MONEY.getMessage().replaceAll("PMONEYAMOUNTP", "\\$" + moneyAmount));
                return;
            }
            p.v.getEconomy().withdrawPlayer((OfflinePlayer)owner, (double)p.getConfig().getInt("warpMoneyPrice.price"));
        }
        else if (useEco && useItem) {
            if (itemStack.getAmount() < priceAmount || !canAfford) {
                if (Warp.price.getItemMeta().hasDisplayName()) {
                    owner.sendMessage(ChatColor.RED + Messages.CANT_AFFORD_BOTH.getMessage().replaceAll("PMONEYAMOUNTP", "\\$" + p.getConfig().getDouble("warpMoneyPrice.price")).replaceAll("PITEMAMOUNTP", priceAmount + "").replaceAll("PITEMP", Warp.price.getItemMeta().getDisplayName() + "(s)"));
                    return;
                }
                owner.sendMessage(ChatColor.RED + Messages.CANT_AFFORD_BOTH.getMessage().replaceAll("PMONEYAMOUNTP", "\\$" + p.getConfig().getDouble("warpMoneyPrice.price")).replaceAll("PITEMAMOUNTP", priceAmount + "").replaceAll("PITEMP", itemStack.getType().name().toLowerCase().replaceAll("_", " ") + "(s)"));
                return;
            }
            else {
                owner.getInventory().removeItem(new ItemStack[] { p.getConfig().getItemStack("warpPrice.item") });
                p.v.getEconomy().withdrawPlayer((OfflinePlayer)owner, (double)p.getConfig().getInt("warpMoneyPrice.price"));
            }
        }
        Warp.warps.add(name);
        this.wC.set("warps." + name + ".location.x", (Object)loc.getX());
        this.wC.set("warps." + name + ".location.y", (Object)loc.getY());
        this.wC.set("warps." + name + ".location.z", (Object)loc.getZ());
        this.wC.set("warps." + name + ".location.world", (Object)loc.getWorld().getName());
        this.wC.set("warps." + name + ".location.pitch", (Object)(double)loc.getPitch());
        this.wC.set("warps." + name + ".location.yaw", (Object)(double)loc.getYaw());
        this.wC.set("warps." + name + ".location.direction", (Object)loc.getDirection());
        this.wC.set("warps." + name + ".owner-UUID", (Object)owner.getUniqueId().toString());
        this.wC.set("warps." + name + ".visitorCount", (Object)0);
        this.wC.set("warpList", (Object)Warp.warps);
        try {
            this.wC.save(new File(p.getDataFolder(), "warps.yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.gui.addItem(name, Warp.p);
        if (sender.hasPermission("pwarp.noprice")) {
            sender.sendMessage(ChatColor.GREEN + Messages.CREATED_FREE_WARP.getMessage());
        }
        else if (p.getConfig().getBoolean("warpPrice.enable") && p.getConfig().getString("warpPrice.item") != null && !p.getConfig().getBoolean("warpMoneyPrice.enable")) {
            final ItemStack paidPrice = p.getConfig().getItemStack("warpPrice.item");
            if (paidPrice.hasItemMeta()) {
                if (paidPrice.getItemMeta().hasDisplayName()) {
                    owner.sendMessage(ChatColor.GREEN + Messages.CREATED_ITEM_PAID_WARP.getMessage().replaceAll("PITEMAMOUNTP", paidPrice.getAmount() + "").replaceAll("PITEMP", paidPrice.getItemMeta().getDisplayName() + "(s)"));
                }
                else {
                    sender.sendMessage(ChatColor.GREEN + Messages.CREATED_ITEM_PAID_WARP.getMessage().replaceAll("PITEMAMOUNTP", paidPrice.getAmount() + "").replaceAll("PITEMP", paidPrice.getType().name().toLowerCase().replaceAll("_", " ") + "(s)"));
                }
            }
            else {
                sender.sendMessage(ChatColor.GREEN + Messages.CREATED_ITEM_PAID_WARP.getMessage().replaceAll("PITEMAMOUNTP", paidPrice.getAmount() + "").replaceAll("PITEMP", paidPrice.getType().name().toLowerCase().replaceAll("_", " ") + "(s)"));
            }
        }
        else if (p.getConfig().getBoolean("warpMoneyPrice.enable") && p.getConfig().getString("warpMoneyPrice.price") != null && !p.getConfig().getBoolean("warpPrice.enable")) {
            owner.sendMessage(ChatColor.GREEN + Messages.CREATED_MONEY_PAID_WARP.getMessage().replaceAll("PMONEYAMOUNTP", "\\$" + p.getConfig().getDouble("warpMoneyPrice.price")));
        }
        else if (p.getConfig().getBoolean("warpPrice.enable") && p.getConfig().getString("warpPrice.item") != null && p.getConfig().getBoolean("warpMoneyPrice.enable") && p.getConfig().getString("warpMoneyPrice.price") != null) {
            final ItemStack paidPrice = p.getConfig().getItemStack("warpPrice.item");
            if (paidPrice.hasItemMeta()) {
                if (paidPrice.getItemMeta().hasDisplayName()) {
                    owner.sendMessage(ChatColor.GREEN + Messages.CREATED_BOTH_PAID_WARP.getMessage().replaceAll("PITEMAMOUNTP", "" + paidPrice.getAmount()).replaceAll("PITEMP", paidPrice.getItemMeta().getDisplayName() + "(s)").replaceAll("PMONEYAMOUNTP", "\\$" + p.getConfig().getDouble("warpMoneyPrice.price")));
                }
                else {
                    sender.sendMessage(ChatColor.GREEN + Messages.CREATED_BOTH_PAID_WARP.getMessage().replaceAll("PITEMAMOUNTP", "" + paidPrice.getAmount()).replaceAll("PITEMP", paidPrice.getType().name().toLowerCase().replaceAll("_", " ") + "(s)").replaceAll("PMONEYAMOUNTP", "\\$" + p.getConfig().getDouble("warpMoneyPrice.price")));
                }
            }
            else {
                sender.sendMessage(ChatColor.GREEN + Messages.CREATED_BOTH_PAID_WARP.getMessage().replaceAll("PITEMAMOUNTP", "" + paidPrice.getAmount()).replaceAll("PITEMP", paidPrice.getType().name().toLowerCase().replaceAll("_", " ") + "(s)").replaceAll("PMONEYAMOUNTP", "\\$" + p.getConfig().getDouble("warpMoneyPrice.price")));
            }
        }
        else {
            sender.sendMessage(ChatColor.GREEN + Messages.CREATED_FREE_WARP.getMessage());
        }
    }

    public void removeWarp(String name, final CommandSender sender, final PWarpPlugin p) {
        Warp.p = p;
        this.wC = p.wF.getWarpFile();
        name = name.toLowerCase();
        if (!this.wC.isConfigurationSection("warps." + name)) {
            sender.sendMessage(ChatColor.RED + Messages.WARP_NOT_EXISTING.getMessage());
            (Warp.warps = (List<String>)this.wC.getStringList("warpList")).remove(name);
            this.wC.set("warpList", (Object)Warp.warps);
            try {
                this.wC.save(new File(p.getDataFolder(), "warps.yml"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            final Player player = (Player)sender;
            if (!player.getUniqueId().toString().equals(this.wC.getString("warps." + name + ".owner-UUID")) && !sender.hasPermission("pwarp.delete.others") && !sender.isOp()) {
                sender.sendMessage(ChatColor.RED + Messages.NOT_AN_OWNER.getMessage());
                return;
            }
            this.wC.set("warps." + name, (Object)null);
            (Warp.warps = (List<String>)this.wC.getStringList("warpList")).remove(name);
            this.wC.set("warpList", (Object)Warp.warps);
            sender.sendMessage(ChatColor.GREEN + Messages.REMOVED_WARP.getMessage());
            try {
                this.wC.save(new File(p.getDataFolder(), "warps.yml"));
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
            this.gui.delItem(name);
        }
    }

    public void goToWarp(String name, final CommandSender sender, final PWarpPlugin pl) {
        Warp.p = pl;
        this.wC = Warp.p.wF.getWarpFile();
        name = name.toLowerCase();
        final Player player = (Player)sender;
        final Location loc = new Location((World)Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0, 0.0f, 0.0f);
        if (!this.wC.isConfigurationSection("warps." + name.toLowerCase()) && !this.wC.isConfigurationSection("warps." + name.toLowerCase() + ".location")) {
            sender.sendMessage(ChatColor.RED + Messages.WARP_NOT_EXISTING.getMessage());
            return;
        }
        if (Warp.p.getConfig().get("worldToWorldTeleport") == null) {
            Warp.p.getConfig().set("worldToWorldTeleport", (Object)true);
            Warp.p.saveConfig();
        }
        if (!Warp.p.getConfig().getBoolean("worldToWorldTeleport") && !sender.hasPermission("pwarp.worldtoworld.bypass") && !player.getLocation().getWorld().getName().equalsIgnoreCase(this.wC.getString("warps." + name + ".location.world"))) {
            sender.sendMessage(ChatColor.RED + Messages.W2WTELEPORT.getMessage());
            return;
        }
        if (Warp.p.getConfig().get("teleportDelayInSeconds") == null) {
            Warp.p.getConfig().set("teleportDelayInSeconds", 0);
            Warp.p.saveConfig();
        }
        if (!this.isSafe(sender, name, pl)) {
            return;
        }
        if (this.wC.getString("warps." + name) == null) {
            sender.sendMessage(ChatColor.RED + Messages.CONFIGURED_WRONG.getMessage());
        }
        else {
            if (this.isPrivate(name, pl) && !this.isTrusted(sender, name, pl) && !sender.hasPermission("pwarp.private.bypass")) {
                sender.sendMessage(ChatColor.RED + Messages.NOT_TRUSTED.getMessage());
                return;
            }
            loc.setX(wC.getDouble("warps." + name + ".location.x"));
            loc.setY(wC.getDouble("warps." + name + ".location.y") + 1.0);
            loc.setZ(wC.getDouble("warps." + name + ".location.z"));
            if (this.wC.getString("warps." + name + ".location.direction") != null && this.wC.getString("warps." + name + ".location.pitch") != null && this.wC.getString("warps." + name + ".location.yaw") != null) {
                loc.setDirection(this.wC.getVector("warps." + name + ".location.direction"));
                loc.setPitch((float)this.wC.getLong("warps." + name + ".location.pitch"));
                loc.setYaw((float)this.wC.getLong("warps." + name + ".location.yaw"));
            }
            loc.setWorld(Bukkit.getServer().getWorld(this.wC.getString("warps." + name.toLowerCase() + ".location.world")));
            if (sender.hasPermission("pwarp.bypass.delay") || Warp.p.getConfig().getInt("teleportDelayInSeconds") == 0) {
                PaperLib.teleportAsync(player, loc);
                sender.sendMessage(ChatColor.GREEN + Messages.TELEPORTED.getMessage());
            }
            else if (Warp.p.getConfig().getInt("teleportDelayInSeconds") != 0) {
                sender.sendMessage(ChatColor.GREEN + Messages.DONT_MOVE.getMessage().replaceAll("PSECONDSP", Warp.p.getConfig().getInt("teleportDelayInSeconds") + ""));
                final Location playerLoc = player.getLocation();
                Warp.teleportingPlayers.add(player.getUniqueId().toString());
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Warp.p, new Runnable() {
                    @Override
                    public void run() {
                        if (player.getLocation().getBlockX() != playerLoc.getBlockX() || player.getLocation().getBlockY() != playerLoc.getBlockY() || player.getLocation().getBlockZ() != playerLoc.getBlockZ()) {
                            sender.sendMessage(ChatColor.RED + Messages.MOVED.getMessage());
                            Warp.teleportingPlayers.remove(player.getUniqueId().toString());
                            return;
                        }
                        PaperLib.teleportAsync(player, loc);
                        sender.sendMessage(ChatColor.GREEN + Messages.TELEPORTED.getMessage());
                        Warp.teleportingPlayers.remove(player.getUniqueId().toString());
                    }
                }, (long)(20 * Warp.p.getConfig().getInt("teleportDelayInSeconds")));
            }
            if (this.wC.getString("warps." + name + ".visitorCount").equals(null)) {
                this.wC.set("warps." + name + ".visitorCount", (Object)0);
            }
            int visitorCounter = this.wC.getInt("warps." + name + ".visitorCount");
            if (!player.getUniqueId().toString().equals(this.wC.getString("warps." + name + ".owner-UUID"))) {
                ++visitorCounter;
            }
            this.wC.set("warps." + name + ".visitorCount", (Object)visitorCounter);
            final Calendar currentDate = Calendar.getInstance();
            this.wC.set("warps." + name + ".date", (Object)((currentDate.get(2) + 1) * 30 + currentDate.get(5) + 1 + (currentDate.get(1) + 1) * 364));
            try {
                this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            this.gui.updateLore(name, pl);
        }
    }

    public void setWarpPrice(final PWarpPlugin pl, final CommandSender sender, final int amount) {
        Warp.p = pl;
        final Player player = (Player)sender;
        Warp.price = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(Warp.price);
        if (Warp.price.getType().equals((Object)Material.AIR)) {
            sender.sendMessage(ChatColor.RED + Messages.HOLD_ITEM.getMessage());
            return;
        }
        Warp.price.setAmount(amount);
        Warp.p.getConfig().set("warpPrice.item", (Object)Warp.price);
        Warp.p.getConfig().set("warpPrice.enable", (Object)true);
        Warp.p.saveConfig();
        sender.sendMessage(ChatColor.GREEN + Messages.SET_PRICE.getMessage());
    }

    public void reloadConfig(final CommandSender sender, final PWarpPlugin pl) {
        (Warp.p = pl).reloadConfig();
        Warp.p.messageFile.reloadMessages();
        Warp.p.wF.reloadWarps();

        Bukkit.getScheduler().cancelTask(taskId);
        automatedRemoval(pl);

        sender.sendMessage(ChatColor.GREEN + Messages.RELOAD_PLUGIN.getMessage());
    }

    public void removeAllWarps(final PWarpPlugin pl, final CommandSender sender) {
        Warp.p = pl;
        this.wC = Warp.p.wF.getWarpFile();
        if (this.wC.getStringList("warpList").isEmpty()) {
            sender.sendMessage(ChatColor.RED + Messages.NO_WARPS.getMessage());
            return;
        }
        (Warp.warps = (List<String>)this.wC.getStringList("warpList")).clear();
        this.wC.set("warpList", (Object)Warp.warps);
        this.wC.set("warps", (Object)null);
        try {
            this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        sender.sendMessage(ChatColor.GREEN + Messages.REMOVED_ALL.getMessage());
        GUI.guis.clear();
        this.gui.getInventories(pl);
    }

    public void setLimit(final PWarpPlugin pl, final CommandSender sender, final String limitString) {
        Warp.p = pl;
        int limit;
        try {
            limit = Integer.parseInt(limitString);
        }
        catch (Exception e) {
            sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setlimit <limit>"));
            return;
        }
        if (limit == 0) {
            sender.sendMessage(ChatColor.RED + Messages.LIMIT_ABOVE_0.getMessage());
            return;
        }
        Warp.p.getConfig().set("warpLimit", (Object)limit);
        sender.sendMessage(ChatColor.GREEN + Messages.LIMIT_CHANGED.getMessage());
        Warp.p.saveConfig();
    }

    public boolean isSafe(final CommandSender sender, String name, final PWarpPlugin pl) {
        final Player player = (Player)sender;
        Warp.p = pl;
        this.wC = Warp.p.wF.getWarpFile();
        name = name.toLowerCase();
        Location loc = null;
        if (Warp.p.getConfig().get("checkWarpSafety") == null) {
            Warp.p.getConfig().set("checkWarpSafety", (Object)true);
        }
        if (!Warp.p.getConfig().getBoolean("checkWarpSafety")) {
            return true;
        }
        if (this.wC.getString("warps." + name) == null) {
            loc = player.getLocation();
            loc.setY(loc.getY() - 1.0);
            if (loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.LAVA) {
                sender.sendMessage(ChatColor.RED + Messages.SET_UNSAFE.getMessage());
                return false;
            }
            return true;
        }
        else {
            loc = new Location((World)Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0, 0.0f, 0.0f);
            if (this.wC.getString("warps." + name + ".location.x") == null || this.wC.getString("warps." + name + ".location.y") == null || this.wC.getString("warps." + name + ".location.z") == null) {
                sender.sendMessage(ChatColor.RED + Messages.CONFIGURED_WRONG.getMessage());
                return false;
            }
            loc.setX(this.wC.getDouble("warps." + name + ".location.x"));
            loc.setY(this.wC.getDouble("warps." + name + ".location.y") - 1.0);
            loc.setZ(this.wC.getDouble("warps." + name + ".location.z"));
            try {
                loc.setWorld(Bukkit.getServer().getWorld((String)Objects.requireNonNull(this.wC.getString("warps." + name + ".location.world"))));
            }
            catch (Exception e) {
                sender.sendMessage(ChatColor.RED + Messages.CONFIGURED_WRONG.getMessage());
                return false;
            }
            loc.getBlock();
            loc.getBlock().getType();
            if (loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.LAVA || loc.getBlock().getType() == Material.FIRE || (loc.getBlock().getType() == Material.COBWEB && !(loc.getBlock().getBlockData() instanceof Slab)) || !loc.getBlock().getType().isSolid() || loc.getBlock().getBlockData() instanceof Gate || loc.getBlock().getBlockData() instanceof Door || loc.getBlock().getBlockData() instanceof TrapDoor || loc.getBlock().getBlockData() instanceof Fence || loc.getBlock().getBlockData() instanceof Cake || loc.getBlock().getType() == Material.CREEPER_HEAD || loc.getBlock().getType() == Material.DRAGON_HEAD || loc.getBlock().getType() == Material.PLAYER_HEAD || loc.getBlock().getType() == Material.ZOMBIE_HEAD || loc.getBlock().getType() == Material.CONDUIT || loc.getBlock().getType() == Material.SKELETON_SKULL || loc.getBlock().getType() == Material.WITHER_SKELETON_SKULL) {
                sender.sendMessage(ChatColor.RED + Messages.IS_UNSAFE.getMessage());
                return false;
            }
            loc.setY(loc.getY() + 1.0);
            if ((loc.getBlock().getType() != Material.AIR && loc.getBlock().getType().isSolid()) || loc.getBlock().getType() == Material.FIRE || loc.getBlock().getType() == Material.COBWEB || (loc.getBlock().getType() == Material.LAVA && !(loc.getBlock().getBlockData() instanceof Slab))) {
                sender.sendMessage(ChatColor.RED + Messages.IS_OBSTRUCTED.getMessage());
                return false;
            }
            loc.setY(loc.getY() + 1.0);
            if (loc.getBlock().getType() == Material.FIRE || (loc.getBlock().getType() != Material.AIR && loc.getBlock().getType().isSolid())) {
                sender.sendMessage(ChatColor.RED + Messages.IS_OBSTRUCTED.getMessage());
                return false;
            }
            return true;
        }
    }

    private boolean isTrusted(final CommandSender sender, String warp, final PWarpPlugin pl) {
        warp = warp.toLowerCase();
        final Player player = (Player)sender;
        Warp.p = pl;
        this.wC = Warp.p.wF.getWarpFile();
        final String playerName = player.getUniqueId().toString();
        if (!this.isPrivate(warp, pl)) {
            return true;
        }
        if (this.wC.getString("warps." + warp + ".owner-UUID").equals(playerName)) {
            return true;
        }
        if (this.wC.getStringList("warps." + warp + ".trusted") == null) {
            return false;
        }
        final List<String> trustedPlayers = (List<String>)this.wC.getStringList("warps." + warp + ".trusted");
        return trustedPlayers.contains(playerName);
    }

    private boolean isPrivate(String name, final PWarpPlugin pl) {
        name = name.toLowerCase();
        Warp.p = pl;
        this.wC = Warp.p.wF.getWarpFile();
        if (this.wC.getString("warps." + name + ".isPrivate") == null) {
            this.wC.set("warps." + name + ".isPrivate", (Object)false);
        }
        else if (this.wC.getString("warps." + name + ".isPrivate") != "true" && this.wC.getString("warps." + name + ".isPrivate") != "false") {
            this.wC.set("warps." + name + ".isPrivate", (Object)false);
        }
        return this.wC.getBoolean("warps." + name + ".isPrivate");
    }

    public void setWarpLore(final CommandSender sender, final PWarpPlugin pl, final List<String> lore, String name, final int loreNum) {
        name = name.toLowerCase();
        (Warp.p = pl).reloadConfig();
        this.wC = Warp.p.wF.getWarpFile();
        if (loreNum > 2 || loreNum < 0) {
            sender.sendMessage(ChatColor.RED + Messages.LORE_LIMIT.getMessage());
            return;
        }
        if (this.wC.getString("warps." + name) == null) {
            sender.sendMessage(ChatColor.RED + Messages.WARP_NOT_EXISTING.getMessage());
            return;
        }
        final Player player = (Player)sender;
        if (!this.wC.getString("warps." + name + ".owner-UUID").equals(player.getUniqueId().toString()) && !sender.isOp() && !sender.hasPermission("pwarp.remove.others")) {
            sender.sendMessage(ChatColor.RED + Messages.NOT_AN_OWNER.getMessage());
            return;
        }
        List<String> configLore = new ArrayList<String>(3);
        final String loreString = String.join(" ", lore);
        if (this.wC.getStringList("warps." + name + ".lore").isEmpty()) {
            configLore.add("&1");
            configLore.add("&1");
            configLore.add("&1");
            configLore.set(loreNum, loreString);
            this.wC.set("warps." + name + ".lore", (Object)configLore);
            try {
                this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            sender.sendMessage(ChatColor.GREEN + Messages.UPDATED_LORE.getMessage());
            this.gui.updateLore(name, pl);
        }
        else {
            configLore = (List<String>)this.wC.getStringList("warps." + name + ".lore");
            configLore.set(loreNum, loreString);
            this.wC.set("warps." + name + ".lore", (Object)configLore);
            try {
                this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            this.gui.updateLore(name, pl);
            sender.sendMessage(ChatColor.GREEN + Messages.UPDATED_LORE.getMessage());
        }
    }

    public void resetLore(String name, final CommandSender sender, final PWarpPlugin pl) {
        Warp.p = pl;
        this.wC = Warp.p.wF.getWarpFile();
        name = name.toLowerCase();
        final Player player = (Player)sender;
        if (this.wC.getString("warps." + name) == null) {
            sender.sendMessage(ChatColor.RED + Messages.WARP_NOT_EXISTING.getMessage());
            return;
        }
        if (!this.wC.getString("warps." + name + ".owner-UUID").equals(player.getUniqueId().toString()) && !sender.isOp() && !sender.hasPermission("pwarp.setlore.others")) {
            sender.sendMessage(ChatColor.RED + Messages.NOT_AN_OWNER.getMessage());
            return;
        }
        this.wC.set("warps." + name + ".lore", (Object)null);
        try {
            this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        sender.sendMessage(ChatColor.GREEN + Messages.RESET_LORE.getMessage());
        this.gui.updateLore(name, pl);
    }

    public void moveWarp(final CommandSender sender, final PWarpPlugin pl, String name) {
        Warp.p = pl;
        this.wC = Warp.p.wF.getWarpFile();
        final Player player = (Player)sender;
        name = name.toLowerCase();
        final Location loc = player.getLocation();
        if (this.wC.getString("warps." + name) == null) {
            sender.sendMessage(ChatColor.RED + Messages.WARP_NOT_EXISTING.getMessage());
            return;
        }
        if (!this.wC.getString("warps." + name + ".owner-UUID").equals(player.getUniqueId().toString()) && !sender.isOp() && !sender.hasPermission("pwarp.movewarp.others")) {
            sender.sendMessage(ChatColor.RED + Messages.NOT_AN_OWNER.getMessage());
            return;
        }
        this.wC.set("warps." + name + ".location.x", (Object)loc.getX());
        this.wC.set("warps." + name + ".location.y", (Object)loc.getY());
        this.wC.set("warps." + name + ".location.z", (Object)loc.getZ());
        this.wC.set("warps." + name + ".location.world", (Object)Objects.requireNonNull(loc.getWorld()).getName());
        this.wC.set("warps." + name + ".location.pitch", (Object)(double)loc.getPitch());
        this.wC.set("warps." + name + ".location.yaw", (Object)(double)loc.getYaw());
        this.wC.set("warps." + name + ".location.direction", (Object)loc.getDirection());
        try {
            this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(ChatColor.GREEN + Messages.MOVED_WARP.getMessage());
    }

    public void automatedRemoval(final PWarpPlugin pl) {
        Warp.p = pl;
        this.wC = Warp.p.wF.getWarpFile();
        FileConfiguration config = Warp.p.getConfig();
        if (config.get("automatedOldWarpRemoval") == null) {
            config.set("automatedOldWarpRemoval", true);
        }
        if (config.get("automatedInactiveRemovalInMinutes") == null) {
            config.set("automatedInactiveRemovalInMinutes", 60);
        }
        if (!config.getBoolean("automatedOldWarpRemoval")) { // false
            return;
        }
        Bukkit.getLogger().info("Starting automatedRemoval task...");
        taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(Warp.p, (Runnable) () -> {
            final Calendar currentDate = Calendar.getInstance();
            if (Warp.this.wC.getStringList("warpList").isEmpty()) {
                Warp.this.automatedRemoval(Warp.p);
                return;
            }
            for (final String warp : Warp.this.wC.getStringList("warpList")) {
                if (Warp.this.wC.getString("warps." + warp + ".date") == null) {
                    Warp.this.wC.set("warps." + warp + ".date", (Object)((currentDate.get(2) + 1) * 30 + currentDate.get(5) + 1 + (currentDate.get(1) + 1) * 364));
                }
                final int daysBetween = (currentDate.get(2) + 1) * 30 + currentDate.get(5) + 1 + (currentDate.get(1) + 1) * 364 - Warp.this.wC.getInt("warps." + warp + ".date");
                if (Warp.p.getConfig().getInt("inactiveWarpDays") == 0) {
                    Warp.p.getConfig().set("inactiveWarpDays", (Object)30);
                }
                if (daysBetween >= Warp.p.getConfig().getInt("inactiveWarpDays")) {
                    Warp.this.wC.set("warps." + warp, null);
                    final List<String> warps = Warp.this.wC.getStringList("warpList");
                    warps.remove(warp);
                    Warp.this.wC.set("warpList", (Object)warps);
                    Warp.this.gui.delItem(warp);
                }
            }
            Warp.p.saveConfig();
            try {
                Warp.this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            for (final String warp : Warp.this.wC.getStringList("warpList")) {
                final OfflinePlayer warpOwner = Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(Warp.this.wC.getString("warps." + warp + ".owner-UUID"))));
                final long now = System.currentTimeMillis();
                final long diffInDays = (now - warpOwner.getLastPlayed()) / 86400000L;
                if (diffInDays > Warp.p.getConfig().getLong("inactiveWarpDays")) {
                    Warp.this.wC.set("warps." + warp, (Object)null);
                    final List<String> warps2 = (List<String>)Warp.this.wC.getStringList("warpList");
                    warps2.remove(warp);
                    Warp.this.wC.set("warpList", (Object)warps2);
                    Warp.this.gui.delItem(warp);
                }
            }
            try {
                Warp.this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            automatedRemoval(Warp.p);
        }, (long)(1200 * Warp.p.getConfig().getInt("automatedInactiveRemovalInMinutes")));
    }

    public void removeOldWarps(final PWarpPlugin pl, final CommandSender sender) {
        Warp.p = pl;
        this.wC = Warp.p.wF.getWarpFile();
        final Calendar currentDate = Calendar.getInstance();
        if (this.wC.getStringList("warpList").isEmpty()) {
            sender.sendMessage(ChatColor.RED + Messages.NO_WARPS.getMessage());
            return;
        }
        for (final String warp : this.wC.getStringList("warpList")) {
            if (this.wC.getString("warps." + warp + ".date") == null) {
                this.wC.set("warps." + warp + ".date", (Object)((currentDate.get(2) + 1) * 30 + currentDate.get(5) + 1 + (currentDate.get(1) + 1) * 364));
            }
            final int daysBetween = (currentDate.get(2) + 1) * 30 + currentDate.get(5) + 1 + (currentDate.get(1) + 1) * 364 - this.wC.getInt("warps." + warp + ".date");
            if (Warp.p.getConfig().getInt("inactiveWarpDays") == 0) {
                Warp.p.getConfig().set("inactiveWarpDays", (Object)30);
            }
            if (daysBetween >= Warp.p.getConfig().getInt("inactiveWarpDays")) {
                this.wC.set("warps." + warp, (Object)null);
                final List<String> warps = (List<String>)this.wC.getStringList("warpList");
                warps.remove(warp);
                this.wC.set("warpList", (Object)warps);
                this.gui.delItem(warp);
            }
        }
        Warp.p.saveConfig();
        try {
            this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for (final String warp : this.wC.getStringList("warpList")) {
            final OfflinePlayer warpOwner = Bukkit.getOfflinePlayer(UUID.fromString(this.wC.getString("warps." + warp + ".owner-UUID")));
            final long now = System.currentTimeMillis();
            final long diffInDays = (now - warpOwner.getLastPlayed()) / 86400000L;
            if (diffInDays > Warp.p.getConfig().getLong("inactiveWarpDays")) {
                this.wC.set("warps." + warp, (Object)null);
                final List<String> warps2 = (List<String>)this.wC.getStringList("warpList");
                warps2.remove(warp);
                this.wC.set("warpList", (Object)warps2);
                this.gui.delItem(warp);
            }
        }
        try {
            this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        sender.sendMessage(ChatColor.GREEN + Messages.REMOVED_INACTIVE.getMessage().replaceAll("PINACTIVEP", Warp.p.getConfig().getString("inactiveWarpDays")));
    }

    public void setTrust(final PWarpPlugin pl, final CommandSender sender, final OfflinePlayer trustedPlayer, String name, final boolean removeOrAdd) {
        Warp.p = pl;
        this.wC = Warp.p.wF.getWarpFile();
        final Player player = (Player)sender;
        name = name.toLowerCase();
        if (this.wC.getString("warps." + name) == null) {
            sender.sendMessage(ChatColor.RED + Messages.WARP_NOT_EXISTING.getMessage());
            return;
        }
        if (!this.wC.getString("warps." + name + ".owner-UUID").equals(player.getUniqueId().toString()) && !sender.hasPermission("pwarp.private.others")) {
            sender.sendMessage(ChatColor.RED + Messages.NOT_AN_OWNER.getMessage());
            return;
        }
        List<String> trustedPlayers = null;
        if ((this.wC.getStringList("warps." + name + ".trusted").equals(null) || this.wC.getStringList("warps." + name + ".trusted").isEmpty()) && !removeOrAdd) {
            sender.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_TRUSTED.getMessage());
            return;
        }
        trustedPlayers = (List<String>)this.wC.getStringList("warps." + name + ".trusted");
        if (removeOrAdd) {
            if (trustedPlayers.contains(trustedPlayer.getUniqueId().toString())) {
                sender.sendMessage(ChatColor.RED + Messages.PLAYER_ALREADY_TRUSTED.getMessage());
                return;
            }
            trustedPlayers.add(trustedPlayer.getUniqueId().toString());
            sender.sendMessage(ChatColor.GREEN + Messages.PLAYER_TRUSTED.getMessage().replaceAll("PPLAYERP", trustedPlayer.getName()));
        }
        else {
            if (!trustedPlayers.contains(trustedPlayer.getUniqueId().toString()) && !removeOrAdd) {
                sender.sendMessage("2");
                sender.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_TRUSTED.getMessage());
                return;
            }
            trustedPlayers.remove(trustedPlayer.getUniqueId().toString());
            sender.sendMessage(ChatColor.GREEN + Messages.PLAYER_UNTRUSTED.getMessage().replaceAll("PPLAYERP", trustedPlayer.getName()));
        }
        this.wC.set("warps." + name + ".trusted", (Object)trustedPlayers);
        try {
            this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWarpMoneyPrice(final PWarpPlugin pl, final CommandSender sender, final int amount) {
        Warp.p = pl;
        Warp.p.getConfig().set("warpMoneyPrice.price", (Object)amount);
        Warp.p.getConfig().set("warpMoneyPrice.enable", (Object)true);
        Warp.p.saveConfig();
        sender.sendMessage(ChatColor.GREEN + Messages.SET_PRICE.getMessage());
    }

    public void setItem(final PWarpPlugin pl, final CommandSender sender, String name) {
        Warp.p = pl;
        this.wC = Warp.p.wF.getWarpFile();
        name = name.toLowerCase();
        final Player player = (Player)sender;
        if (this.wC.getString("warps." + name) == null) {
            sender.sendMessage(ChatColor.RED + Messages.WARP_NOT_EXISTING.getMessage());
            return;
        }
        if (!this.wC.getString("warps." + name + ".owner-UUID").equals(player.getUniqueId().toString()) && !player.hasPermission("pwarp.setitem.others")) {
            sender.sendMessage(ChatColor.RED + Messages.NOT_AN_OWNER.getMessage());
            return;
        }
        final ItemStack handItem = player.getInventory().getItemInMainHand();
        if (handItem.getType().equals((Object)Material.AIR)) {
            sender.sendMessage(ChatColor.RED + Messages.HOLD_ITEM.getMessage());
            return;
        }
        player.getInventory().setItemInMainHand(handItem);
        handItem.setAmount(1);
        this.wC.set("warps." + name + ".item", (Object)handItem);
        try {
            this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        sender.sendMessage(ChatColor.GREEN + Messages.CHANGED_WARP_ICON.getMessage());
        this.gui.changeMaterial(name, handItem.getType());
    }

    public void resetItem(final PWarpPlugin pl, final CommandSender sender, String name) {
        Warp.p = pl;
        this.wC = Warp.p.wF.getWarpFile();
        name = name.toLowerCase();
        final Player player = (Player)sender;
        if (this.wC.getString("warps." + name) == null) {
            sender.sendMessage(ChatColor.RED + Messages.WARP_NOT_EXISTING.getMessage());
            return;
        }
        if (!this.wC.getString("warps." + name + ".owner-UUID").equals(player.getUniqueId().toString()) && !player.hasPermission("pwarp.setitem.others")) {
            sender.sendMessage(ChatColor.RED + Messages.NOT_AN_OWNER.getMessage());
            return;
        }
        this.wC.set("warps." + name + ".item", (Object)null);
        try {
            this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        sender.sendMessage(ChatColor.GREEN + Messages.RESET_WARP_ICON.getMessage());
        this.gui.changeMaterial(name, Material.CONDUIT);
    }

    public void renameWarp(final PWarpPlugin pl, final CommandSender sender, String name, String newName) {
        Warp.p = pl;
        this.wC = Warp.p.wF.getWarpFile();
        name = name.toLowerCase();
        newName = newName.toLowerCase();
        final Player player = (Player)sender;
        if (this.wC.getString("warps." + name) == null) {
            sender.sendMessage(ChatColor.RED + Messages.WARP_NOT_EXISTING.getMessage());
            return;
        }
        if (!this.wC.getString("warps." + name + ".owner-UUID").equals(player.getUniqueId().toString()) && !player.hasPermission("pwarp.rename.others")) {
            sender.sendMessage(ChatColor.RED + Messages.NOT_AN_OWNER.getMessage());
            return;
        }
        if (this.wC.isConfigurationSection("warps." + newName)) {
            sender.sendMessage(ChatColor.RED + Messages.NAME_IN_USE.getMessage().replaceAll("PWARPNAMEP", newName));
            return;
        }
        this.gui.delItem(name);
        this.wC.set("warps." + newName, (Object)this.wC.getConfigurationSection("warps." + name).getValues(true));
        final String ownerUUID = this.wC.getString("warps." + name + ".owner-UUID");
        this.wC.set("warps." + newName + ".owner-UUID", (Object)ownerUUID);
        this.wC.set("warps." + name, (Object)null);
        final List<String> warps = (List<String>)this.wC.getStringList("warpList");
        warps.remove(name);
        warps.add(newName);
        this.wC.set("warpList", (Object)warps);
        try {
            this.wC.save(new File(Warp.p.getDataFolder(), "warps.yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.gui.addItem(newName, Warp.p);
        if (this.wC.getItemStack("warps." + newName + ".item") != null) {
            this.gui.changeMaterial(newName, this.wC.getItemStack("warps." + newName + ".item").getType());
        }
        sender.sendMessage(ChatColor.GREEN + Messages.RENAMED_WARP.getMessage());
    }

    static {
        teleportingPlayers = new ArrayList<String>();
    }
}
