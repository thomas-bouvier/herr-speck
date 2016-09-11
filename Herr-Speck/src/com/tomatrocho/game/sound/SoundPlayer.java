package com.tomatrocho.game.sound;

import java.io.File;
import java.net.MalformedURLException;

import com.tomatrocho.game.OS;

import paulscode.sound.Library;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryJavaSound;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundPlayer {
	
	
	/**
	 * 
	 */
	private static final String BACKGROUND_MUSIC = "background";
	
	/**
	 * 
	 */
	private final Class<? extends Library> libraryType;
	
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
	private boolean oggPlaybackSupport;
	
	/**
	 * 
	 */
	private boolean wavPlaybackSupport;
	
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
			
			oggPlaybackSupport = true;
		}
		catch (SoundSystemException ex) {
			oggPlaybackSupport = false;
		}
		try {
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			
			wavPlaybackSupport = true;
		}
		catch (SoundSystemException ex) {
			wavPlaybackSupport = false;
		}
		
		Class<? extends Library> libraryType = LibraryJavaSound.class;
//        if (SoundSystem.libraryCompatible(LibraryLWJGLOpenAL.class))
//        	libraryType = LibraryLWJGLOpenAL.class;
        this.libraryType = libraryType;
		
		try {
			soundSystem = new SoundSystem(libraryType);
		}
		catch (SoundSystemException ex) {
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
		System.out.println("\nBackground music started.");
		
		if (!muted && hasOggPlaybackSupport()) {
			String musicPath = "/resources/sounds/ambient.ogg";
			
			try {
			    File musicFile = new File(OS.getAppDirectory("game"), musicPath);
			    if (musicFile.exists())
			    	soundSystem.backgroundMusic(BACKGROUND_MUSIC, musicFile.toURI().toURL(), musicPath, true);
			}
			catch (MalformedURLException ex) {
			    ex.printStackTrace();
			}
		}
		
		soundSystem.setVolume(BACKGROUND_MUSIC, musicVolume);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void setListenerPosition(double x, double y) {
		soundSystem.setListenerPosition((float) x, (float) y, 50);
	}
	
	/**
	 * 
	 */
	public void dispose() {
		soundSystem.cleanup();
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
	public boolean muted() {
		return muted;
	}
}
