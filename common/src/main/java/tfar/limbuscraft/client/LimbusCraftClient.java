package tfar.limbuscraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.texture.OverlayTexture;
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

    public static int brownOverlay(float u) {
        return 0xa0008;//no overlay =0xa0000
    }
}
