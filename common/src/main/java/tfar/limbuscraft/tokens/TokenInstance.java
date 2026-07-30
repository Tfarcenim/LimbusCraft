package tfar.limbuscraft.tokens;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import tfar.limbuscraft.attachments.DataAttachmentUtil;

public record TokenInstance(Token token, int potency,int count) {

    public static final Codec<TokenInstance> CODEC = RecordCodecBuilder.create(
            tokenInstanceInstance -> tokenInstanceInstance.group(
                    TokenRegistry.TOKEN_CODEC.fieldOf("token").forGetter(TokenInstance::token),
                    Codec.INT.fieldOf("potency").forGetter(TokenInstance::potency),
                    Codec.INT.fieldOf("count").forGetter(TokenInstance::count)
            ).apply(tokenInstanceInstance, TokenInstance::new)
    );

    public static final StreamCodec<FriendlyByteBuf,TokenInstance> STREAM_CODEC = StreamCodec.composite(
            TokenRegistry.TOKEN_STREAM_CODEC,TokenInstance::token, ByteBufCodecs.INT,TokenInstance::potency,
            ByteBufCodecs.INT,TokenInstance::count,
            TokenInstance::new);

    public void onApplied(LivingEntity entity,boolean isNew) {

    }

    public TokenInstance increaseCount(int delta) {
        return new TokenInstance(token,potency,count + delta);
    }

    public TokenInstance decrement() {
        return new TokenInstance(token,potency,count - 1);
    }

    public TokenInstance withCount(int delta) {
        return new TokenInstance(token,potency,delta);
    }


    public boolean shouldTick(LivingEntity entity,long combatTimer) {
        return token.shouldTick(entity,combatTimer);
    }

    public void tick(LivingEntity livingEntity) {
        token.tick(livingEntity,this);
    }

    public void trigger(LivingEntity livingEntity) {
        token.trigger(livingEntity,this);
    }

    public void decrementOrRemoveToken(LivingEntity livingEntity) {
        if (count > 1) {
            DataAttachmentUtil.addOrReplaceToken(livingEntity,decrement());
        }  else {
            DataAttachmentUtil.removeToken(livingEntity,token);
        }
    }

    public void removeToken(LivingEntity entity) {
        DataAttachmentUtil.removeToken(entity,token);
    }
}
