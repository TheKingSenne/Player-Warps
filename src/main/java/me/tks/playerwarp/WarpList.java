package me.tks.playerwarp;

import com.google.gson.Gson;
import me.tks.events.PWarpAddedEvent;
import me.tks.messages.Messages;
import me.tks.utils.PlayerUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class WarpList implements Serializable {

    // List of all warps
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

        WarpList warpList = new WarpList();

        // Check for legacy version
        File oldFile = new File(PWarp.getProvidingPlugin(PWarp.class).getDataFolder(), "warps.yml");

        File warpFile = new File(PWarp.getPlugin(PWarp.class).getDataFolder(), "warps.json");

        // Legacy file has been found and current warpfile is empty
        if (oldFile.exists() && (!warpFile.exists() || warpFile.length() == 0)) {

            return fromLegacy(oldFile);
        }

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

            String json;

            try {
                json = this.toJson();
            }
            catch (Exception e) {
                Bukkit.getLogger().info("Error: warps could not be saved.");
                return;
            }

            fW.write(json);

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
        Bukkit.getPluginManager().callEvent(new PWarpAddedEvent(warp));
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
        player.sendMessage(ChatColor.GREEN + Messages.REMOVED_WARP.getMessage()
        .replaceAll("PWARPNAMEP", warp.getName()));

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
        listOwnedWarps(player, (Player) requested);
    }

    public void listOwnedWarps(Player player) {
        listOwnedWarps(player, player);
    }

    /**
     * List all warps owned by a player.
     * @param player player that requested
     * @param target player to list warps from
     */
    public void listOwnedWarps(Player player, Player target) {

        ArrayList<String> owned = (ArrayList<String>) this.warps.stream()
            .filter(x -> x.isOwner(target))
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
        int tempLimit;

        if (limit == 0) return 0;

        for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
            int length = perm.getPermission().length();

            if (length >= 18) {

                String permission = perm.getPermission().substring(0, 16);

                if (!permission.equals("pwarp.otherlimit")) {
                    continue;
                }
                try {
                    tempLimit = Integer.parseInt(perm.getPermission().substring(17));

                    if (tempLimit > newLimit) newLimit = tempLimit;
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

    /**
     * Converts a legacy warp file (before recode) to the current format
     * @param file outdated file
     * @return a new warp list
     */
    public static WarpList fromLegacy(File file) {
        FileConfiguration fC = new YamlConfiguration();
        try {
            fC.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            Bukkit.getLogger().info(ChatColor.RED + "[PWarp] An error occurred trying to convert from a legacy version.");
            return new WarpList();
        }

        List<String> warps = fC.getStringList("warpList");
        ArrayList<Warp> converted = new ArrayList<>();

        for (String warpName : warps) {
            double x = fC.getDouble("warps." + warpName + ".location.x");
            double y = fC.getDouble("warps." + warpName + ".location.y");
            double z = fC.getDouble("warps." + warpName + ".location.z");
            String worldName = fC.getString("warps." + warpName + ".location.world");

            // Something wrong in old config
            if (worldName == null) continue;

            World world = Bukkit.getWorld(worldName);

            // World no longer exists
            if (world == null) continue;

            double pitch = fC.getDouble("warps." + warpName + ".location.pitch");
            double yaw = fC.getDouble("warps." + warpName + ".location.yaw");
            String uuid = fC.getString("warps." + warpName + ".owner-UUID");
            int visitorCount = fC.getInt("warps." + warpName + ".visitorCount");
            boolean isHidden = fC.getBoolean("warps." + warpName + ".isHidden");
            boolean isPrivate = fC.getBoolean("warps." + warpName + ".isPrivate");
            List<String> trusted = fC.getStringList("warps." + warpName + ".trusted");
            ItemStack item = fC.getItemStack("warps." + warpName + ".item");

            if (item == null) item = new ItemStack(Material.CONDUIT);

            List<String> lore = null;

            if (item.getItemMeta() != null)
                lore = item.getItemMeta().getLore();

            if (lore == null) lore = new ArrayList<>();

            converted.add(new Warp(warpName, new Location(world,x,y,z, (float) yaw, (float) pitch), isPrivate, trusted, item, uuid, (ArrayList<String>) lore,visitorCount, isHidden));
        }

        // Remove old warpList
        if (!file.delete()) Bukkit.getLogger().info("[PWarp] Legacy file has been removed");

        return new WarpList(converted);
    }


}
