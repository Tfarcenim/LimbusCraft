package tfar.limbuscraft.datagen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.init.LimbusDamageTypes;
import tfar.limbuscraft.tags.LimbusDamageTypeTags;

import java.util.concurrent.CompletableFuture;

public class LimbusDamageTypeTagsProvider extends DamageTypeTagsProvider {

    public LimbusDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,@Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, LimbusCraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(LimbusDamageTypeTags.SEVEN_DEADLY_SINS)
                .add(LimbusDamageTypes.WRATH,LimbusDamageTypes.LUST,LimbusDamageTypes.PRIDE,LimbusDamageTypes.SLOTH,
                        LimbusDamageTypes.GLUTTONY,LimbusDamageTypes.GLOOM);

        tag(LimbusDamageTypeTags.LIMBUS).addTag(LimbusDamageTypeTags.SEVEN_DEADLY_SINS);
        tag(DamageTypeTags.NO_KNOCKBACK).addTag(LimbusDamageTypeTags.SEVEN_DEADLY_SINS);

        tag(DamageTypeTags.BYPASSES_ARMOR).add(LimbusDamageTypes.GLUTTONY);
        tag(DamageTypeTags.BYPASSES_EFFECTS).add(LimbusDamageTypes.GLUTTONY);
        tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(LimbusDamageTypes.GLUTTONY);
        tag(DamageTypeTags.BYPASSES_RESISTANCE).add(LimbusDamageTypes.GLUTTONY);
        tag(DamageTypeTags.BYPASSES_SHIELD).add(LimbusDamageTypes.GLUTTONY);
        tag(LimbusDamageTypeTags.BYPASSES_LIMBUS_PROTECTION)
                .addTag(Tags.DamageTypes.IS_TECHNICAL)
                .add(LimbusDamageTypes.GLUTTONY);

        tag(LimbusDamageTypeTags.PHYSICAL)
                .addTags(DamageTypeTags.DAMAGES_HELMET,DamageTypeTags.IS_DROWNING,DamageTypeTags.IS_EXPLOSION,
                        DamageTypeTags.IS_FIRE,DamageTypeTags.IS_PROJECTILE)
                .add(DamageTypes.CACTUS,DamageTypes.CRAMMING,DamageTypes.FREEZE,
                        DamageTypes.MOB_ATTACK,DamageTypes.PLAYER_ATTACK,DamageTypes.SPIT,
                        DamageTypes.STING);
    }
}
