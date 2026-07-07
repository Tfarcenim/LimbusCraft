package tfar.limbuscraft.client;

import net.minecraft.client.gui.screens.MenuScreens;
import tfar.limbuscraft.init.LimbusMenuTypes;

public class LimbusCraftClient {

    public static void setup() {
        MenuScreens.register(LimbusMenuTypes.LIMBUS_TABLE,LimbusTableScreen::new);
    }

}
