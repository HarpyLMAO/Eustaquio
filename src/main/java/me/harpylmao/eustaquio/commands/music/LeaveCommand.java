package me.harpylmao.eustaquio.commands.music;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class LeaveCommand implements BaseCommand {

    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        if ((event.getMember() == null) ||
                (event.getMember().getVoiceState() == null) ||
                !event.getMember().getVoiceState().inVoiceChannel() ||
                (event.getMember().getVoiceState().getChannel() == null)) {
            textChannel.sendMessage(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("You are not in a voice channel!").build()).queue();
            return;
        }

        Bot.getInstance().getGuildAudioManager().leaveVoiceChannel(event.getGuild());
        textChannel.sendMessage(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Left voice channel!").build()).queue();
    }

    @Override
    public String category() {
        return "music";
    }

    @Override
    public String usage() {
        return "USAGE: \n" +
                " - /leave";
    }

    @Override
    public String getName() {
        return "leave";
    }
}
