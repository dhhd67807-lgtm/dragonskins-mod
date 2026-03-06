package gg.dragonskins.mixin;

import gg.dragonskins.NametagRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(
        method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at = @At("HEAD")
    )
    private void onRenderLabel(
        AbstractClientPlayerEntity player,
        Text text,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light,
        CallbackInfo ci
    ) {
        NametagRenderer.renderCustomNametag(
            matrices,
            vertexConsumers,
            player.getUuid(),
            text,
            ((PlayerEntityRenderer)(Object)this).getTextRenderer(),
            light
        );
    }
}
