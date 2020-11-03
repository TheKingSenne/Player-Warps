
//public class GUI implements Listener {
//    private static ItemStack pane;
//    private static final ItemStack backButton;
//    private static final ItemStack nextButton;
//    public static List<Inventory> guis = new ArrayList<>();
//    private static List<String> warpsSorted;
//    private static FileConfiguration wC;
//
////    public void setUpGui(final PWarpPlugin p) {
////        if (p.getConfig().get("guiRefreshRateInMinutes") == null) {
////            p.getConfig().set("guiRefreshRateInMinutes", 30);
////            p.saveConfig();
////        }
////        GUI.wC = p.wF.getWarpFile();
////        this.sortWarps();
////        this.getInventories(p);
////        Bukkit.getScheduler().scheduleSyncDelayedTask(p, () -> GUI.this.setUpGui(p), (1200 * p.getConfig().getInt("guiRefreshRateInMinutes")));
////    }
//
//    private void sortWarps() {
//        if (GUI.wC.getStringList("warpList").isEmpty()) {
//            return;
//        }
//        final HashMap<Integer, List<String>> warpsWithVisitors = new HashMap<>();
//        final List<String> warps = GUI.wC.getStringList("warpList");
//        final List<String> sortedWarps = new ArrayList<>();
//        for (final String warpName : warps) {
//            if (GUI.wC.getString("warps." + warpName + ".isHidden") == null) {
//                GUI.wC.set("warps." + warpName + ".isHidden", false);
//            }
//            if (!GUI.wC.getBoolean("warps." + warpName + ".isHidden") && GUI.wC.getString("warps." + warpName + ".owner-UUID") != null) {
//                if (!warpsWithVisitors.containsKey(GUI.wC.getInt("warps." + warpName + ".visitorCount"))) {
//                    warpsWithVisitors.put(GUI.wC.getInt("warps." + warpName + ".visitorCount"), new ArrayList<>());
//                }
//                final List<String> addedWarps = warpsWithVisitors.get(GUI.wC.getInt("warps." + warpName + ".visitorCount"));
//                addedWarps.add(warpName);
//                warpsWithVisitors.put(GUI.wC.getInt("warps." + warpName + ".visitorCount"), addedWarps);
//            }
//        }
//        final List<Integer> visitors = new ArrayList<>(warpsWithVisitors.keySet());
//        visitors.sort(Collections.reverseOrder());
//        for (final int visitorAmount : visitors) {
//            for (final String warp : warpsWithVisitors.get(visitorAmount)) {
//                sortedWarps.add(warp);
//            }
//        }
//        if (sortedWarps.isEmpty()) {
//            return;
//        }
//        GUI.warpsSorted = sortedWarps;
//    }
//
////    public void getInventories(final PWarpPlugin p) {
////        final List<String> warps = GUI.warpsSorted;
////        final ArrayList<Inventory> inventories = new ArrayList<>();
////        final int invAmount = (int)Math.ceil(GUI.wC.getStringList("warpList").size() / 36);
////        final ItemStack guiPage = new ItemStack(Material.PAPER, 1);
////        final ItemMeta pageMeta = Bukkit.getItemFactory().getItemMeta(Material.PAPER);
////        for (int a = 0; a <= invAmount; ++a) {
////            final Inventory inv = this.basicGui();
////            for (int i = 0; i < 36 && i < warps.size(); ++i) {
////                final List<String> lore = new ArrayList<>();
////                if (i + a * 36 < warps.size() && GUI.wC.getString("warps." + warps.get(i + a * 36) + ".owner-UUID") != null && GUI.wC.get("warps." + warps.get(i + a * 36) + ".location.world") != null) {
////                    ItemStack warpIcon;
////                    ItemMeta m;
////                    if (GUI.wC.get("warps." + warps.get(i + a * 36) + ".item") == null || GUI.wC.get("warps." + warps.get(i + a * 36) + ".item.type") == "AIR") {
////                        warpIcon = new ItemStack(Material.CONDUIT);
////                        m = Bukkit.getItemFactory().getItemMeta(Material.CONDUIT);
////                        assert m != null;
////                        m.setDisplayName(ChatColor.YELLOW + warps.get(i + a * 36).substring(0, 1).toUpperCase() + warps.get(i + a * 36).substring(1));
////                    }
////                    else {
////                        warpIcon = GUI.wC.getItemStack("warps." + warps.get(i + a * 36) + ".item");
////                        assert warpIcon != null;
////                        m = Bukkit.getItemFactory().getItemMeta(warpIcon.getType());
////                        assert m != null;
////                        m.setDisplayName(ChatColor.YELLOW + warps.get(i + a * 36).substring(0, 1).toUpperCase() + warps.get(i + a * 36).substring(1));
////                    }
////                    lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-------------------");
////                    if (!GUI.wC.getStringList("warps." + warps.get(i + a * 36) + ".lore").isEmpty()) {
////                        final List<String> customLore = GUI.wC.getStringList("warps." + warps.get(i + a * 36) + ".lore");
////                        lore.add(ChatColor.AQUA + customLore.get(0).replace('&', '§'));
////                        lore.add(ChatColor.AQUA + customLore.get(1).replace('&', '§'));
////                        lore.add(ChatColor.AQUA + customLore.get(2).replace('&', '§'));
////                        lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-------------------");
////                    }
////                    lore.add(ChatColor.AQUA + Messages.GUI_WARP_OWNER.getMessage() + " " + Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(GUI.wC.getString("warps." + warps.get(i + a * 36) + ".owner-UUID")))).getName());
////                    lore.add(ChatColor.AQUA + Messages.GUI_VISITORS.getMessage() + " " + GUI.wC.getInt("warps." + warps.get(i + a * 36) + ".visitorCount"));
////                    lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-------------------");
////                    m.setLore(lore);
////                    warpIcon.setItemMeta(m);
////                    inv.addItem(warpIcon);
////                }
////            }
////            assert pageMeta != null;
////            pageMeta.setDisplayName(ChatColor.AQUA + Messages.GUI_PAGE.getMessage() + (a + 1));
////            guiPage.setItemMeta(pageMeta);
////            inv.setItem(49, guiPage);
////            inventories.add(inv);
////        }
////        closeInventoriesForViewers(guis);
////        GUI.guis = inventories;
////    }
//
//    private void closeInventoriesForViewers(List<Inventory> inventories) {
//        // fixes dupe glitch
//        List<HumanEntity> viewers = new ArrayList<>();
//        inventories.forEach(i -> viewers.addAll(i.getViewers()));
//        viewers.forEach(HumanEntity::closeInventory);
//        viewers.clear();
//    }
//
//    public void changeMaterial(String name, final Material material) {
//        name = name.toLowerCase();
//        for (final Inventory inv : GUI.guis) {
//            int i = 9;
//            while (i <= 44) {
//                if (inv.getItem(i) != null && Objects.requireNonNull(inv.getItem(i)).hasItemMeta() && Objects.requireNonNull(Objects.requireNonNull(inv.getItem(i)).getItemMeta()).getDisplayName().equals(ChatColor.YELLOW + name.substring(0, 1).toUpperCase() + name.substring(1))) {
//                    final ItemStack warpIcon = inv.getItem(i);
//                    assert warpIcon != null;
//                    warpIcon.setType(material);
//                    inv.setItem(i, warpIcon);
//                    break;
//                }
//                else {
//                    ++i;
//                }
//            }
//        }
//    }
//
////    public void addItem(String name, final PWarpPlugin p) {
////        name = name.toLowerCase();
////        final List<String> lore = new ArrayList<>();
////        final ItemStack warpIcon = new ItemStack(Material.CONDUIT);
////        final ItemMeta m = Bukkit.getItemFactory().getItemMeta(Material.CONDUIT);
////        assert m != null;
////        m.setDisplayName(ChatColor.YELLOW + name.substring(0, 1).toUpperCase() + name.substring(1));
////        lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-------------------");
////        lore.add(ChatColor.AQUA + Messages.GUI_WARP_OWNER.getMessage() + " " + Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(GUI.wC.getString("warps." + name + ".owner-UUID")))).getName());
////        lore.add(ChatColor.AQUA + Messages.GUI_VISITORS.getMessage() + " " + GUI.wC.getInt("warps." + name + ".visitorCount"));
////        lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-------------------");
////        m.setLore(lore);
////        warpIcon.setItemMeta(m);
////        if (GUI.guis.get(GUI.guis.size() - 1).getItem(44) == null) {
////            GUI.guis.get(GUI.guis.size() - 1).addItem(warpIcon);
////        }
////        else {
////            final Inventory inv = this.basicGui();
////            final ItemStack guiPage = new ItemStack(Material.PAPER, 1);
////            final ItemMeta pageMeta = Bukkit.getItemFactory().getItemMeta(Material.PAPER);
////            assert pageMeta != null;
////            pageMeta.setDisplayName(ChatColor.AQUA + Messages.GUI_PAGE.getMessage() + " " + (GUI.guis.size() + 1));
////            guiPage.setItemMeta(pageMeta);
////            inv.setItem(49, guiPage);
////            inv.addItem(warpIcon);
////            GUI.guis.add(inv);
////        }
////    }
//
//    public void delItem(String name) {
//        name = name.toLowerCase();
//        for (final Inventory inv : GUI.guis) {
//            for (int i = 9; i <= 44; ++i) {
//                if (inv.getItem(i) != null && Objects.requireNonNull(inv.getItem(i)).hasItemMeta() && Objects.requireNonNull(Objects.requireNonNull(inv.getItem(i)).getItemMeta()).getDisplayName().equals(ChatColor.YELLOW + name.substring(0, 1).toUpperCase() + name.substring(1))) {
//                    inv.setItem(i, null);
//                    break;
//                }
//            }
//        }
//    }
//
//    public void updateLore(String name, final PWarpPlugin p) {
//        name = name.toLowerCase();
//        for (final Inventory inv : GUI.guis) {
//            int i = 9;
//            while (i <= 44) {
//                if (inv.getItem(i) != null && Objects.requireNonNull(inv.getItem(i)).hasItemMeta() && Objects.requireNonNull(Objects.requireNonNull(inv.getItem(i)).getItemMeta()).getDisplayName().equals(ChatColor.YELLOW + name.substring(0, 1).toUpperCase() + name.substring(1))) {
//                    final ItemStack warpIcon = inv.getItem(i);
//                    assert warpIcon != null;
//                    final ItemMeta m = warpIcon.getItemMeta();
//                    final List<String> lore = new ArrayList<>();
//                    lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-------------------");
//                    if (!GUI.wC.getStringList("warps." + name + ".lore").isEmpty()) {
//                        final List<String> customLore = GUI.wC.getStringList("warps." + name + ".lore");
//                        for (int a = 0; a < customLore.size(); ++a) {
//                            lore.add(ChatColor.AQUA + customLore.get(a).replace('&', '§'));
//                        }
//                        lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-------------------");
//                    }
//                    lore.add(ChatColor.AQUA + Messages.GUI_WARP_OWNER.getMessage() + " " + Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(GUI.wC.getString("warps." + name + ".owner-UUID")))).getName());
//                    lore.add(ChatColor.AQUA + Messages.GUI_VISITORS.getMessage() + " " + GUI.wC.getInt("warps." + name + ".visitorCount"));
//                    lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-------------------");
//                    assert m != null;
//                    m.setLore(lore);
//                    warpIcon.setItemMeta(m);
//                    inv.setItem(i, warpIcon);
//                    break;
//                }
//                else {
//                    ++i;
//                }
//            }
//        }
//    }
//
//    public void setGuiItem(final PWarpPlugin p, final CommandSender sender) {
//        final Player player = (Player)sender;
//        final ItemStack guiItem = player.getInventory().getItemInMainHand();
//        if (guiItem == null) {
//            player.sendMessage(ChatColor.RED + Messages.HOLD_ITEM.getMessage());
//            return;
//        }
//        player.getInventory().setItemInMainHand(guiItem);
//        guiItem.setAmount(1);
//        p.getConfig().set("guiItem", guiItem);
//        p.saveConfig();
//        for (final Inventory gui : GUI.guis) {
//            gui.setItem(4, guiItem);
//        }
//        sender.sendMessage(ChatColor.GREEN + Messages.GUI_ITEM_CHANGED.getMessage());
//    }
//
//    public void setSeparator(final CommandSender sender, final PWarpPlugin p) {
//        final Player player = (Player)sender;
//        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
//            sender.sendMessage(ChatColor.RED + Messages.HOLD_ITEM.getMessage());
//            return;
//        }
//        final ItemStack paneItem = player.getInventory().getItemInMainHand();
//        paneItem.setAmount(1);
//        p.getConfig().set("paneItem", paneItem);
//        p.saveConfig();
//        for (final Inventory gui : GUI.guis) {
//            for (int a = 0; a < 9; ++a) {
//                if (a != 4) {
//                    gui.setItem(a, paneItem);
//                    if (a != 3 && a != 5) {
//                        gui.setItem(a + 45, paneItem);
//                    }
//                }
//            }
//        }
//        sender.sendMessage(ChatColor.GREEN + Messages.CHANGED_SEPARATOR.getMessage());
//    }
//
//
//    static {
//        GUI.pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
//        backButton = new ItemStack(Material.STONE_BUTTON, 1);
//        nextButton = new ItemStack(Material.STONE_BUTTON, 1);
//        GUI.warpsSorted = new ArrayList<>();
//        GUI.wC = new YamlConfiguration();
//    }
//}
