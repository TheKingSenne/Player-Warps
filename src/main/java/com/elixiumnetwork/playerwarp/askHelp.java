package com.elixiumnetwork.playerwarp;

import me.tks.messages.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

interface askHelp {
    default void generalHelp(final CommandSender sender, final int page) {
        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------" + ChatColor.RESET + ChatColor.YELLOW + "[PlayerWarps]" + ChatColor.RESET + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "---------------------");
        if (sender.isOp() || sender.hasPermission("pwarp.staffhelp")) {
            if (page == 1) {
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setitemprice <amount>" + ChatColor.GRAY + " - " + Messages.HELP_SETITEMPRICE.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setprice <amount>" + ChatColor.GRAY + " - " + Messages.HELP_SETPRICE.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp reload" + ChatColor.GRAY + " - " + Messages.HELP_RELOAD.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp guiitem" + ChatColor.GRAY + " - " + Messages.HELP_GUIITEM.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp deleteall" + ChatColor.GRAY + " - " + Messages.HELP_DELETEALL.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setdelay <seconds>" + ChatColor.GRAY + " - " + Messages.HELP_SETDELAY.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setlimit <limit>" + ChatColor.GRAY + " - " + Messages.HELP_SETLIMIT.getMessage());
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp clearoldwarps" + ChatColor.GRAY + " - " + Messages.HELP_CLEAROLDWARPS.getMessage().replaceAll("PINACTIVEDAYSP", (JavaPlugin.getPlugin(PWarpPlugin.class)).getConfig().getInt("inactiveWarpDays") + ""));
                //sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp set <name>" + ChatColor.GRAY + " - " + Messages.HELP_SET.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp delete <name>" + ChatColor.GRAY + " - " + Messages.HELP_DELETE.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp warp <warp> | /pww <warp>" + ChatColor.GRAY + " - " + Messages.HELP_WARP.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setseparator" + ChatColor.GRAY + " - " + Messages.HELP_SETSEPARATOR.getMessage());
            }
            else if (page == 2) {
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwg | /pws " + ChatColor.GRAY + " - " + Messages.HELP_PWG.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setlore <warp> <1,2,3> <lore>" + ChatColor.GRAY + " - " + Messages.HELP_SETLORE.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp resetlore <warp>" + ChatColor.GRAY + " - " + Messages.HELP_RESETLORE.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp movewarp <warp>" + ChatColor.GRAY + " - " + Messages.HELP_MOVEWARP.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setpublic <warp>" + ChatColor.GRAY + " - " + Messages.HELP_SETPUBLIC.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setprivate <warp>" + ChatColor.GRAY + " - " + Messages.HELP_SETPRIVATE.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp trust <warp> <player>" + ChatColor.GRAY + " - " + Messages.HELP_TRUST.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp untrust <warp> <player>" + ChatColor.GRAY + " - " + Messages.HELP_UNTRUST.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp setitem <warp>" + ChatColor.GRAY + " - " + Messages.HELP_SETITEM.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp resetitem <warp>" + ChatColor.GRAY + " - " + Messages.HELP_RESETITEM.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp blacklist add/remove <world>" + ChatColor.GRAY + " - " + Messages.HELP_BLACKLISTADDREMOVE.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp blacklist list" + ChatColor.GRAY + " - " + Messages.HELP_BLACKLISTLIST.getMessage());
            }
            else if (page == 3) {
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp listown" + ChatColor.GRAY + " - " + Messages.HELP_LISTOWN.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp hooks" + ChatColor.GRAY + " - " + Messages.HELP_HOOKS.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp w2w enable/disable" + ChatColor.GRAY + " - " + Messages.HELP_W2W.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp listother <player>" + ChatColor.GRAY + " - " + Messages.HELP_LISTOTHER.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp sethidden <warp> <true/false>" + ChatColor.GRAY + " - " + Messages.HELP_SETHIDDEN.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp info" + ChatColor.GRAY + " - " + Messages.HELP_INFO.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp rename <warp> <name>" + ChatColor.GRAY + " - " + Messages.HELP_RENAME.getMessage());
                sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /pwarp warpsafety <true/false>" + ChatColor.GRAY + " - " + Messages.HELP_WARPSAFETY.getMessage());
            }
        }
        else if (page == 1) {
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
    }
}
