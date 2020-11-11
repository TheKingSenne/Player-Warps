package me.tks.playerwarp;

import me.tks.messages.Messages;
import me.tks.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private final WarpList wL;
    private final GuiCatalog gC;

    public Commands(WarpList wL, GuiCatalog gC) {
        this.wL = wL;
        this.gC = gC;
    }

// IDEAS:
    // - Particle effects (might make an addon)
    // - Gui managing per warp (might make an addon idk)
    // - Different sorting for gui -> but based on what though; possible ideas so far: visitors, alphabetically, item
    //
    //


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            // Main command for plugin
            if(cmd.getName().equalsIgnoreCase("pwarp")) {

                // Player didn't enter enough arguments
                if (args.length == 0) {

                    player.sendMessage(ChatColor.RED + Messages.NEED_HELP.getMessage());
                    return true;

                }

                // Go to warp
                else if (args[0].equalsIgnoreCase("warp")) {

                    // Check for correct arguments
                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp warp <warpName>"));
                        return true;
                    }

                    // Check if warp exists
                    if (!wL.warpExistsWithMessage(sender, args[1])) {
                        return true;
                    }

                    wL.getWarp(args[1]).goTo(player);
                    return true;
                }

                // Set warp
                else if (args[0].equalsIgnoreCase("set")) {

                    // Check for correct arguments
                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setwarp <warpName>"));
                        return true;
                    }

                    Warp.setWarp(player, args[1], wL);

                    return true;
                }

                // Delete warp
                else if (args[0].equalsIgnoreCase("delete")) {

                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp delete <warpName>"));
                        return true;
                    }

                    wL.removeWarp(player, args[1]);

                    return true;
                }

                // Delete all warps
                else if (args[0].equalsIgnoreCase("deleteAll")) {
                    // Check permission
                    if (!PlayerUtils.hasPermissionWithMessage(player, "pwarp.deleteall")) return true;


                    wL.removeAllWarps(player);
                    return true;
                }

                // Change item type of a warp
                else if (args[0].equalsIgnoreCase("setItem")) {

                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setitem <warpName>"));
                        return true;
                    }

                    if (!wL.warpExistsWithMessage(sender, args[1])) return true;

                    wL.getWarp(args[1]).setItemStack(player);

                    return true;
                }

                // Move a warp to a new location
                else if (args[0].equalsIgnoreCase("move")) {

                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp move <warpName>"));
                        return true;
                    }

                    if (!wL.warpExistsWithMessage(player, args[1])) return true;

                    wL.getWarp(args[1]).move(player);
                    return true;
                }

                // Makes a warp public
                else if (args[0].equalsIgnoreCase("setpublic")) {

                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setpublic <warpName>"));
                        return true;
                    }

                    if (!wL.warpExistsWithMessage(player, args[1])) return true;

                    wL.getWarp(args[1]).setPrivacyState(player, false);
                    return true;
                }

                // Makes a warp private
                else if (args[0].equalsIgnoreCase("setprivate")) {

                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setprivate <warpName>"));
                        return true;
                    }

                    if (!wL.warpExistsWithMessage(player, args[1])) return true;

                    wL.getWarp(args[1]).setPrivacyState(player, true);
                    return true;
                }

                // Adds a trusted player
                else if (args[0].equalsIgnoreCase("trust")) {

                    if (args.length != 3) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp trust <playerName> <warpName>"));
                        return true;
                    }

                    if (!wL.warpExistsWithMessage(player, args[2])) return true;

                    OfflinePlayer p = PlayerUtils.getOfflinePlayerFromName(player, args[1]);

                    if (p == null) return true;

                    wL.getWarp(args[2]).addTrustedPlayer(player, p);
                    return true;
                }

                // Remove a trusted player
                else if (args[0].equalsIgnoreCase("untrust")) {

                    if (args.length != 3) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp untrust <playerName> <warpName>"));
                        return true;
                    }

                    if (!wL.warpExistsWithMessage(player, args[2])) return true;

                    OfflinePlayer p = PlayerUtils.getOfflinePlayerFromName(player, args[1]);

                    if (p == null) return true;

                    wL.getWarp(args[2]).removeTrustedPlayer(player, p);
                    return true;
                }

                // Open the first page of the GUI
                else if (args[0].equalsIgnoreCase("gui")){

                    gC.openFirstGui(player);
                    return true;
                }

                // Set the default warp limit
                else if (args[0].equalsIgnoreCase("setLimit")) {

                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setlimit <limit>"));
                        return true;
                    }

                    if (!PlayerUtils.hasPermissionWithMessage(player, "pwarp.setlimit")) return true;


                    PWarp.pC.setStandardLimit(sender, args[1]);
                    return true;
                }

                // Set the gui refresh rate
                else if (args[0].equalsIgnoreCase("refreshRate")) {

                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp refreshrate <minutes>"));
                        return true;
                    }

                    if (!PlayerUtils.hasPermissionWithMessage(player, "pwarp.refreshrate")) return true;

                    PWarp.pC.setRefreshRateInMinutes(sender, args[1]);

                    return true;
                }

                // Help command
                else if (args[0].equalsIgnoreCase("help")) {
                    if (args.length == 1) {
                        help(sender, 1);
                        return true;
                    }
                    else if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp help <page>"));
                        return true;
                    }

                    try {
                        help(sender, Integer.parseInt(args[1]));
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
                    }
                    return true;
                }

                // Set lore command
                else if (args[0].equalsIgnoreCase("setLore")) {

                    if (args.length < 4) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setlore <warp> <1|2|3> <new lore>"));
                        return true;
                    }

                    if (!wL.warpExistsWithMessage(player, args[1])) return true;

                    int number;

                    try {
                        number = Integer.parseInt(args[2]);
                    }
                    catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setlore <warp> <1|2|3> <new lore>"));
                        return true;
                    }

                    wL.getWarp(args[1]).setLore(player, PlayerUtils.playerArrayInputToString(args, 2), number);

                    return true;
                }

                // Rename a warp
                else if (args[0].equalsIgnoreCase("rename")) {

                    if (args.length != 3) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp rename <warp> <newName>"));
                        return true;
                    }

                    if (!wL.warpExistsWithMessage(sender, args[1])) return true;

                    wL.getWarp(args[1]).changeName(player, args[2]);

                    return true;
                }

                // Set the top GUI item
                else if (args[0].equalsIgnoreCase("guiItem")) {

                    if (!PlayerUtils.hasPermissionWithMessage(player, "pwarp.guiitem")) return true;

                    PWarp.pC.setGuiItem(player);

                    return true;
                }

                // Set separator item
                else if (args[0].equalsIgnoreCase("separator")) {

                    if (!PlayerUtils.hasPermissionWithMessage(player, "pwarp.separator")) return true;

                    PWarp.pC.setSeparatorItem(player);

                    return true;
                }

                // Set delay
                else if(args[0].equalsIgnoreCase("setDelay")) {

                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setdelay <delay>"));
                        return true;
                    }

                    if (!PlayerUtils.hasPermissionWithMessage(player, "pwarp.setdelay")) return true;

                    try {
                        PWarp.pC.setTeleportDelay(Integer.parseInt(args[1]));
                        player.sendMessage(ChatColor.GREEN + Messages.SET_DELAY.getMessage());
                    }
                    catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
                        player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwarp setdelay <delay>"));
                    }

                    return true;
                }

                player.sendMessage(ChatColor.RED + Messages.NEED_HELP.getMessage());
                return true;
            }

            // Gui command
            else if (cmd.getName().equalsIgnoreCase("pwg")) {

                gC.openFirstGui((Player) sender);

                return true;
            }

            // Fast warp command
            else if (cmd.getName().equalsIgnoreCase("pwwarp")) {
                if (args.length != 1) {
                    player.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/pwwarp <warp>"));
                    return true;
                }

                if (!wL.warpExistsWithMessage(sender, args[0])) return true;

                wL.getWarp(args[0]).goTo(player);

                return true;
            }


            sender.sendMessage(ChatColor.RED + Messages.NEED_HELP.getMessage());
            return true;
        }


        return true;
    }

    public void help(CommandSender sender, int page) {

        if (page > 2 || page < 1) {
            sender.sendMessage(ChatColor.RED + Messages.PAGE_NOT_EXISTING.getMessage());
            return;
        }

        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------" + ChatColor.RESET + ChatColor.YELLOW + "[PlayerWarps]" + ChatColor.RESET + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "---------------------");
        //if (sender.isOp() || sender.hasPermission("pwarp.staffhelp")) {
            if (page == 1) {
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setitemprice <amount>" + ChatColor.GRAY + " - " + Messages.HELP_SETITEMPRICE.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setprice <amount>" + ChatColor.GRAY + " - " + Messages.HELP_SETPRICE.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp reload" + ChatColor.GRAY + " - " + Messages.HELP_RELOAD.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp guiitem" + ChatColor.GRAY + " - " + Messages.HELP_GUIITEM.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp deleteall" + ChatColor.GRAY + " - " + Messages.HELP_DELETEALL.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setdelay <seconds>" + ChatColor.GRAY + " - " + Messages.HELP_SETDELAY.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setlimit <limit>" + ChatColor.GRAY + " - " + Messages.HELP_SETLIMIT.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp clearoldwarps" + ChatColor.GRAY + " - " + Messages.HELP_CLEAROLDWARPS.getMessage().replaceAll("PINACTIVEDAYSP", (JavaPlugin.getPlugin(PWarpPlugin.class)).getConfig().getInt("inactiveWarpDays") + ""));
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp set <name>" + ChatColor.GRAY + " - " + Messages.HELP_SET.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp delete <name>" + ChatColor.GRAY + " - " + Messages.HELP_DELETE.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp warp <warp>" + ChatColor.GRAY + " - " + Messages.HELP_WARP.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setseparator" + ChatColor.GRAY + " - " + Messages.HELP_SETSEPARATOR.getMessage());
            }
            else if (page == 2) {
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwg | /pws " + ChatColor.GRAY + " - " + Messages.HELP_PWG.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setlore <warp> <1,2,3> <lore>" + ChatColor.GRAY + " - " + Messages.HELP_SETLORE.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp resetlore <warp>" + ChatColor.GRAY + " - " + Messages.HELP_RESETLORE.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp move <warp>" + ChatColor.GRAY + " - " + Messages.HELP_MOVEWARP.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setpublic <warp>" + ChatColor.GRAY + " - " + Messages.HELP_SETPUBLIC.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setprivate <warp>" + ChatColor.GRAY + " - " + Messages.HELP_SETPRIVATE.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp trust <player> <warp>" + ChatColor.GRAY + " - " + Messages.HELP_TRUST.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp untrust <player> <warp>" + ChatColor.GRAY + " - " + Messages.HELP_UNTRUST.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setitem <warp>" + ChatColor.GRAY + " - " + Messages.HELP_SETITEM.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp resetitem <warp>" + ChatColor.GRAY + " - " + Messages.HELP_RESETITEM.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp blacklist add/remove <world>" + ChatColor.GRAY + " - " + Messages.HELP_BLACKLISTADDREMOVE.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp blacklist list" + ChatColor.GRAY + " - " + Messages.HELP_BLACKLISTLIST.getMessage());
            }
            //else if (page == 3) {
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp listown" + ChatColor.GRAY + " - " + Messages.HELP_LISTOWN.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp hooks" + ChatColor.GRAY + " - " + Messages.HELP_HOOKS.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp w2w enable/disable" + ChatColor.GRAY + " - " + Messages.HELP_W2W.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp listother <player>" + ChatColor.GRAY + " - " + Messages.HELP_LISTOTHER.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp sethidden <warp> <true/false>" + ChatColor.GRAY + " - " + Messages.HELP_SETHIDDEN.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp info" + ChatColor.GRAY + " - " + Messages.HELP_INFO.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp rename <warp> <name>" + ChatColor.GRAY + " - " + Messages.HELP_RENAME.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp warpsafety <true/false>" + ChatColor.GRAY + " - " + Messages.HELP_WARPSAFETY.getMessage());
            //}
        //}
        /*else if (page == 1) {
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp set <name>" + ChatColor.GRAY + " - " + Messages.HELP_SET.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp delete <name>" + ChatColor.GRAY + " - " + Messages.HELP_DELETE.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp warp <warp> | /pww <warp>" + ChatColor.GRAY + " - " + Messages.HELP_WARP.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwg | /pws | /pw gui" + ChatColor.GRAY + " - " + Messages.HELP_PWG.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setlore <warp> <1,2,3> <lore>" + ChatColor.GRAY + " - " + Messages.HELP_SETLORE.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp resetlore <warp>" + ChatColor.GRAY + " - " + Messages.HELP_RESETLORE.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp movewarp <warp>" + ChatColor.GRAY + " - " + Messages.HELP_MOVEWARP.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setpublic <warp>" + ChatColor.GRAY + " - " + Messages.HELP_SETPUBLIC.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setprivate <warp>" + ChatColor.GRAY + " - " + Messages.HELP_SETPRIVATE.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp trust <warp> <player>" + ChatColor.GRAY + " - " + Messages.HELP_TRUST.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp untrust <warp> <player>" + ChatColor.GRAY + " - " + Messages.HELP_UNTRUST.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setitem <warp>" + ChatColor.GRAY + " - " + Messages.HELP_SETITEM.getMessage());
        }
        else if (page == 2) {
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp resetitem <warp>" + ChatColor.GRAY + " - " + Messages.HELP_RESETITEM.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp listown" + ChatColor.GRAY + " - " + Messages.HELP_LISTOWN.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp listother <player>" + ChatColor.GRAY + " - " + Messages.HELP_LISTOTHER.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp rename <warp> <name>" + ChatColor.GRAY + " - " + Messages.HELP_RENAME.getMessage());
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp sethidden <warp> <true/false>" + ChatColor.GRAY + " - " + Messages.HELP_SETHIDDEN.getMessage());
        }
*/

    }


}
