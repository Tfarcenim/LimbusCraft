package tfar.limbuscraft.tokens;

import net.minecraft.world.entity.LivingEntity;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.LimbusStats;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.init.LimbusDamageTypes;

public class SinkingBane extends Bane {
    public SinkingBane() {
        super("sinking");
    }


    //When an enemy has SP, sinking will reduce their SP by the potency whenever they take direct damage
    // (the trigger is the same as rupture.) The minimum SP a unit can have is -45. SP affects coinflips,
    // which we’ll touch on later. When an enemy has the minimum sanity of -45,
    // any additional sinking that affects them will deal gloom damage instead.
    //
    //When an enemy does not have SP, sinking will affect them the same way as Rupture,
    // except for the fact that it is affected by damage reductions.
    // Most Abnormalities and some Distortions do not have Sanity, but don’t worry about them right now.
    public void trigger(LivingEntity livingEntity, TokenInstance tokenInstance) {
        boolean hasSanity = LimbusCraft.hasSanity(livingEntity) && DataAttachmentUtil.getSanity(livingEntity) > LimbusStats.MIN_SANITY;

        if (hasSanity) {
            DataAttachmentUtil.addSanity(livingEntity,-tokenInstance.potency());
        } else {
            livingEntity.hurt(livingEntity.damageSources().source(LimbusDamageTypes.GLOOM), tokenInstance.potency());
        }
        //todo apply rupture stacks BEFORE this point
        tokenInstance.decrementOrRemoveToken(livingEntity);
    }
}
