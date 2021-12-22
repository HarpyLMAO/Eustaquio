package me.harpylmao.eustaquio.audio.factories;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.audio.TrackScheduler;
import me.harpylmao.eustaquio.audio.handlers.AudioSoundLoadHandler;
import me.harpylmao.eustaquio.audio.objects.RequestType;
import me.harpylmao.eustaquio.audio.objects.SpotifyType;
import me.harpylmao.eustaquio.audio.objects.YoutubeType;
import me.harpylmao.eustaquio.audio.sources.SpotifySource;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class SearchFactory {
	private Logger logger = LoggerFactory.getLogger(SearchFactory.class);

	private String search;
	private CommandEvent event;
	private TrackScheduler trackScheduler;
	private AudioPlayerManager audioPlayerManager;

	private Bot eustaquio;

	/**
	 * SearchFactory Constructor.
	 *
	 * @param search Song/URL to search for.
	 */
	public SearchFactory(String search, CommandEvent event, TrackScheduler trackScheduler, AudioPlayerManager audioPlayerManager, Bot eustaquio) {
		this.search = search;
		this.event = event;
		this.trackScheduler = trackScheduler;
		this.audioPlayerManager = audioPlayerManager;

		this.eustaquio = eustaquio;
	}

	public String search() {
		RequestType requestType = Objects.requireNonNull(RequestType.getRequestType(search));

		try {
			switch (requestType) {
				case YOUTUBE: {
					YoutubeType youtubeType = YoutubeType.getYouTubeType(search);

					/* Searches for song using URL */
					if (youtubeType == YoutubeType.TRACK) {
						return search;
					}

					return search;
				}

				case SPOTIFY: {
					SpotifyType spotifyType = Objects.requireNonNull(SpotifyType.getSpotifyType(search));
					SpotifySource spotifySource = new SpotifySource();

					/* Search for song using youtube by using name. */
					if (spotifyType == SpotifyType.TRACK) {
						return "ytsearch: " + spotifySource.getTrackNameAndArtist(search);
					}

					/* Searches for playlist and then add all of the song's to the queue. */
					if (spotifyType == SpotifyType.PLAYLIST) {
						String id = null;
						if (search.startsWith("spotify:playlist:")) {
							id = search.split(":")[2];
						}
						if (search.startsWith("open.spotify.com/playlist/")) {
							String temp = search.split("/")[2];
							id = temp.split("\\?")[0];
						}
						if (search.startsWith("https://open.spotify.com/playlist/")) {
							String temp = search.split("/")[4];
							id = temp.split("\\?")[0];
						}

						String playlistName = spotifySource.getPlayListName(id);

						this.logger.info("Loading Playlist: " + playlistName);

						for (String song : spotifySource.getPlaylistByID(id)) {
							audioPlayerManager.loadItem("ytsearch: " + song, new AudioSoundLoadHandler(logger, event.getMember(), event, false, trackScheduler,"ytsearch: " + song));
						}

						return "Spotify.PLAYLIST " + playlistName;
					}

					/* Searches for album and then add all of the song's to the queue. */
					if (spotifyType == SpotifyType.ALBUM) {

						String id = null;
						if (search.startsWith("spotify:album:")) {
							id = search.split(":")[2];
						}
						if (search.startsWith("open.spotify.com/album/")) {
							String temp = search.split("/")[2];
							id = temp.split("\\?")[0];
						}
						if (search.startsWith("https://open.spotify.com/album/")) {
							String temp = search.split("/")[4];
							id = temp.split("\\?")[0];
						}

						String albumName = spotifySource.getAlbumName(id);

						this.logger.info("Loading Album: " + albumName);

						for (String song : spotifySource.getAlbum(id)) {
							audioPlayerManager.loadItem("ytsearch: " + song, new AudioSoundLoadHandler(logger, event.getMember(), event, false, trackScheduler,"ytsearch: " + song));
						}

						return "Spotify.ALBUM " + albumName;
					}
				}
				case SEARCH: {
					return "ytsearch: " + search;
				}

			}
		} catch (IOException | ParseException | SpotifyWebApiException e) {
			e.printStackTrace();
		}

		return "";
	}

	public String getSearch() {
		return search;
	}

}