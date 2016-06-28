package com.tomatrocho.game.input;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Keys {

	/**
	 *
	 */
	public class Key {

		/**
		 *
		 */
		private String name;

		/**
		 *
		 */
		private boolean nextState = false;

		/**
		 *
		 */
		private boolean down;

		/**
		 *
		 */
		private boolean wasDown;


		/**
		 *
		 * @param name
         */
		public Key(String name) {
			this.name = name;
			keys.add(this);
		}

		/**
		 *
		 */
		public void tick() {
			wasDown = down;
			down = nextState;
		}

		/**
		 *
		 * @param state
         */
		public void toggle(boolean state) {
			if (state != down) {
				down = state;
			}
		}

		/**
		 *
		 * @param nextState
         */
		public void setNextState(boolean nextState) {
			this.nextState = nextState;
		}

		/**
		 *
		 * @return
         */
		public boolean down() {
			return down;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean wasPressed() {
			return !wasDown && down;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean wasReleased() {
			return wasDown && !down;
		}
	}

	/**
	 *
	 */
	private List<Key> keys = new ArrayList<>();

	public Key fullscreen = new Key("fullscreen");
	
	public Key up = new Key("up");
	public Key down = new Key("down");
	public Key left = new Key("left");
	public Key right = new Key("right");
	public Key fire = new Key("fire");
	public Key fireUp = new Key("fireUp");
	public Key fireDown = new Key("fireDown");
	public Key fireLeft = new Key("fireLeftp");
	public Key fireRight = new Key("fireRight");
	public Key debug = new Key("debug");


	/**
	 *
	 */
	public void tick() {
		for (Key key : keys) {
			key.tick();
		}
	}

	/**
	 *
	 * @return
     */
	public List<Key> getKeys() {
		return keys;
	}
}
