package easton.varcommands.mixin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import easton.varcommands.VariableCommands;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ExecuteCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@Mixin(ExecuteCommand.class)
public abstract class ExecuteCommandMixin {

    @Shadow
    private static Collection<ServerCommandSource> getSourceOrEmptyForConditionFork(CommandContext<ServerCommandSource> context, boolean positive, boolean value) {
        return null;
    }

    @Inject(method = "addConditionArguments", at = @At("RETURN"))
    private static void addVarIf(CommandNode<ServerCommandSource> root, LiteralArgumentBuilder<ServerCommandSource> argumentBuilder, boolean positive, CallbackInfoReturnable<ArgumentBuilder<ServerCommandSource, ?>> cir) {
        argumentBuilder.then(literal("var").then(argument("variable", StringArgumentType.string())
                .then(literal("=").then(argument("value", StringArgumentType.string()).fork(root, context -> getSourceOrEmptyForConditionFork(context, positive, VariableCommands.executeComparison("=", context, positive))).executes(context -> VariableCommands.executeComparison("=", context, positive) ? 1 : -1)))
                .then(literal(">").then(argument("value", StringArgumentType.string()).fork(root, context -> getSourceOrEmptyForConditionFork(context, positive, VariableCommands.executeComparison(">", context, positive))).executes(context -> VariableCommands.executeComparison(">", context, positive) ? 1 : -1)))
                .then(literal("<").then(argument("value", StringArgumentType.string()).fork(root, context -> getSourceOrEmptyForConditionFork(context, positive, VariableCommands.executeComparison("<", context, positive))).executes(context -> VariableCommands.executeComparison("<", context, positive) ? 1 : -1)))
                .then(literal(">=").then(argument("value", StringArgumentType.string()).fork(root, context -> getSourceOrEmptyForConditionFork(context, positive, VariableCommands.executeComparison(">=", context, positive))).executes(context -> VariableCommands.executeComparison(">=", context, positive) ? 1 : -1)))
                .then(literal("<=").then(argument("value", StringArgumentType.string()).fork(root, context -> getSourceOrEmptyForConditionFork(context, positive, VariableCommands.executeComparison("<=", context, positive))).executes(context -> VariableCommands.executeComparison("<=", context, positive) ? 1 : -1)))));
    }
}
