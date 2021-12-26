package me.harpylmao.eustaquio.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import me.harpylmao.eustaquio.utils.MusicUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrackScheduler extends AudioEventAdapter {

	private Guild guild;
	private AudioPlayer audioPlayer;
	private MusicManager guildAudioManager;

	private boolean loop = false;
	private boolean loopQueue = false;

	private TextChannel logChannel;

	private List<AudioTrack> trackQueue = new ArrayList<>();
	private AudioTrack lastSong = null;
	private boolean lastBoolean;

	public TrackScheduler(AudioPlayer audioPlayer, MusicManager guildAudioManager, Guild guild) {
		this.guild = guild;
		this.audioPlayer = audioPlayer;
		this.guildAudioManager = guildAudioManager;
	}

	/**
	 * Queues Song.
	 *
	 * @param track Track to Queue.
	 */
	public void queueSong(AudioTrack track) {
		this.trackQueue.add(track);
		if (audioPlayer.getPlayingTrack() == null) audioPlayer.playTrack(trackQueue.get(0));
	}

	/**
	 * Clear's Audio Queue.
	 */
	public void clearQueue() {
		this.trackQueue.clear();
		this.audioPlayer.stopTrack();
	}

	/**
	 * Skips Song.
	 */
	public void skip() {
		this.audioPlayer.getPlayingTrack().setPosition(this.audioPlayer.getPlayingTrack().getDuration());
	}

	/**
	 * Skip's to certain position in queue.
	 *
	 * @param pos Position to set song.
	 */
	public void skipTo(int pos) {
		if (pos > 1) {
			this.trackQueue.subList(1, pos).clear();
		}

		this.skip();
	}

	public boolean removeSong(int song) {
		if (trackQueue.get(song) == null) {
			return false;
		} else {
			trackQueue.remove(song);
			return true;
		}
	}

	/**
	 * Check's if Audio is paused.
	 *
	 * @return If Paused.
	 */
	public boolean isPaused() {
		return this.audioPlayer.isPaused();
	}

	/**
	 * Set's the Audio to paused parameter.
	 *
	 * @param paused Pause or not.
	 */
	public void setPaused(boolean paused) {
		this.audioPlayer.setPaused(paused);
	}

	/**
	 * Set's Volume of Audio Player.
	 *
	 * @param volume Volume to set Audio player.
	 */
	public void setVolume(int volume) {
		this.audioPlayer.setVolume(volume);
	}

	/**
	 * Shuffles queue of songs.
	 */
	public void shuffle() {
		Collections.shuffle(this.trackQueue.subList(1, this.trackQueue.size()));
	}

	/**
	 * Toggles loop for singular song.
	 *
	 * @param event Get's info from Command Event.
	 */
	public void toggleLoop(CommandEvent event) {
		MessageChannel channel = event.getChannel();

		if (this.loopQueue && !this.loop) toggleLoopQueue(event);
		if (!this.loop) {
			this.loop = true;
			if (channel != null) event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Loop Enabled!").build()).queue();
		} else {
			this.loop = false;
			if (channel != null) event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Loop Disabled!").build()).queue();
		}
	}

	/**
	 * Toggles Loop for queue of song.
	 *
	 * @param event Get's info from Command Event.
	 */
	public void toggleLoopQueue(CommandEvent event) {
		MessageChannel channel = event.getChannel();

		if (this.loop && !this.loopQueue) toggleLoop(event);
		if (!this.loopQueue) {
			this.loopQueue = true;
			if (channel != null) event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription(":repeat: Loop Queue Enabled!").build()).queue();
		} else {
			this.loopQueue = false;
			if (channel != null) event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription(":x: Loop Queue Disabled!").build()).queue();
		}

	}

	/**
	 * Clear's Queue.
	 */
	public void resetQueue() {
		this.trackQueue.clear();
	}

	/**
	 * Gets copy of Current Queue.
	 *
	 * @return List of AudioTracks.
	 */
	public List<AudioTrack> getTrackQueue() {
		return this.trackQueue;
	}

	/**
	 * Sets Log Channel to specified MessageChannel
	 *
	 * @param logChannel MessageChannel.
	 */
	public void setLogChannel(TextChannel logChannel) {
		this.logChannel = logChannel;
	}

	/**
	 * Gets Log Channel.
	 *
	 * @return MessageChannel
	 */
	public TextChannel getLogChannel() {
		return this.logChannel;
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		EmbedBuilder builder = new EmbedBuilder()
				.setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored())
				.setTitle("Now Playing")
				.setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")")
				.addField("Song Duration", MusicUtils.getDuration(track), true)
				.setThumbnail(MusicUtils.getThumbnail(track));
		builder.addField("Up Next", (this.trackQueue.size() > 1) ? ("[" + this.trackQueue.get(1).getInfo().title + "](" + this.trackQueue.get(1).getInfo().uri + ")") : "Nothing", true);
		this.logChannel.sendMessageEmbeds(builder.build()).queue((message -> message.delete().queueAfter(track.getDuration(), TimeUnit.MILLISECONDS)));
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (!lastBoolean) {
			this.lastBoolean = true;
			this.lastSong = track;
		}
		this.trackQueue.remove(track);
		if (this.loop && endReason.mayStartNext) {
			this.addTrack(track.getInfo().uri,this. guild, false);
		} else if (this.loopQueue && endReason.mayStartNext) {
			this.addTrack(track.getInfo().uri, this.guild, true);
		} else if (endReason.mayStartNext && this.trackQueue.size() > 0) player.playTrack(this.trackQueue.get(0));
	}

	/**
	 * Adds track and loads it.
	 *
	 * @param name Name of Song.
	 * @param guild Guild.
	 * @param queueSong If song need's to be queued.
	 */
	public void addTrack(String name, Guild guild, boolean queueSong) {
		guildAudioManager.getPlayerManager().loadItem(name, new AudioLoadResultHandler() {
			TrackScheduler scheduler = guildAudioManager.getGuildAudio(guild).getTrackScheduler();

			@Override
			public void trackLoaded(AudioTrack audioTrack) {
				if (queueSong) {
					scheduler.queueSong(audioTrack);
				}
				scheduler.getTrackQueue().add(0, audioTrack);
				scheduler.getAudioPlayer().playTrack(scheduler.getTrackQueue().get(0));
			}

			@Override
			public void playlistLoaded(AudioPlaylist audioPlaylist) {
				for (AudioTrack track : audioPlaylist.getTracks()) {
					scheduler.queueSong(track);
				}
			}

			@Override
			public void noMatches() {}

			@Override
			public void loadFailed(FriendlyException ignore) {}
		});
	}

	public boolean isLastBoolean() {
		return lastBoolean;
	}

	public void setLastSong(AudioTrack lastSong) {
		this.lastSong = lastSong;
	}

	public void setLastBoolean(boolean lastBoolean) {
		this.lastBoolean = lastBoolean;
	}

	public AudioPlayer getAudioPlayer() {
		return this.audioPlayer;
	}

	public AudioTrack getLastSong() {
		return lastSong;
	}
}