package me.harpylmao.eustaquio.listeners;

import lombok.RequiredArgsConstructor;
import me.harpylmao.eustaquio.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class MusicListeners extends ListenerAdapter {

    private final Bot eustaquio;

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        Member eustaquioMember = event.getGuild().getMember(event.getJDA().getSelfUser());

        if (event.getMember().equals(eustaquioMember)) {
            eustaquioMember.deafen(true).queue();
        }
    }

    @Override
    public void onGuildVoiceGuildDeafen(@NotNull GuildVoiceGuildDeafenEvent event) {
        Member eustaquioMember = event.getGuild().getMember(event.getJDA().getSelfUser());

        if (event.getMember().equals(eustaquioMember)) {
            if (event.isGuildDeafened()) return;

            eustaquio.getGuildAudioManager().getGuildAudio(event.getGuild()).getTrackScheduler().getLogChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription(("Please do not undeafen the bot!")).build()).queue();
            eustaquioMember.deafen(true).queue();
        }
    }
}
