package me.harpylmao.eustaquio.utils;


import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.net.MalformedURLException;
import java.net.URL;

public class MusicUtils {

	public static final int EMBED_COLOR = 0x7289da;

	/**
	 * Get's Time left in audio track.
	 *
	 * @param audioTrack Audio Track to get duration.
	 * @return Duration.
	 */
	public static String getDuration(AudioTrack audioTrack) {
		long msPos = audioTrack.getInfo().length;
		long minPos = msPos / 60000;
		msPos = msPos % 60000;
		int secPos = (int) Math.floor((float) msPos / 1000f);

		return minPos + ":" + ((secPos < 10) ? "0" + secPos : secPos);
	}

	/**
	 * Get's Thumbnail.
	 *
	 * @param audioTrack Audio Track.
	 * @return String of URL
	 */
	public static String getThumbnail(AudioTrack audioTrack) {
		return String.format("https://img.youtube.com/vi/%s/0.jpg", audioTrack.getInfo().uri.substring(32));
	}

	/**
	 * Check's if string is a link.
	 *
	 * @param link String of Link.
	 * @return if Link.
	 */
	public static boolean isValidLink(String link) {
		return link.matches("(.*)open.spotify.com(.*)|spotify(.*)|(.*)youtube.com(.*)|(.*)youtu.be(.*)");
	}

	public static boolean ifURL(String url) {
		try {
			new URL(url);
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
	}
}