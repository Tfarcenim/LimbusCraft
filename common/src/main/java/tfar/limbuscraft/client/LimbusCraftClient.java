package tfar.limbuscraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import tfar.limbuscraft.init.LimbusMenuTypes;

public class LimbusCraftClient {

    public static TokenTextureManager tokenTextureManager;

    public static void setup() {
        MenuScreens.register(LimbusMenuTypes.LIMBUS_TABLE,LimbusTableScreen::new);
    }

    public static void reloadListeners() {
        Minecraft minecraft = Minecraft.getInstance();
        tokenTextureManager = new TokenTextureManager(minecraft.getTextureManager());
    }

}
