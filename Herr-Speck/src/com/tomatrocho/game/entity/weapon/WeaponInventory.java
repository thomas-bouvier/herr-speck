package com.tomatrocho.game.entity.weapon;

import java.util.LinkedList;

public class WeaponInventory {

	/**
	 * 
	 */
	private LinkedList<Weapon> weapons = new LinkedList<>();
	
	
	/**
	 * 
	 * @param weapon
	 * @return
	 */
	public boolean add(Weapon weapon) {
		if (weapons.contains(weapon)) {
			return false;
		}
		return weapons.add(weapon);
	}
	
	/**
	 * 
	 */
	public void cycleLeft() {
		if (!empty()) {
			weapons.addLast(weapons.pollFirst());
		}
	}
	
	/**
	 * 
	 */
	public void cycleRight() {
		if (!empty()) {
			weapons.addFirst(weapons.pollLast());
		}
	}
	
	/**
	 * 
	 */
	public void clearAll() {
		weapons.clear();
	}
	
	/**
	 * 
	 * @param slot
	 * @return
	 */
	public Weapon get(int slot) {
		if (size() > slot) {
			return weapons.get(slot);
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public int size() {
		return weapons.size();
	}
	
	/**
	 * 
	 * @param weapon
	 * @return
	 */
	public boolean has(Weapon weapon) {
		return weapons.contains(weapon);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean empty() {
		return weapons.isEmpty();
	}
}
