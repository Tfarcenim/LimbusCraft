package tfar.limbuscraft;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import tfar.limbuscraft.attachments.DataAttachmentUtil;
import tfar.limbuscraft.tokens.Token;
import tfar.limbuscraft.tokens.TokenInstance;
import tfar.limbuscraft.tokens.TokenRegistry;

import java.util.Collection;

public class LimbusCommands {


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(LimbusCraft.MOD_ID)
                .then(Commands.literal("sanity")
                        .then(Commands.literal("set")
                                .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .then(Commands.argument("sanity", IntegerArgumentType.integer(LimbusStats.MIN_SANITY,LimbusStats.MAX_SANITY))
                                                .executes(LimbusCommands::setSanity)
                                        )
                                )
                        )
                )
                .then(Commands.literal("token")
                        .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.literal("clear")
                                .executes(LimbusCommands::clearTokensFromSelf)
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .executes(LimbusCommands::clearTokensFromTargets)
                                        .then(Commands.argument("token", StringArgumentType.string()).suggests(
                                                (context, builder) ->
                                                        SharedSuggestionProvider.suggest(TokenRegistry.TOKENS.keySet(),builder)
                                                ).executes(LimbusCommands::clearTokenFromTargets)
                                        )
                                )
                        ).then(Commands.literal("give")
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .then(Commands.argument("token", StringArgumentType.string()).suggests(
                                                        (context, builder) ->
                                                                SharedSuggestionProvider.suggest(TokenRegistry.TOKENS.keySet(),builder)
                                                ).then(Commands.argument("potency", IntegerArgumentType.integer(1))
                                                        .then(Commands.argument("count", IntegerArgumentType.integer(1))
                                                                .executes(LimbusCommands::addTokenToTargets)
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    static int clearTokensFromSelf(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        DataAttachmentUtil.clearTokens(player);
        return 1;
    }

    static int clearTokensFromTargets(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(ctx,"targets");
        for (Entity target : targets) {
            if (target instanceof LivingEntity livingTarget) {
                DataAttachmentUtil.clearTokens(livingTarget);
            }
        }
        return 1;
    }

    public static final SimpleCommandExceptionType ERROR_NOI_A_TOKEN = new SimpleCommandExceptionType(
            Component.literal("No such token")
    );

    static int clearTokenFromTargets(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(ctx,"targets");
        Token token = TokenRegistry.TOKENS.get(StringArgumentType.getString(ctx, "token"));
        if (token == null) {
            throw ERROR_NOI_A_TOKEN.create();
        }
        for (Entity target : targets) {
            if (target instanceof LivingEntity livingTarget) {
                DataAttachmentUtil.removeToken(livingTarget, token);
            }
        }
        return 1;
    }

    static int addTokenToTargets(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Token token = TokenRegistry.TOKENS.get(StringArgumentType.getString(ctx, "token"));
        if (token == null) {
            throw ERROR_NOI_A_TOKEN.create();
        }
        Collection<? extends Entity> targets = EntityArgument.getEntities(ctx,"targets");
        int potency = IntegerArgumentType.getInteger(ctx, "potency");
        int count  = IntegerArgumentType.getInteger(ctx, "count");
        TokenInstance tokenInstance = new TokenInstance(token,potency,count);
        for (Entity target : targets) {
            if (target instanceof LivingEntity livingTarget) {
                DataAttachmentUtil.addOrReplaceToken(livingTarget, tokenInstance);
            }
        }
        return 1;
    }

    static int setSanity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(context,"targets");
        int sanity = IntegerArgumentType.getInteger(context,"sanity");
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                DataAttachmentUtil.setSanity(livingEntity,sanity);
            }
        }
        return 1;
    }
}
