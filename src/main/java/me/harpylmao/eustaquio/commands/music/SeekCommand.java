package me.harpylmao.eustaquio.commands.music;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.audio.MusicManager;
import me.harpylmao.eustaquio.audio.TrackScheduler;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class SeekCommand implements BaseCommand {
    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        MusicManager musicManager = Bot.getInstance().getGuildAudioManager();
        if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
            textChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("There are no songs playing.").build()).queue();
            return;
        }

        TrackScheduler trackScheduler = musicManager.getGuildAudio(event.getGuild()).getTrackScheduler();
        try {
            trackScheduler.getTrackQueue().get(0).setPosition(Math.min(Integer.parseInt(args[0]) * 1000L, trackScheduler.getTrackQueue().get(0).getDuration()));
        } catch (IndexOutOfBoundsException e) {
            textChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Please specify a time to seek to!").build()).queue();
            return;
        } catch (NumberFormatException e) {
            textChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Please specify a *number*!").build()).queue();
            return;
        }

        textChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Seeked to " + args[0] + " seconds on song `" + trackScheduler.getTrackQueue().get(0).getInfo().title + "`!").build()).queue();
    }

    @Override
    public String usage() {
        return "/seek (time [120 = 2 minutes])";
    }

    @Override
    public String getName() {
        return "seek";
    }

    @Override
    public String category() {
        return "music";
    }
}
