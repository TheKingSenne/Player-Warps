package me.tks.messages;

import me.tks.playerwarp.PWarp;

public enum MessagePathAndDefault {
    WARP_CONFIGURED_WRONG("configuredWrong", "Error: This warp has been configured wrong. Does this world still exist?"),
    NO_PERMISSION("noPermission", "Error: You do not have permission."),
    NOT_AN_OWNER("notAnOwner", "Error: You do not own this warp."),
    WARP_NOT_EXISTING("warpNotExisting", "Error: This warp does not exist"),
    PLUGIN_NEEDS_NUMBER("needsNumber", "Error: Please specify a number."),
    CORRECT_USAGE("correctUsage", "Error: Correct usage is PUSAGEP"),
    MADE_PUBLIC("madePublic", "Your warp has been made public."),
    MADE_PRIVATE("madePrivate", "Your warp has been made private."),
    PLAYER_NOT_EXISTING("noPlayer", "Error: This player does not exist."),
    NEED_HELP("needHelp", "For help, please use /pwarp help"),
    GUI_INVENTORY_NAME("guiName", "PlayerWarps"),
    BACK_BUTTON("backButton", "Back"),
    NEXT_BUTTON("nextButton", "Next"),
    NO_WARPS("noWarps", "Error: There are no warps available."),
    GUI_PAGE("guiPage", "Page:"),
    GUI_WARP_OWNER("guiOwner", "Owner:"),
    GUI_VISITORS("guiVisitors", "Visitors:"),
    GUI_ITEM_CHANGED("guiItemChanged", "The GUI-item has been changed."),
    LIMIT_REACHED("limitReached", "Error: You cannot have more than PLIMITP warps."),
    NAME_IN_USE("nameAlreadyUsed", "Error: PWARPNAMEP is already in use."),
    CANT_AFFORD_MONEY("cantAffordMoney", "Error: You can't afford a new warp. A new warp costs PMONEYP."),
    CREATED_ITEM_PAID_WARP("createdItemPaidWarp", "You have created a new warp and have paid PITEMAMOUNTP PITEMP."),
    CREATED_MONEY_PAID_WARP("createdMoneyPaidWarp", "You have created a new warp and have paid PMONEYP."),
    CREATED_BOTH_PAID_WARP("createdBothPaidWarp", "You have created a new warp and have paid PMONEYP and PITEMAMOUNTP PITEMP."),
    CREATED_FREE_WARP("createdFreeWarp", "You have created a new warp."),
    REMOVED_WARP("removedWarp", "Your warp has been successfully removed."),
    NOT_TRUSTED("notTrusted", "Error: You aren't trusted to this warp"),
    TELEPORTED("onTeleport", "You have been teleported."),
    PAGE_NOT_EXISTING("pageLimit", "Error: This page does not exist."),
    HOLD_ITEM("holdItem", "Error: Please hold an item"),
    SET_PRICE("setPrice", "You have changed the warp price."),
    REMOVED_ALL("allWarpsRemoved", "You have removed all warps."),
    LIMIT_ABOVE_0("limitAbove0", "Error: Please specify a number above 0."),
    LIMIT_CHANGED("changedLimit", "You have changed the warp limit."),
    SET_UNSAFE("setUnsafe", "Error: You can't set warps or warp in lava or air."),
    IS_UNSAFE("isUnsafe", "Error: This warp is unsafe."),
    IS_OBSTRUCTED("isObstructed", "Error: This warp is obstructed."),
    LORE_LIMIT("loreLimit", "Error: You can only set 3 lores."),
    UPDATED_LORE("updatedLore", "You have changed your warp lore."),
    MOVED_WARP("movedWarp", "Your warp has been moved."),
//    REMOVED_INACTIVE("removedInactive", "All warps that have not been visited or where players haven't logged on in PINACTIVEP days have been removed."),
    PLAYER_NOT_TRUSTED("playerNotTrusted", "Error: This player is not trusted to your warp."),
    PLAYER_ALREADY_TRUSTED("playerAlreadyTrusted", "Error: This player has already been trusted to your warp."),
    PLAYER_TRUSTED("playerTrusted", "You have trusted PPLAYERP to your warp."),
    PLAYER_UNTRUSTED("playerUntrusted", "You have untrusted PPLAYERP from your warp."),
    CANT_AFFORD_BOTH("cantAffordBoth", "Error: You can't afford a new warp. A new warp costs PMONEYP and PITEMAMOUNTP PITEMP."),
    CHANGED_WARP_ICON("changedIcon", "Your warp icon has been changed."),
    CANT_AFFORD_ITEM("cantAffordItem", "Error: You can't afford a new warp. A new warp costs PITEMAMOUNTP PITEMP."),
    SERVER_NAME("serverName", "PlayerWarps"),
    MOVED("moved", "Error: You moved."),
    DONT_MOVE("dontMove", "You will be teleported in PSECONDSP seconds. Please do not move."),
    SET_DELAY("setDelay", "You have changed the delay."),
    BLACKLISTED_WORLD("blacklistedWorld", "Error: you can't set a warp in this world."),
    WORLD_NOT_BLACKLISTED("worldNotBlacklisted", "Error: PWORLDP is not blacklisted."),
    ADDED_BLACKLIST("addedBlacklist", "Added PWORLDP to the blacklist."),
    REMOVED_BLACKLIST("removedBlacklist", "Removed PWORLDP from the blacklist."),
    CHANGED_SEPARATOR("changedSeparator", "You have successfully changed the separator item."),
    NO_ACCESS_GP("noAccessGriefprevention", "Error: you don't have access to this claim."),
    W2WTELEPORT("noWorldToWorldTeleport", "Error: you can't teleport to another world."),
    OWNED_WARPS("ownedWarps", "[PPLAYERP's warps]"),
    NO_OWNED_WARPS("noOwnedWarps", "Error: you don't own any warps."),
    NO_WORLDS_BLACKLISTED("noWorldsBlacklisted", "Error: there are no worlds blacklisted."),
    ENABLED_W2W("enabledWorldToWorldTeleport", "You successfully enabled world to world teleport."),
    DISABLED_W2W("disabledWorldToWorldTeleport", "You successfully disabled world to world teleport."),
    NO_COMMANDS_ALLOWED("noCommandsAllowed", "Error: you can't use any PWarp commands whilst teleporting."),
    ALREADY_BLACKLISTED("alreadyBlacklisted", "Error: this world is already blacklisted."),
    WORLD_NOT_EXISTING("worldNotExisting", "Error: this world doesn't exist."),
    TRUE_OR_FALSE("trueOrFalse", "Error: please use true or false."),
    HIDDEN_UNHIDDEN("hiddenUnhidden", "You have successfully hidden/unhidden your warp."),
    RENAMED_WARP("renamedWarp", "Your warp has been renamed."),
    HELP_SETITEMPRICE("helpSetItemPrice", "Sets the item cost of a warp."),
    HELP_SETPRICE("helpSetPrice", "Sets the money cost for a warp."),
    HELP_GUIITEM("helpGuiItem", "Sets the top item in the GUI."),
    HELP_DELETEALL("helpDeleteAll", "Deletes all existing warps."),
    HELP_SETDELAY("helpSetDelay", "Sets the teleport delay."),
    HELP_SETLIMIT("helpSetLimit", "Sets the default warp limit."),
//    HELP_CLEAROLDWARPS("helpClearOldWarps", "Removes all warps which haven't been visited for over PINACTIVEDAYSP days."),
    HELP_SET("helpSet", "Creates a new warp."),
    HELP_DELETE("helpDelete", "Deletes your warp."),
    HELP_WARP("helpWarp", "Teleports you to a warp."),
    HELP_SETSEPARATOR("helpSetSeparator", "Changes the top and bottom items in the GUI."),
    HELP_PWG("helpPwg", "Opens the GUI."),
    HELP_SETLORE("helpSetLore", "Sets your warp lore."),
    HELP_MOVEWARP("helpMoveWarp", "Moves your warp to your current position."),
    HELP_SETPUBLIC("helpSetPublic", "Makes your warp public."),
    HELP_SETPRIVATE("helpSetPrivate", "Makes your warp private."),
    HELP_TRUST("helpTrust", "Trusts a player to your warp."),
    HELP_UNTRUST("helpUntrust", "Untrusts a player from your warp."),
    HELP_SETITEM("helpSetItem", "Changes your warpicon in the GUI."),
    HELP_BLACKLISTADDREMOVE("helpBlacklistAddRemove", "Blacklists a world."),
    HELP_BLACKLISTLIST("helpBlacklistList", "Lists blacklisted worlds."),
    HELP_LISTOWN("helpListOwn", "Lists your warps."),
    HELP_HOOKS("helpHooks", "Lists hooked plugins."),
    HELP_W2W("helpW2w", "Enables/disables world to world teleporting."),
    HELP_LISTOTHER("helpListOther", "Lists another player's owned warps."),
    HELP_SETHIDDEN("helpSetHidden", "Hides your warp in the GUI."),
    HELP_INFO("helpInfo", "Displays general info."),
    HELP_RENAME("helpRename", "Renames your warp."),
    HELP_WARPSAFETY("helpWarpSafety", "Enables/disables the global warp safety."),
    WARP_SAFETY_UPDATED("warpSafetyUpdated", "The warp safety has been updated."),
    UPDATED_DEFAULT_PRIVACY("updatedDefaultPrivacy", "You have successfully changed the default warp privacy."),
    HELP_UPDATE_PRIVACY("helpUpatePrivate", "Changes the default warp privacy"),;

    private final String msg;
    private final String path;

    /**
     * Constructor for the message ennum.
     * @param path String containing path
     * @param msg String containing message
     */
    MessagePathAndDefault(String path, String msg) {
        this.msg = msg;
        this.path = path;
    }

    /**
     * Getter for the message path.
     * @return String containing path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Getter for the default message.
     * @return String containing default message
     */
    public String getDefaultMessage() {
        return this.msg;
    }
}
