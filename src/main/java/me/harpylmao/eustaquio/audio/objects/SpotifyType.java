package me.harpylmao.eustaquio.audio.objects;

public enum SpotifyType {

	TRACK,
	PLAYLIST,
	ALBUM,
	ARTIST
	;

	/**
	 * Get's the Spotify Type of the String.
	 *
	 * @param link String of the message.
	 * @return Spotify Type of message.
	 */
	public static SpotifyType getSpotifyType(String link) {
		if (link.contains("track")) {
			return SpotifyType.TRACK;
		} else if (link.contains("playlist")) {
			return SpotifyType.PLAYLIST;
		} else if (link.contains("album")) {
			return SpotifyType.ALBUM;
		} else if (link.contains("artist") || link.contains("\uD83E\uDDD1\u200D\uD83C\uDFA8")) {
			return SpotifyType.ARTIST;
		}
		return null;
	}

}
