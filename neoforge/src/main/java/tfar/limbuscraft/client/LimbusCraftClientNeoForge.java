package tfar.limbuscraft.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.tokens.Token;
import tfar.limbuscraft.tokens.TokenInstance;

import java.util.List;
import java.util.Map;

@Mod(value = LimbusCraft.MOD_ID,dist = Dist.CLIENT)
public class LimbusCraftClientNeoForge {

    public LimbusCraftClientNeoForge(IEventBus bus) {
        bus.addListener(this::setup);
        bus.addListener(this::registerOverlay);
        NeoForge.EVENT_BUS.addListener(this::renderOverlayEvent);

        //NeoForge.EVENT_BUS.addListener(this::renderAboveNameTag);
    }

    /**
     * @param bufferSource
     * @param entity
     * @param poseStack
     * @param quaternionf
     * @param packedLight
     * @see net.minecraft.client.renderer.entity.EntityRenderer#renderNameTag
     */
    public static void renderAboveNameTag(Entity entity, float partialTick,PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Quaternionf quaternionf) {

        if (entity instanceof LivingEntity livingEntity) {

            EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

            double d0 = entityRenderDispatcher.distanceToSqr(livingEntity);
            if (net.neoforged.neoforge.client.ClientHooks.isNameplateInRenderDistance(livingEntity, d0)) {
                Vec3 vec3 = livingEntity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, livingEntity.getViewYRot(partialTick));
                if (vec3 != null) {
                    Map<Token, TokenInstance> tokens = DataAttachmentUtil.getTokens(livingEntity);
                    poseStack.pushPose();
                    poseStack.translate(vec3.x, vec3.y + 0.5, vec3.z);

                    poseStack.mulPose(entityRenderDispatcher.cameraOrientation());
                   // poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    //for (TokenInstance token : tokens) {
                    Component displayName = livingEntity.getDisplayName();//todo use event?
                    boolean flag = !livingEntity.isDiscrete();
                    int y = "deadmau5".equals(displayName.getString()) ? -10 : 0;

                    Matrix4f matrix4f = poseStack.last().pose();

                    float width = 1;
                    float height = 1;
                    float z = 0.0001f;

                    VertexConsumer builder = bufferSource.getBuffer(LimbusRenderTypes.TOKEN);

                    MobEffectTextureManager mobeffecttexturemanager = Minecraft.getInstance().getMobEffectTextures();
                    TextureAtlasSprite textureatlassprite = mobeffecttexturemanager.get(MobEffects.POISON);

                    builder.addVertex(matrix4f, width, height, z)
                            .setUv(textureatlassprite.getU1(), textureatlassprite.getV0()).setColor(1f, 1f, 1f, 1f)
                            .setLight(packedLight);
                    builder.addVertex(matrix4f, width, 0, z)
                            .setUv(textureatlassprite.getU1(), textureatlassprite.getV1()).setColor(1f, 1f, 1f, 1f)
                            .setLight(packedLight);
                    builder.addVertex(matrix4f, 0, 0, z).setUv(textureatlassprite.getU0(), textureatlassprite.getV1())
                            .setColor(1f, 1f, 1f, 1f).setLight(packedLight);
                    builder.addVertex(matrix4f, 0, height, z)
                            .setUv(textureatlassprite.getU0(), textureatlassprite.getV0())
                            .setColor(1f, 1f, 1f, 1f).setLight(packedLight);


                    //  }
                    poseStack.popPose();
                }
            }
        }
    }

    void setup(FMLClientSetupEvent event) {
        LimbusCraftClient.setup();
    }

    void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH,LimbusCraft.id("sanity"),LimbusGuiLayers.SANITY);
        event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH,LimbusCraft.id("health"),LimbusGuiLayers.HEALTH_);
    }

    void renderOverlayEvent(RenderGuiLayerEvent.Pre event) {
        if (event.getName().equals(VanillaGuiLayers.PLAYER_HEALTH)) {
            event.setCanceled(true);
        }
    }
}
