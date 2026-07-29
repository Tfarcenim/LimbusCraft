package tfar.limbuscraft.tokens;

import net.minecraft.world.entity.LivingEntity;

public class ChargeBoon extends Boon{
    public ChargeBoon() {
        super("charge");
    }

    @Override
    public boolean shouldTick(LivingEntity entity, long combatTimer) {
        return combatTimer % 120 == 0;
    }

    @Override
    public void tick(LivingEntity entity, TokenInstance tokenInstance) {
        tokenInstance.decrementOrRemoveToken(entity);
    }
}
