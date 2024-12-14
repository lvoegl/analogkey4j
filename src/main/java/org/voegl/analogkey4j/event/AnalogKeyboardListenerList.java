package org.voegl.analogkey4j.event;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.voegl.analogkey4j.AnalogKeyboardManager;
import org.voegl.analogkey4j.plugins.AnalogKeyboardDevice;

/**
 * A manager handling all listeners subscribed to any analog keyboard events. Library users should
 * use the add and remove listener function provided in {@link AnalogKeyboardManager}. It can fire
 * events in a non-blocking way by using an {@link ExecutorService}.
 */
public class AnalogKeyboardListenerList {
  private final Set<AnalogKeyboardListener> listeners =
      Collections.synchronizedSet(new HashSet<>());

  private final ExecutorService executorService =
      Executors.newFixedThreadPool(
          3,
          runnable -> {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setName("analog keyboard event thread");
            thread.setDaemon(true);
            return thread;
          });

  /**
   * Adds a new listener to the pool of listeners.
   *
   * @param listener The listener to be added.
   */
  public void add(AnalogKeyboardListener listener) {
    listeners.add(listener);
  }

  /**
   * Removes a listener from the pool of listeners.
   *
   * @param listener The listener to be removed.
   */
  public void remove(AnalogKeyboardListener listener) {
    listeners.remove(listener);
  }

  /**
   * Sends a keyboard added event to all listeners.
   *
   * @param hidDevice The devices that is added.
   */
  public void fireKeyboardAdded(AnalogKeyboardDevice hidDevice) {
    executorService.submit(() -> listeners.forEach(listener -> listener.keyboardAdded(hidDevice)));
  }

  /**
   * Sends a keyboard removed event to all listeners.
   *
   * @param hidDevice The devices that is removed.
   */
  public void fireKeyboardRemoved(AnalogKeyboardDevice hidDevice) {
    executorService.submit(
        () -> listeners.forEach(listener -> listener.keyboardRemoved(hidDevice)));
  }

  /**
   * Sends a keyboard opened event to all listeners.
   *
   * @param hidDevice The devices that is opened.
   */
  public void fireKeyboardOpened(AnalogKeyboardDevice hidDevice) {
    executorService.submit(() -> listeners.forEach(listener -> listener.keyboardOpened(hidDevice)));
  }

  /**
   * Sends a keyboard closed event to all listeners.
   *
   * @param hidDevice The devices that is closed.
   */
  public void fireKeyboardClosed(AnalogKeyboardDevice hidDevice) {
    executorService.submit(() -> listeners.forEach(listener -> listener.keyboardClosed(hidDevice)));
  }

  /**
   * Sends a keyboard error event to all listeners.
   *
   * @param hidDevice The devices for which an error has occurred.
   * @param message The error message.
   */
  public void fireKeyboardError(AnalogKeyboardDevice hidDevice, String message) {
    executorService.submit(
        () -> listeners.forEach(listener -> listener.keyboardError(hidDevice, message)));
  }

  /**
   * Sends a key pressed event to all listeners.
   *
   * @param hidDevice The device which the key belongs to.
   * @param states The keys and corresponding states that have changed.
   */
  public void fireKeyPressed(AnalogKeyboardDevice hidDevice, Set<AnalogKeyState> states) {
    executorService.submit(
        () ->
            listeners.forEach(
                listener -> listener.keyPressed(hidDevice, Collections.unmodifiableSet(states))));
  }
}
