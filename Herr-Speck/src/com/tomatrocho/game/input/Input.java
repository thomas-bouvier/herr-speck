package com.tomatrocho.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Input implements KeyListener {

    /**
     *
     */
    private Map<Keys.Key, Integer> mappings = new HashMap<>();


    /**
     *
     * @param keys
     */
    public Input(Keys keys) {
        init(keys.up, KeyEvent.VK_Z);
        init(keys.right, KeyEvent.VK_D);
        init(keys.down, KeyEvent.VK_S);
        init(keys.left, KeyEvent.VK_Q);
        init(keys.fire, KeyEvent.VK_SPACE);
        init(keys.fireUp, KeyEvent.VK_UP);
        init(keys.fireDown, KeyEvent.VK_DOWN);
        init(keys.fireLeft, KeyEvent.VK_LEFT);
        init(keys.fireRight, KeyEvent.VK_RIGHT);
        init(keys.debug, KeyEvent.VK_F2);
        init(keys.fullscreen, KeyEvent.VK_F11);
    }

    /**
     *
     * @param key
     * @param keyCode
     */
    private void init(Keys.Key key, int keyCode) {
        mappings.put(key, keyCode);
    }

    /**
     *
     * @param e
     * @param state
     */
    private void toggleState(KeyEvent e, boolean state) {
        Keys.Key key = null;
        for (Keys.Key k : mappings.keySet()) {
            if (mappings.get(k) == e.getKeyCode()) {
                key = k;
            }
        }
        if (key != null) {
            key.setNextState(state);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        toggleState(e, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        toggleState(e, false);
    }
}
