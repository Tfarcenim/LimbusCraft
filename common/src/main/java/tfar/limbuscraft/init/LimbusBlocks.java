package tfar.limbuscraft.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.block.LimbusTableBlock;

public class LimbusBlocks {

    public static final Block LIMBUS_TABLE = register("limbus_table",new LimbusTableBlock(BlockBehaviour.Properties.of()));

    public static void init() {}

    public static Block register(String key, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, LimbusCraft.id(key), block);
    }
}
