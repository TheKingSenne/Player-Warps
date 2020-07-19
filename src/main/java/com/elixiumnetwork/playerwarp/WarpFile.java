package com.elixiumnetwork.playerwarp;

import org.bukkit.configuration.file.*;
import java.io.*;
import org.bukkit.plugin.java.*;

public class WarpFile
{
    private FileConfiguration warpFile;
    private File customConfigFile;
    
    public void createWarpFile(final PWarpPlugin p) {
        this.customConfigFile = new File(p.getDataFolder(), "warps.yml");
        if (!this.customConfigFile.exists()) {
            this.customConfigFile.getParentFile().mkdirs();
            p.saveResource("warps.yml", false);
        }
        this.warpFile = (FileConfiguration)new YamlConfiguration();
        try {
            this.warpFile.load(this.customConfigFile);
            this.warpFile.save(this.customConfigFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void transferWarps(final PWarpPlugin p) {
        if (p.getConfig().getStringList("warpList") != null && p.getConfig().isConfigurationSection("warps")) {
            this.warpFile.set("warps", (Object)p.getConfig().getConfigurationSection("warps").getValues(true));
            this.warpFile.set("warpList", (Object)p.getConfig().getStringList("warpList"));
            p.getConfig().set("warps", (Object)null);
            p.getConfig().set("warpList", (Object)null);
            p.saveConfig();
            this.customConfigFile = new File(p.getDataFolder(), "warps.yml");
            try {
                this.warpFile.save(this.customConfigFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public FileConfiguration getWarpFile() {
        return this.warpFile;
    }
    
    public void reloadWarps() {
        this.customConfigFile = new File(((PWarpPlugin)JavaPlugin.getPlugin((Class)PWarpPlugin.class)).getDataFolder(), "warps.yml");
        this.warpFile = (FileConfiguration)YamlConfiguration.loadConfiguration(this.customConfigFile);
    }
}
