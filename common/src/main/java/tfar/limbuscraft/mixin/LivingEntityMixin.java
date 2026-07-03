package tfar.limbuscraft.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.limbuscraft.LimbusCraft;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "onLeaveCombat",at = @At("RETURN"))
    private void onLeaveCombat(CallbackInfo ci){
        LimbusCraft.onLeaveCombat((LivingEntity)(Object)this);
    }

    @Inject(method = "onEnterCombat",at = @At("RETURN"))
    private void onEnterCombat(CallbackInfo ci){
        LimbusCraft.onEnterCombat((LivingEntity)(Object)this);
    }
}
