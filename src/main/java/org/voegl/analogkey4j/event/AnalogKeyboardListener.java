package org.voegl.analogkey4j.event;

import java.util.EventListener;
import java.util.Set;
import org.voegl.analogkey4j.plugins.AnalogKeyboardDevice;

/**
 * An interface with all methods required to implement a listener that can subscribe to analog
 * keyboard events. This interface should be implemented by library users to receive events from the
 * keyboard(s).
 */
public interface AnalogKeyboardListener extends EventListener {
  /**
   * This method is called if any open analog keyboard's key state has changed.
   *
   * @param keyboard The keyboard for which a key has changed its state.
   * @param keyStates The key states that have changed.
   */
  void keyPressed(AnalogKeyboardDevice keyboard, Set<AnalogKeyState> keyStates);

  /**
   * This method is called if any supported analog keyboard has been attached.
   *
   * @param keyboard The keyboard that was added.
   */
  void keyboardAdded(AnalogKeyboardDevice keyboard);

  /**
   * This method is called if any supported keyboard has been removed.
   *
   * @param keyboard The keyboard that was removed
   */
  void keyboardRemoved(AnalogKeyboardDevice keyboard);

  /**
   * This method is called if any supported analog keyboard was opened.
   *
   * @param keyboard The keyboard that was opened.
   */
  void keyboardOpened(AnalogKeyboardDevice keyboard);

  /**
   * This method is called if any supported analog keyboard was closed.
   *
   * @param keyboard The keyboard that was closed.
   */
  void keyboardClosed(AnalogKeyboardDevice keyboard);

  /**
   * This method is called if an error has occurred for any supported analog keyboard.
   *
   * @param keyboard The keyboard for which an error has occurred.
   * @param message The error message.
   */
  void keyboardError(AnalogKeyboardDevice keyboard, String message);
}
