package me.harpylmao.eustaquio.commands.music;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.audio.MusicManager;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class ShuffleCommand implements BaseCommand {
    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        MusicManager musicManager = Bot.getInstance().getGuildAudioManager();
        if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
            textChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("There are no songs playing.").build()).queue();
            return;
        }
        musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().shuffle();
        textChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("🔀 Shuffled Queue!").build()).queue();
    }

    @Override
    public String usage() {
        return "/shuffle";
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public String category() {
        return "music";
    }
}
