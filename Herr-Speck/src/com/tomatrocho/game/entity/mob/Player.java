package com.tomatrocho.game.entity.mob;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.entity.Mob;
import com.tomatrocho.game.entity.animation.MuzzleAnimation;
import com.tomatrocho.game.entity.weapon.Rifle;
import com.tomatrocho.game.entity.weapon.WeaponInventory;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.gfx.Art;
import com.tomatrocho.game.gfx.Bitmap;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.input.Keys;
import com.tomatrocho.game.input.Mouse;
import com.tomatrocho.game.math.Vec2;
import com.tomatrocho.game.world.level.World;

public class Player extends Mob {
	
	/**
	 * 
	 */
	public static final int FIRE_MOUSE_BUTTON = 1;

    /**
     *
     */
    private int walkTime = 0;

    /**
     *
     */
    private Keys keys;
    
    /**
     * 
     */
    private boolean mouseAiming = false;

    /**
     *
     */
    private Mouse mouse;
    
    /**
     * 
     */
    private WeaponInventory weaponInventory = new WeaponInventory();
    
    /**
     * 
     */
    private int weaponInventorySlot = 0;
    
    /**
     * 
     */
    private Vec2 muzzlePosition;

    /**
     * 
     * @param keys
     * @param mouse
     */
    public Player(World world, Keys keys, Mouse mouse) {
    	this(world, 0, 0, keys, mouse);
    }

    /**
     * 
     * @param x
     * @param y
     * @param keys
     * @param mouse
     */
    public Player(World world, int x, int y, Keys keys, Mouse mouse) {
    	super(world, x, y, Team.TEAM_1);
    	speed = 2;
    	radius.x = 10;
    	radius.y = 13;
        this.keys = keys;
        this.mouse = mouse;
        weaponInventory.add(new Rifle(this));
        weapon = weaponInventory.get(weaponInventorySlot);
    }

    /**
     *
     */
    public void tick() {
    	if (!mouse.isHidden()) {
    		aimByMouse(mouse.getX() - HerrSpeck.W / 2, mouse.getY() - HerrSpeck.H / 2);
    	} else {
    		aimByKeyboard();
    	}
    	
    	moving = false;
    	// movement
        double xa = 0, ya = 0;
        if (keys.up.down()) {
            ya--;
        }
        if (keys.down.down()) {
            ya++;
        }
        if (keys.left.down()) {
            xa--;
        }
        if (keys.right.down()) {
            xa++;
        }
        // update orientation for rendering
        if (!mouseAiming && xa * xa + ya * ya != 0) {
            aimVector.set(xa, ya);
            aimVector.normalize();
            updateFacing();
        }
        // shooting
        double xaShot = 0, yaShot = 0;
        if (keys.fireUp.down()) {
        	yaShot--;
        }
        if (keys.fireDown.down()) {
        	yaShot++;
        }
        if (keys.fireLeft.down()) {
        	xaShot--;
        }
        if (keys.fireRight.down()) {
        	xaShot++;
        }
        // update orientation for rendering
        if (!mouseAiming && fireKeyDown() && xaShot * xaShot + yaShot * yaShot != 0) {
        	aimVector.set(xaShot,  yaShot);
        	aimVector.normalize();
        	updateFacing();
        }
        // handle movement
        if (xa != 0 || ya != 0) {
            handleMovement(xa, ya);
            moving = true;
        }
        handleShooting(xa, ya);
        // handle map revealing
        //world.reveal(World.getTileFromPosition(new Vec2(x, y)), 10);
    }
    
    /**
     * 
     * @param x
     * @param y
     */
    protected void aimByMouse(int x, int y) {
    	if (x != 0 || y != 0) {
    		mouseAiming = true;
    		aimVector.set(x, y);
        	aimVector.normalize();
        	updateFacing();
    	}
    }
    
    /**
     * 
     */
    protected void aimByKeyboard() {
    	mouseAiming = false;
    }
	
	/**
    *
    */
   protected void handleMovement(double xa, double ya) {
       int diffOrient = facing - ((int) (-Math.atan2(xa, ya) * 8 / (2 * Math.PI) + 8.5) & 7);
       if (diffOrient >= 4) {
           diffOrient -= 8;
       }
       if (diffOrient < -4) {
           diffOrient += 8;
       }
       if (diffOrient > 2 || diffOrient < -4) {
           walkTime--;
       } else {
           walkTime++;
       }
       // computing diagonal speed
       final double speed = this.speed / Math.sqrt(xa * xa + ya * ya);
       xa *= speed;
       ya *= speed;
       x += xa;
       y += ya;
   }
   
   /**
    * 
    * @param xa
    * @param ya
    */
   protected void handleShooting(double xa, double ya) {
	   weapon.tick();
	   if (fireKeyDown() || mouse.isDown(FIRE_MOUSE_BUTTON)) {
		   if (weapon instanceof Rifle) {
			   ((Rifle) weapon).fire(xa, ya);
			   // muzzle animation
			   world.addEntity(new MuzzleAnimation(world, muzzlePosition.x, muzzlePosition.y, 1));
		   }
	   }
   }

   /**
    * 
    * @return
    */
   private boolean fireKeyDown() {
	   return keys.fireDown.down() || keys.fireUp.down() || keys.fireDown.down() || keys.fireLeft.down() || keys.fireRight.down();
   }
   
    @Override
    public void render(IAbstractScreen screen) {
    	super.render(screen);
    	final IAbstractBitmap sprite = getSprite();
        screen.blit(sprite, x - sprite.getW() / 2, y - sprite.getH() / 2);
    }

    @Override
    public IAbstractBitmap getSprite() {
    	IAbstractBitmap sprite = null;
    	if (moving) {
    		final int frame = (walkTime / 4 % 6 + 6) % 6;
    		sprite = (Bitmap) Art.player[frame][facing];
    	} else {
    		sprite = (Bitmap) Art.player[0][facing];
    	}
    	return sprite;
    }
    
    /**
     * Updates player orientation for rendering.
     */
    private void updateFacing() {
        facing = (int) (-Math.atan2(aimVector.x, aimVector.y) * 8 / (2 * Math.PI) + 8.5) & 7;
    }
    
    /**
     * 
     * @param position
     */
    public void setMuzzlePosition(Vec2 position) {
    	this.muzzlePosition = position;
    }
    
    /**
     * 
     * @return
     */
    public Vec2 getBarrelOffsets() {
    	Vec2 offsets = new Vec2();
    	offsets.x = -13.5 * Math.sin(facing * Math.PI / 4);
    	offsets.y = Math.sin(facing * Math.PI / 4) + 11 * Math.cos(facing * Math.PI / 4) + 8;
    	return offsets;
    }
}