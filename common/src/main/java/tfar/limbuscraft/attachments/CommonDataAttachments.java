package tfar.limbuscraft.attachments;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import tfar.limbuscraft.platform.Services;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommonDataAttachments {

    private static final Map<ResourceLocation,CommonDataAttachment<?>> MAP =new HashMap<>();

    public static final CommonDataAttachment<Integer> SANITY =
            register(CommonDataAttachment.create(o -> 0)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .codec(Codec.INT)
                    .build("sanity"));

    public static CommonDataAttachment<?> lookup(ResourceLocation location) {
        return MAP.get(location);
    }

    static <T> CommonDataAttachment<T> register(CommonDataAttachment<T> type) {
        Services.PLATFORM.registerDataAttachment(type);
        Objects.requireNonNull(type.getAttachment());
        MAP.put(type.name,type);
        return type;
    }

    public static void init() {

    }
}