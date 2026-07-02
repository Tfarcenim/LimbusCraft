package tfar.limbuscraft;


import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import tfar.limbuscraft.mixin.AttributeSupplierBuilderAccess;
import tfar.limbuscraft.mixin.DefaultAttributesAccess;
import tfar.limbuscraft.mixin.EntityAttributeModificationEventAccess;

import java.util.HashSet;
import java.util.Set;

@Mod(LimbusCraft.MOD_ID)
public class LimbusCraftNeoForge {

    public LimbusCraftNeoForge(IEventBus eventBus) {
        eventBus.addListener(this::attributeSetup);
        eventBus.addListener(this::register);
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        LimbusCraft.init();
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,this::livingDamage);
        NeoForge.EVENT_BUS.addListener(this::commands);
    }

    void register(RegisterEvent event) {
        LimbusCraft.register();
    }

    void commands(RegisterCommandsEvent event) {
        LimbusCommands.register(event.getDispatcher());
    }

    void livingDamage(LivingIncomingDamageEvent event) {
        DamageSource source = event.getSource();
        if (true) {
            event.setAmount(event.getAmount() * 5);
        }
    }

    void attributeSetup(EntityAttributeModificationEvent event) {

        Set<EntityType<? extends LivingEntity>> added = new HashSet<>();
        //modded stuff
        var map = ((EntityAttributeModificationEventAccess)event).getEntityAttributes();
        for (var entry : map.entrySet()) {
            EntityType<? extends LivingEntity> key = entry.getKey();
            AttributeSupplier.Builder value = entry.getValue();
            var builderMap = ((AttributeSupplierBuilderAccess)value).getBuilder();
            AttributeInstance attributeInstance = builderMap.get(Attributes.MAX_HEALTH);
            added.add(key);
            event.add(key, Attributes.MAX_HEALTH,attributeInstance.getBaseValue() * 5);
        }

        //vanilla
        var defaultMap = DefaultAttributesAccess.getSUPPLIERS();
        for (var entry : defaultMap.entrySet()) {
            EntityType<? extends LivingEntity> key = entry.getKey();
            if (added.contains(key)) {continue;}
            AttributeSupplier value = entry.getValue();
            event.add(key, Attributes.MAX_HEALTH,value.getBaseValue(Attributes.MAX_HEALTH) * 5);
        }


    }
}