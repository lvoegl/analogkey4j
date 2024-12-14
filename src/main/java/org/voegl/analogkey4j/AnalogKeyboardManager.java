package org.voegl.analogkey4j;

import org.voegl.analogkey4j.event.AnalogKeyboardListener;
import org.voegl.analogkey4j.event.HidServicesListenerImpl;

public class AnalogKeyboardManager {

  private final HidServicesListenerImpl hidServicesListener = new HidServicesListenerImpl();

  /**
   * Starts the HidServices and initializes attached devices. This method configures the HID
   * service, grabs the initial list of attached devices and begins listening for newly attached HID
   * events.
   */
  public void start() {
    hidServicesListener.start();
  }

  /**
   * Stops the HidServices and closes any active analog keyboard devices. This method ensures all
   * devices are properly closed and sends close and remove events accordingly.
   */
  public void stop() {
    hidServicesListener.stop();
  }

  /**
   * Adds an event listener to the list of event listeners.
   *
   * @param listener The event listener to be added.
   */
  public void addListener(AnalogKeyboardListener listener) {
    hidServicesListener.addListener(listener);
  }

  /**
   * Removes an event listener to the list of event listeners.
   *
   * @param listener The event listener to be removed.
   */
  public void removeListener(AnalogKeyboardListener listener) {
    hidServicesListener.addListener(listener);
  }
}
