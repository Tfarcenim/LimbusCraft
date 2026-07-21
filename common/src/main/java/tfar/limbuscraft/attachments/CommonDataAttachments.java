package tfar.limbuscraft.attachments;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import tfar.limbuscraft.platform.Services;
import tfar.limbuscraft.tokens.Token;
import tfar.limbuscraft.tokens.TokenInstance;
import tfar.limbuscraft.tokens.TokenRegistry;

import java.util.*;

public class CommonDataAttachments {

    private static final Map<ResourceLocation,CommonDataAttachment<?>> MAP =new HashMap<>();

    public static final CommonDataAttachment<Integer> SANITY =
            register(CommonDataAttachment.create(o -> 0)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .codec(Codec.INT)
                    .build("sanity"));

    public static final CommonDataAttachment<Map<Token,TokenInstance>> TOKENS = register(CommonDataAttachment
            .<Map<Token,TokenInstance>>create(o -> Map.of())
            .networkSynchronized(ByteBufCodecs.map(
                    LinkedHashMap::new, TokenRegistry.TOKEN_STREAM_CODEC,TokenInstance.STREAM_CODEC
            ))
            .codec(Codec.unboundedMap(TokenRegistry.TOKEN_CODEC,TokenInstance.CODEC))
            .build("tokens")
    );

    public static final CommonDataAttachment<List<Float>> STAGGER_THRESHOLDS =
            register(CommonDataAttachment.<List<Float>>create(o -> List.of())
                    .networkSynchronized(ByteBufCodecs.FLOAT.apply(ByteBufCodecs.list()))
                    .codec(Codec.FLOAT.listOf())
                    .build("stagger_thresholds"));

    public static final CommonDataAttachment<Integer> STAGGERED =
            register(CommonDataAttachment.create(o -> 0)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .codec(Codec.INT)
                    .build("staggered"));

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