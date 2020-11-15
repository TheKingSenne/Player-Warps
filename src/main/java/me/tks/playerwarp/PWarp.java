package me.tks.playerwarp;

import me.tks.dependencies.VaultPlugin;
import me.tks.messages.MessageFile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

// TO-DO:
// - Hide command - DONE
// - check unused messages - DONE
// - Permissions - DONE
// - fix this JavaDoc lmao - DONE
// - Edit help menu - DONE
// - Info command - DONE
// - Blacklist - DONE
// - W2W teleport - DONE
// - Safe warps - DONE
// - Virtual economy - DONE
// - Item economy - DONE
// - Listown/listother command - DONE
// - Adding extra names for commands - DONE
// - GP support
// - OP help menu
// - Automated remover for old warps?
// - Hooks command - when GP is implemented
// - Fix code dupe for item from player getter -> method created, just needs to be implemented
// - Exempt permission for teleport delay and all other crap
// - Update info file²

// Ideas:
// - Add total amount of warps to info command
public class PWarp extends JavaPlugin {

    /**
     * Global plugin variables.
     */
    public static WarpList wL = null;
    public static GuiCatalog gC = null;
    public static MessageFile messageFile = null;
    public static PluginConfiguration pC = null;
    public static List<String> hooks;
    public static Events events;

    /**
     * Loads everything when plugin gets enabled.
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
        hooks = new ArrayList<>();

        // Set up Vault
        VaultPlugin.setUp();

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
     * Saves everything when plugin closes.
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
