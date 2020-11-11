
//public class PWarpPlugin extends JavaPlugin implements askHelp {
//    private final Warp warp;
//    private final GUI gui;
//    public final MessageFile messageFile;
//    public final VaultPlugin v;
//    private final List<String> hooks;
//
//
//    public PWarpPlugin() {
//        this.warp = new Warp();
//        this.gui = new GUI();
//        this.messageFile = new MessageFile();
//        this.v = new VaultPlugin();
//        this.hooks = new ArrayList<>();
//    }
//
//
//    @Override
//    public void onEnable() {
//        Bukkit.getConsoleSender().sendMessage("[PWarp] PWarp has been enabled!");
//        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
//            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[PWarp] Error: this plugin requires Vault! Errors may occur.");
//            this.hooks.add("Vault" + ChatColor.RED + " \u2718");
//        }
//        else {
//            this.v.setupEconomy();
//            this.v.setupPermissions();
//            Bukkit.getConsoleSender().sendMessage("[PWarp] Successfully hooked into Vault!");
//            this.hooks.add("Vault" + ChatColor.GREEN + " \u2714");
//        }
//        if (Bukkit.getPluginManager().getPlugin("GriefPrevention") != null) {
//            Bukkit.getConsoleSender().sendMessage("[PWarp] Successfully hooked into GriefPrevention!");
//            this.hooks.add("GriefPrevention" + ChatColor.GREEN + " \u2714");
//        }
//        else {
//            this.hooks.add("GriefPrevention" + ChatColor.RED + " \u2718");
//        }
//        this.saveResource("info.yml", true);
//
//        Bukkit.getPluginManager().registerEvents(new GUI(), this);
//        this.warp.automatedRemoval(this);
//        //this.gui.setUpGui(this);
//        this.saveDefaultConfig();
//        this.getConfig().options().copyDefaults(true);
//        this.saveConfig();
//    }
//
//    @Override
//    public void onDisable() {
//        Bukkit.getConsoleSender().sendMessage("[PWarp] PWarp has been disabled!");
//        this.saveConfig();
//    }
//
//    @Override
//    public boolean onCommand(final CommandSender sender, final Command cmd, final String lbl, final String[] args) {
//        if (cmd.getName().equalsIgnoreCase("pwarp")) {
//            if (sender instanceof Player) {
//                final Player executer = (Player)sender;
//                if (Warp.teleportingPlayers != null && Warp.teleportingPlayers.contains(executer.getUniqueId().toString())) {
//                    sender.sendMessage(ChatColor.RED + Messages.NO_COMMANDS_ALLOWED.getMessage());
//                    return true;
//                }
//                if (args.length < 1) {
//                    askHelp.generalHelp(sender, 1);
//                    return true;
//                }

//                else if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("place") || args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("setwarp") || args[0].equalsIgnoreCase("placewarp")) {
//                    if (args.length != 2) {
//                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp set <name>"));
//                        return true;
//                    }
//                    if (this.getConfig().get("defaultSetWarpAccess") == null) {
//                        this.getConfig().set("defaultSetWarpAccess", true);
//                    }
//                    if (!this.getConfig().getBoolean("defaultSetWarpAccess") && !sender.hasPermission("pwarp.setwarp")) {
//                        sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                        return true;
//                    }
//                    if (!this.warp.isSafe(sender, args[1], this)) {
//                        return true;
//                    }
//                    final Warp warp = new Warp();
//                    final Location loc = ((Player)sender).getLocation();
//                    warp.setWarp(args[1], loc, sender, this);
//                    return true;
//                }
//                else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("removewarp") || args[0].equalsIgnoreCase("deletewarp") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delwarp")) {
//                    if (args.length != 2) {
//                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp delete <name>"));
//                        return true;
//                    }
//                    this.warp.removeWarp(args[1], sender, this);
//                    return true;
//                }
//                else if (args[0].equalsIgnoreCase("warp")) {
//                    if (args.length != 2) {
//                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp warp <warp>"));
//                        return true;
//                    }
//                    this.warp.goToWarp(args[1].toLowerCase(), sender, this);
//                    return true;
//                }
//                else if (args[0].equalsIgnoreCase("setitemprice")) {
//                    if (!sender.isOp() && !sender.hasPermission("pwarp.setprice")) {
//                        sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                        return true;
//                    }
//                    if (args.length != 2) {
//                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setitemprice <amount>"));
//                        return true;
//                    }
//                    int amount = 0;
//                    try {
//                        amount = Integer.parseInt(args[1]);
//                    }
//                    catch (NumberFormatException e4) {
//                        sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
//                        return true;
//                    }
//                    this.warp.setWarpPrice(this, sender, amount);
//                    return true;
//                }
//                else if (args[0].equalsIgnoreCase("reload")) {
//                    if (!sender.isOp() && !sender.hasPermission("pwarp.reload")) {
//                        sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                        return true;
//                    }
//                    this.warp.reloadConfig(sender, this);
//                    return true;
//                }
//                else {
//                    if (args[0].equalsIgnoreCase("deleteall") || args[0].equalsIgnoreCase("removeall") || args[0].equalsIgnoreCase("removeallwarps") || args[0].equalsIgnoreCase("deleteallwarps") || args[0].equalsIgnoreCase("delall") || args[0].equalsIgnoreCase("delallwarps")) {
//                        if (sender.isOp() || sender.hasPermission("pwarp.deleteall")) {
//                            this.warp.removeAllWarps(this, sender);
//                        }
//                        else {
//                            sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                        }
//                        return true;
//                    }
//                    if (args[0].equalsIgnoreCase("setlimit") || args[0].equalsIgnoreCase("limit")) {
//                        if (!sender.isOp() && !sender.hasPermission("pwarp.setlimit")) {
//                            sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                            return true;
//                        }
//                        if (args.length < 2) {
//                            sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setlimit <limit>"));
//                            return true;
//                        }
//                        this.warp.setLimit(this, sender, args[1]);
//                        return true;
//                    }
//                    else if (args[0].equalsIgnoreCase("clearoldwarps")) {
//                        if (!sender.isOp() && !sender.hasPermission("pwarp.clearoldwarps")) {
//                            sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                            return true;
//                        }
//                        this.warp.removeOldWarps(this, sender);
//                        return true;
//                    }


//                    else if (args[0].equalsIgnoreCase("setprice")) {
//                        if (!sender.isOp() && !sender.hasPermission("pwarp.setprice")) {
//                            sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                            return true;
//                        }
//                        if (args.length != 2) {
//                            sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setprice <amount>"));
//                            return true;
//                        }
//                        int amount = 0;
//                        try {
//                            amount = Integer.parseInt(args[1]);
//                        }
//                        catch (Exception e3) {
//                            sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
//                            return true;
//                        }
//                        this.warp.setWarpMoneyPrice(this, sender, amount);
//                        return true;
//                    }
//                    else if (args[0].equalsIgnoreCase("resetitem")) {
//                        if (args.length != 2) {
//                            sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp resetitem <warp>"));
//                            return true;
//                        }
//                        this.warp.resetItem(this, sender, args[1]);
//                        return true;
//                    }
//                    else {
//                        if (!args[0].equalsIgnoreCase("setdelay")) {
//                            if (args[0].equalsIgnoreCase("blacklist")) {
//                                if (args.length != 3 && args.length != 2) {
//                                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp blacklist add/remove <world> or /pwarp blacklist list"));
//                                    return true;
//                                }
//                                if (!args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove") && !args[1].equalsIgnoreCase("list")) {
//                                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp blacklist add/remove <world> or /pwarp blacklist list"));
//                                    return true;
//                                }
//                                if (args[1].equalsIgnoreCase("add")) {
//                                    if (!sender.hasPermission("pwarp.blacklist.edit")) {
//                                        sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                                        return true;
//                                    }
//                                    List<String> worlds = new ArrayList<>();
//                                    if (this.getConfig().getStringList("blacklist") == null) {
//                                        worlds.add(args[2].toLowerCase());
//                                    }
//                                    else {
//                                        worlds = this.getConfig().getStringList("blacklist");
//                                        if (worlds.contains(args[2].toLowerCase())) {
//                                            sender.sendMessage(ChatColor.RED + Messages.ALREADY_BLACKLISTED.getMessage());
//                                            return true;
//                                        }
//                                        worlds.add(args[2].toLowerCase());
//                                    }
//                                    this.getConfig().set("blacklist", worlds);
//                                    this.saveConfig();
//                                    sender.sendMessage(ChatColor.GREEN + Messages.ADDED_BLACKLIST.getMessage().replaceAll("PWORLDP", args[2].toLowerCase()));
//                                    return true;
//                                }
//                                else if (args[1].equalsIgnoreCase("remove")) {
//                                    if (!sender.hasPermission("pwarp.blacklist.edit")) {
//                                        sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                                        return true;
//                                    }
//                                    List<String> worlds;
//                                    if (this.getConfig().getStringList("blacklist") == null) {
//                                        sender.sendMessage(ChatColor.RED + Messages.WORLD_NOT_BLACKLISTED.getMessage().replaceAll("PWORLDP", args[2].toLowerCase()));
//                                        return true;
//                                    }
//                                    if (!this.getConfig().getStringList("blacklist").contains(args[2].toLowerCase())) {
//                                        sender.sendMessage(ChatColor.RED + Messages.WORLD_NOT_BLACKLISTED.getMessage().replaceAll("PWORLDP", args[2].toLowerCase()));
//                                        return true;
//                                    }
//                                    worlds = this.getConfig().getStringList("blacklist");
//                                    worlds.remove(args[2].toLowerCase());
//                                    this.getConfig().set("blacklist", worlds);
//                                    this.saveConfig();
//                                    sender.sendMessage(ChatColor.GREEN + Messages.REMOVED_BLACKLIST.getMessage().replaceAll("PWORLDP", args[2].toLowerCase()));
//                                    return true;
//                                }
//                                else if (args[1].equalsIgnoreCase("list")) {
//                                    if (!sender.hasPermission("pwarp.blacklist.list")) {
//                                        sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                                        return true;
//                                    }
//                                    if (this.getConfig().getStringList("blacklist") == null | this.getConfig().getStringList("blacklist").isEmpty()) {
//                                        sender.sendMessage(ChatColor.RED + Messages.NO_WORLDS_BLACKLISTED.getMessage());
//                                        return true;
//                                    }
//                                    final List<String> blacklist = this.getConfig().getStringList("blacklist");
//                                    sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.RESET + ChatColor.YELLOW + "[Blacklist]" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----" + ChatColor.GOLD + "\n » " + ChatColor.YELLOW + String.join(ChatColor.GOLD + "\n » " + ChatColor.YELLOW, blacklist));
//                                    return true;
//                                }
//                            }

//                            else if (args[0].equalsIgnoreCase("listown")) {
//                                if (args.length != 1) {
//                                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp listown"));
//                                    return true;
//                                }
//                                if (this.wF.getWarpFile().getStringList("warpList") == null | this.wF.getWarpFile().getStringList("warpList").isEmpty()) {
//                                    sender.sendMessage(ChatColor.RED + Messages.NO_WARPS.getMessage());
//                                    return true;
//                                }
//                                final List<String> ownedWarps = new ArrayList<>();
//                                final Player player = (Player)sender;
//                                for (int i = 0; i < this.wF.getWarpFile().getStringList("warpList").size(); ++i) {
//                                    if (this.wF.getWarpFile().getString("warps." + this.wF.getWarpFile().getStringList("warpList").get(i) + ".owner-UUID").equals(player.getUniqueId().toString())) {
//                                        ownedWarps.add(this.wF.getWarpFile().getStringList("warpList").get(i));
//                                    }
//                                }
//                                sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.RESET + ChatColor.YELLOW + Messages.OWNED_WARPS.getMessage().replaceAll("PPLAYERP", player.getName()) + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----" + ChatColor.GOLD + "\n » " + ChatColor.YELLOW + String.join(ChatColor.GOLD + "\n » " + ChatColor.YELLOW, ownedWarps));
//                                return true;
//                            }
//                            else if (args[0].equalsIgnoreCase("hooks")) {
//                                if (!sender.hasPermission("pwarp.hooks")) {
//                                    sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                                    return true;
//                                }
//                                if (args.length != 1) {
//                                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp hooks"));
//                                    return true;
//                                }
//                                sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.RESET + ChatColor.YELLOW + "[Hooks]" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----" + ChatColor.GOLD + "\n » " + ChatColor.YELLOW + String.join(ChatColor.GOLD + "\n » " + ChatColor.YELLOW, this.hooks));
//                                return true;
//                            }
//                            else if (args[0].equalsIgnoreCase("w2w")) {
//                                if (!sender.hasPermission("pwarp.worldtoworld")) {
//                                    sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                                    return true;
//                                }
//                                if (args.length != 2) {
//                                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp w2w enable/disable"));
//                                    return true;
//                                }
//                                if (args[1].equalsIgnoreCase("enable")) {
//                                    this.getConfig().set("worldToWorldTeleport", true);
//                                    this.saveConfig();
//                                    sender.sendMessage(ChatColor.GREEN + Messages.ENABLED_W2W.getMessage());
//                                    return true;
//                                }
//                                if (args[1].equalsIgnoreCase("disable")) {
//                                    this.getConfig().set("worldToWorldTeleport", false);
//                                    this.saveConfig();
//                                    sender.sendMessage(ChatColor.GREEN + Messages.DISABLED_W2W.getMessage());
//                                    return true;
//                                }
//                                sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp w2w enable/disable"));
//                                return true;
//                            }
//                            else if (args[0].equalsIgnoreCase("listother")) {
//                                if (args.length != 2) {
//                                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp listother <player>"));
//                                    return true;
//                                }
//                                if (!sender.hasPermission("pwarp.listother")) {
//                                    sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                                    return true;
//                                }
//                                final List<String> ownedWarps = new ArrayList<>();
//                                OfflinePlayer player2 = null;
//                                try {
//                                    player2 = Bukkit.getOfflinePlayer(args[1]);
//                                    if (!player2.hasPlayedBefore()) {
//                                        sender.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_EXISTING.getMessage());
//                                        return true;
//                                    }
//                                }
//                                catch (Exception e5) {
//                                    sender.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_EXISTING.getMessage());
//                                    return true;
//                                }
//                                for (int i = 0; i < this.wF.getWarpFile().getStringList("warpList").size(); ++i) {
//                                    if (this.wF.getWarpFile().getString("warps." + this.wF.getWarpFile().getStringList("warpList").get(i) + ".owner-UUID").equals(player2.getUniqueId().toString())) {
//                                        ownedWarps.add(this.wF.getWarpFile().getStringList("warpList").get(i));
//                                    }
//                                }
//                                sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.RESET + ChatColor.YELLOW + Messages.OWNED_WARPS.getMessage().replaceAll("PPLAYERP", player2.getName()) + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----" + ChatColor.GOLD + "\n » " + ChatColor.YELLOW + String.join(ChatColor.GOLD + "\n » " + ChatColor.YELLOW, ownedWarps));
//                                return true;
//                            }
//                            else if (args[0].equalsIgnoreCase("info")) {
//                                if (!sender.hasPermission("pwarp.info")) {
//                                    sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                                    return true;
//                                }
//                                sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "----------" + ChatColor.RESET + ChatColor.YELLOW + "[Info]" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "----------");
//                                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Developer" + ChatColor.GRAY + " - The_King_Senne | TKS Plugins");
//                                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Plugin version" + ChatColor.GRAY + " - " + this.getDescription().getVersion());
//                                if (this.getConfig().get("warpLimit") == null) {
//                                    sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Default limit" + ChatColor.GRAY + " - No limit");
//                                }
//                                else {
//                                    sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Default limit" + ChatColor.GRAY + " - " + this.getConfig().getInt("warpLimit") + " Warps");
//                                }
//                                if (this.wF.getWarpFile().getStringList("warpList").isEmpty()) {
//                                    sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Total warps" + ChatColor.GRAY + " - 0 Warps");
//                                }
//                                else {
//                                    sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Total warps" + ChatColor.GRAY + " - " + this.wF.getWarpFile().getStringList("warpList").size() + " Warps");
//                                }
//                                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Tp cooldown" + ChatColor.GRAY + " - " + this.getConfig().getInt("teleportDelayInSeconds") + " Seconds");
//                                if (this.getConfig().get("worldToWorldTeleport") != null) {
//                                    if (this.getConfig().getBoolean("worldToWorldTeleport")) {
//                                        sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " W2w tp" + ChatColor.GRAY + " - Enabled");
//                                    }
//                                    else {
//                                        sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " W2w tp" + ChatColor.GRAY + " - Disabled");
//                                    }
//                                }
//                                if (this.getConfig().get("checkWarpSafety") != null) {
//                                    if (this.getConfig().getBoolean("checkWarpSafety")) {
//                                        sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Warp safety" + ChatColor.GRAY + " - Enabled");
//                                    }
//                                    else {
//                                        sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Warp safety" + ChatColor.GRAY + " - Disabled");
//                                    }
//                                }
//                                return true;
//                            }
//
//                            else if (args[0].equalsIgnoreCase("sethidden")) {
//                                if (!sender.hasPermission("pwarp.sethidden")) {
//                                    sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                                }
//                                if (args.length != 3) {
//                                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp sethidden <warp> <true/false>"));
//                                    return true;
//                                }
//                                if (this.wF.getWarpFile().getString("warps." + args[1].toLowerCase()) == null) {
//                                    sender.sendMessage(ChatColor.RED + Messages.WARP_NOT_EXISTING.getMessage());
//                                    return true;
//                                }
//                                final Player player = (Player)sender;
//                                boolean hidden;
//                                try {
//                                    hidden = Boolean.parseBoolean(args[2]);
//                                }
//                                catch (Exception e5) {
//                                    sender.sendMessage(ChatColor.RED + Messages.TRUE_OR_FALSE.getMessage());
//                                    return true;
//                                }
//                                if (this.wF.getWarpFile().getString("warps." + args[1].toLowerCase() + "owner-UUID") != null && !player.getUniqueId().toString().equals(this.wF.getWarpFile().getString("warps." + args[1].toLowerCase() + "owner-UUID")) && !sender.hasPermission("pwarp.sethidden.others")) {
//                                    sender.sendMessage(ChatColor.RED + Messages.NOT_AN_OWNER.getMessage());
//                                    return true;
//                                }
//                                if (hidden) {
//                                    this.gui.delItem(args[1].toLowerCase());
//                                }
//                                else {
//                                    //this.gui.addItem(args[1].toLowerCase(), this);
//                                }
//                                this.wF.getWarpFile().set("warps." + args[1].toLowerCase() + ".isHidden", hidden);
//                                try {
//                                    this.wF.getWarpFile().save(new File(this.getDataFolder(), "warps.yml"));
//                                }
//                                catch (IOException e2) {
//                                    e2.printStackTrace();
//                                }
//                                sender.sendMessage(ChatColor.GREEN + Messages.HIDDEN_UNHIDDEN.getMessage());
//                                return true;
//                            }
//                            else if (args[0].equalsIgnoreCase("warpsafety")) {
//                                if (!sender.hasPermission("pwarp.warpsafety")) {
//                                    sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                                    return true;
//                                }
//                                if (args.length != 2 || (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false"))) {
//                                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp warpsafety <true/false>"));
//                                    return true;
//                                }
//                                this.getConfig().set("checkWarpSafety", Boolean.parseBoolean(args[1].toLowerCase()));
//                                this.saveConfig();
//                                sender.sendMessage(ChatColor.GREEN + Messages.WARP_SAFETY_UPDATED.getMessage());
//                                return true;
//                            }
//                            sender.sendMessage(ChatColor.RED + Messages.NEED_HELP.getMessage());
//                            return true;
//                        }
//                        if (!sender.hasPermission("pwarp.setdelay")) {
//                            sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                            return true;
//                        }
//                        if (args.length != 2) {
//                            sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setdelay <seconds>"));
//                            return true;
//                        }
//                        int seconds = 0;
//                        try {
//                            seconds = Integer.parseInt(args[1]);
//                        }
//                        catch (Exception e3) {
//                            sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
//                            return true;
//                        }
//                        this.getConfig().set("teleportDelayInSeconds", seconds);
//                        this.saveConfig();
//                        sender.sendMessage(ChatColor.GREEN + Messages.SET_DELAY.getMessage());
//                        return true;
//                    }
//                }
//            }
//            else {
//                if (args.length < 1) {
//                    askHelp.generalHelp(sender, 1);
//                    return true;
//                }
//                if (cmd.getName().equalsIgnoreCase("pwarp")) {
//                    if (args[0].equalsIgnoreCase("reload")) {
//                        this.warp.reloadConfig(sender, this);
//                        return true;
//                    }
//                }
//                else if (args[0].equalsIgnoreCase("help")) {
//                    if (args.length == 2) {
//                        int page2 = 1;
//                        try {
//                            page2 = Integer.parseInt(args[1]);
//                        }
//                        catch (Exception e6) {
//                            sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
//                            return true;
//                        }
//                        if (page2 > 3) {
//                            sender.sendMessage(ChatColor.RED + Messages.PAGE_NOT_EXISTING.getMessage());
//                            return true;
//                        }
//                        if (!sender.hasPermission("pwarp.staffhelp") && !sender.isOp() && page2 > 2) {
//                            sender.sendMessage(ChatColor.RED + Messages.PAGE_NOT_EXISTING.getMessage());
//                            return true;
//                        }
//                        askHelp.generalHelp(sender, page2);
//                        return true;
//                    }
//                    else {
//                        if (args.length == 1) {
//                            askHelp.generalHelp(sender, 1);
//                            return true;
//                        }
//                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp help <page>"));
//                        return true;
//                    }
//                }
//                else if (args[0].equalsIgnoreCase("info")) {
//                    sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "----------" + ChatColor.RESET + ChatColor.YELLOW + "[Info]" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "----------");
//                    sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Developer" + ChatColor.GRAY + " - The_King_Senne | TKS Plugins");
//                    sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Plugin version" + ChatColor.GRAY + " - " + this.getDescription().getVersion());
//                    if (this.getConfig().get("warpLimit") == null) {
//                        sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Default limit" + ChatColor.GRAY + " - No limit");
//                    }
//                    else {
//                        sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Default limit" + ChatColor.GRAY + " - " + this.getConfig().getInt("warpLimit") + " Warps");
//                    }
//                    if (this.getConfig().getStringList("warpList").isEmpty()) {
//                        sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Total warps" + ChatColor.GRAY + " - 0 Warps");
//                    }
//                    else {
//                        sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Total warps" + ChatColor.GRAY + " - " + this.getConfig().getStringList("warpList").size() + " Warps");
//                    }
//                    sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Tp cooldown" + ChatColor.GRAY + " - " + this.getConfig().getInt("teleportDelayInSeconds") + " Seconds");
//                    if (this.getConfig().get("worldToWorldTeleport") != null) {
//                        if (this.getConfig().getBoolean("worldToWorldTeleport")) {
//                            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " W2w tp" + ChatColor.GRAY + " - Enabled");
//                        }
//                        else {
//                            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " W2w tp" + ChatColor.GRAY + " - Disabled");
//                        }
//                    }
//                    if (this.getConfig().get("checkWarpSafety") != null) {
//                        if (this.getConfig().getBoolean("checkWarpSafety")) {
//                            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Warp safety" + ChatColor.GRAY + " - Enabled");
//                        }
//                        else {
//                            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " Warp safety" + ChatColor.GRAY + " - Disabled");
//                        }
//                    }
//                }
//                else if (args[0].equalsIgnoreCase("listother")) {
//                    if (args.length != 2) {
//                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp listother <player>"));
//                        return true;
//                    }
//                    final List<String> ownedWarps2 = new ArrayList<>();
//                    OfflinePlayer player3 = null;
//                    try {
//                        player3 = Bukkit.getOfflinePlayer(args[1]);
//                        if (!player3.hasPlayedBefore()) {
//                            sender.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_EXISTING.getMessage());
//                            return true;
//                        }
//                    }
//                    catch (Exception e3) {
//                        sender.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_EXISTING.getMessage());
//                        return true;
//                    }
//                    for (int j = 0; j < this.wF.getWarpFile().getStringList("warpList").size(); ++j) {
//                        if (this.wF.getWarpFile().getString("warps." + this.wF.getWarpFile().getStringList("warpList").get(j) + ".owner-UUID").equals(player3.getUniqueId().toString())) {
//                            ownedWarps2.add(this.wF.getWarpFile().getStringList("warpList").get(j));
//                        }
//                    }
//                    sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.RESET + ChatColor.YELLOW + Messages.OWNED_WARPS.getMessage().replaceAll("PPLAYERP", player3.getName()) + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----" + ChatColor.GOLD + "\n » " + ChatColor.YELLOW + String.join(ChatColor.GOLD + "\n » " + ChatColor.YELLOW, ownedWarps2));
//                    return true;
//                }
//                else if (args[0].equalsIgnoreCase("hooks")) {
//                    if (!sender.hasPermission("pwarp.hooks")) {
//                        sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                        return true;
//                    }
//                    if (args.length != 1) {
//                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp hooks"));
//                        return true;
//                    }
//                    sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.RESET + ChatColor.YELLOW + "[Hooks]" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----" + ChatColor.GOLD + "\n » " + ChatColor.YELLOW + String.join(ChatColor.GOLD + "\n » " + ChatColor.YELLOW, this.hooks));
//                    return true;
//                }
//                else if (args[0].equalsIgnoreCase("w2w")) {
//                    if (!sender.hasPermission("pwarp.worldtoworld")) {
//                        sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                        return true;
//                    }
//                    if (args.length != 2) {
//                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp w2w enable/disable"));
//                        return true;
//                    }
//                    if (args[1].equalsIgnoreCase("enable")) {
//                        this.getConfig().set("worldToWorldTeleport", true);
//                        this.saveConfig();
//                        sender.sendMessage(ChatColor.GREEN + Messages.ENABLED_W2W.getMessage());
//                        return true;
//                    }
//                    if (args[1].equalsIgnoreCase("disable")) {
//                        this.getConfig().set("worldToWorldTeleport", false);
//                        this.saveConfig();
//                        sender.sendMessage(ChatColor.GREEN + Messages.DISABLED_W2W.getMessage());
//                        return true;
//                    }
//                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp w2w enable/disable"));
//                    return true;
//                }
//                else if (args[0].equalsIgnoreCase("setdelay")) {
//                    if (!sender.hasPermission("pwarp.setdelay")) {
//                        sender.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
//                        return true;
//                    }
//                    if (args.length != 2) {
//                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setdelay <seconds>"));
//                        return true;
//                    }
//                    int seconds2 = 0;
//                    try {
//                        seconds2 = Integer.parseInt(args[1]);
//                    }
//                    catch (Exception e6) {
//                        sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
//                        return true;
//                    }
//                    this.getConfig().set("teleportDelayInSeconds", seconds2);
//                    this.saveConfig();
//                    sender.sendMessage(ChatColor.GREEN + Messages.SET_DELAY.getMessage());
//                    return true;
//                }
//                else if (args[0].equalsIgnoreCase("blacklist")) {
//                    if (args.length != 3 && args.length != 2) {
//                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp blacklist add/remove <world> or /pwarp blacklist list"));
//                        return true;
//                    }
//                    if (!args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove") && !args[1].equalsIgnoreCase("list")) {
//                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp blacklist add/remove <world> or /pwarp blacklist list"));
//                        return true;
//                    }
//                    if (args[1].equalsIgnoreCase("add")) {
//                        List<String> worlds2 = new ArrayList<>();
//                        if (this.getConfig().getStringList("blacklist") == null) {
//                            worlds2.add(args[2].toLowerCase());
//                        }
//                        else {
//                            worlds2 = this.getConfig().getStringList("blacklist");
//                            worlds2.add(args[2].toLowerCase());
//                        }
//                        this.getConfig().set("blacklist", worlds2);
//                        this.saveConfig();
//                        sender.sendMessage(ChatColor.GREEN + Messages.ADDED_BLACKLIST.getMessage().replaceAll("PWORLDP", args[2].toLowerCase()));
//                        return true;
//                    }
//                    if (args[1].equalsIgnoreCase("remove")) {
//                        List<String> worlds2 = new ArrayList<>();
//                        if (this.getConfig().getStringList("blacklist") == null) {
//                            sender.sendMessage(ChatColor.RED + Messages.WORLD_NOT_BLACKLISTED.getMessage().replaceAll("PWORLDP", args[2].toLowerCase()));
//                            return true;
//                        }
//                        if (!this.getConfig().getStringList("blacklist").contains(args[2].toLowerCase())) {
//                            sender.sendMessage(ChatColor.RED + Messages.WORLD_NOT_BLACKLISTED.getMessage().replaceAll("PWORLDP", args[2].toLowerCase()));
//                            return true;
//                        }
//                        worlds2 = this.getConfig().getStringList("blacklist");
//                        worlds2.remove(args[2].toLowerCase());
//                        this.getConfig().set("blacklist", worlds2);
//                        this.saveConfig();
//                        sender.sendMessage(ChatColor.GREEN + Messages.REMOVED_BLACKLIST.getMessage().replaceAll("PWORLDP", args[2].toLowerCase()));
//                        return true;
//                    }
//                    else if (args[1].equalsIgnoreCase("list")) {
//                        if (this.getConfig().getStringList("blacklist") == null | this.getConfig().getStringList("blacklist").isEmpty()) {
//                            sender.sendMessage(ChatColor.RED + Messages.NO_WORLDS_BLACKLISTED.getMessage());
//                            return true;
//                        }
//                        final List<String> blacklist2 = this.getConfig().getStringList("blacklist");
//                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.RESET + ChatColor.YELLOW + "[Blacklist]" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----" + ChatColor.GOLD + "\n » " + ChatColor.YELLOW + String.join(ChatColor.GOLD + "\n » " + ChatColor.YELLOW, blacklist2));
//                        return true;
//                    }
//                }
//                else {
//                    if (args[0].equalsIgnoreCase("clearoldwarps")) {
//                        this.warp.removeOldWarps(this, sender);
//                        return true;
//                    }
//                    if (args[0].equalsIgnoreCase("deleteall") || args[0].equalsIgnoreCase("removeall") || args[0].equalsIgnoreCase("removeallwarps") || args[0].equalsIgnoreCase("deleteallwarps") || args[0].equalsIgnoreCase("delall") || args[0].equalsIgnoreCase("delallwarps")) {
//                        if (sender.isOp() || sender.hasPermission("pwarp.deleteall")) {
//                            this.warp.removeAllWarps(this, sender);
//                        }
//                        return true;
//                    }
//                    if (!args[0].equalsIgnoreCase("setlimit") && !args[0].equalsIgnoreCase("limit")) {
//                        sender.sendMessage(ChatColor.RED + "This command can't be used by a console.");
//                        return true;
//                    }
//                    if (args.length < 2) {
//                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setlimit <limit>"));
//                        return true;
//                    }
//                    this.warp.setLimit(this, sender, args[1]);
//                    return true;
//                }
//                return true;
//            }
//        }
