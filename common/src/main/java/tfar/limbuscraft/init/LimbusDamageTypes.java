package tfar.limbuscraft.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;
import tfar.limbuscraft.LimbusCraft;

public interface LimbusDamageTypes {
    ResourceKey<DamageType> BURN = create("burn");

    static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, LimbusCraft.id(name));
    }

    static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(BURN, new DamageType("burn", 0.1F, DamageEffects.BURNING));
    }
}
