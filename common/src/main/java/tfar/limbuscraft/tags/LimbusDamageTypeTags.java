package tfar.limbuscraft.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import tfar.limbuscraft.LimbusCraft;

public class LimbusDamageTypeTags {
    public static final TagKey<DamageType> LIMBUS = create("limbus");
    public static final TagKey<DamageType> SEVEN_DEADLY_SINS = create("seven_deadly_sins");
    public static final TagKey<DamageType> PHYSICAL = create("physical");

    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, LimbusCraft.id(name));
    }
}
