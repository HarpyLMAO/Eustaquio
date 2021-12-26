package me.harpylmao.eustaquio.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.beam.BeamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.getyarn.GetyarnAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.audio.factories.SearchFactory;
import me.harpylmao.eustaquio.audio.guild.GuildMusicManager;
import me.harpylmao.eustaquio.audio.handlers.AudioSoundLoadHandler;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MusicManager {
	private Logger logger = LoggerFactory.getLogger(MusicManager.class);

	private AudioPlayerManager playerManager;
	private Map<Long, GuildMusicManager> guildMusicManager;

	private Bot eustaquio;

	public MusicManager(Bot eustaquio) {
		this.eustaquio = eustaquio;

		this.logger.info("Loading AudioManager...");
		this.guildMusicManager = new HashMap<>();
		this.playerManager = new DefaultAudioPlayerManager();

		this.playerManager.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
		this.playerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
		this.playerManager.getConfiguration().setFilterHotSwapEnabled(true);

		AudioSourceManagers.registerRemoteSources(this.playerManager);
		AudioSourceManagers.registerLocalSource(this.playerManager);

		this.playerManager.registerSourceManager(new BeamAudioSourceManager());
		this.playerManager.registerSourceManager(new HttpAudioSourceManager());
		this.playerManager.registerSourceManager(new VimeoAudioSourceManager());
		this.playerManager.registerSourceManager(new GetyarnAudioSourceManager());
		this.playerManager.registerSourceManager(new YoutubeAudioSourceManager());
		this.playerManager.registerSourceManager(new BandcampAudioSourceManager());
		this.playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
		this.playerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());

		this.logger.info("Loaded AudioManager!");
	}

	/**
	 * Get's Guild Audio Manger.
	 *
	 * @param guild Guild to get AudioManager.
	 * @return GuildMusicManager.
	 */
	public synchronized GuildMusicManager getGuildAudio(Guild guild) {
		return this.guildMusicManager.computeIfAbsent(guild.getIdLong(), (guildAudio) -> {
			GuildMusicManager guildMusicManager = new GuildMusicManager(this, guild);

			guild.getAudioManager().setSendingHandler(guildMusicManager.getAudioSendProvider());

			return guildMusicManager;
		});
	}

	/**
	 * Join's Voice Chanel and set's log channel.
	 *
	 * @param voiceChannel   Voice Channel.
	 * @param messageChannel Message Channel.
	 * @param guild          Guild.
	 */
	public void joinVoiceChannel(VoiceChannel voiceChannel, TextChannel messageChannel, Guild guild, Member member) {
		AudioManager audioManager = guild.getAudioManager();
		GuildMusicManager guildMusicManager = getGuildAudio(guild);

		guildMusicManager.getTrackScheduler().setLogChannel(messageChannel);

		if (voiceChannel instanceof StageChannel) {
			try {
				StageChannel stageChannel = (StageChannel) voiceChannel;
				if (stageChannel.getStageInstance() == null) {
					stageChannel.createStageInstance("Music").queue();
				}
				guild.requestToSpeak();
				audioManager.openAudioConnection(stageChannel);
			} catch (Exception e) {
				messageChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Cannot join stage channel.").build()).queue();
			}
			return;
		}

		audioManager.openAudioConnection(voiceChannel);
	}

	/**
	 * Leave's Voice Channel.
	 *
	 * @param guild Guild
	 */
	public void leaveVoiceChannel(Guild guild) {
		guild.getAudioManager().closeAudioConnection();
		this.getGuildAudio(guild).getTrackScheduler().clearQueue();
	}

	/**
	 * Loads and plays a track by it's URL.
	 *
	 * @param event       CommandEvent for getting details.
	 * @param trackURL    The URL of the track you want to play.
	 * @param sendMessage Sends message if true.
	 */
	public void loadAndPlay(CommandEvent event, String trackURL, boolean sendMessage) {
		GuildMusicManager musicManager = getGuildAudio(event.getGuild());
		Member member = event.getMember();

		if (member == null || member.getVoiceState() == null || !member.getVoiceState().inVoiceChannel() || member.getVoiceState().getChannel() == null) {
			event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Please connect to a voice channel first!").build()).queue();
			return;
		}

		this.joinVoiceChannel(member.getVoiceState().getChannel(), event.getChannel(), event.getGuild(), event.getMember());

		musicManager.getTrackScheduler().setLogChannel(event.getChannel());

		TrackScheduler trackScheduler = musicManager.getTrackScheduler();
		String finalTrackURL = new SearchFactory(trackURL, event , trackScheduler, playerManager, eustaquio).search();

		if (finalTrackURL == null) {
			event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("An error occurred!").build()).queue();
			return;
		}

		/* Spotify Playlist */
		if (finalTrackURL.startsWith("Spotify.PLAYLIST ")) {
			event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Loading playlist `" + finalTrackURL.replace("Spotify.PLAYLIST ","") + "`").build()).queue();
			return;
		}

		/* Spotify Album */
		if (finalTrackURL.startsWith("Spotify.ALBUM ")) {
			event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Loading album `" + finalTrackURL.replace("Spotify.ALBUM ","") + "`").build()).queue();
			return;
		}
		playerManager.loadItem(finalTrackURL, new AudioSoundLoadHandler(this.logger, member, event, sendMessage, trackScheduler, finalTrackURL));
	}

	public AudioPlayerManager getPlayerManager() {
		return playerManager;
	}
}