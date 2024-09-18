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

@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMixin {
    @Redirect(method="onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt;"), require = 0)
    private OptionalInt showEnderChestHandledScreen(PlayerEntity playerEntity, NamedScreenHandlerFactory screenHandlerFactory) {
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
