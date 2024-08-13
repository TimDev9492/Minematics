package me.timwastaken.minematics.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ItemActionRegistry {
    private static final Map<ItemMetadata, Consumer<Player>> itemActions = new HashMap<>();

    public static void insertAction(Material mat, String itemName, Consumer<Player> action) {
        itemActions.put(new ItemMetadata(mat, itemName), action);
    }

    public static Consumer<Player> getActionFor(ItemStack stack) {
        return itemActions.entrySet().stream()
                .filter(entry -> entry.getKey().equals(stack))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    public static Collection<ItemStack> getTriggerItems() {
        return itemActions.keySet().stream()
                .map(ItemMetadata::createItemStack)
                .toList();
    }
}

record ItemMetadata(Material mat, String itemName) {
    ItemStack createItemStack() {
        ItemStack stack = new ItemStack(mat);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(itemName);
        meta.setRarity(ItemRarity.EPIC);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof ItemStack stack) return equals(stack);
        if (obj instanceof ItemMetadata metadata) {
            return mat == metadata.mat && itemName.equals(metadata.itemName);
        }
        return false;
    }

    public boolean equals(ItemStack stack) {
        return stack.getType() == mat && stack.getItemMeta().getDisplayName().equals(itemName);
    }
}
