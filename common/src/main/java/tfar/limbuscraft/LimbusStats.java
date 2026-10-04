package tfar.limbuscraft;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import tfar.limbuscraft.tokens.TokenInstance;
import tfar.limbuscraft.tokens.TokenRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LimbusStats {
    public static final int MIN_SANITY = -45;
    public static final int MAX_SANITY = 45;
    public static final int DEFAULT_RUPTURE_TIMER = 500;

    public static Map<Item,LimbusItemAttributes> LIMBUS_ITEM_ATTRIBUTES = new HashMap<>();

    static {
        LIMBUS_ITEM_ATTRIBUTES.put(Items.STICK,new LimbusItemAttributes(
                List.of(new LimbusItemAttributes.Entry(
                        new TokenInstance(TokenRegistry.BLEED,1,1),.5)
        )));
    }

}
