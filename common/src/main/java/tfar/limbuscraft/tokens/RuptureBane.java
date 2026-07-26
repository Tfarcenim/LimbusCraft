package tfar.limbuscraft.tokens;

import net.minecraft.world.entity.LivingEntity;
import tfar.limbuscraft.LimbusStats;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.ducks.LivingEntityDuck;
import tfar.limbuscraft.init.LimbusDamageTypes;
import tfar.limbuscraft.world.LimbusCombatTracker;

public class RuptureBane extends Bane{
    public RuptureBane() {
        super("rupture");
    }

    @Override
    public boolean shouldTick(LivingEntity entity, long combatTimer) {
        LimbusCombatTracker limbusCombatTracker = ((LivingEntityDuck)entity).getLimbusCombatTracker();
        long timeSinceLastHit = limbusCombatTracker.getTimeSinceLastPhysicalHit();
        if (timeSinceLastHit < LimbusStats.DEFAULT_RUPTURE_TIMER) {
            return false;
        }
        return timeSinceLastHit % 10 == 0;
    }

    @Override
    public void tick(LivingEntity entity, TokenInstance tokenInstance) {
        trigger(entity,tokenInstance);
    }

    @Override
    public void trigger(LivingEntity livingEntity, TokenInstance tokenInstance) {
        livingEntity.hurt(livingEntity.damageSources().source(LimbusDamageTypes.GLUTTONY),tokenInstance.potency());
        //todo apply rupture stacks BEFORE this point
        tokenInstance.decrementOrRemoveToken(livingEntity);
    }
}
