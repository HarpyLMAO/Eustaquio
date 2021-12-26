package me.harpylmao.eustaquio.commands.music;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PlayCommand implements BaseCommand {

    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member author, String[] args, GuildMessageReceivedEvent event) {
        Bot.getInstance().getGuildAudioManager().loadAndPlay(command, args[0], true);
    }

    @Override
    public String category() {
        return "music";
    }

    @Override
    public String usage() {
        return "USAGE: \n" +
                " - /play [song name]/[song/playlist link]";
    }

    @Override
    public String getName() {
        return "play";
    }
}
