package net.fsmdev.biggerenderchests.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for PlayerEntity to resize the ender chest inventory from 27 slots to 54 slots.
 * This mixin intercepts the constructor to modify the inventory size after initialization.
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    /**
     * Shadow method to access the player's ender chest inventory.
     * @return The ender chest inventory instance
     */
    @Shadow 
    public abstract EnderChestInventory getEnderChestInventory();

    /**
     * Injects at the return of any PlayerEntity constructor to resize the ender chest.
     * By not specifying constructor parameters, this works across Minecraft versions.
     * 
     * @param ci Callback info provided by Mixin framework
     */
    @Inject(method="<init>", at = @At(value="RETURN"))
    private void resizableEnderChest(final CallbackInfo ci) {
        // Cast the ender chest inventory to access size-setting methods via our accessor mixin
        final SimpleInventoryMixin enderChestInventory = (SimpleInventoryMixin) this.getEnderChestInventory();
        
        // Resize from 27 (3x9) to 54 (6x9) slots
        enderChestInventory.setSize(54);
        enderChestInventory.setStacks(DefaultedList.ofSize(54, ItemStack.EMPTY));
    }
}