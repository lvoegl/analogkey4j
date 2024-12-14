package org.voegl.analogkey4j.parser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.voegl.analogkey4j.event.AnalogKeyState;
import org.voegl.analogkey4j.key.HidKey;
import org.voegl.analogkey4j.key.HidKeyMap;

/**
 * Provides a generic implementation for an HID message parser that is able to parse most analog
 * keyboard's HID messages.
 */
public class HidParser {
  private static final float SAFE_ZERO = (float) 1 / (2 * 0xFF);
  protected Set<AnalogKeyState> lastKeyStates = Collections.emptySet();
  private final HidKeyMap keyMap;

  /**
   * Constructs an HID parser that depends on the specific key map used by the keyboard model.
   *
   * @param keyMap The key map used.
   */
  public HidParser(HidKeyMap keyMap) {
    this.keyMap = keyMap;
  }

  /**
   * Helper function that fills reset (0 pressure) key states for keys, that have been pressed in
   * the last sent message, but are not pressed anymore now.
   *
   * @param states The already parsed states from the current message.
   */
  private void resetLastKeys(Set<AnalogKeyState> states) {
    Set<HidKey> keys = new HashSet<>();
    for (AnalogKeyState state : states) {
      keys.add(state.key());
    }
    for (AnalogKeyState state : lastKeyStates) {
      if (state.value() > SAFE_ZERO && !keys.contains(state.key())) {
        states.add(new AnalogKeyState(state.key(), 0.0f));
      }
    }
  }

  /**
   * Provides a (as far as I know) generic implementation to read analog key press values from an
   * HID message.
   *
   * @param data The HID message in plain bytes.
   * @param len The length of the HID message.
   * @return A {@link Set} of {@link AnalogKeyState} which have changed.
   */
  public Set<AnalogKeyState> parse(byte[] data, int len) {
    Set<AnalogKeyState> states = new HashSet<>();

    for (int i = 0; i < len - 2; i += 3) {
      byte type = data[i];

      if (type != (byte) 0) {
        continue;
      }
      byte key = data[i + 1];
      if (key == (byte) 0) {
        break;
      }
      byte value = data[i + 2];
      float pressed = (value & 0xFF) / 255.0f;

      states.add(new AnalogKeyState(keyMap.getKey(key), pressed));
    }

    resetLastKeys(states);
    lastKeyStates = states;
    return states;
  }
}
