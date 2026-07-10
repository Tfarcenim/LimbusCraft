package tfar.limbuscraft.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import tfar.limbuscraft.LimbusCraft;

public class LimbusRenderTypes extends RenderStateShard {

    protected static final RenderStateShard.TextureStateShard TOKEN_SHEET = new RenderStateShard.TextureStateShard(
            LimbusCraft.id("textures/atlas/tokens.png"), false, false);

    public static final RenderType TOKEN = getTokenType();


    private LimbusRenderTypes(String string, Runnable r, Runnable r1) {
        super(string, r, r1);
    }

    private static RenderType getTokenType() {
        RenderType.CompositeState renderTypeState = RenderType.CompositeState.builder()
                .setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                .setTextureState(TOKEN_SHEET)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .createCompositeState(false);
        return RenderType.create("tokens", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, false, renderTypeState);
    }
}
