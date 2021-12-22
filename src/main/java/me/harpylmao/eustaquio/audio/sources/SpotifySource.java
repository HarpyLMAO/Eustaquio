package me.harpylmao.eustaquio.audio.sources;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.IPlaylistItem;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import org.apache.hc.core5.http.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SpotifySource {
	private Logger logger = LoggerFactory.getLogger(SpotifySource.class);

	private String CLIENT_ID = "b8f0979cbd2b48e1a9a57f9f5cca1e38";
	private String CLIENT_SECRET = "98d11bd4a5c14c6298c38dcf18ea66c1";

	private SpotifyApi spotifyApi = new SpotifyApi.Builder()
			.setClientId(CLIENT_ID)
			.setClientSecret(CLIENT_SECRET)
			.build();

	public SpotifySource() throws IOException, ParseException, SpotifyWebApiException {
		ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
		ClientCredentials clientCredentials = clientCredentialsRequest.execute();

		spotifyApi.setAccessToken(clientCredentials.getAccessToken());
	}

	/**
	 * Get the name of a Spotify track.
	 *
	 * @param id The track id which is included in the song link and the Spotify URI.
	 * @return String containing the name of the corresponding Song.
	 * @throws IOException            In case of networking issues.
	 * @throws SpotifyWebApiException The Web API returned an error further specified in this exception's root cause.
	 * @throws ParseException         String parsing error.
	 */
	public String getTrackName(String id) throws IOException, SpotifyWebApiException, ParseException {
		Track track = spotifyApi.getTrack(id).build().execute();
		return track.getName();
	}

	/**
	 * Get the first Artist of a Spotify track.
	 *
	 * @param id The track id which is included in the song link and the Spotify URI.
	 * @return String containing the name of the corresponding Song.
	 * @throws IOException            In case of networking issues.
	 * @throws SpotifyWebApiException The Web API returned an error further specified in this exception's root cause.
	 * @throws ParseException         String parsing error.
	 */
	public String getTrackArtists(String id) throws IOException, SpotifyWebApiException, ParseException {
		GetTrackRequest getTrackRequest = spotifyApi.getTrack(id).build();
		Track track = getTrackRequest.execute();

		return Arrays.stream(track.getArtists()).findFirst().get().getName();
	}

	/**
	 * Returns {@link List <String>} of Strings where each entry contains the name and artist of a song in the following format: song+Name+artist1+name+Artist2+name... Example: Heathens+Twenty+One+Pilots
	 *
	 * @param id The playlist id which is included in the playlist link and the Spotify URI.
	 * @return A {@link List<String>} of Strings.
	 * @throws IOException If an I/O error occurs while creating the input stream.
	 */
	public List<String> getPlaylist(String id) throws IOException {
		InputStream response;
		List<String> finalOutput = new ArrayList<>();
		URLConnection connection = new URL("https://api.spotify.com/v1/playlists/" + id + "/tracks?market=DE&fields=items(track(name%2C%20artists(name)))").openConnection();
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Authorization", "Bearer " + spotifyApi.getAccessToken());
		response = connection.getInputStream();
		try (Scanner scanner = new Scanner(response)) {
			String responseBody = scanner.useDelimiter("\\A").next();
			JSONObject jsonObject = new JSONObject(responseBody);
			JSONArray items = jsonObject.getJSONArray("items");
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i);
				JSONObject track = item.getJSONObject("track");
				String name = track.getString("name");
				String names = "";
				JSONArray artists = track.getJSONArray("artists");
				for (int j = 0; j < artists.length(); j++) {
					JSONObject object = artists.getJSONObject(j);
					names = names.concat("+" + object.getString("name"));
				}
				String output = name + names;
				output = output.replace(" ", "+");

				finalOutput.add(output);
			}
		}
		return finalOutput;
	}

	public List<String> getPlaylistByID(String id) throws IOException, ParseException, SpotifyWebApiException {
		List<String> playlistSongs = new ArrayList<>();
		Playlist playlist = spotifyApi.getPlaylist(id).build().execute();

		for (PlaylistTrack playlistTrack : playlist.getTracks().getItems()) {
			IPlaylistItem item = playlistTrack.getTrack();
			playlistSongs.add(item.getName() + " " + getTrackArtists(item.getId()));
		}

		return playlistSongs;
	}

	/**
	 * Get's album as a list of songs.
	 *
	 * @param id ID of album.
	 * @return List of album song's in song name + artist name.
	 * @throws IOException If an I/O error occurs while creating the input stream.
	 * @throws ParseException String parsing error.
	 * @throws SpotifyWebApiException The Web API returned an error further specified in this exception's root cause.
	 */
	public List<String> getAlbum(String id) throws IOException, ParseException, SpotifyWebApiException {
		List<String> albumSongs = new ArrayList<>();
		Album album = spotifyApi.getAlbum(id).build().execute();
		String artist = album.getArtists()[0].getName();

		for (TrackSimplified songs : album.getTracks().getItems()) {
			albumSongs.add(songs.getName() + " " + artist);
		}

		return albumSongs;
	}

	/**
	 * Get's Name of playlist.
	 *
	 * @param id ID of playlist
	 * @return Playlist name.
	 * @throws IOException If an I/O error occurs while creating the input stream.
	 * @throws ParseException String parsing error.
	 * @throws SpotifyWebApiException The Web API returned an error further specified in this exception's root cause.
	 */
	public String getPlayListName(String id) throws IOException, ParseException, SpotifyWebApiException {
		return spotifyApi.getPlaylist(id).build().execute().getName();
	}

	/**
	 * Get's name of album.
	 *
	 * @param id ID of album.
	 * @return Album name.
	 * @throws IOException If an I/O error occurs while creating the input stream.
	 * @throws ParseException String parsing error.
	 * @throws SpotifyWebApiException The Web API returned an error further specified in this exception's root cause.
	 */
	public String getAlbumName(String id) throws IOException, ParseException, SpotifyWebApiException {
		return spotifyApi.getAlbum(id).build().execute().getName();
	}

	/**
	 * Get's Song from URL and translate it to a name and an artisit.
	 *
	 * @param url URL to get track.
	 * @return Song to search
	 * @throws IOException If an I/O error occurs while creating the input stream.
	 * @throws ParseException String parsing error.
	 * @throws SpotifyWebApiException The Web API returned an error further specified in this exception's root cause.
	 */
	public String getTrackNameAndArtist(String url) throws IOException, ParseException, SpotifyWebApiException {
		if (url.contains("open.spotify.com/track/")) {
			url = url.replace("https://", "");
			url = url.split("/")[2];
			url = url.split("\\?")[0];
		} else if (url.startsWith("spotify:track:")) {
			url = url.split(":")[2];
		}

		return getTrackName(url) + " " + getTrackArtists(url);
	}

}