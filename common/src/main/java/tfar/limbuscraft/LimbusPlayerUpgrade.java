package tfar.limbuscraft;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.*;

//Upgrade 1: +3 HP (max +360)
//Upgrade 2: +0.1 Attack Damage (max +6 damage)
//Upgrade 3: +2% Speed (max 120%)
//Upgrade 4: +0.05 Entity Reach (Max +3)
//Upgrade 5: +0.02 Attack speed (max +1)
public record LimbusPlayerUpgrade(String name, Holder<Attribute> attribute, double factor, int maxLevels) {

    public static final Map<String, LimbusPlayerUpgrade> ATTRIBUTES = new LinkedHashMap<>();
    private static List<LimbusPlayerUpgrade> LIST;

    public static final LimbusPlayerUpgrade HEALTH = register(new LimbusPlayerUpgrade("health",Attributes.MAX_HEALTH,3,120));
    public static final LimbusPlayerUpgrade ATTACK_DAMAGE = register(new LimbusPlayerUpgrade("attack_damage",Attributes.ATTACK_DAMAGE,.1,120));
    public static final LimbusPlayerUpgrade MOVEMENT_SPEED = register(new LimbusPlayerUpgrade("movement_speed",Attributes.MAX_HEALTH,.02,120));
    public static final LimbusPlayerUpgrade ENTITY_REACH = register(new LimbusPlayerUpgrade("entity_reach",Attributes.MAX_HEALTH,.05,120));
    public static final LimbusPlayerUpgrade ATTACK_SPEED = register(new LimbusPlayerUpgrade("attack_speed",Attributes.ATTACK_SPEED,.02,120));

    public static final Codec<LimbusPlayerUpgrade> CODEC = Codec.STRING.xmap(ATTRIBUTES::get, LimbusPlayerUpgrade::name);

    public static final StreamCodec<RegistryFriendlyByteBuf, LimbusPlayerUpgrade> STREAM_CODEC = StreamCodec.of(
            (buffer, value) -> buffer.writeUtf(value.name),
            buffer -> ATTRIBUTES.get(buffer.readUtf()));

    public static LimbusPlayerUpgrade register(LimbusPlayerUpgrade attribute) {
        ATTRIBUTES.put(attribute.name, attribute);
        return attribute;
    }

    public static List<LimbusPlayerUpgrade> ordered() {
        if (LIST == null) {
            LIST = new ArrayList<>(ATTRIBUTES.values());
        }
        return LIST;
    }

    public static long scaling(long currentLevel) {
        return (currentLevel+1) * (currentLevel+1);
    }

    public record Instance(LimbusPlayerUpgrade attribute, int levels) {

        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
           LimbusPlayerUpgrade.CODEC.fieldOf("attribute").forGetter(Instance::attribute),
                Codec.INT.fieldOf("level").forGetter(Instance::levels)
        ).apply(instance, Instance::new));

        public static final StreamCodec<RegistryFriendlyByteBuf,Instance> STREAM_CODEC = StreamCodec.composite(
                LimbusPlayerUpgrade.STREAM_CODEC,Instance::attribute,
                ByteBufCodecs.INT,Instance::levels,Instance::new
        );
    }

}
