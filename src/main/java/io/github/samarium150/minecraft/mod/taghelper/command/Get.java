package io.github.samarium150.minecraft.mod.taghelper.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.samarium150.minecraft.mod.taghelper.config.TagHelperConfig;
import io.github.samarium150.minecraft.mod.taghelper.permission.PermissionManager;
import io.github.samarium150.minecraft.mod.taghelper.util.CommandUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class Get {
    
    private Get() { }
    
    private static int executesHolding(@Nonnull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "get")) {
            ItemStack item = CommandUtil.getMainHandItem(source);
            if (item == null) return Command.SINGLE_SUCCESS;
            displayNBT(source, item);
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesSlot(@Nonnull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "get")) {
            int slotId = IntegerArgumentType.getInteger(context, "slot");
            ItemStack item = CommandUtil.getItemFromSlot(source, slotId);
            if (item == null) return Command.SINGLE_SUCCESS;
            displayNBT(source, item);
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesHotbar(@Nonnull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "get") && TagHelperConfig.enableHotbarCommands.get()) {
            List<ItemStack> processedItems = new ArrayList<>();
            int count = CommandUtil.processHotbarItems(source, item -> processedItems.add(item));
            
            if (count == 0) {
                source.sendFailure(Component.literal("no items found in hotbar"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSystemMessage(Component.literal("Processing " + count + " items in hotbar:"));
            for (int i = 0; i < processedItems.size(); i++) {
                ItemStack item = processedItems.get(i);
                MutableComponent text = Component.literal("Item " + (i + 1) + ": " + item.getDisplayName().getString() + " - ");
                displayNBT(source, item, text);
            }
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesInventory(@Nonnull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "get") && TagHelperConfig.enableInventoryCommands.get()) {
            List<ItemStack> processedItems = new ArrayList<>();
            int count = CommandUtil.processInventoryItems(source, item -> processedItems.add(item));
            
            if (count == 0) {
                source.sendFailure(Component.literal("no items found in inventory"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSystemMessage(Component.literal("Processing " + count + " items in inventory:"));
            for (int i = 0; i < processedItems.size(); i++) {
                ItemStack item = processedItems.get(i);
                MutableComponent text = Component.literal("Item " + (i + 1) + ": " + item.getDisplayName().getString() + " - ");
                displayNBT(source, item, text);
            }
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesEChest(@Nonnull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (PermissionManager.hasPermission(source, "get") && TagHelperConfig.enableEnderChestCommands.get()) {
            List<ItemStack> processedItems = new ArrayList<>();
            int count = CommandUtil.processEnderChestItems(source, item -> processedItems.add(item));
            
            if (count == 0) {
                source.sendFailure(Component.literal("no items found in ender chest"));
                return Command.SINGLE_SUCCESS;
            }
            
            source.sendSystemMessage(Component.literal("Processing " + count + " items in ender chest:"));
            for (int i = 0; i < processedItems.size(); i++) {
                ItemStack item = processedItems.get(i);
                MutableComponent text = Component.literal("Item " + (i + 1) + ": " + item.getDisplayName().getString() + " - ");
                displayNBT(source, item, text);
            }
        } else
            source.sendFailure(Component.literal("You don't have permission to use this command"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static void displayNBT(CommandSourceStack source, ItemStack item) {
        CompoundTag targetNBT = item.getTag();
        MutableComponent text = Component.literal("NBT: ");
        if (targetNBT == null)
            text.append("null");
        else
            text.append(targetNBT.toString());
        source.sendSystemMessage(text);
    }
    
    private static void displayNBT(CommandSourceStack source, ItemStack item, MutableComponent prefix) {
        CompoundTag targetNBT = item.getTag();
        if (targetNBT == null)
            prefix.append("null");
        else
            prefix.append(targetNBT.toString());
        source.sendSystemMessage(prefix);
    }
    
    public static void register(@Nonnull CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> literal = CommandUtil.literal
            .then(Commands.literal("holding")
                .then(Commands.literal("get")
                    .executes(Get::executesHolding)))
            .then(Commands.literal("slot")
                .then(Commands.argument("slot", IntegerArgumentType.integer(0, 40))
                    .then(Commands.literal("get")
                        .executes(Get::executesSlot))))
            .then(Commands.literal("hotbar")
                .then(Commands.literal("get")
                    .executes(Get::executesHotbar)))
            .then(Commands.literal("inventory")
                .then(Commands.literal("get")
                    .executes(Get::executesInventory)))
            .then(Commands.literal("echest")
                .then(Commands.literal("get")
                    .executes(Get::executesEChest)));
                                        
        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(literal);
        dispatcher.register(CommandUtil.alias.redirect(cmd));
    }
}
