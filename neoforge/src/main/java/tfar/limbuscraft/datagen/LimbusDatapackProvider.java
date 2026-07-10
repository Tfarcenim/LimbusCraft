package tfar.limbuscraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.init.LimbusDamageTypes;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LimbusDatapackProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, LimbusDamageTypes::bootstrap);

    public LimbusDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(LimbusCraft.MOD_ID));
    }
}
