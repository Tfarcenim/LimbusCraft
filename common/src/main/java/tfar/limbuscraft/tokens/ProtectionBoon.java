package tfar.limbuscraft.tokens;

import net.minecraft.world.entity.LivingEntity;

public class ProtectionBoon extends Boon{
    public ProtectionBoon() {
        super("protection");
    }

    @Override
    public boolean shouldTick(LivingEntity entity, long combatTimer) {
        return combatTimer % 10 == 0;
    }

    @Override
    public void tick(LivingEntity entity, TokenInstance tokenInstance) {
        tokenInstance.removeToken(entity);
    }
}
