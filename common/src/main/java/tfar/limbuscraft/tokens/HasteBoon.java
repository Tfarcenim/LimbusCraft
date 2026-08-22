package tfar.limbuscraft.tokens;

import net.minecraft.world.entity.LivingEntity;

public class HasteBoon extends Boon{
    public HasteBoon() {
        super("haste");
    }

    @Override
    public boolean shouldTick(LivingEntity entity, long combatTimer) {
        return combatTimer % 200 == 0;
    }

    @Override
    public void tick(LivingEntity entity, TokenInstance tokenInstance) {
        tokenInstance.removeToken(entity);
    }
}
