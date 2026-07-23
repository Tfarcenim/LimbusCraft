package tfar.limbuscraft.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import tfar.limbuscraft.ducks.LivingEntityDuck;
import tfar.limbuscraft.world.LimbusCombatTracker;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements LivingEntityDuck {

    @Unique
    protected LimbusCombatTracker limbusCombatTracker =  new LimbusCombatTracker((LivingEntity) (Object)this);

    @Override
    public LimbusCombatTracker getLimbusCombatTracker() {
        return limbusCombatTracker;
    }

}
