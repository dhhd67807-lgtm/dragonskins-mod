package gg.dragonskins.mixin;

import com.mojang.authlib.GameProfile;
import gg.dragonskins.SkinLoader;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {

    @Shadow
    public abstract GameProfile getProfile();

    @Inject(method = "getSkinTextures", at = @At("HEAD"), cancellable = true, require = 0)
    private void onGetSkinTexture(CallbackInfoReturnable<Identifier> cir) {
        try {
            Identifier customSkin = SkinLoader.loadSkin(getProfile());
            if (customSkin != null) {
                cir.setReturnValue(customSkin);
            }
        } catch (Exception e) {
            // Silently fail
        }
    }
}
