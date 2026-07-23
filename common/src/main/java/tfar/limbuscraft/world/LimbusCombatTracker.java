package tfar.limbuscraft.world;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.ducks.LivingEntityDuck;
import tfar.limbuscraft.tags.LimbusDamageTypeTags;

public class LimbusCombatTracker {

    public static final Logger LOG = LoggerFactory.getLogger("Limbus Combat Tracker");

    public final LivingEntity mob;
    public boolean inCombat;
    public long combatStartTime;
    public long lastAction;

    public static final long TIME_LIMIT = 600;

    public LimbusCombatTracker(LivingEntity mob) {
        this.mob = mob;
    }

    public void tick() {
        if (inCombat) {
            long levelTime = mob.level().getGameTime();
            if (levelTime - lastAction > TIME_LIMIT) {
                stopCombat();
            }
        }
        int staggerTimer = DataAttachmentUtil.getStaggerTimer(mob);
        if (staggerTimer > 0) {
            staggerTimer--;
            DataAttachmentUtil.setStaggerTimer(mob, staggerTimer);
            if (staggerTimer == 0) {
                DataAttachmentUtil.setStaggered(mob, 0);
            }
        }
    }

    public long getDuration() {
        if (inCombat) {
            return mob.level().getGameTime() - combatStartTime;
        } else {
            return -1;
        }
    }

    private void onHitOther(LivingEntity other) {
        if (!inCombat) {
            startCombat();
        } else {
            updateCombat();
        }
    }

    public void onHit(DamageSource source) {
        if (triggersCombat(source)) {
            if (!inCombat) {
                startCombat();
            } else {
                updateCombat();
            }
            if (source.getEntity() instanceof LivingEntity livingEntity) {
                ((LivingEntityDuck)livingEntity).getLimbusCombatTracker().onHitOther(mob);
            }
        }
    }

    public static boolean triggersCombat(DamageSource source) {
        return source.is(LimbusDamageTypeTags.LIMBUS) || source.getEntity() instanceof LivingEntity;
    }

    public void startCombat() {
        inCombat = true;
        combatStartTime = mob.level().getGameTime();
        lastAction = mob.level().getGameTime();
        if (LimbusCraft.DEBUG) {
            LOG.info("Combat started for {}", mob);
            if (mob instanceof Player player) {
                player.sendSystemMessage(Component.literal("You have started combat"));
            }
        }
            LimbusCraft.computeStaggerThresholds(mob);
    }

    public void updateCombat(){
        lastAction = mob.level().getGameTime();
    }

    public void stopCombat() {
        inCombat = false;
        combatStartTime = -1;
        lastAction = -1;
        LOG.info("Combat stopped for {}", mob);
        if (mob instanceof Player player) {
            player.sendSystemMessage(Component.literal("You have left combat"));
        }
    }
}
