package tfar.limbuscraft;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.limbuscraft.attachments.CommonDataAttachments;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.ducks.LivingEntityDuck;
import tfar.limbuscraft.init.LimbusBlocks;
import tfar.limbuscraft.init.LimbusItems;
import tfar.limbuscraft.init.LimbusMenuTypes;
import tfar.limbuscraft.platform.Services;
import tfar.limbuscraft.tokens.Token;
import tfar.limbuscraft.tokens.TokenInstance;
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
                    if (token.shouldTick(duration)) {
                        token.tick(livingEntity);
                    }
                }

                if (duration % 200 == 0) {
                    DataAttachmentUtil.setSanity(livingEntity,DataAttachmentUtil.getSanity(livingEntity)+5);
                }
            }
        }
    }

    public static void computeStaggerThresholds(LivingEntity entity) {
        RandomSource random = entity.getRandom();
        float maxHealth = entity.getMaxHealth();
        int staggerLines = Math.min(5,(int)Math.ceil(maxHealth / 100));
        List<Float> staggerThresholds = new ArrayList<>();
        for (int i = 0; i < staggerLines; i++) {
            float sectionMin =  (float) i / staggerLines;
            float sectionMax = (float) (i+1) / staggerLines;

            float fraction = .15f + .70f * random.nextFloat();

            float sectionPos = sectionMin + (sectionMax - sectionMin) * fraction;

            float threshold = sectionPos * maxHealth;
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
}