package easton.varcommands.mixin;

import easton.varcommands.VariableCommands;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandManager.class)
public class CommandManagerMixin {

    @ModifyVariable(method = "execute", at = @At("HEAD"), ordinal = 0)
    public String parseVar(String command) {
        int index = command.indexOf('$');
        while (index != -1) {
            command = replaceVar(command, index);
            index = command.indexOf('$');
        }
        return command;
    }

    private static String replaceVar(String command, int index) {
        int spaceIndex = command.indexOf(' ', index);
        String key = spaceIndex == -1 ? command.substring(index + 1) : command.substring(index + 1, spaceIndex);
        return command.substring(0, index) + VariableCommands.VARS.get(key) + (spaceIndex != -1 ? command.substring(spaceIndex) : "");
    }
}
