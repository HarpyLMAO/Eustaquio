package me.harpylmao.eustaquio.commands.command.handler;

import lombok.AllArgsConstructor;
import me.harpylmao.eustaquio.commands.command.CommandManager;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import me.harpylmao.eustaquio.commands.command.objects.CommandPreConstructor;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Ryzeon Project: JDA-CommandAPI Date: 06/06/2021 @ 21:03 Twitter: @Ryzeon_ 😎 Github:
 * github.ryzeon.me
 */
@AllArgsConstructor
public class CommandHandler extends ListenerAdapter {

    private final CommandManager commandManager;

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.getMessage().getContentRaw().startsWith(commandManager.getPrefix())) return;
        if (event.getMessage().getContentRaw().equalsIgnoreCase("!")) return;
        CommandPreConstructor commandPreConstructor =
                new CommandPreConstructor(
                        event.getMessage().getContentRaw(), commandManager.getPrefix(), commandManager);
        if (commandPreConstructor.getBaseCommand() != null) {
            try {
                commandPreConstructor
                        .getBaseCommand()
                        .execute(
                                new CommandEvent(
                                        event.getJDA(),
                                        (int) event.getResponseNumber(),
                                        event.getMessage(),
                                        commandPreConstructor
                                ),
                                event.getChannel(),
                                event.getMember(),
                                commandPreConstructor.getArgs(),
                                event);
            } catch (Exception exception) {
                commandManager
                        .getLogger()
                        .warning("An error occurred while executing " + commandPreConstructor.getLabel());
                event.getMessage().reply(commandManager.getErrorMessage()).queue();
                exception.printStackTrace();
            }
        } else {
            if (commandManager.isSendMessageIfCommandNoFound())
                event.getMessage().reply(commandManager.getNoFoundMessage().replace("$1", commandPreConstructor.getLabel())).queue();
        }
    }
}
