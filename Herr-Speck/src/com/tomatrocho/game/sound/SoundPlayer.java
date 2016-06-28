package com.tomatrocho.game.sound;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.tomatrocho.game.OS;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryJavaSound;

public class SoundPlayer {
	
	
	/**
	 * 
	 */
	private static final String BACKGROUND_MUSIC = "background";
	
	/**
	 * 
	 */
	private SoundSystem soundSystem;
	
	/**
	 * 
	 */
	private boolean muted = false;
	
	/**
	 * 
	 */
	private boolean oggPlaybackSupport = true;
	
	/**
	 * 
	 */
	private boolean wavPlaybackSupport = true;
	
	/**
	 * 
	 */
	private float volume = 1.0f;
	
	/**
	 * 
	 */
	private float musicVolume = 1.0f;
	
	/**
	 * 
	 */
	private float soundVolume = 1.0f;
	
	
	/**
	 * 
	 */
	public SoundPlayer() {
		try {
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
		} catch (SoundSystemException ex) {
			oggPlaybackSupport = false;
		}
		try {
			SoundSystemConfig.setCodec("wav", CodecWav.class);
		} catch (SoundSystemException ex) {
			wavPlaybackSupport = false;
		}
		try {
			soundSystem = new SoundSystem(LibraryJavaSound.class);
		} catch (SoundSystemException ex) {
			ex.printStackTrace();
		}
		if (soundSystem != null) {
			soundSystem.setMasterVolume(volume);
			soundSystem.setVolume(BACKGROUND_MUSIC, musicVolume);
		}
	}

	/**
	 * 
	 */
	public void startBackgroundMusic() {
		System.out.println("Starting background music");
		if (!muted && hasOggPlaybackSupport()) {
			String musicPath = "/resources/sounds/ambient.ogg";
			try {
			    File musicFile = new File(OS.getAppDirectory("game"), musicPath);
			    URL musicUrl = musicFile.toURI().toURL();
			    if (musicFile.exists()){
			    	soundSystem.backgroundMusic(BACKGROUND_MUSIC, musicUrl, musicPath, true);
			    }
			} catch (MalformedURLException e) {
			    e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void setListenerPosition(int x, int y) {
		soundSystem.setListenerPosition((float) x, (float) y, 50);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasOggPlaybackSupport() {
		return soundSystem != null && oggPlaybackSupport;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasWavPlaybackSupport() {
		return soundSystem != null && wavPlaybackSupport;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isMuted() {
		return muted;
	}
}
