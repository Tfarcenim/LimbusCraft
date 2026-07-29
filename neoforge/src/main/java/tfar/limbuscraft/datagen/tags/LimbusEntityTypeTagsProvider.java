package tfar.limbuscraft.datagen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.tags.LimbusEntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class LimbusEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public LimbusEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, LimbusCraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(LimbusEntityTypeTags.HAS_SANITY)
                .addTags(EntityTypeTags.ILLAGER)
                .add(EntityType.PLAYER,
                        EntityType.AXOLOTL,EntityType.ALLAY,EntityType.BAT,EntityType.BEE,
                        EntityType.CAMEL,EntityType.CAT,EntityType.CHICKEN,EntityType.COD,EntityType.COW,EntityType.DOLPHIN,
                        EntityType.ELDER_GUARDIAN,EntityType.ENDERMAN,EntityType.FROG,
                        EntityType.GLOW_SQUID,EntityType.GOAT,EntityType.GUARDIAN,EntityType.HORSE,
                        EntityType.IRON_GOLEM,
                        EntityType.LLAMA,EntityType.MULE,EntityType.PANDA,EntityType.PARROT,
                        EntityType.PIG,EntityType.PIGLIN,EntityType.POLAR_BEAR,EntityType.PUFFERFISH,
                        EntityType.SALMON, EntityType.SQUID,EntityType.STRIDER,
                        EntityType.TADPOLE,EntityType.TRADER_LLAMA,
                        EntityType.TROPICAL_FISH,EntityType.TURTLE,EntityType.VILLAGER,EntityType.WANDERING_TRADER,
                        EntityType.WITCH,EntityType.WOLF);
    }
}
