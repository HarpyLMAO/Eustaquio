package me.harpylmao.eustaquio.audio.objects;

public enum YoutubeType {

	TRACK,
	PLAYLIST
	;

	/**
	 * Get's the Youtube Type of the String.
	 *
	 * @param link String of the message.
	 * @return Type of Youtube Song.
	 */
	public static YoutubeType getYouTubeType(String link) {
		if (link.contains("youtube.com/watch?v=") || link.contains("youtu.be/")) {
			return YoutubeType.TRACK;
		} else if (link.contains("youtube.com/playlist?list=")) {
			return YoutubeType.PLAYLIST;
		}
		System.err.println("Something went wrong!");
		return null;
	}

}
