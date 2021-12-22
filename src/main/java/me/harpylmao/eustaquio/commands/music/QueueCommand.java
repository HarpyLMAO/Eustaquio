package me.harpylmao.eustaquio.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.audio.MusicManager;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class QueueCommand implements BaseCommand {

    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member author, String[] args, GuildMessageReceivedEvent event) {
        MusicManager musicManager = Bot.getInstance().getGuildAudioManager();
        EmbedBuilder embed = new EmbedBuilder();

        if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
            embed.setDescription("There's no song in the queue for me to play. **/play** a song first.");
            textChannel.sendMessage(embed.build()).queue();
            return;
        }
        List<AudioTrack> tracks = musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue();
        if (tracks.size() == 0 || tracks.get(0) == null) {
            embed.setDescription("There's no song in the queue for me to play. **/play** a song first.");
            textChannel.sendMessage(embed.build()).queue();
            return;
        }

        int totalLength = 0;
        String description = "";
        int finish = tracks.size();
        if (finish > 11) {
            finish = 11;
        }
        for (int i = 0; i < finish; i++) {
            AudioTrack track = tracks.get(i);
            long msPos = track.getInfo().length;
            long minPos = msPos / 60000;
            msPos = msPos % 60000;
            int secPos = (int) Math.floor((float) msPos / 1000f);
            String length = minPos + ":" + ((secPos < 10) ? "0" + secPos : secPos);
            String song = "[" + track.getInfo().title + "](" + track.getInfo().uri + ")";
            if (i == 0) {
                description += "__Now Playing:__";
            } else if (i == 1) {
                description += "\n__Up Next:__";
            }

            if (i == 0) {
                description += String.format("\n%s | `%s`\n", song, length);
            } else {
                description += String.format("\n`%d.` %s | `%s`\n", i, song, length);
            }
            totalLength += track.getInfo().length;
        }
        int minTime = totalLength / 60000;
        totalLength %= 60000;
        int secTime = totalLength / 1000;

        if (tracks.size() > 1) {
            description += "\n**" + (tracks.size() - 1) + " Songs in Queue | " + minTime + ":" + ((secTime < 10) ? "0" + secTime : secTime) + " Total Length**";
        }

        embed.setTitle("Music Queue");
        embed.setDescription(description);

        textChannel.sendMessage(embed.build()).queue();
        return;
    }

    @Override
    public String category() {
        return "music";
    }

    @Override
    public String usage() {
        return "USAGE: \n" +
                " - /queue [song name]/[song/playlist link]";
    }

    @Override
    public String getName() {
        return "queue";
    }
}
