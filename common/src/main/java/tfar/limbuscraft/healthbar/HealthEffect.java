package tfar.limbuscraft.healthbar;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public enum HealthEffect {
    NONE(16),POISON(52),WITHER(88),FROZEN(88 + 54);

    public final int i;

    HealthEffect(int i){
        this.i = i;
    }

    public static HealthEffect getHealthEffect(Player player) {
        HealthEffect effect = NONE;//16
        if (player.hasEffect(MobEffects.POISON)) effect = POISON;//evaluates to 52
        else if (player.hasEffect(MobEffects.WITHER)) effect = WITHER;//evaluates to 88
        else if (player.isFullyFrozen()) effect = FROZEN;
        return effect;
    }
}
