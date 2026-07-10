package tfar.limbuscraft.client;

import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.tokens.Token;

public class TokenTextureManager extends TextureAtlasHolder {
    public TokenTextureManager(TextureManager textureManager) {
        super(textureManager, LimbusCraft.id("textures/atlas/tokens.png"), LimbusCraft.id("tokens"));
    }

    public TextureAtlasSprite get(Token token) {
        return getSprite(LimbusCraft.id(token.id()));
    }
}
