package tfar.limbuscraft.tokens;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.init.LimbusDamageTypes;

public class BleedBane extends Bane{
    public BleedBane() {
        super("bleed");
    }


    @Override
    public boolean shouldTick(LivingEntity entity, long combatTimer) {
        return combatTimer % 160 == 0;
    }

    @Override
    public void tick(LivingEntity entity, TokenInstance tokenInstance) {
        trigger(entity, tokenInstance);
    }

    @Override
    public void trigger(LivingEntity livingEntity, TokenInstance tokenInstance) {
        int potency = tokenInstance.potency();
        DamageSource source = livingEntity.damageSources().source(LimbusDamageTypes.LUST);
        livingEntity.hurt(source,potency);
        TokenInstance reducedCount = tokenInstance.increaseCount(-1);
        if (reducedCount.count() >0) {
            DataAttachmentUtil.addOrReplaceToken(livingEntity,reducedCount);
        } else {
            DataAttachmentUtil.removeToken(livingEntity,this);
        }
    }
}
