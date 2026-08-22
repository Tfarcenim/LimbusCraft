package tfar.limbuscraft;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.tokens.Token;
import tfar.limbuscraft.tokens.TokenInstance;

import java.util.List;
import java.util.Map;

public record LimbusItemAttributes(List<Entry> entries) {
    public void apply(LivingEntity target) {
        RandomSource random = target.getRandom();
        Map<Token, TokenInstance> tokens = DataAttachmentUtil.getTokens(target);
        for (Entry entry : entries) {
            if (tokens.containsKey(entry.tokenInstance.token())) {
                tokens.put(entry.tokenInstance.token(),entry.tokenInstance.increment());
            } else {
                tokens.put(entry.tokenInstance.token(),entry.tokenInstance);
            }
        }
        DataAttachmentUtil.setTokens(target,tokens);
    }
    public record Entry(TokenInstance tokenInstance,double chance) {

    }
}
