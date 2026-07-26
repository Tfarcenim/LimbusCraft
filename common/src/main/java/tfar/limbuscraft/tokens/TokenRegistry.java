package tfar.limbuscraft.tokens;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;

public class TokenRegistry {

    public static final Map<String,Token> TOKENS = new HashMap<>();
    public static final Codec<Token> TOKEN_CODEC = Codec.STRING.xmap(TOKENS::get, Token::id);
    public static final StreamCodec<ByteBuf,Token> TOKEN_STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
            TOKENS::get, Token::id);

    public static final Token BURN = register(new BurnBane());
    public static final Token BLEED = register(new BleedBane());
    public static final Token TREMOR = register(new TremorBane());
    public static final Token RUPTURE = register(new RuptureBane());

    static<T extends Token> T register(T token) {
        TOKENS.put(token.id(), token);
        return token;
    }

}
