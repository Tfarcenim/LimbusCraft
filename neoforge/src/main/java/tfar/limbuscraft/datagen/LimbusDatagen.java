package tfar.limbuscraft.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tfar.limbuscraft.datagen.tags.LimbusDamageTypeTagsProvider;
import tfar.limbuscraft.datagen.tags.LimbusEntityTypeTagsProvider;
import tfar.limbuscraft.init.LimbusBlocks;

public class LimbusDatagen {

    public static void gather(GatherDataEvent event) {
        boolean server = event.includeServer();
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        var lookup = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if (server) {
            DatapackBuiltinEntriesProvider provider = new LimbusDatapackProvider(output,lookup);
            event.addProvider(provider);
            lookup = provider.getRegistryProvider();
            event.addProvider(new LimbusDamageTypeTagsProvider(output, lookup, existingFileHelper));
            event.addProvider(new LimbusEntityTypeTagsProvider(output, lookup, existingFileHelper));
        }
    }
}
