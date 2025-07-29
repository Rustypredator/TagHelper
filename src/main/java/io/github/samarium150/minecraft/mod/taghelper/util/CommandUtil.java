package io.github.samarium150.minecraft.mod.taghelper.util;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class CommandUtil {
    
    private CommandUtil() { }
    
    public static final LiteralArgumentBuilder<CommandSourceStack> literal = Commands.literal(GeneralUtil.MOD_ID)
            .requires(commandSource -> commandSource.hasPermission(2));
    public static final LiteralArgumentBuilder<CommandSourceStack> alias = Commands.literal("th");
    
    @Nullable
    public static ItemStack getMainHandItem(@Nonnull CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        ItemStack item = player.getMainHandItem();
        if (item.isEmpty()) {
            source.sendFailure(new TextComponent("no item in the main hand"));
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
            source.sendFailure(new TextComponent("invalid slot ID (must be between 0 and 40)"));
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
                    source.sendFailure(new TextComponent("invalid slot ID"));
                    return null;
            }
        } else {
            // Regular inventory slots (0-35)
            item = inventory.getItem(slotId);
        }
        
        if (item.isEmpty()) {
            source.sendFailure(new TextComponent("no item in slot " + slotId));
            return null;
        }
        return item;
    }
}
