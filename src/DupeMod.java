package com.example.dupe;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class DupeMod implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("dupe")  // Без слеша в коде, в игре будет /dupe
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    PlayerEntity player = source.getPlayer();
                    
                    if (player == null) {
                        source.sendError(Text.literal("Эта команда доступна только игрокам!"));
                        return 0;
                    }

                    ItemStack itemInOffHand = player.getOffHandStack();

                    if (itemInOffHand.isEmpty()) {
                        player.sendMessage(Text.literal("§c[Ошибка] В левой руке нет предмета!"), false);
                        return 0;
                    }

                    ItemStack duplicatedItem = itemInOffHand.copy();
                    
                    if (!player.getInventory().insertStack(duplicatedItem)) {
                        player.dropItem(duplicatedItem, false);
                        player.sendMessage(Text.literal("§aПредмет продублирован и выпал на землю!"), false);
                    } else {
                        player.sendMessage(Text.literal("§aПредмет в левой руке продублирован!"), false);
                    }

                    return 1;
                })
            );
        });
    }
}