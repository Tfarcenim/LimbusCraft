package tfar.limbuscraft.attachments;

import net.minecraft.world.entity.LivingEntity;
import tfar.limbuscraft.platform.Services;
import tfar.limbuscraft.tokens.Token;
import tfar.limbuscraft.tokens.TokenInstance;

import java.util.HashMap;
import java.util.Map;

public class DataAttachmentUtil {

    public static int getSanity(LivingEntity entity) {
        return Services.PLATFORM.getOrCreateAttachedValue(entity,CommonDataAttachments.SANITY);
    }

    public static void setSanity(LivingEntity entity, int sanity) {
        Services.PLATFORM.setAttachedValue(entity,CommonDataAttachments.SANITY, sanity);
    }

    public static Map<Token, TokenInstance> getTokens(LivingEntity entity) {
        return Services.PLATFORM.getAttachedValue(entity,CommonDataAttachments.TOKENS);
    }

    public static void setTokens(LivingEntity entity, Map<Token, TokenInstance> tokens) {
        Services.PLATFORM.setAttachedValue(entity,CommonDataAttachments.TOKENS, tokens);
    }

    public static void addOrReplaceToken(LivingEntity entity, TokenInstance token) {
        Map<Token, TokenInstance> tokens = new HashMap<>(DataAttachmentUtil.getTokens(entity));
        tokens.put(token.token(),token);
        setTokens(entity,tokens);
    }

    public static void removeToken(LivingEntity entity, Token token) {
        Map<Token, TokenInstance> tokens = new HashMap<>(DataAttachmentUtil.getTokens(entity));
        tokens.remove(token);
        setTokens(entity,tokens);
    }

    public static void clearTokens(LivingEntity entity) {
        Services.PLATFORM.setAttachedValue(entity,CommonDataAttachments.TOKENS, CommonDataAttachments.TOKENS.defaultValueSupplier.apply(entity));
    }

    public static int getStaggered(LivingEntity entity) {
        return Services.PLATFORM.getOrDefaultAttachedValue(entity,CommonDataAttachments.STAGGERED,0);
    }

    public static void setStaggered(LivingEntity entity, int staggered) {
        Services.PLATFORM.setAttachedValue(entity,CommonDataAttachments.STAGGERED, staggered);
    }
}
