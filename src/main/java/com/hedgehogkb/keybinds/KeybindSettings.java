package com.hedgehogkb.keybinds;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class KeybindSettings {
    public static class Keybinds {
        private HashMap<Integer, InputType> keybinds;

        private Keybinds(int player) {
            keybinds = new HashMap<>();
            switch (player) {
                case 1:
                    keybinds.put(KeyEvent.VK_A, InputType.BACKWARD);
                    keybinds.put(KeyEvent.VK_D, InputType.FORWARD);
                    keybinds.put(KeyEvent.VK_W, InputType.UP);
                    keybinds.put(KeyEvent.VK_S, InputType.DOWN);
                    keybinds.put(KeyEvent.VK_SHIFT, InputType.SPRINT);
                    keybinds.put(KeyEvent.VK_P, InputType.SPECIAL);
                    keybinds.put(KeyEvent.VK_O, InputType.NORMAL);
                    keybinds.put(KeyEvent.VK_I, InputType.SHIELD);
                    keybinds.put(KeyEvent.VK_U, InputType.GRAP);
                    break;

                case 2:
                    keybinds.put(KeyEvent.VK_LEFT, InputType.BACKWARD);
                    keybinds.put(KeyEvent.VK_RIGHT, InputType.FORWARD);
                    keybinds.put(KeyEvent.VK_UP, InputType.UP);
                    keybinds.put(KeyEvent.VK_DOWN, InputType.DOWN);
                    keybinds.put(KeyEvent.VK_PLUS, InputType.SPRINT);
                    keybinds.put(KeyEvent.VK_NUMPAD9, InputType.SPECIAL);
                    keybinds.put(KeyEvent.VK_NUMPAD8, InputType.NORMAL);
                    keybinds.put(KeyEvent.VK_NUMPAD7, InputType.SHIELD);
                    keybinds.put(KeyEvent.VK_NUMPAD4, InputType.GRAP);
                    break;
                default:
                    throw new IllegalArgumentException("Right now more than 2 players aren't allowed");
            }

        }

        public InputType getKeybind(Integer keycode) {
            if (!keybinds.containsKey(keycode)) {
                return null;
            }
            return keybinds.get(keycode);
        }
    }

    /**
     * Takes in the number player and retuned the keybinds of that player.
     * @param player
     * @return
     */
    public Keybinds getKeybinds(int player) {
        return new Keybinds(player);
    }
}
