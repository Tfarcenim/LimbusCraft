package tfar.limbuscraft.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import tfar.limbuscraft.LimbusCraft;
import tfar.limbuscraft.tokens.Token;

public class TokenTextureManager extends TextureAtlasHolder {
    public TokenTextureManager(TextureManager textureManager) {
        super(textureManager, LimbusCraft.id("textures/atlas/tokens.png"), LimbusCraft.id("tokens"));
    }

    public TextureAtlasSprite get(Token token) {
        TextureAtlasSprite sprite = getSprite(LimbusCraft.id(token.id()));
        return sprite;
    }
}
