package tfar.limbuscraft.datagen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
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
        tag(LimbusDamageTypeTags.LIMBUS)
                .add(LimbusDamageTypes.WRATH,LimbusDamageTypes.LUST,LimbusDamageTypes.PRIDE,LimbusDamageTypes.SLOTH);

        this.tag(DamageTypeTags.NO_KNOCKBACK).add(LimbusDamageTypes.WRATH,LimbusDamageTypes.LUST,LimbusDamageTypes.PRIDE,LimbusDamageTypes.SLOTH);
    }
}
