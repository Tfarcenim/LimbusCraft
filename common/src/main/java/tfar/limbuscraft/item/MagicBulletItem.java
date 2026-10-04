package tfar.limbuscraft.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MagicBulletItem extends Item {
    public MagicBulletItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        EntityHitResult entityHitResult = raycast(player);

        if (entityHitResult != null) {
            Entity entity = entityHitResult.getEntity();
            if (!level.isClientSide) {
                entity.kill();
            }
        }

        return super.use(level, player, usedHand);
    }

    public static EntityHitResult raycast(Player player) {
        HitResult hitResultOnViewVector = ProjectileUtil.getHitResultOnViewVector(player, e -> true, 128);
        return hitResultOnViewVector instanceof EntityHitResult ? (EntityHitResult) hitResultOnViewVector : null;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }
}
