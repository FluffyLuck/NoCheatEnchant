package dev.nocheatenchant;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class HoldEnchantMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(
                CommandManager.literal("giveenchant")
                    .then(CommandManager.argument("enchantment", IdentifierArgumentType.identifier())
                        .executes(context -> {
                            var player = context.getSource().getPlayer();
                            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
                            if (stack.isEmpty()) {
                                player.sendMessage(Text.literal("You are not holding an item!"), false);
                                return 0;
                            }

                            Identifier enchantmentId = context.getArgument("enchantment", Identifier.class);
                            var enchantment = context.getSource().getServer().getRegistryManager()
                                .getOrThrow(RegistryKeys.ENCHANTMENT)
                                .getEntry(enchantmentId);
                            if (enchantment.isEmpty()) {
                                player.sendMessage(Text.literal("Unknown enchantment: " + enchantmentId), false);
                                return 0;
                            }

                            stack.addEnchantment(enchantment.get(), 1);
                            player.sendMessage(Text.literal("Enchanted successfully!"), false);
                            return 1;
                        })
                    )
            )
        );
    }
}