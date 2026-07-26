package tfar.limbuscraft.tokens;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.init.LimbusDamageTypes;

public class BurnBane extends Bane{
    public BurnBane() {
        super("burn");
    }

    @Override
    public boolean shouldTick(LivingEntity entity, long combatTimer) {
        return combatTimer % 80 == 0;
    }

    @Override
    public void tick(LivingEntity entity, TokenInstance tokenInstance) {
        int potency = tokenInstance.potency();

        DamageSource source = entity.damageSources().source(LimbusDamageTypes.WRATH);
        entity.hurt(source,potency);
        TokenInstance reducedCount = tokenInstance.increaseCount(-1);
        if (reducedCount.count() >0) {
            DataAttachmentUtil.addOrReplaceToken(entity,reducedCount);
        } else {
            DataAttachmentUtil.removeToken(entity,this);
        }
    }
}
