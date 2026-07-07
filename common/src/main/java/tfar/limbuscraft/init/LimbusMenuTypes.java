package tfar.limbuscraft.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.world.LimbusTableMenu;

public class LimbusMenuTypes {
    public static final MenuType<LimbusTableMenu> LIMBUS_TABLE = register(
            "limbus_table",LimbusTableMenu::new);

    public static void init() {}

    private static <T extends AbstractContainerMenu> MenuType<T> register(String key, MenuType.MenuSupplier<T> factory) {
        return Registry.register(BuiltInRegistries.MENU, LimbusCraft.id(key), new MenuType<>(factory, FeatureFlags.VANILLA_SET));
    }

}
