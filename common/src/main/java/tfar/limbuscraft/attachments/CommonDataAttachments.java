package tfar.limbuscraft.attachments;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import tfar.limbuscraft.platform.Services;
import tfar.limbuscraft.tokens.TokenInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommonDataAttachments {

    private static final Map<ResourceLocation,CommonDataAttachment<?>> MAP =new HashMap<>();

    public static final CommonDataAttachment<Integer> SANITY =
            register(CommonDataAttachment.create(o -> 0)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .codec(Codec.INT)
                    .build("sanity"));

    public static final CommonDataAttachment<List<TokenInstance>> TOKENS = register(CommonDataAttachment
            .<List<TokenInstance>>create(o -> List.of())
            .networkSynchronized(TokenInstance.STREAM_CODEC.apply(ByteBufCodecs.list()))
            .codec(TokenInstance.CODEC.listOf())
            .build("tokens")
    );

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