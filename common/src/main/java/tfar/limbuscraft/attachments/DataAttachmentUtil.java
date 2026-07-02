package tfar.limbuscraft.attachments;

import net.minecraft.world.entity.LivingEntity;
import tfar.limbuscraft.platform.Services;

public class DataAttachmentUtil {

    public static int getSanity(LivingEntity entity) {
        return Services.PLATFORM.getOrCreateAttachedValue(entity,CommonDataAttachments.SANITY);
    }

    public static void setSanity(LivingEntity entity, int sanity) {
        Services.PLATFORM.setAttachedValue(entity,CommonDataAttachments.SANITY, sanity);
    }

}
