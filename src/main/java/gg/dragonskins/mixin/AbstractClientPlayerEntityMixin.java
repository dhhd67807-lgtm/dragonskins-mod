package gg.dragonskins.mixin;

import com.mojang.authlib.GameProfile;
import gg.dragonskins.CapeLoader;
import gg.dragonskins.SkinLoader;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
    
    @Shadow
    public abstract GameProfile getGameProfile();

    @Inject(method = "getSkinTextures", at = @At("HEAD"), cancellable = true, require = 0)
    private void onGetSkinTexture(CallbackInfoReturnable<Identifier> cir) {
        try {
            Identifier customSkin = SkinLoader.loadSkin(getGameProfile());
            if (customSkin != null) {
                cir.setReturnValue(customSkin);
            }
        } catch (Exception e) {
            // Silently fail and use default skin
        }
    }

    @Inject(method = "getCapeTexture", at = @At("HEAD"), cancellable = true, require = 0)
    private void onGetCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        try {
            Identifier customCape = CapeLoader.loadCape(getGameProfile());
            if (customCape != null) {
                cir.setReturnValue(customCape);
            }
        } catch (Exception e) {
            // Silently fail and use default cape
        }
    }
}
