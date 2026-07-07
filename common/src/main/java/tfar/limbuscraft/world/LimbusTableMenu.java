package tfar.limbuscraft.world;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import tfar.limbuscraft.init.LimbusMenuTypes;

public class LimbusTableMenu extends AbstractContainerMenu {
    public LimbusTableMenu(int containerId, Inventory inventory) {
        super(LimbusMenuTypes.LIMBUS_TABLE, containerId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
