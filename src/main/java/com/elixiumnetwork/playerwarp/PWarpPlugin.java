
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
//        if (Bukkit.getPluginManager().getPlugin("GriefPreventionPlugin") != null) {
//            Bukkit.getConsoleSender().sendMessage("[PWarp] Successfully hooked into GriefPreventionPlugin!");
//            this.hooks.add("GriefPreventionPlugin" + ChatColor.GREEN + " \u2714");
//        }
//        else {
//            this.hooks.add("GriefPreventionPlugin" + ChatColor.RED + " \u2718");
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



//                else {
//                    if (args[0].equalsIgnoreCase("clearoldwarps")) {
//                        this.warp.removeOldWarps(this, sender);
//                        return true;
//                    }

