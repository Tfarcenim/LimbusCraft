package tfar.limbuscraft;


import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.datagen.LimbusDatagen;
import tfar.limbuscraft.mixin.AttributeSupplierBuilderAccess;
import tfar.limbuscraft.mixin.DefaultAttributesAccess;
import tfar.limbuscraft.mixin.EntityAttributeModificationEventAccess;
import tfar.limbuscraft.tags.LimbusDamageTypeTags;
import tfar.limbuscraft.tokens.Token;
import tfar.limbuscraft.tokens.TokenInstance;
import tfar.limbuscraft.tokens.TokenRegistry;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mod(LimbusCraft.MOD_ID)
public class LimbusCraftNeoForge {

    public LimbusCraftNeoForge(IEventBus eventBus) {
        eventBus.addListener(this::attributeSetup);
        eventBus.addListener(this::register);
        eventBus.addListener(LimbusDatagen::gather);
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        LimbusCraft.init();
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,this::livingIncomingDamage);
        NeoForge.EVENT_BUS.addListener(this::commands);
        NeoForge.EVENT_BUS.addListener(this::onCommand);
        NeoForge.EVENT_BUS.addListener(this::controlTeleport);
        NeoForge.EVENT_BUS.addListener(this::entityTick);
        NeoForge.EVENT_BUS.addListener(this::livingDamagePost);
    }

    void entityTick(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            LimbusCraft.entityTick(livingEntity);
        }
    }

    void onCommand(CommandEvent event) {
        ParseResults<CommandSourceStack> parseResults = event.getParseResults();
        CommandSourceStack source = parseResults.getContext().getSource();
        if (source.getEntity() instanceof ServerPlayer serverPlayer) {
            boolean inCombat = serverPlayer.getCombatTracker().inCombat;
            if (inCombat && !serverPlayer.getAbilities().instabuild) {
                event.setCanceled(true);
                serverPlayer.sendSystemMessage(Component.literal("Can't use that command in combat"));
            }
        }
    }

    void controlTeleport(EntityTeleportEvent event) {
        if (!(event instanceof EntityTeleportEvent.EnderPearl)) {
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity livingEntity) {
                boolean inCombat = livingEntity.getCombatTracker().inCombat;
                if (!inCombat) return;
                if (livingEntity instanceof ServerPlayer serverPlayer) {
                    if (serverPlayer.getAbilities().instabuild) return;
                    event.setCanceled(true);
                } else {
                    event.setCanceled(true);
                }
            }
        }
    }

    void register(RegisterEvent event) {
        LimbusCraft.register();
    }

    void commands(RegisterCommandsEvent event) {
        LimbusCommands.register(event.getDispatcher());
    }

    void livingIncomingDamage(LivingIncomingDamageEvent event) {
        DamageSource source = event.getSource();
        if (!source.is(LimbusDamageTypeTags.LIMBUS)) {
            event.setAmount(event.getAmount() * 5);
        }
    }

    void livingDamagePost(LivingDamageEvent.Post event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity livingAttacker) {
            Map<Token, TokenInstance> tokens = DataAttachmentUtil.getTokens(livingAttacker);
            TokenInstance tokenInstance = tokens.get(TokenRegistry.BLEED);
            if (tokenInstance != null) {
                tokenInstance.tick(livingAttacker);
            }
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