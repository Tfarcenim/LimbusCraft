package tfar.limbuscraft.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.init.LimbusBlocks;

public class LimbusBlockStateProvider extends BlockStateProvider {
    public LimbusBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, LimbusCraft.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        horizontalBlock(LimbusBlocks.MIRROR,models().getExistingFile(modLoc("block/mirror")));
        simpleBlockItem(LimbusBlocks.MIRROR,models().getExistingFile(modLoc("block/mirror")));
    }
}
