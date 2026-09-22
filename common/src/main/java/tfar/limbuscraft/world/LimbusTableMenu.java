package tfar.limbuscraft.world;

import net.minecraft.util.Unit;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import tfar.limbuscraft.LimbusPlayerUpgrade;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.init.LimbusDataComponents;
import tfar.limbuscraft.init.LimbusMenuTypes;

import java.util.HashMap;
import java.util.Map;

public class LimbusTableMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;

    public final DataSlot dataSlot;

    protected final SimpleContainer simpleContainer = new SimpleContainer(1);

    public final int inventoryX = 8;
    public final int inventoryY = 120;

    public LimbusTableMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL, DataSlot.standalone());
    }

    public LimbusTableMenu(int containerId, Inventory inventory, ContainerLevelAccess access, DataSlot dataSlot) {
        super(LimbusMenuTypes.LIMBUS_TABLE, containerId);
        this.access = access;
        this.dataSlot = dataSlot;

        addSlot(new Slot(simpleContainer, 0, 80, 35));

        for (int k = 0; k < 3; k++) {
            for (int i1 = 0; i1 < 9; i1++) {
                this.addSlot(new Slot(inventory, i1 + k * 9 + 9, inventoryX + i1 * 18, inventoryY + k * 18));
            }
        }

        for (int l = 0; l < 9; l++) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, inventoryY + 58));
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
                case TRANSFORM -> {
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
                case UPGRADE_0 ,UPGRADE_1,UPGRADE_2,UPGRADE_3,UPGRADE_4 -> {
                    return tryUpgrade(player, id);
                }
            }
        }

        return super.clickMenuButton(player, id);
    }

    boolean tryUpgrade(Player player,int slot) {
        Map<LimbusPlayerUpgrade, LimbusPlayerUpgrade.Instance> instances = new HashMap<>(DataAttachmentUtil.getPlayerUpgrades(player));

        LimbusPlayerUpgrade upgrade = LimbusPlayerUpgrade.byIndex(slot);
        LimbusPlayerUpgrade.Instance instance = instances.get(upgrade);

        if (instance != null && instance.levels() >= upgrade.maxLevels()) {
            return false;//don't upgrade past max
        }

        long cost = LimbusPlayerUpgrade.getNextCost(instance == null ? 0 : instance.levels());

        if (cost > player.experienceLevel && !player.getAbilities().instabuild) {
            return false;
        }
        if (!player.getAbilities().instabuild) {
            player.giveExperienceLevels((int) -cost);
        }


        if (instance != null) {
            instances.put(upgrade, instance.upgrade());
        } else {//upgrade isn't present, add it
            instances.put(upgrade,upgrade.create());
        }

        DataAttachmentUtil.setPlayerUpgrades(player,instances);//forces sync

        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((p_39371_, p_39372_) -> this.clearContainer(player, this.simpleContainer));

    }

    public enum ButtonUsed {
        UPGRADE_0, UPGRADE_1, UPGRADE_2, UPGRADE_3, UPGRADE_4, TRANSFORM, LEFT_SLOT, RIGHT_SLOT;
    }

}
