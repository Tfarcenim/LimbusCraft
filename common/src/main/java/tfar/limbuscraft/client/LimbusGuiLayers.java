package tfar.limbuscraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import tfar.limbuscraft.Color;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.healthbar.*;
import tfar.limbuscraft.platform.Services;

public class LimbusGuiLayers {

    public static final ResourceLocation SANITY_ORB = LimbusCraft.id("sanity_orb");

    public static final LayeredDraw.Layer SANITY = (guiGraphics, deltaTracker) -> {
        Entity cameraEntity = Minecraft.getInstance().cameraEntity;
        boolean b = Minecraft.getInstance().gameMode.canHurtPlayer();
        if (b && cameraEntity instanceof LivingEntity livingEntity) {
            int sanity = DataAttachmentUtil.getSanity(livingEntity);
            int j = (guiGraphics.guiWidth() - 16) / 2;
            int k = guiGraphics.guiHeight() - 31 - 4-16;
            Color color = sanityColor(sanity);
            guiGraphics.drawString(Minecraft.getInstance().font,sanity+"",j,k,color.colorToText());
            color.color2Gl();
            guiGraphics.blitSprite(SANITY_ORB, j, k, 0,16,16);
            Color.reset();
        }
    };

    public static final Health HEALTH = new Health(BarSettings.getBuilder().setColorProvider(new SingleColorProvider(Color.RED)).build());

    public static final LayeredDraw.Layer HEALTH_ = (guiGraphics, deltaTracker) -> {
        Entity entity = Minecraft.getInstance().getCameraEntity();
        if (!(entity instanceof Player player)) return;
        if (player.getAbilities().instabuild || player.isSpectator()) return;

        BarOverlay overlay = HEALTH;
        Gui gui = Minecraft.getInstance().gui;
            BarSide side = overlay.getSide();
            try {
                if (overlay.render(gui, guiGraphics, player, Services.PLATFORM.getHeight(gui, side))) {
                    Services.PLATFORM.raiseHeight(gui, side, 10);
                }
            } catch (Throwable e) {
               LimbusCraft.LOG.error("disabling broken overlay {}", overlay.name());
                e.printStackTrace();
                overlay.setErrored();
            }
    };

    public static Color sanityColor(int sanity) {
        if (sanity == 0)return Color.WHITE;

        float magnitude = Math.abs(sanity) / 45f;
        int f = (int) (255 * magnitude);
        if (sanity > 0) {
            return Color.fromRGB(255-f, 255-f, 255);
        } else {
            return Color.fromRGB(255, 255-f, 255-f);
        }
    }

}
