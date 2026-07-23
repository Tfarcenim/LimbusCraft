package tfar.limbuscraft;


import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.data.DataAccessor;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.datagen.LimbusDatagen;
import tfar.limbuscraft.ducks.LivingEntityDuck;
import tfar.limbuscraft.mixin.AttributeSupplierBuilderAccess;
import tfar.limbuscraft.mixin.DefaultAttributesAccess;
import tfar.limbuscraft.mixin.EntityAttributeModificationEventAccess;
import tfar.limbuscraft.tags.LimbusDamageTypeTags;
import tfar.limbuscraft.tokens.Token;
import tfar.limbuscraft.tokens.TokenInstance;
import tfar.limbuscraft.tokens.TokenRegistry;
import tfar.limbuscraft.world.LimbusCombatTracker;

import java.util.HashSet;
import java.util.List;
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
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::livingIncomingDamage);
        NeoForge.EVENT_BUS.addListener(this::commands);
        NeoForge.EVENT_BUS.addListener(this::onCommand);
        NeoForge.EVENT_BUS.addListener(this::controlTeleport);
        NeoForge.EVENT_BUS.addListener(this::entityTickPre);
        NeoForge.EVENT_BUS.addListener(this::entityTickPost);
        NeoForge.EVENT_BUS.addListener(this::livingDamagePost);
    }

    void entityTickPre(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            LimbusCombatTracker limbusCombatTracker = ((LivingEntityDuck)livingEntity).getLimbusCombatTracker();
            limbusCombatTracker.tick();
        }
    }

    void entityTickPost(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            LimbusCraft.entityTick(livingEntity);
        }
    }

    void onCommand(CommandEvent event) {
        ParseResults<CommandSourceStack> parseResults = event.getParseResults();
        CommandSourceStack source = parseResults.getContext().getSource();
        if (source.getEntity() instanceof ServerPlayer serverPlayer) {
            boolean inCombat = serverPlayer.getCombatTracker().inCombat;
            if (inCombat && !serverPlayer.getAbilities().instabuild && !serverPlayer.hasPermissions(Commands.LEVEL_MODERATORS)) {
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
        CommandBuildContext buildContext = event.getBuildContext();

        LiteralArgumentBuilder<CommandSourceStack> literalargumentbuilder = Commands.literal("neo")
                .requires(p_139381_ -> p_139381_.hasPermission(Commands.LEVEL_GAMEMASTERS));


        for (DataCommands.DataProvider provider : DataCommands.TARGET_PROVIDERS) {
            literalargumentbuilder.then(provider.wrap(
                            Commands.literal("get"),
                            p -> p
                                    .then(Commands.argument("id", ResourceArgument.resource(buildContext,
                                                    NeoForgeRegistries.Keys.ATTACHMENT_TYPES))
                                            .executes(p1 ->
                                                    getAttachmentValue(p1, provider.access(p1))
                                            )
                                    )
                    )
            );
        }
        event.getDispatcher().register(literalargumentbuilder);
    }

    static int getAttachmentValue(CommandContext<CommandSourceStack> context, DataAccessor accessor) throws CommandSyntaxException {
        Holder.Reference<AttachmentType<?>> attachmentType = ResourceArgument.getResource(context, "id",
                NeoForgeRegistries.Keys.ATTACHMENT_TYPES);
        CompoundTag compoundtag = accessor.getData();
        CompoundTag attachmentTag = compoundtag.getCompound(AttachmentHolder.ATTACHMENTS_NBT_KEY);
        ResourceLocation resourceLocation = attachmentType.key().location();
        Tag tag = attachmentTag.get(resourceLocation.toString());
        context.getSource().sendSuccess(() -> accessor.getPrintSuccess(tag), false);
        return 1;
    }

    void livingIncomingDamage(LivingIncomingDamageEvent event) {
        DamageSource source = event.getSource();
        LivingEntity entity = event.getEntity();
        if (!source.is(LimbusDamageTypeTags.LIMBUS)) {
            event.setAmount(event.getAmount() * 5);
        }

        int staggerCount = DataAttachmentUtil.getStaggered(entity);
        if (staggerCount > 0) {
            float multiplier = 1;
            if (staggerCount == 1) {multiplier = 1.2F;}
            else if (staggerCount == 2) {multiplier = 1.5F;}
            else if (staggerCount >= 3) {multiplier = 2;}
            event.setAmount(event.getAmount() * multiplier);
        }

        ((LivingEntityDuck)event.getEntity()).getLimbusCombatTracker().onHit(source);
    }

    void livingDamagePost(LivingDamageEvent.Post event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        LivingEntity target = event.getEntity();
        checkForStagger(target, event.getNewDamage());
        if (attacker instanceof LivingEntity livingAttacker) {
            Map<Token, TokenInstance> tokens = DataAttachmentUtil.getTokens(livingAttacker);
            TokenInstance tokenInstance = tokens.get(TokenRegistry.BLEED);
            if (tokenInstance != null) {
                tokenInstance.tick(livingAttacker);
            }
        }
    }

    void checkForStagger(LivingEntity livingEntity, float damage) {
        int staggeredCount = DataAttachmentUtil.getStaggered(livingEntity);
        if (staggeredCount > 0) {return;}
        float lowerBound = livingEntity.getHealth();
        float upperBound = livingEntity.getHealth() + damage;
        List<Float> staggerThresholds = DataAttachmentUtil.getStaggerThresholds(livingEntity);
        int staggerCount = 0;
        for (float staggerThreshold : staggerThresholds) {
            if (lowerBound <= staggerThreshold && upperBound >= staggerThreshold) {
                staggerCount++;
            }
        }
        if (staggerCount > 0) {
            LimbusCombatTracker.LOG.info("{} is staggered {} times", livingEntity.getName(), staggerCount);
            DataAttachmentUtil.setStaggerTimer(livingEntity, 120);
        }
        DataAttachmentUtil.setStaggered(livingEntity, staggerCount);
    }

    void attributeSetup(EntityAttributeModificationEvent event) {

        Set<EntityType<? extends LivingEntity>> added = new HashSet<>();
        //modded stuff
        var map = ((EntityAttributeModificationEventAccess) event).getEntityAttributes();
        for (var entry : map.entrySet()) {
            EntityType<? extends LivingEntity> key = entry.getKey();
            AttributeSupplier.Builder value = entry.getValue();
            var builderMap = ((AttributeSupplierBuilderAccess) value).getBuilder();
            AttributeInstance attributeInstance = builderMap.get(Attributes.MAX_HEALTH);
            added.add(key);
            event.add(key, Attributes.MAX_HEALTH, attributeInstance.getBaseValue() * 5);
        }

        //vanilla
        var defaultMap = DefaultAttributesAccess.getSUPPLIERS();
        for (var entry : defaultMap.entrySet()) {
            EntityType<? extends LivingEntity> key = entry.getKey();
            if (added.contains(key)) {
                continue;
            }
            AttributeSupplier value = entry.getValue();
            event.add(key, Attributes.MAX_HEALTH, value.getBaseValue(Attributes.MAX_HEALTH) * 5);
        }


    }
}