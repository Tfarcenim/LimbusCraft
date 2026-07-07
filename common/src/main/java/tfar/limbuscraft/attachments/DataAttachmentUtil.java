package tfar.limbuscraft.attachments;

import net.minecraft.world.entity.LivingEntity;
import tfar.limbuscraft.platform.Services;
import tfar.limbuscraft.tokens.TokenInstance;

import java.util.List;

public class DataAttachmentUtil {

    public static int getSanity(LivingEntity entity) {
        return Services.PLATFORM.getOrCreateAttachedValue(entity,CommonDataAttachments.SANITY);
    }

    public static void setSanity(LivingEntity entity, int sanity) {
        Services.PLATFORM.setAttachedValue(entity,CommonDataAttachments.SANITY, sanity);
    }

    public static List<TokenInstance> getTokens(LivingEntity entity) {
        return Services.PLATFORM.getAttachedValue(entity,CommonDataAttachments.TOKENS);
    }

    public static void setTokens(LivingEntity entity, List<TokenInstance> tokens) {
        Services.PLATFORM.setAttachedValue(entity,CommonDataAttachments.TOKENS, tokens);
    }

}
