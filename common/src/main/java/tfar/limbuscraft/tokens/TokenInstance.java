package tfar.limbuscraft.tokens;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record TokenInstance(Token token, int potency) {

    public static final Codec<TokenInstance> CODEC = RecordCodecBuilder.create(
            tokenInstanceInstance -> tokenInstanceInstance.group(
                    TokenRegistry.TOKEN_CODEC.fieldOf("token").forGetter(TokenInstance::token),
                    Codec.INT.fieldOf("potency").forGetter(TokenInstance::potency)
            ).apply(tokenInstanceInstance, TokenInstance::new)
    );

    public static final StreamCodec<FriendlyByteBuf,TokenInstance> STREAM_CODEC = StreamCodec.composite(
            TokenRegistry.TOKEN_STREAM_CODEC,TokenInstance::token, ByteBufCodecs.INT,TokenInstance::potency,
            TokenInstance::new);

}
