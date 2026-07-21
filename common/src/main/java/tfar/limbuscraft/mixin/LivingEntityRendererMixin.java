package tfar.limbuscraft.mixin;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.client.LimbusCraftClient;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "getOverlayCoords",at = @At("HEAD"),cancellable = true)
    private static void onGetOverlayCoords(LivingEntity entity,float u,CallbackInfoReturnable<Integer> cir){
        if (DataAttachmentUtil.getStaggered(entity) > 0) {
            cir.setReturnValue(LimbusCraftClient.brownOverlay(u));
        }
    }
}
