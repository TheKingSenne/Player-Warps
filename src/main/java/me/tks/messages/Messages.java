package me.tks.messages;

import me.tks.playerwarp.PWarp;
import net.md_5.bungee.api.ChatColor;

public enum Messages {
    WARP_CONFIGURED_WRONG(PWarp.messageFile.getMessageFile().getString("configuredWrong")),
    NO_PERMISSION(PWarp.messageFile.getMessageFile().getString("noPermission")),
    NOT_AN_OWNER(PWarp.messageFile.getMessageFile().getString("notAnOwner")),
    WARP_NOT_EXISTING(PWarp.messageFile.getMessageFile().getString("warpNotExisting")),
    PLUGIN_NEEDS_NUMBER(PWarp.messageFile.getMessageFile().getString("needsNumber")),
    CORRECT_USAGE(PWarp.messageFile.getMessageFile().getString("correctUsage")),
    MADE_PUBLIC(PWarp.messageFile.getMessageFile().getString("madePublic")),
    MADE_PRIVATE(PWarp.messageFile.getMessageFile().getString("madePrivate")),
    PLAYER_NOT_EXISTING(PWarp.messageFile.getMessageFile().getString("noPlayer")),
    NEED_HELP(PWarp.messageFile.getMessageFile().getString("needHelp")),
    GUI_INVENTORY_NAME(PWarp.messageFile.getMessageFile().getString("guiName")),
    BACK_BUTTON(PWarp.messageFile.getMessageFile().getString("backButton")),
    NEXT_BUTTON(PWarp.messageFile.getMessageFile().getString("nextButton")),
    NO_WARPS(PWarp.messageFile.getMessageFile().getString("noWarps")),
    GUI_PAGE(PWarp.messageFile.getMessageFile().getString("guiPage")),
    GUI_WARP_OWNER(PWarp.messageFile.getMessageFile().getString("guiOwner")),
    GUI_VISITORS(PWarp.messageFile.getMessageFile().getString("guiVisitors")),
    GUI_ITEM_CHANGED(PWarp.messageFile.getMessageFile().getString("guiItemChanged")),
    LIMIT_REACHED(PWarp.messageFile.getMessageFile().getString("limitReached")),
    NAME_IN_USE(PWarp.messageFile.getMessageFile().getString("nameAlreadyUsed")),
    CANT_AFFORD_MONEY(PWarp.messageFile.getMessageFile().getString("cantAffordMoney")),
    CREATED_ITEM_PAID_WARP(PWarp.messageFile.getMessageFile().getString("createdItemPaidWarp")),
    CREATED_MONEY_PAID_WARP(PWarp.messageFile.getMessageFile().getString("createdMoneyPaidWarp")),
    CREATED_BOTH_PAID_WARP(PWarp.messageFile.getMessageFile().getString("createdBothPaidWarp")),
    CREATED_FREE_WARP(PWarp.messageFile.getMessageFile().getString("createdFreeWarp")),
    REMOVED_WARP(PWarp.messageFile.getMessageFile().getString("removedWarp")),
    NOT_TRUSTED(PWarp.messageFile.getMessageFile().getString("notTrusted")),
    TELEPORTED(PWarp.messageFile.getMessageFile().getString("onTeleport")),
    PAGE_NOT_EXISTING(PWarp.messageFile.getMessageFile().getString("pageLimit")),
    HOLD_ITEM(PWarp.messageFile.getMessageFile().getString("holdItem")),
    SET_PRICE(PWarp.messageFile.getMessageFile().getString("setPrice")),
    REMOVED_ALL(PWarp.messageFile.getMessageFile().getString("allWarpsRemoved")),
    LIMIT_ABOVE_0(PWarp.messageFile.getMessageFile().getString("limitAbove0")),
    LIMIT_CHANGED(PWarp.messageFile.getMessageFile().getString("changedLimit")),
    SET_UNSAFE(PWarp.messageFile.getMessageFile().getString("setUnsafe")),
    IS_UNSAFE(PWarp.messageFile.getMessageFile().getString("isUnsafe")),
    IS_OBSTRUCTED(PWarp.messageFile.getMessageFile().getString("isObstructed")),
    LORE_LIMIT(PWarp.messageFile.getMessageFile().getString("loreLimit")),
    UPDATED_LORE(PWarp.messageFile.getMessageFile().getString("updatedLore")),
    MOVED_WARP(PWarp.messageFile.getMessageFile().getString("movedWarp")),
//    REMOVED_INACTIVE(PWarp.messageFile.getMessageFile().getString("removedInactive")),
    PLAYER_NOT_TRUSTED(PWarp.messageFile.getMessageFile().getString("playerNotTrusted")),
    PLAYER_ALREADY_TRUSTED(PWarp.messageFile.getMessageFile().getString("playerAlreadyTrusted")),
    PLAYER_TRUSTED(PWarp.messageFile.getMessageFile().getString("playerTrusted")),
    PLAYER_UNTRUSTED(PWarp.messageFile.getMessageFile().getString("playerUntrusted")),
    CANT_AFFORD_BOTH(PWarp.messageFile.getMessageFile().getString("cantAffordBoth")),
    CHANGED_WARP_ICON(PWarp.messageFile.getMessageFile().getString("changedIcon")),
    CANT_AFFORD_ITEM(PWarp.messageFile.getMessageFile().getString("cantAffordItem")),
    SERVER_NAME(PWarp.messageFile.getMessageFile().getString("serverName")),
    MOVED(PWarp.messageFile.getMessageFile().getString("moved")),
    DONT_MOVE(PWarp.messageFile.getMessageFile().getString("dontMove")),
    SET_DELAY(PWarp.messageFile.getMessageFile().getString("setDelay")),
    BLACKLISTED_WORLD(PWarp.messageFile.getMessageFile().getString("blacklistedWorld")),
    WORLD_NOT_BLACKLISTED(PWarp.messageFile.getMessageFile().getString("worldNotBlacklisted")),
    ADDED_BLACKLIST(PWarp.messageFile.getMessageFile().getString("addedBlacklist")),
    REMOVED_BLACKLIST(PWarp.messageFile.getMessageFile().getString("removedBlacklist")),
    CHANGED_SEPARATOR(PWarp.messageFile.getMessageFile().getString("changedSeparator")),
    NO_ACCESS_GP(PWarp.messageFile.getMessageFile().getString("noAccessGriefprevention")),
    W2WTELEPORT(PWarp.messageFile.getMessageFile().getString("noWorldToWorldTeleport")),
    OWNED_WARPS(PWarp.messageFile.getMessageFile().getString("ownedWarps")),
    NO_OWNED_WARPS(PWarp.messageFile.getMessageFile().getString("noOwnedWarps")),
    NO_WORLDS_BLACKLISTED(PWarp.messageFile.getMessageFile().getString("noWorldsBlacklisted")),
    ENABLED_W2W(PWarp.messageFile.getMessageFile().getString("enabledWorldToWorldTeleport")),
    DISABLED_W2W(PWarp.messageFile.getMessageFile().getString("disabledWorldToWorldTeleport")),
    NO_COMMANDS_ALLOWED(PWarp.messageFile.getMessageFile().getString("noCommandsAllowed")),
    ALREADY_BLACKLISTED(PWarp.messageFile.getMessageFile().getString("alreadyBlacklisted")),
    WORLD_NOT_EXISTING(PWarp.messageFile.getMessageFile().getString("worldNotExisting")),
    TRUE_OR_FALSE(PWarp.messageFile.getMessageFile().getString("trueOrFalse")),
    HIDDEN_UNHIDDEN(PWarp.messageFile.getMessageFile().getString("hiddenUnhidden")),
    RENAMED_WARP(PWarp.messageFile.getMessageFile().getString("renamedWarp")),
    HELP_SETITEMPRICE(PWarp.messageFile.getMessageFile().getString("helpSetItemPrice")),
    HELP_SETPRICE(PWarp.messageFile.getMessageFile().getString("helpSetPrice")),
    HELP_GUIITEM(PWarp.messageFile.getMessageFile().getString("helpGuiItem")),
    HELP_DELETEALL(PWarp.messageFile.getMessageFile().getString("helpDeleteAll")),
    HELP_SETDELAY(PWarp.messageFile.getMessageFile().getString("helpSetDelay")),
    HELP_SETLIMIT(PWarp.messageFile.getMessageFile().getString("helpSetLimit")),
//    HELP_CLEAROLDWARPS(PWarp.messageFile.getMessageFile().getString("helpClearOldWarps")),
    HELP_SET(PWarp.messageFile.getMessageFile().getString("helpSet")),
    HELP_DELETE(PWarp.messageFile.getMessageFile().getString("helpDelete")),
    HELP_WARP(PWarp.messageFile.getMessageFile().getString("helpWarp")),
    HELP_SETSEPARATOR(PWarp.messageFile.getMessageFile().getString("helpSetSeparator")),
    HELP_PWG(PWarp.messageFile.getMessageFile().getString("helpPwg")),
    HELP_SETLORE(PWarp.messageFile.getMessageFile().getString("helpSetLore")),
    HELP_MOVEWARP(PWarp.messageFile.getMessageFile().getString("helpMoveWarp")),
    HELP_SETPUBLIC(PWarp.messageFile.getMessageFile().getString("helpSetPublic")),
    HELP_SETPRIVATE(PWarp.messageFile.getMessageFile().getString("helpSetPrivate")),
    HELP_TRUST(PWarp.messageFile.getMessageFile().getString("helpTrust")),
    HELP_UNTRUST(PWarp.messageFile.getMessageFile().getString("helpUntrust")),
    HELP_SETITEM(PWarp.messageFile.getMessageFile().getString("helpSetItem")),
    HELP_BLACKLISTADDREMOVE(PWarp.messageFile.getMessageFile().getString("helpBlacklistAddRemove")),
    HELP_BLACKLISTLIST(PWarp.messageFile.getMessageFile().getString("helpBlacklistList")),
    HELP_LISTOWN(PWarp.messageFile.getMessageFile().getString("helpListOwn")),
    HELP_HOOKS(PWarp.messageFile.getMessageFile().getString("helpHooks")),
    HELP_W2W(PWarp.messageFile.getMessageFile().getString("helpW2w")),
    HELP_LISTOTHER(PWarp.messageFile.getMessageFile().getString("helpListOther")),
    HELP_SETHIDDEN(PWarp.messageFile.getMessageFile().getString("helpSetHidden")),
    HELP_INFO(PWarp.messageFile.getMessageFile().getString("helpInfo")),
    HELP_RENAME(PWarp.messageFile.getMessageFile().getString("helpRename")),
    HELP_WARPSAFETY(PWarp.messageFile.getMessageFile().getString("helpWarpSafety")),
    WARP_SAFETY_UPDATED(PWarp.messageFile.getMessageFile().getString("warpSafetyUpdated")),
    UPDATED_DEFAULT_PRIVACY(PWarp.messageFile.getMessageFile().getString("updatedDefaultPrivacy")),
    HELP_UPDATE_PRIVACY(PWarp.messageFile.getMessageFile().getString("helpUpatePrivate")),;


    private final String msg;

    /**
     * Constructor for message enum.
     * @param msg String containing the message.
     */
    Messages(String msg) {
        this.msg = ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * Getter for a message.
     * @return String containing the message.
     */
    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', this.msg);
    }
}
