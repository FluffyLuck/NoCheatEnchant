package dev.nocheatenchant;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("nocheatenchant")
public class HoldEnchantMod {
    public HoldEnchantMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        var enchantmentLookup = event.getBuildContext().holderLookup(Registries.ENCHANTMENT);
        var command = Commands.literal("giveenchant")
                .then(Commands.argument("enchantment", ResourceArgument.resource(event.getBuildContext(), Registries.ENCHANTMENT))
                    .suggests((context, builder) -> {
                        enchantmentLookup.listElementIds().forEach(id -> builder.suggest(id.toString()));
                        return builder.buildFuture();
                    })
                    .then(Commands.argument("level", IntegerArgumentType.integer(1, 255))
                        .suggests((context, builder) -> {
                            for (int suggestedLevel = 1; suggestedLevel <= 255; suggestedLevel++) {
                                builder.suggest(suggestedLevel);
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayer();
                            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                            if (stack.isEmpty()) {
                                player.sendSystemMessage(Component.literal("You are not holding an item!"));
                                return 0;
                            }

                            Enchantment enchantment = ResourceArgument.getEnchantment(context, "enchantment").value();
                            int level = IntegerArgumentType.getInteger(context, "level");
                            stack.enchant(enchantment, level);
                            player.sendSystemMessage(Component.literal("Enchanted successfully!"));
                            return 1;
                        })
                    )
                );
            var commandNode = event.getDispatcher().register(command);
            event.getDispatcher().register(Commands.literal("giveechant").redirect(commandNode));
    }
}