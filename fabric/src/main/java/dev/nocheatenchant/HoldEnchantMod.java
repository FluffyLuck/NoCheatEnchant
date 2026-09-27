package dev.nocheatenchant;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class HoldEnchantMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(
                CommandManager.literal("giveenchant")
                    .then(CommandManager.argument("enchantment", RegistryEntryArgumentType.enchantment(registryAccess))
                        .executes(context -> {
                            var player = context.getSource().getPlayer();
                            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
                            if (stack.isEmpty()) {
                                player.sendMessage(Text.literal("You are not holding an item!"), false);
                                return 0;
                            }

                            var enchantment = RegistryEntryArgumentType.getEnchantment(context, "enchantment");
                            stack.addEnchantment(enchantment, 1);
                            player.sendMessage(Text.literal("Enchanted successfully!"), false);
                            return 1;
                        })
                    )
            )
        );
    }
}