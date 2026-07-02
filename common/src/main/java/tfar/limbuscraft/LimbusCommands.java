package tfar.limbuscraft;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import tfar.limbuscraft.attachments.DataAttachmentUtil;

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
        );
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
