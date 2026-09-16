package tfar.limbuscraft.world;

import net.minecraft.util.Unit;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.init.LimbusDataComponents;
import tfar.limbuscraft.init.LimbusMenuTypes;

public class LimbusTableMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;

    public final DataSlot dataSlot;

    protected final SimpleContainer simpleContainer = new SimpleContainer(1);

    public LimbusTableMenu(int containerId, Inventory inventory) {
        this(containerId,inventory,ContainerLevelAccess.NULL, DataSlot.standalone());
    }
    public LimbusTableMenu(int containerId, Inventory inventory, ContainerLevelAccess access, DataSlot dataSlot) {
        super(LimbusMenuTypes.LIMBUS_TABLE, containerId);
        this.access = access;
        this.dataSlot = dataSlot;

        addSlot(new Slot(simpleContainer, 0, 80, 35));

        for (int k = 0; k < 3; k++) {
            for (int i1 = 0; i1 < 9; i1++) {
                this.addSlot(new Slot(inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; l++) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, 142));
        }
        addDataSlot(dataSlot);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {

        if (!player.level().isClientSide) {
            switch (ButtonUsed.values()[id]) {
                case TRANSFORM-> {
                    ItemStack stack = simpleContainer.getItem(0);
                    if (stack.has(LimbusDataComponents.LIMBUS_WEAPON)) {
                        stack.remove(LimbusDataComponents.LIMBUS_WEAPON);
                    } else {
                        stack.set(LimbusDataComponents.LIMBUS_WEAPON, Unit.INSTANCE);
                    }
                    return true;
                }
                case LEFT_SLOT -> {
                    DataAttachmentUtil.decLimbusSlot(player);
                    return true;
                }
                case RIGHT_SLOT -> {
                    DataAttachmentUtil.incLimbusSlot(player);
                    return true;
                }
            }
        }

        return super.clickMenuButton(player, id);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((p_39371_, p_39372_) -> this.clearContainer(player, this.simpleContainer));

    }

    public enum ButtonUsed {
        TRANSFORM,LEFT_SLOT,RIGHT_SLOT;
    }

}
