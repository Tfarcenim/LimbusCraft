package tfar.limbuscraft.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.limbuscraft.attachments.DataAttachmentUtil;

@Mixin(Mob.class)
public class MobMixin {
    @Inject(method = "serverAiStep",at = @At("HEAD"),cancellable = true)
    private void onServerAiStep(CallbackInfo ci) {
        int stagger = DataAttachmentUtil.getStaggered((Mob)(Object)this);
        if (stagger > 0) {
            ci.cancel();
        }
    }
}
