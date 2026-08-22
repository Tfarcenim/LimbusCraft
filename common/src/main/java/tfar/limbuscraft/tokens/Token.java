package tfar.limbuscraft.tokens;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Map;
import java.util.Objects;

public class Token {
    private final String id;
    private final TokenType type;

    private final Map<Holder<Attribute>, MobEffect.AttributeTemplate> attributeModifiers = new Object2ObjectOpenHashMap();

    public Token(String id, TokenType type) {
        this.id = id;
        this.type = type;
    }

    public String id() {
        return id;
    }

    public TokenType type() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Token) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return "Token[" +
                "id=" + id + ", " +
                "type=" + type + ']';
    }

    public void tick(LivingEntity entity,TokenInstance tokenInstance) {

    }

    public boolean shouldTick(LivingEntity entity, long combatTimer) {
        return false;
    }

    public void trigger(LivingEntity livingEntity, TokenInstance tokenInstance) {

    }

    public Token addAttributeModifier(Holder<Attribute> attribute, ResourceLocation id, double amount, AttributeModifier.Operation operation) {
        this.attributeModifiers.put(attribute, new MobEffect.AttributeTemplate(id, amount, operation));
        return this;
    }

    public void removeAttributeModifiers(AttributeMap attributeMap) {
        for(Map.Entry<Holder<Attribute>, MobEffect.AttributeTemplate> entry : this.attributeModifiers.entrySet()) {
            AttributeInstance attributeInstance = attributeMap.getInstance(entry.getKey());
            if (attributeInstance != null) {
                attributeInstance.removeModifier(entry.getValue().id());
            }
        }

    }

    public void addAttributeModifiers(AttributeMap attributeMap, TokenInstance tokenInstance) {
        for(Map.Entry<Holder<Attribute>, MobEffect.AttributeTemplate> entry : this.attributeModifiers.entrySet()) {
            AttributeInstance attributeInstance = attributeMap.getInstance(entry.getKey());
            if (attributeInstance != null) {
                attributeInstance.removeModifier(entry.getValue().id());
                attributeInstance.addPermanentModifier(entry.getValue().create(tokenInstance.potency()-1));
            }
        }
    }
}
