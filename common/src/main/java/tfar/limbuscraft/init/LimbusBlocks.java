package tfar.limbuscraft.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.block.LimbusMirrorBlock;

public class LimbusBlocks {

    public static final Block MIRROR = register("mirror",new LimbusMirrorBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static void init() {}

    public static Block register(String key, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, LimbusCraft.id(key), block);
    }
}
