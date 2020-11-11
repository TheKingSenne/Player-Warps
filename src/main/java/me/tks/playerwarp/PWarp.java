package me.tks.playerwarp;

import me.tks.dependencies.VaultPlugin;
import me.tks.messages.MessageFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

// TO-DO:
// - fix this JavaDoc lmao
// - Info command
// - Hooks command
// - Blacklist
// - W2W teleport
// - Safe warps
// - Virtual economy
// - Item economy
// - Automated remover for old warps?
// - Listown/listother command
// - GP support
// - Fix code dupe for item from player getter
// - Adding extra names for commands
// - Exempt permission for teleport delay

public class PWarp extends JavaPlugin {

      public static VaultPlugin v;
      public static WarpList wL = null;
      public static GuiCatalog gC = null;
      public static MessageFile messageFile = null;
      public static PluginConfiguration pC = null;
      public List<String> hooks;
      public static Events events;

    /**
     * Loads everything when plugin gets enabled
     */
    @Override
    public void onEnable() {

        // Register all events
        events = new Events();
        Bukkit.getPluginManager().registerEvents( events, this);

        // Set up custom message file
        messageFile = new MessageFile();
        messageFile.createMessageFile(this);
        messageFile.checkConfig();

        // Load plugin configuration
        pC = PluginConfiguration.read();

        // Sets up the Warp List and all GUI's
        wL = WarpList.read();
        gC = new GuiCatalog();
        gC.reloadGuis(this);

        // Load commands
        Commands cmd = new Commands(wL, gC);

        this.getCommand("pwarp").setExecutor(cmd);
        this.getCommand("pwg").setExecutor(cmd);
        this.getCommand("pwwarp").setExecutor(cmd);

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
            v = new VaultPlugin();
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
