package tfar.limbuscraft.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import tfar.limbuscraft.Color;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.attachments.DataAttachmentUtil;

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
            color.color2Gl();
            guiGraphics.blitSprite(SANITY_ORB, j, k, 0,16,16);
            Color.reset();
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
