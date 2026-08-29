package tfar.limbuscraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import tfar.limbuscraft.world.LimbusTableMenu;

public class LimbusTableBlock extends Block {
    public static final MapCodec<LimbusTableBlock> CODEC = simpleCodec(LimbusTableBlock::new);
    public static final MutableComponent CONTAINER_TITLE = Component.translatable("container.mirror");

    public LimbusTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            //player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (p_52229_, inventory, player) ->
                        new LimbusTableMenu(p_52229_, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE
        );
    }
}
