package dev.nocheatenchant;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class HoldEnchantMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            var enchantmentLookup = registryAccess.getOrThrow(RegistryKeys.ENCHANTMENT);
            var command = CommandManager.literal("giveenchant")
                    .then(CommandManager.argument("enchantment", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.ENCHANTMENT))
                        .suggests((context, builder) -> {
                            enchantmentLookup.streamKeys().forEach(key -> builder.suggest(key.getValue().toString()));
                            return builder.buildFuture();
                        })
                        .then(CommandManager.argument("level", IntegerArgumentType.integer(1, 255))
                            .suggests((context, builder) -> {
                                for (int suggestedLevel = 1; suggestedLevel <= 255; suggestedLevel++) {
                                    builder.suggest(suggestedLevel);
                                }
                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                var player = context.getSource().getPlayer();
                                ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
                                if (stack.isEmpty()) {
                                    player.sendMessage(Text.literal("You are not holding an item!"), false);
                                    return 0;
                                }

                                var enchantment = RegistryEntryReferenceArgumentType.getEnchantment(context, "enchantment");
                                int level = IntegerArgumentType.getInteger(context, "level");
                                stack.addEnchantment(enchantment, level);
                                player.sendMessage(Text.literal("Enchanted successfully!"), false);
                                return 1;
                            })
                        )
                    );
            var commandNode = dispatcher.register(command);
            dispatcher.register(CommandManager.literal("giveechant").redirect(commandNode));
        });
    }
}