package tfar.limbuscraft.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.item.BladeLineageKatanaItem;
import tfar.limbuscraft.item.MagicBulletItem;
import tfar.limbuscraft.item.SpicebushItem;

public class LimbusItems {
    public static final Item LIMBUS_TABLE = Items.registerBlock(LimbusBlocks.MIRROR);

    public static final Item BLADE_LINEAGE_KATANA = registerItem("blade_lineage_katana",new BladeLineageKatanaItem(new Item.Properties()));
    public static final Item MAGIC_BULLET = registerItem("magic_bullet",new MagicBulletItem(new Item.Properties()));
    public static final Item SPICEBUSH = registerItem("spicebush",new SpicebushItem(new Item.Properties()));

    //Blade lineage katana, and Spicebush

    public static void init() {}

    public static Item registerItem(String key, Item item) {
        return Items.registerItem(LimbusCraft.id(key), item);
    }
}
