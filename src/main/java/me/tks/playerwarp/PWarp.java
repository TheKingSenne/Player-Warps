package me.tks.playerwarp;

import com.elixiumnetwork.vault.VaultPlugin;
import me.tks.messages.MessageFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

// TO-DO:
// - Lore
// - fix this JavaDoc lmao
// - teleport delay
// - help menu


public class PWarp extends JavaPlugin {

      public static VaultPlugin v;
      public static WarpList wL = null;
      public static GuiCatalog gC = null;
      public static MessageFile messageFile = null;
      public static PluginConfiguration pC = null;
      public List<String> hooks;

    /**
     * Loads everything when plugin gets enabled
     */
    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(new Events(), this);

        // Set up custom message file
        messageFile = new MessageFile();
        messageFile.createMessageFile(this);
        messageFile.checkConfig();

        // Sets up the Warp List and all GUI's
        wL = WarpList.read();
        gC = new GuiCatalog();
        gC.createGuis(wL);

        // Load plugin configuration
        pC = PluginConfiguration.read();

        // Start automated GUI refresh
        gC.reloadGuis(this);

        // Load commands
        Commands cmd = new Commands(this, wL, gC);

        this.getCommand("pwarp").setExecutor(cmd);
        this.getCommand("pwg").setExecutor(cmd);
        this.getCommand("pwarp").setExecutor(cmd);

        // Load all hooks
        hooks = new ArrayList<String>();

        // Check if vault is installed
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {

            // Log that vault couldn't be found
            Bukkit.getLogger().info(ChatColor.RED + "[PWarp] Error: this plugin requires Vault! Errors may occur.");
            this.hooks.add("Vault" + ChatColor.RED + " \u2718");
        }
        // Set up the vault economy and permissions
        else {
            v.setupEconomy();
            v.setupPermissions();

            // Log that vault has been enabled
            Bukkit.getLogger().info("[PWarp] Successfully hooked into Vault!");
            this.hooks.add("Vault" + ChatColor.GREEN + " \u2714");
        }
        // Hook for GriefPrevention, currently disabled
//        if (Bukkit.getPluginManager().getPlugin("GriefPrevention") != null) {
//            Bukkit.getConsoleSender().sendMessage("[PWarp] Successfully hooked into GriefPrevention!");
//            this.hooks.add("GriefPrevention" + ChatColor.GREEN + " \u2714");
//        }
//        else {
//            this.hooks.add("GriefPrevention" + ChatColor.RED + " \u2718");
//        }

        // Just a file to give general info about the plugin
        this.saveResource("info.yml", true);


        // Log that plugin has been enabled
        Bukkit.getLogger().info("[PWarp] PWarp has been enabled!");

//        this.warp.automatedRemoval(this);
//        this.saveDefaultConfig();
//        this.getConfig().options().copyDefaults(true);
//        this.saveConfig();
    }

    /**
     * Saves everything when plugin closes
     */
    @Override
    public void onDisable() {
        //this.saveConfig();

        // Write warps and configuration back
        wL.write();
        pC.write();

        // Log that plugin has been disabled
        Bukkit.getLogger().info("[PWarp] PWarp has been disabled!");
    }




}
