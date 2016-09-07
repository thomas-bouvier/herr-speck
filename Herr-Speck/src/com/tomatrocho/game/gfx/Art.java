package com.tomatrocho.game.gfx;

public class Art {
	
	// font
	public static IAbstractBitmap[][] font;

	// player
	public static IAbstractBitmap[][] player;
	
	// mobs
	public static IAbstractBitmap[][] bat;
	
	// tiles 16x16
	public static IAbstractBitmap[][] stoneTiles;
	public static IAbstractBitmap[][] rocksTiles;
	public static IAbstractBitmap[][] sandstoneTiles;
	public static IAbstractBitmap[][] sandstoneWallTiles;
	public static IAbstractBitmap[][] waterTiles;
	
	// tiles 16x32
	public static IAbstractBitmap[][] highRocksTiles;
	
	// tiles 32x32
	public static IAbstractBitmap[][] puddleTiles;
	
	// projectiles
	public static IAbstractBitmap[][] bullets;
	
	// effects
	public static IAbstractBitmap[][] darkness;
	public static IAbstractBitmap[][] muzzle;
	
	// shadows
	public static IAbstractBitmap bigShadow;
	
	/**
	*
	* @param screen
	*/
	public static void loadAllRessources(IAbstractScreen screen) {
		System.out.println("Loading ressources...");
		
		font = screen.cut("/art/font/font.png", 9);
		player = screen.cut("/art/mobs/player.png", 32, 32);
		bat = screen.cut("/art/mobs/bat.png", 32, 32);
	    stoneTiles = screen.cut("/art/map/stone.png", 16, 16);
	    rocksTiles = screen.cut("/art/map/rocks.png", 16, 16);
	    sandstoneTiles = screen.cut("/art/map/sandstone.png", 16, 16);
	    sandstoneWallTiles = screen.cut("/art/map/bedrock.png", 16, 16);
	    waterTiles = screen.cut("/art/map/water.png", 16, 16);
	    highRocksTiles = screen.cut("/art/map/high_rocks.png", 16, 32);
	    puddleTiles = screen.cut("/art/map/puddle.png", 32, 32);
	    bullets = screen.cut("/art/projectiles/bullet.png", 16, 16);
	    darkness = screen.cut("/art/effects/darkness.png", 16, 16);
	    muzzle = screen.cut("/art/effects/muzzle.png", 16, 16);
	    bigShadow = screen.load("/art/shadows/big_shadow.png");
	    
	    System.out.println("Ressources loaded.");
	}
}
