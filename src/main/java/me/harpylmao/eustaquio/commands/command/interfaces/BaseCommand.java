package me.harpylmao.eustaquio.commands.command.interfaces;

import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by Ryzeon Project: JDA-CommandAPI Date: 06/06/2021 @ 20:44 Twitter: @Ryzeon_ 😎 Github:
 * github.ryzeon.me
 */
public interface BaseCommand {

    void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event);

    default String usage() {
        return "N/A";
    }

    default List<String> aliases() {
        return Collections.emptyList();
    }

    String getName();

    default String category() {
        return "General";
    }
}
