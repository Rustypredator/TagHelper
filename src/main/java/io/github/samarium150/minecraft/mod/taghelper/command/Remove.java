package io.github.samarium150.minecraft.mod.taghelper.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.samarium150.minecraft.mod.taghelper.config.TagHelperConfig;
import io.github.samarium150.minecraft.mod.taghelper.util.CommandUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.Nonnull;

public final class Remove {
    
    private Remove() { }
    
    private static int executesHoldingWithTag(@Nonnull CommandContext<CommandSourceStack> context, String tag)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (TagHelperConfig.enableRemoveCommand.get()) {
            ItemStack item = CommandUtil.getMainHandItem(source);
            if (item == null) return Command.SINGLE_SUCCESS;
            removeTagAndNotify(source, item, tag);
        } else
            source.sendFailure(new TextComponent("remove command is disabled in config"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesHolding(@Nonnull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (TagHelperConfig.enableRemoveCommand.get()) {
            ItemStack item = CommandUtil.getMainHandItem(source);
            if (item == null) return Command.SINGLE_SUCCESS;
            removeAllTagsAndNotify(source, item);
        } else
            source.sendFailure(new TextComponent("remove command is disabled in config"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesSlotWithTag(@Nonnull CommandContext<CommandSourceStack> context, String tag)
            throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (TagHelperConfig.enableRemoveCommand.get()) {
            int slotId = IntegerArgumentType.getInteger(context, "slot");
            ItemStack item = CommandUtil.getItemFromSlot(source, slotId);
            if (item == null) return Command.SINGLE_SUCCESS;
            removeTagAndNotify(source, item, tag);
        } else
            source.sendFailure(new TextComponent("remove command is disabled in config"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static int executesSlot(@Nonnull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (TagHelperConfig.enableRemoveCommand.get()) {
            int slotId = IntegerArgumentType.getInteger(context, "slot");
            ItemStack item = CommandUtil.getItemFromSlot(source, slotId);
            if (item == null) return Command.SINGLE_SUCCESS;
            removeAllTagsAndNotify(source, item);
        } else
            source.sendFailure(new TextComponent("remove command is disabled in config"));
        return Command.SINGLE_SUCCESS;
    }
    
    private static void removeTagAndNotify(CommandSourceStack source, ItemStack item, String tag) {
        CompoundTag targetNBT = item.getTag();
        if (targetNBT == null) return;
        targetNBT.remove(tag);
        item.setTag(targetNBT);
        MutableComponent text = new TextComponent("current NBT: ")
            .append(targetNBT.toString());
        source.sendSuccess(text, false);
    }
    
    private static void removeAllTagsAndNotify(CommandSourceStack source, ItemStack item) {
        item.setTag(null);
        source.sendSuccess(new TextComponent("NBT is removed"), false);
    }
    
    public static void register(@Nonnull CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> literal = CommandUtil.literal
                .then(Commands.literal("holding")
                    .then(Commands.literal("remove")
                        .then(Commands.argument("key", StringArgumentType.string())
                            .executes(context -> executesHoldingWithTag(context,
                                StringArgumentType.getString(context, "key")))
                        )
                        .executes(Remove::executesHolding)
                    )
                )
                .then(Commands.literal("slot")
                    .then(Commands.argument("slot", IntegerArgumentType.integer(0, 40))
                        .then(Commands.literal("remove")
                            .then(Commands.argument("key", StringArgumentType.string())
                                .executes(context -> executesSlotWithTag(context,
                                    StringArgumentType.getString(context, "key")))
                            )
                            .executes(Remove::executesSlot)
                        )
                    )
                );
        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(literal);
        dispatcher.register(CommandUtil.alias.redirect(cmd));
    }
}
