package tfar.limbuscraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.world.LimbusTableMenu;

public class LimbusMirrorBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<LimbusMirrorBlock> CODEC = simpleCodec(LimbusMirrorBlock::new);
    public static final MutableComponent CONTAINER_TITLE = Component.translatable("container.mirror");

    public LimbusMirrorBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    protected static final VoxelShape EAST_AABB =Block.box(0, 0, 0, 4, 16, 16);
    protected static final VoxelShape WEST_AABB = Block.box(12, 0, 0, 16, 16, 16);
    protected static final VoxelShape NORTH_AABB = Block.box(0, 0, 12, 16, 16, 16);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 0, 0, 16, 16, 4);

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_AABB;
            case WEST -> WEST_AABB;
            case EAST -> EAST_AABB;
            default -> NORTH_AABB;
        };
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (i, inventory, player) ->
                        new LimbusTableMenu(i, inventory, ContainerLevelAccess.create(level, pos),new LimbusDataSlot(player)), CONTAINER_TITLE
        );
    }

    public static class LimbusDataSlot extends DataSlot {

        private final Player player;

        public LimbusDataSlot(Player player) {
            this.player = player;
        }

        @Override
        public int get() {
            return DataAttachmentUtil.getLimbusSlot(player);
        }

        @Override
        public void set(int value) {
            DataAttachmentUtil.setLimbusSlot(player, value);
        }
    }
}
