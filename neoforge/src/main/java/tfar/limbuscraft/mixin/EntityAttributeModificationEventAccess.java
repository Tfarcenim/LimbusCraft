package tfar.limbuscraft.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(EntityAttributeModificationEvent.class)
public interface EntityAttributeModificationEventAccess {
    @Accessor
    Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> getEntityAttributes();
}
