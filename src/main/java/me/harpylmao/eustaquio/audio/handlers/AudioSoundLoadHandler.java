package me.harpylmao.eustaquio.audio.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.audio.TrackScheduler;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import me.harpylmao.eustaquio.utils.MusicUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;

public class AudioSoundLoadHandler implements AudioLoadResultHandler {

	private Logger logger;
	private Member member;
	private CommandEvent event;
	private boolean sendMessage;
	private TrackScheduler trackScheduler;
	private String finalTrackURL;

	public AudioSoundLoadHandler(Logger logger, Member member, CommandEvent event, boolean sendMessage, TrackScheduler trackScheduler, String finalTrackURL) {
		this.logger = logger;
		this.member = member;
		this.event = event;
		this.sendMessage = sendMessage;
		this.trackScheduler = trackScheduler;
		this.finalTrackURL = finalTrackURL;
	}

	@Override
	public void trackLoaded(AudioTrack audioTrack) {
		logger.info("Loaded Song: " + audioTrack.getInfo().title + " By: " + audioTrack.getInfo().author);
		if (!trackScheduler.getTrackQueue().isEmpty()) {
			EmbedBuilder embed = new EmbedBuilder()
					.setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri)
					.addField("Song Duration", MusicUtils.getDuration(audioTrack), true)
					.addField("Position in Queue", String.valueOf(trackScheduler.getTrackQueue().size()), true)
					.setFooter("Added by " + member.getUser().getAsTag(), member.getUser().getEffectiveAvatarUrl())
					.setThumbnail(MusicUtils.getThumbnail(audioTrack));

			if (sendMessage) {
				event.getChannel().sendMessage(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription(":white_check_mark: **" + audioTrack.getInfo().title + "** successfully added to the queue!").build()).queue();
				event.getChannel().sendMessage(embed.build()).queue();
			}

		} else {

			if (sendMessage) {
				event.getChannel().sendMessage(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Song added to queue.").build()).queue();
			}

		}

		if (trackScheduler.getTrackQueue().contains(audioTrack)) {
			logger.info("Clone Song Found: " + audioTrack.getInfo().title);
			trackScheduler.queueSong(audioTrack.makeClone());
			return;
		}

		trackScheduler.queueSong(audioTrack);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		if (finalTrackURL.contains("ytsearch: ")) {
			AudioTrack audioTrack = playlist.getTracks().get(0);

			logger.info("Loaded Song: " + audioTrack.getInfo().title + " By: " + audioTrack.getInfo().author);

			if (sendMessage) {
				if (!trackScheduler.getTrackQueue().isEmpty()) {
					EmbedBuilder embed = new EmbedBuilder()
							.setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri)
							.addField("Song Duration", MusicUtils.getDuration(playlist.getTracks().get(0)), true)
							.addField("Position in Queue", String.valueOf(trackScheduler.getTrackQueue().size()), true)
							.setFooter("Added by " + member.getUser().getAsTag(), member.getUser().getEffectiveAvatarUrl())
							.setThumbnail(MusicUtils.getThumbnail(playlist.getTracks().get(0)));

					event.getChannel().sendMessage(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription(":white_check_mark: **" + audioTrack.getInfo().title + "** successfully added to the queue!").build()).queue();
					event.getChannel().sendMessage(embed.build()).queue();
				} else {
					event.getChannel().sendMessage(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Song added to queue.").build()).queue();
				}
			}

			trackScheduler.queueSong(playlist.getTracks().get(0));


		} else  {
			event.getChannel().sendMessage(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Loading playlist `" + playlist.getName() + "`").build()).queue();

			logger.info("Loading Playlist: " + playlist.getName());

			for (AudioTrack audioTrack : playlist.getTracks()) {
				logger.info("Loaded Song: " + audioTrack.getInfo().title + " By: " + audioTrack.getInfo().author);
				trackScheduler.queueSong(audioTrack);
			}
		}
	}

	@Override
	public void noMatches() {
		logger.info("Could not find song!");
		event.getChannel().sendMessage(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("Could not find song!").build()).queue();
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		logger.error("An error occurred loading the song!", exception);

		event.getChannel().sendMessage(new EmbedBuilder().setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored()).setDescription("An error occurred!").build()).queue();
	}

}
