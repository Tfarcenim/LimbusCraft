package tfar.limbuscraft.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;
import tfar.limbuscraft.LimbusCraft;

public interface LimbusDamageTypes {
    ResourceKey<DamageType> LUST = create("lust");
    ResourceKey<DamageType> PRIDE = create("pride");
    ResourceKey<DamageType> SLOTH = create("sloth");
    ResourceKey<DamageType> WRATH = create("wrath");
    ResourceKey<DamageType> GLUTTONY = create("gluttony");
    ResourceKey<DamageType> GLOOM = create("gloom");
    ResourceKey<DamageType> ENVY = create("envy");

    static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, LimbusCraft.id(name));
    }

    static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(GLUTTONY, new DamageType("gluttony", 0, DamageEffects.HURT));
        context.register(WRATH, new DamageType("burn", 0, DamageEffects.BURNING));
        context.register(LUST, new DamageType("lust", 0, DamageEffects.HURT));
        context.register(PRIDE, new DamageType("pride", 0, DamageEffects.HURT));
        context.register(SLOTH, new DamageType("sloth", 0, DamageEffects.HURT));
        context.register(GLOOM, new DamageType("gloom", 0, DamageEffects.HURT));
        context.register(ENVY, new DamageType("envy", 0, DamageEffects.HURT));
    }
}
