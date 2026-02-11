package net.mctitan.rpg.packethandlers.hidestackdata;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class StripMetaData {
    public static void stripdata(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer persistentData = meta.getPersistentDataContainer();
        for(NamespacedKey key : persistentData.getKeys()) {
            persistentData.remove(key);
        }
        stack.setItemMeta(meta);
    }
}
