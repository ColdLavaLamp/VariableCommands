package easton.varcommands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;

import java.util.concurrent.CompletableFuture;

public class OperationArgumentType implements ArgumentType<Operation> {

    @Override
    public Operation parse(StringReader reader) throws CommandSyntaxException {
        Operation op = Operation.CHAR_TO_OP.get(reader.peek());
        reader.skip();
        return op;
    }

    public static Operation getOperation(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Operation.class);
    }

    public static OperationArgumentType operation() {
        return new OperationArgumentType();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(new String[]{"+", "-", "*", "/", "^"}, builder);
    }
}
