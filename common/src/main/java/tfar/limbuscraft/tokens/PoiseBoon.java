package tfar.limbuscraft.tokens;

import net.minecraft.world.entity.LivingEntity;

public class PoiseBoon extends Boon {
    public PoiseBoon() {
        super("poise");
    }

    @Override
    public void trigger(LivingEntity livingEntity, TokenInstance tokenInstance) {
        tokenInstance.decrementOrRemoveToken(livingEntity);
    }
}
