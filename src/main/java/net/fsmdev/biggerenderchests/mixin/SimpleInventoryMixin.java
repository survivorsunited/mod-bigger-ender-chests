package net.fsmdev.biggerenderchests.mixin;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessor mixin for SimpleInventory to allow modification of private fields.
 * This provides setters for the size and item stack list, which are normally private.
 * EnderChestInventory extends SimpleInventory, so this allows us to resize ender chests.
 */
@Mixin(SimpleInventory.class)
public interface SimpleInventoryMixin {
    /**
     * Accessor to set the size field of SimpleInventory.
     * This allows changing the inventory capacity at runtime.
     * 
     * @param size The new size for the inventory
     */
    @Accessor("size")
    @Mutable 
    void setSize(int size);

    /**
     * Accessor to set the heldStacks field of SimpleInventory.
     * This allows replacing the item stack list with a larger one.
     * 
     * @param itemStacks The new DefaultedList of ItemStacks
     */
    @Accessor("heldStacks")
    @Mutable 
    void setStacks(DefaultedList<ItemStack> itemStacks);
}
