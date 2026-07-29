package tfar.limbuscraft.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import tfar.limbuscraft.LimbusCraft;

public class LimbusEntityTypeTags {

    public static final TagKey<EntityType<?>> HAS_SANITY = create("has_sanity");

    private static TagKey<EntityType<?>> create(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, LimbusCraft.id(name));
    }
}
