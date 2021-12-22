package me.harpylmao.eustaquio.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class AudioSendProvider implements AudioSendHandler {
	private AudioPlayer audioPlayer;
	private ByteBuffer buffer;
	private MutableAudioFrame frame;

	public AudioSendProvider(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
		this.buffer = ByteBuffer.allocate(2046);
		this.frame = new MutableAudioFrame();
		this.frame.setBuffer(buffer);
	}

	@Override
	public boolean canProvide() {
		return audioPlayer.provide(frame);
	}

	@Override
	public ByteBuffer provide20MsAudio() {
		buffer.flip();
		return buffer;
	}

	@Override
	public boolean isOpus() {
		return true;
	}

}