package org.voegl.analogkey4j.key;

import java.util.HashMap;

/**
 * A base implementation for a HID key map providing mappings from HID key codes to {@link HidKey}.
 * It can be extended for keyboards using specialized key codes, but should be capable of handling
 * most keyboards already.
 */
public class HidKeyMap {

  private static HashKeyMap keyMap;
  private static HidKeyMap INSTANCE;

  /**
   * A {@link HashMap} providing storage for mappings from HID keycodes represented as {@link Byte}
   * to {@link HidKey}. This class is not intended for outside use.
   */
  private static class HashKeyMap extends HashMap<Byte, HidKey> {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = Math.abs(Byte.MIN_VALUE) + Math.abs(Byte.MAX_VALUE);

    /**
     * Puts an int into this key map. It provides a safety mechanism to deal with overflow values.
     *
     * @param key The key to provide a value for.
     * @param value The HID key code value for the key.
     */
    void put(HidKey key, int value) {
      if (value < MIN_VALUE || value > MAX_VALUE) {
        throw new IllegalArgumentException("Invalid Byte value " + value);
      }
      super.put((byte) value, key);
    }
  }

  /**
   * Constructs a new key map with the corresponding default HID values for each known key. This
   * constructor must only be called from the {@link #getInstance()} method.
   */
  private HidKeyMap() {
    keyMap =
        new HashKeyMap() {
          {
            put(HidKey.A, 0x04);
            put(HidKey.B, 0x05);
            put(HidKey.C, 0x06);
            put(HidKey.D, 0x07);
            put(HidKey.E, 0x08);
            put(HidKey.F, 0x09);
            put(HidKey.G, 0x0a);
            put(HidKey.H, 0x0b);
            put(HidKey.I, 0x0c);
            put(HidKey.J, 0x0d);
            put(HidKey.K, 0x0e);
            put(HidKey.L, 0x0f);
            put(HidKey.M, 0x10);
            put(HidKey.N, 0x11);
            put(HidKey.O, 0x12);
            put(HidKey.P, 0x13);
            put(HidKey.Q, 0x14);
            put(HidKey.R, 0x15);
            put(HidKey.S, 0x16);
            put(HidKey.T, 0x17);
            put(HidKey.U, 0x18);
            put(HidKey.V, 0x19);
            put(HidKey.W, 0x1a);
            put(HidKey.X, 0x1b);
            put(HidKey.Y, 0x1c);
            put(HidKey.Z, 0x1d);
            put(HidKey.N1, 0x1e);
            put(HidKey.N2, 0x1f);
            put(HidKey.N3, 0x20);
            put(HidKey.N4, 0x21);
            put(HidKey.N5, 0x22);
            put(HidKey.N6, 0x23);
            put(HidKey.N7, 0x24);
            put(HidKey.N8, 0x25);
            put(HidKey.N9, 0x26);
            put(HidKey.N0, 0x27);
            put(HidKey.Enter, 0x28);
            put(HidKey.Escape, 0x29);
            put(HidKey.Backspace, 0x2a);
            put(HidKey.Tab, 0x2b);
            put(HidKey.Space, 0x2c);
            put(HidKey.Minus, 0x2d);
            put(HidKey.Equal, 0x2e);
            put(HidKey.BracketLeft, 0x2f);
            put(HidKey.BracketRight, 0x30);
            put(HidKey.Backslash, 0x31);
            put(HidKey.Semicolon, 0x33);
            put(HidKey.Quote, 0x34);
            put(HidKey.Backquote, 0x35);
            put(HidKey.Comma, 0x36);
            put(HidKey.Period, 0x37);
            put(HidKey.Slash, 0x38);
            put(HidKey.CapsLock, 0x39);
            put(HidKey.F1, 0x3a);
            put(HidKey.F2, 0x3b);
            put(HidKey.F3, 0x3c);
            put(HidKey.F4, 0x3d);
            put(HidKey.F5, 0x3e);
            put(HidKey.F6, 0x3f);
            put(HidKey.F7, 0x40);
            put(HidKey.F8, 0x41);
            put(HidKey.F9, 0x42);
            put(HidKey.F10, 0x43);
            put(HidKey.F11, 0x44);
            put(HidKey.F12, 0x45);
            put(HidKey.PrintScreen, 0x46);
            put(HidKey.ScrollLock, 0x47);
            put(HidKey.PauseBreak, 0x48);
            put(HidKey.Insert, 0x49);
            put(HidKey.Home, 0x4a);
            put(HidKey.PageUp, 0x4b);
            put(HidKey.Delete, 0x4c);
            put(HidKey.End, 0x4d);
            put(HidKey.PageDown, 0x4e);
            put(HidKey.ArrowRight, 0x4f);
            put(HidKey.ArrowLeft, 0x50);
            put(HidKey.ArrowDown, 0x51);
            put(HidKey.ArrowUp, 0x52);
            put(HidKey.NumLock, 0x53);
            put(HidKey.NumpadDivide, 0x54);
            put(HidKey.NumpadMultiply, 0x55);
            put(HidKey.NumpadSubtract, 0x56);
            put(HidKey.NumpadAdd, 0x57);
            put(HidKey.NumpadEnter, 0x58);
            put(HidKey.Numpad1, 0x59);
            put(HidKey.Numpad2, 0x5a);
            put(HidKey.Numpad3, 0x5b);
            put(HidKey.Numpad4, 0x5c);
            put(HidKey.Numpad5, 0x5d);
            put(HidKey.Numpad6, 0x5e);
            put(HidKey.Numpad7, 0x5f);
            put(HidKey.Numpad8, 0x60);
            put(HidKey.Numpad9, 0x61);
            put(HidKey.Numpad0, 0x62);
            put(HidKey.NumpadDecimal, 0x63);
            put(HidKey.InternationalBackslash, 0x64);
            put(HidKey.ContextMenu, 0x65);
            put(HidKey.Power, 0x66);
            put(HidKey.NumpadEqual, 0x67);
            put(HidKey.F13, 0x68);
            put(HidKey.F14, 0x69);
            put(HidKey.F15, 0x6a);
            put(HidKey.F16, 0x6b);
            put(HidKey.F17, 0x6c);
            put(HidKey.F18, 0x6d);
            put(HidKey.F19, 0x6e);
            put(HidKey.F20, 0x6f);
            put(HidKey.F21, 0x70);
            put(HidKey.F22, 0x71);
            put(HidKey.F23, 0x72);
            put(HidKey.F24, 0x73);
            put(HidKey.Open, 0x74);
            put(HidKey.Help, 0x75);
            put(HidKey.Again, 0x79);
            put(HidKey.Undo, 0x7a);
            put(HidKey.Cut, 0x7b);
            put(HidKey.Copy, 0x7c);
            put(HidKey.Paste, 0x7d);
            put(HidKey.Find, 0x7e);
            put(HidKey.VolumeMute, 0x7f);
            put(HidKey.VolumeUp, 0x80);
            put(HidKey.VolumeDown, 0x81);
            put(HidKey.NumpadComma, 0x85);
            put(HidKey.InternationalRO, 0x87);
            put(HidKey.KanaMode, 0x88);
            put(HidKey.InternationalYen, 0x89);
            put(HidKey.Convert, 0x8a);
            put(HidKey.NonConvert, 0x8b);
            put(HidKey.Lang1, 0x90);
            put(HidKey.Lang2, 0x91);
            put(HidKey.Lang3, 0x92);
            put(HidKey.Lang4, 0x93);
            put(HidKey.LeftCtrl, 0xe0);
            put(HidKey.LeftShift, 0xe1);
            put(HidKey.LeftAlt, 0xe2);
            put(HidKey.LeftMeta, 0xe3);
            put(HidKey.RightCtrl, 0xe4);
            put(HidKey.RightShift, 0xe5);
            put(HidKey.RightAlt, 0xe6);
            put(HidKey.RightMeta, 0xe7);
          }
        };
  }

  /**
   * Gets an instance of the default HID key map.
   *
   * @return The instance of the key map.
   */
  public static HidKeyMap getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new HidKeyMap();
    }
    return INSTANCE;
  }

  /**
   * Creates or overwrites a HID key code for a known {@link HidKey}. This method should not be
   * called from any outside classes, but only from classes extending {@link HidKeyMap}.
   *
   * @param key The key to change the value for.
   * @param value The key code value to be added or overwritten.
   */
  protected void putKey(HidKey key, int value) {
    keyMap.put(key, value);
  }

  /**
   * Gets the {@link HidKey} representation from a HID key code. This method is useful to get a more
   * human-friendly representation from a key code used in HID messages.
   *
   * @param hidValue The HID key code value seen in HID messages.
   * @return A {@link HidKey} representation of the HID key code, or {@code null} if not found.
   */
  public HidKey getKey(byte hidValue) {
    return keyMap.get(hidValue);
  }
}
