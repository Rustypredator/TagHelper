package io.github.samarium150.minecraft.mod.taghelper.util;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class CommandUtil {
    
    private CommandUtil() { }
    
    public static final LiteralArgumentBuilder<CommandSourceStack> literal = Commands.literal(GeneralUtil.MOD_ID)
            .requires(commandSource -> commandSource.hasPermission(2));
    public static final LiteralArgumentBuilder<CommandSourceStack> alias = Commands.literal("th");
    
    // Slot ranges
    public static final int HOTBAR_START = 0;
    public static final int HOTBAR_END = 8;
    public static final int INVENTORY_START = 9;
    public static final int INVENTORY_END = 35;
    
    @Nullable
    public static ItemStack getMainHandItem(@Nonnull CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        ItemStack item = player.getMainHandItem();
        if (item.isEmpty()) {
            source.sendFailure(Component.literal("no item in the main hand"));
            return null;
        }
        return item;
    }
    
    @Nullable
    public static ItemStack getItemFromSlot(@Nonnull CommandSourceStack source, int slotId) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Inventory inventory = player.getInventory();
        
        // Check if slot is valid
        if (slotId < 0 || slotId >= 41) {
            source.sendFailure(Component.literal("invalid slot ID (must be between 0 and 40)"));
            return null;
        }
        
        ItemStack item;
        // Special slots
        if (slotId >= 36) {
            switch (slotId) {
                case 36: // Boots
                    item = player.getItemBySlot(EquipmentSlot.FEET);
                    break;
                case 37: // Leggings
                    item = player.getItemBySlot(EquipmentSlot.LEGS);
                    break;
                case 38: // Chestplate
                    item = player.getItemBySlot(EquipmentSlot.CHEST);
                    break;
                case 39: // Helmet
                    item = player.getItemBySlot(EquipmentSlot.HEAD);
                    break;
                case 40: // Offhand
                    item = player.getItemBySlot(EquipmentSlot.OFFHAND);
                    break;
                default:
                    // Should never reach here
                    source.sendFailure(Component.literal("invalid slot ID"));
                    return null;
            }
        } else {
            // Regular inventory slots (0-35)
            item = inventory.getItem(slotId);
        }
        
        if (item.isEmpty()) {
            source.sendFailure(Component.literal("no item in slot " + slotId));
            return null;
        }
        return item;
    }
    
    /**
     * Process all non-empty items in the hotbar (slots 0-8)
     * @param source The command source
     * @param itemConsumer Consumer function to apply to each non-empty item
     * @return Number of items processed
     */
    public static int processHotbarItems(@Nonnull CommandSourceStack source, @Nonnull Consumer<ItemStack> itemConsumer) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Inventory inventory = player.getInventory();
        int count = 0;
        
        for (int slot = HOTBAR_START; slot <= HOTBAR_END; slot++) {
            ItemStack item = inventory.getItem(slot);
            if (!item.isEmpty()) {
                itemConsumer.accept(item);
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Process all non-empty items in the main inventory (slots 9-35, excluding hotbar)
     * @param source The command source
     * @param itemConsumer Consumer function to apply to each non-empty item
     * @return Number of items processed
     */
    public static int processInventoryItems(@Nonnull CommandSourceStack source, @Nonnull Consumer<ItemStack> itemConsumer) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Inventory inventory = player.getInventory();
        int count = 0;
        
        for (int slot = INVENTORY_START; slot <= INVENTORY_END; slot++) {
            ItemStack item = inventory.getItem(slot);
            if (!item.isEmpty()) {
                itemConsumer.accept(item);
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Process all non-empty items in the player's ender chest
     * @param source The command source
     * @param itemConsumer Consumer function to apply to each non-empty item
     * @return Number of items processed
     */
    public static int processEnderChestItems(@Nonnull CommandSourceStack source, @Nonnull Consumer<ItemStack> itemConsumer) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Container enderChest = player.getEnderChestInventory();
        int count = 0;
        
        for (int slot = 0; slot < enderChest.getContainerSize(); slot++) {
            ItemStack item = enderChest.getItem(slot);
            if (!item.isEmpty()) {
                itemConsumer.accept(item);
                count++;
            }
        }
        
        return count;
    }
}
