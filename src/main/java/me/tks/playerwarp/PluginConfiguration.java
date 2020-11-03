package me.tks.playerwarp;

import com.google.gson.Gson;
import me.tks.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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

    public PluginConfiguration(int standardLimit, int teleportDelay, int refreshRateInMinutes) {
        this.standardLimit = standardLimit;
        this.teleportDelay = teleportDelay;
        this.refreshRateInMinutes = refreshRateInMinutes;
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
            pC = new PluginConfiguration(0, 0, 15);
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

        return new PluginConfiguration(limit, delay, refreshRate);

    }

}
