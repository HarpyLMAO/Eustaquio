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

public class VolumeCommand implements BaseCommand {

    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        MusicManager musicManager = Bot.getInstance().getGuildAudioManager();
        if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
            textChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("There are no songs playing.").build()).queue();
            return;
        }

        if (args.length == 0) {
            command.reply("**ERROR:** Please provide a volume");
            return;
        }

        if (Integer.parseInt(args[0]) >= 401 || Integer.parseInt(args[0]) <= -1) {
            textChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("The volume is out of range! [0-400]").build()).queue();
            return;
        }

        try {
            musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().setVolume(Integer.parseInt(args[0]));
        } catch (IndexOutOfBoundsException e) {
            textChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Please specify a volume!").build()).queue();
        } catch (NumberFormatException e) {
            textChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("That is not a number!").build()).queue();
        }

        textChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("🔈 Set volume to " + args[0] + "!").build()).queue();
    }

    @Override
    public String usage() {
        return "/volume (volume)";
    }

    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String category() {
        return "music";
    }
}
