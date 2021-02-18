package me.tks.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemUtils {

    /**
     * Gets the enchantments from an item in a formatted String
     * @param i ItemStack
     * @return Formatted string
     */
    private static String getEnchants(ItemStack i){
        List<String> e = new ArrayList<>();
        Map<Enchantment, Integer> en = i.getEnchantments();
        for(Enchantment t : en.keySet()) {
            e.add(t.getName() + ":" + en.get(t));
        }
        return StringUtils.join(e, ",");
    }

    /**
     * Converts an item to a text format
     * @param i ItemStack to convert
     * @return formatted string
     */
    public static String serialize(ItemStack i){
        String[] parts = new String[6];
        parts[0] = i.getType().name();
        parts[1] = Integer.toString(i.getAmount());
        parts[2] = String.valueOf(i.getDurability());
        parts[3] = i.getItemMeta().getDisplayName();
        parts[4] = String.valueOf(i.getData().getData());

//        parts[5] = getEnchants(i);
        return StringUtils.join(parts, ";");
    }

    /**
     * Deserializes an item saved in the correct format (without enchants)
     * @param p string to deserialize
     * @return new ItemStack
     */
    public static ItemStack deserialize(String p){
        String[] a = p.split(";");
        ItemStack i = new ItemStack(Material.getMaterial(a[0]), Integer.parseInt(a[1]));
        i.setDurability((short) Integer.parseInt(a[2]));
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(a[3]);
        i.setItemMeta(meta);

        MaterialData data = i.getData();
        try {
            data.setData((byte) Integer.parseInt(a[4]));
            i.setData(data);
        }
        catch (Exception e) {
            // Nothing happens here
        }
//        if (a.length > 5) {
//            String[] parts = a[5].split(",");
//            for (String s : parts) {
//                String label = s.split(":")[0];
//                String amplifier = s.split(":")[1];
//                Enchantment type = Enchantment.getByName(label);
//                if (type == null)
//                    continue;
//                int f;
//                try {
//                    f = Integer.parseInt(amplifier);
//                } catch(Exception ex) {
//                    continue;
//                }
//                i.addEnchantment(type, f);
//            }
//        }
        return i;
    }
}
