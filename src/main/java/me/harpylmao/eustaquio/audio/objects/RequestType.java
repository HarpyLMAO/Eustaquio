package me.harpylmao.eustaquio.audio.objects;

import me.harpylmao.eustaquio.utils.MusicUtils;

public enum RequestType {

	YOUTUBE,
	SPOTIFY,
	SEARCH
	;

	/**
	 * Get's the Request Type of the String.
	 *
	 * @param type String of the message.
	 * @return Request Type.
	 */
	public static RequestType getRequestType(String type) {
		if (MusicUtils.isValidLink(type)) {
			if (type.contains("youtube") || type.contains("youtu.be")) {
				return RequestType.YOUTUBE;
			} else if (type.contains("spotify")) {
				return RequestType.SPOTIFY;
			}
		} else {
			return RequestType.SEARCH;
		}
		System.err.println("Something went wrong!");
		return null;
	}
}
