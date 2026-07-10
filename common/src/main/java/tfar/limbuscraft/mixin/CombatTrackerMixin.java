package tfar.limbuscraft.mixin;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.tags.LimbusDamageTypeTags;

@Mixin(CombatTracker.class)
public class CombatTrackerMixin {
    @Inject(method = "recordDamage",at = @At("TAIL"))
    private void onRecordDamage(DamageSource source, float amount, CallbackInfo ci){
        LimbusCraft.onRecordDamage((CombatTracker)(Object)this,source);
    }

    @Inject(method = "shouldEnterCombat",at = @At("RETURN"),cancellable = true)
    private static void onShouldEnterCombat(DamageSource source, CallbackInfoReturnable<Boolean> cir){
        boolean b = cir.getReturnValue();
        if (!b && source.is(LimbusDamageTypeTags.LIMBUS)) {
            cir.setReturnValue(true);
        }
    }
}
