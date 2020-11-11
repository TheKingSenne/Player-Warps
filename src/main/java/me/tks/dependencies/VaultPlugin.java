package me.tks.dependencies;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultPlugin {
    private static Permission permission;
    private static Economy economy;

    public VaultPlugin() {
        this.setupEconomy();
        this.setupPermissions();
    }

    public boolean setupPermissions() {
        final RegisteredServiceProvider<Permission> permissionProvider = (RegisteredServiceProvider<Permission>) Bukkit.getServer().getServicesManager().getRegistration((Class)Permission.class);
        if (permissionProvider != null) {
            VaultPlugin.permission = permissionProvider.getProvider();
        }
        return VaultPlugin.permission != null;
    }

    public boolean setupEconomy() {
        final RegisteredServiceProvider<Economy> economyProvider = (RegisteredServiceProvider<Economy>)Bukkit.getServer().getServicesManager().getRegistration((Class)Economy.class);
        if (economyProvider != null) {
            VaultPlugin.economy = economyProvider.getProvider();
        }
        return VaultPlugin.economy != null;
    }

    public Permission getPermissions() {
        return VaultPlugin.permission;
    }

    public Economy getEconomy() {
        return VaultPlugin.economy;
    }

    static {
        VaultPlugin.permission = null;
        VaultPlugin.economy = null;
    }
}
