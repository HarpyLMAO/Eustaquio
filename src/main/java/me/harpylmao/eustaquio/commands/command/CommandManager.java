package me.harpylmao.eustaquio.commands.command;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.harpylmao.eustaquio.commands.command.handler.CommandHandler;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import net.dv8tion.jda.api.JDA;
import org.reflections8.Reflections;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Ryzeon Project: JDA-CommandAPI Date: 06/06/2021 @ 20:44 Twitter: @Ryzeon_ 😎 Github:
 * github.ryzeon.me
 */
public class CommandManager {

    public static CommandManager INSTANCE;

    @Getter
    private final Logger logger = Logger.getLogger("CommandAPI");

    @Getter
    private final List<BaseCommand> commands = new ArrayList<>();

    @Getter
    private final JDA jda;

    @Getter
    private final String prefix;

    @NonNull
    @Setter
    @Getter
    private Color color = Color.GREEN;

    @Getter
    @Setter
    private String noFoundMessage = "No hemos encontrado ningun comando para $1";

    @Getter
    @Setter
    private boolean sendMessageIfCommandNoFound = true;

    @Getter
    @Setter
    private String errorMessage = "Hemos encontrado un error mientras ejecutabamos el comando";

    public CommandManager(JDA jda, String prefix) {
        this.jda = jda;
        this.prefix = prefix;
        INSTANCE = this;
    }

    public void init() {
        jda.addEventListener(new CommandHandler(this));
        logger.info("Successfully init commands!");
    }

    public void registerCommands() {
        Reflections reflections = new Reflections();
        reflections.getSubTypesOf(BaseCommand.class).forEach(clazz -> {
            try {
                BaseCommand command = clazz.newInstance();
                commands.add(command);
                System.out.println("[Eustaquio] -> Registered " + command.getClass().getSimpleName() + " command.");
            } catch (Exception e) {
                System.out.println("CommandException -> " + e.getMessage());
            }
        });
        System.out.println("[Eustaquio] -> " + commands.size() + " of commands registereds successfully.");
    }

    public BaseCommand getCommandByNameOrAlias(String name) {
        for (BaseCommand baseCommand : commands) {
            if (baseCommand.getName() == null) { return null; }
            if (baseCommand.getName().equalsIgnoreCase(name)) return baseCommand;
            if (!baseCommand.aliases().isEmpty()) {
                for (String alt : baseCommand.aliases()) {
                    if (alt.equalsIgnoreCase(name)) return baseCommand;
                }
            }
        }
        return null;
    }
}
