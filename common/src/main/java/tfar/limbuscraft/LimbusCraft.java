package tfar.limbuscraft;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.limbuscraft.attachments.CommonDataAttachments;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.ducks.LivingEntityDuck;
import tfar.limbuscraft.init.LimbusBlocks;
import tfar.limbuscraft.init.LimbusDataComponents;
import tfar.limbuscraft.init.LimbusItems;
import tfar.limbuscraft.init.LimbusMenuTypes;
import tfar.limbuscraft.platform.Services;
import tfar.limbuscraft.tags.LimbusEntityTypeTags;
import tfar.limbuscraft.tokens.Token;
import tfar.limbuscraft.tokens.TokenInstance;
import tfar.limbuscraft.tokens.TokenRegistry;
import tfar.limbuscraft.world.LimbusCombatTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class LimbusCraft {

    public static final String MOD_ID = "limbuscraft";
    public static final String MOD_NAME = "LimbusCraft";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final boolean DEBUG = Services.PLATFORM.isDevelopmentEnvironment();

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
    }

    public static void register() {
        CommonDataAttachments.init();
        LimbusBlocks.init();
        LimbusItems.init();
        LimbusDataComponents.init();
        LimbusMenuTypes.init();
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void entityTick(LivingEntity livingEntity) {
        if (livingEntity.level().isClientSide) {

        } else {
            LimbusCombatTracker combatTracker = ((LivingEntityDuck)livingEntity).getLimbusCombatTracker();
            if (combatTracker.inCombat) {
                //- While in combat, every 10 second all involved parties will gain 5 SP (unless they toggle this in the Limbus table)
                long duration = combatTracker.getDuration();

                Map<Token, TokenInstance> tokenMap = DataAttachmentUtil.getTokens(livingEntity);

                for (TokenInstance token : tokenMap.values()) {
                    if (token.shouldTick(livingEntity, duration)) {
                        token.tick(livingEntity);
                    }
                }

                if (duration % 200 == 0 && hasSanity(livingEntity)) {
                    DataAttachmentUtil.addSanity(livingEntity,5);
                }
            }
        }
    }

    public static void triggerTremorBurst(LivingEntity livingEntity,int triggerTimes) {
        TokenInstance tokenInstance = DataAttachmentUtil.getTokens(livingEntity).get(TokenRegistry.TREMOR);
        if (tokenInstance != null) {
            int stack = tokenInstance.count();
            int actualTriggerCount = Math.min(stack, triggerTimes);
            int remainderStack = stack - actualTriggerCount;

            if (remainderStack > 0) {
                DataAttachmentUtil.addOrReplaceToken(livingEntity,tokenInstance.withCount(remainderStack));
            } else {
                DataAttachmentUtil.removeToken(livingEntity,TokenRegistry.TREMOR);
            }

            List<Float> staggerThresholds = DataAttachmentUtil.getStaggerThresholds(livingEntity);
            List<Float> newStaggerThresholds = new ArrayList<>();
            float currentHealth = livingEntity.getHealth();
            int staggerCount = 0;
            for (int i = 0; i < staggerThresholds.size(); i++) {
                float staggerThreshold = staggerThresholds.get(i);
                if (staggerCount + actualTriggerCount > currentHealth) {
                    staggerCount++;
                } else {
                    newStaggerThresholds.add(i, staggerThreshold + actualTriggerCount);
                }
            }

            if (staggerCount > 0) {
                LimbusCombatTracker.LOG.info("{} is staggered {} times", livingEntity.getName(), staggerCount);
                DataAttachmentUtil.setStaggerTimer(livingEntity, 120);
            }

            DataAttachmentUtil.setStaggerThresholds(livingEntity, newStaggerThresholds);
        }
    }

    public static void computeStaggerThresholds(LivingEntity entity) {
        RandomSource random = entity.getRandom();
        float maxHealth = entity.getMaxHealth();
        float currentHealth = entity.getHealth();
        int staggerLines = Math.min(5,(int)Math.ceil(maxHealth / 100));
        List<Float> staggerThresholds = new ArrayList<>();
        for (int i = 0; i < staggerLines; i++) {
            float sectionMin =  (float) i / staggerLines;
            float sectionMax = (float) (i+1) / staggerLines;

            float fraction = .15f + .70f * random.nextFloat();

            float sectionPos = sectionMin + (sectionMax - sectionMin) * fraction;

            float threshold = sectionPos * maxHealth;

            if (threshold >= currentHealth) {
                if (DEBUG) {
                    entity.getServer().getPlayerList().broadcastAll(new ClientboundSystemChatPacket(
                            Component.literal("Skipping stagger threshold at ")
                                    .append(threshold + "")
                                    .append(" for ")
                                    .append(entity.getName())
                            , false));
                }
                continue;
            }

            staggerThresholds.add(threshold);
        }

        if (DEBUG) {
            entity.getServer().getPlayerList().broadcastAll(new ClientboundSystemChatPacket(
                    Component.literal("Stagger thresholds for ")
                            .append(entity.getName()).append(" ")
                            .append(staggerThresholds.toString()
                    ),false));
        }

        DataAttachmentUtil.setStaggerThresholds(entity, staggerThresholds);
    }

    public static boolean hasSanity(LivingEntity livingEntity) {
        return livingEntity.getType().is(LimbusEntityTypeTags.HAS_SANITY);
    }

    public static void checkForStagger(LivingEntity livingEntity, float damage) {
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
            removeStaggerThresholds(livingEntity,staggerCount);
        }
        DataAttachmentUtil.setStaggered(livingEntity, staggerCount);
    }

    public static void removeStaggerThresholds(LivingEntity livingEntity,int count) {
        List<Float> staggerThresholds = DataAttachmentUtil.getStaggerThresholds(livingEntity);
        for (int i = 0; i < count; i++) {
            if (!staggerThresholds.isEmpty()) {
                staggerThresholds.removeLast();
            }
        }
        DataAttachmentUtil.setStaggerThresholds(livingEntity, staggerThresholds);
    }

    public static void onLimbusCrit(LivingEntity target,LivingEntity attacker) {

    }

}