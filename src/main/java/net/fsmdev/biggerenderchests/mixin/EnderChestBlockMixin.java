package net.fsmdev.biggerenderchests.mixin;

import net.minecraft.block.EnderChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.OptionalInt;

/**
 * Mixin for EnderChestBlock to change the screen handler from 3 rows to 6 rows.
 * This intercepts the screen opening call to use a 9x6 container instead of the default 9x3.
 */
@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMixin {
    /**
     * Redirects the screen handler opening to use a 6-row container instead of 3 rows.
     * This is called when a player right-clicks an ender chest block.
     * Note: require = 0 makes this optional for compatibility, though it may fail silently
     * 
     * @param playerEntity The player opening the ender chest
     * @param screenHandlerFactory The original screen handler factory (unused)
     * @return OptionalInt containing the screen handler sync ID
     */
    @Redirect(method="onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt;"), require = 0)
    private OptionalInt showEnderChestHandledScreen(final PlayerEntity playerEntity, final NamedScreenHandlerFactory screenHandlerFactory) {
        // Create a new screen handler factory that creates a 9x6 (54 slot) container
        // instead of the default 9x3 (27 slot) container
        return playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                (syncId, playerInventory, playerEntityInner) ->
                        GenericContainerScreenHandler.createGeneric9x6(
                                syncId,
                                playerInventory,
                                playerEntityInner.getEnderChestInventory()
                        ),
                Text.translatable("container.enderchest")
        ));
    }
}
