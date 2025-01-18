package easton.varcommands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.HashMap;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class VariableCommands implements ModInitializer {

    public static final HashMap<String, String> VARS = new HashMap<>();

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("var").then(literal("get").then(argument("name", StringArgumentType.string()).executes(context -> {
                context.getSource().getPlayer().sendMessage(new LiteralText(VARS.get(StringArgumentType.getString(context, "name"))), false);
                return 1;
            }))).then(literal("set").then(argument("name", StringArgumentType.string()).then(argument("value", StringArgumentType.string()).executes(context -> {
                String name = StringArgumentType.getString(context, "name");
                String value = StringArgumentType.getString(context, "value");
                VARS.put(name, value);
                context.getSource().getPlayer().sendMessage(new LiteralText("Variable " + name + " has been set to " + value), false);
                return 1;
            })))).then(literal("operation").then(argument("var", StringArgumentType.string()).then(argument("operator", OperationArgumentType.operation()).then(argument("operand", StringArgumentType.string()).executes(context -> {
                Number solution = OperationArgumentType.getOperation(context, "operator").execute(getNumFromString(VARS.get(StringArgumentType.getString(context, "var"))), getNumFromString(StringArgumentType.getString(context, "operand")));
                VARS.put(StringArgumentType.getString(context, "var"), solution.toString());
                return 1;
            }))))).then(literal("deallocate").then(argument("var", StringArgumentType.string()).executes(context -> {
                VARS.remove(StringArgumentType.getString(context, "var"));
                return 1;
            }))));
        });
    }

    public static Number getNumFromString(String s) {
        if (s.contains("."))
            return Double.parseDouble(s);
        return Long.parseLong(s);
    }

    public static boolean executeComparison(String equality, CommandContext<ServerCommandSource> context, boolean positive) throws CommandSyntaxException {
        String var = VARS.get(StringArgumentType.getString(context, "variable"));
        String value = StringArgumentType.getString(context, "value");
        boolean isTrue = switch (equality) {
            default   -> (getNumFromString(var).doubleValue() == getNumFromString(value).doubleValue()) == positive;
            case ">"  -> (getNumFromString(var).doubleValue() > getNumFromString(value).doubleValue()) == positive;
            case "<"  -> (getNumFromString(var).doubleValue() < getNumFromString(value).doubleValue()) == positive;
            case ">=" -> (getNumFromString(var).doubleValue() >= getNumFromString(value).doubleValue()) == positive;
            case "<=" -> (getNumFromString(var).doubleValue() <= getNumFromString(value).doubleValue()) == positive;
        };
        if (isTrue) return true;
        throw new SimpleCommandExceptionType(new TranslatableText("commands.execute.conditional.fail")).create();
    }

}