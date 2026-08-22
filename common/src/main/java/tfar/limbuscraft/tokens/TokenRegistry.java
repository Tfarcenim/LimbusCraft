package tfar.limbuscraft.tokens;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import tfar.limbuscraft.LimbusCraft;

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
    public static final Token SINKING = register(new SinkingBane());

    public static final Token POISE = register(new PoiseBoon());
    public static final Token CHARGE = register(new ChargeBoon());
    public static final Token DAMAGE_UP = register(new DamageUpBoon());
    public static final Token PROTECTION = register(new ProtectionBoon());
    public static final Token PARALYZE = register(new Bane("paralyze"));
    public static final Token HASTE = register(new HasteBoon()
            .addAttributeModifier(Attributes.MOVEMENT_SPEED,
                    LimbusCraft.id("haste"),.04, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_SPEED,
                    LimbusCraft.id("haste"),.04, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    //Damage Up: Works in Stacks, every stack increases someone’s damage by 10% of the original damage.
    // Fully remove all stacks of damage up every 10 seconds.

    //Protection: Works in Stacks, Every stack increases someone’s damage resistance by 10%, up to 10 stacks.
    // Fully remove all stacks of Protection every 10 seconds (NOT THE MINECRAFT ENCHANTMENT)
    //
    //Paralyze: Works in stacks, Stacks are used up when skill coins are flipped.
    // Paralyze makes coin power equal to zero, even when Heads is flipped. Remove all stacks of paralyze when combat ends.
    //
    //Haste: Works in Stacks, increases base speed and attack speed by 4% for every stack.
    // Fully remove all stacks of Haste every 10 seconds. (NOT THE POTION EFFECT)
    //
    //Color Key:

    static<T extends Token> T register(T token) {
        TOKENS.put(token.id(), token);
        return token;
    }

}
