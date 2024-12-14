package org.voegl.analogkey4j;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.HidServicesSpecification;
import org.hid4java.event.HidServicesEvent;
import org.voegl.analogkey4j.event.AnalogKeyboardListener;
import org.voegl.analogkey4j.event.AnalogKeyboardListenerList;
import org.voegl.analogkey4j.plugins.AnalogKeyboardPlugin;
import org.voegl.analogkey4j.plugins.AnalogKeyboardPluginUtil;

/**
 * Manages analog keyboard devices, including starting, stopping, and monitoring their activity. It
 * listens for HID device events and interacts with the devices accordingly, managing a list of
 * active supported analog keyboards.
 */
public class AnalogPluginManager implements HidServicesListener {

  private final AnalogKeyboardListenerList listeners = new AnalogKeyboardListenerList();
  private HidServices hidServices;
  @Getter private final Set<AnalogKeyboardPlugin> keyboardDevices = new HashSet<>();

  /**
   * Starts the HidServices and initializes attached devices. This method configures the HID
   * service, grabs the initial list of attached devices and begins listening for newly attached HID
   * events.
   */
  public void start() {
    HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();
    hidServicesSpecification.setAutoStart(false);
    hidServicesSpecification.setAutoDataRead(false);
    hidServices = HidManager.getHidServices(hidServicesSpecification);
    hidServices.addHidServicesListener(this);
    hidServices.start();

    for (HidDevice hidDevice : hidServices.getAttachedHidDevices()) {
      Optional<AnalogKeyboardPlugin> keyboardDevice =
          AnalogKeyboardPluginUtil.getAvailablePlugin(hidDevice, listeners);
      keyboardDevice.ifPresent(keyboardDevices::add);
    }
  }

  /**
   * Stops the HidServices and closes any active analog keyboard devices. This method ensures all
   * devices are properly closed and sends close and remove events accordingly.
   */
  public void stop() {
    // close all unclosed devices
    for (AnalogKeyboardPlugin device : keyboardDevices) {
      if (!device.isReadDone() && !device.isClosed()) {
        device.close();
      }
    }

    // wait for devices to close
    long startTime = System.currentTimeMillis();
    long timeout = 60000;

    while (keyboardDevices.stream().anyMatch(d -> !d.isClosed())) {
      if (System.currentTimeMillis() - startTime > timeout) {
        throw new RuntimeException("Some keyboard devices did not close after 60 seconds.");
      }

      try {
        // noinspection BusyWait
        Thread.sleep(50);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    // manually send removed events
    for (AnalogKeyboardPlugin keyboardDevice : keyboardDevices) {
      keyboardDevices.remove(keyboardDevice);
      listeners.fireKeyboardRemoved(keyboardDevice.getDevice());
    }

    hidServices.stop();
  }

  /**
   * Adds an event listener to the list of event listeners.
   *
   * @param listener The event listener to be added.
   */
  public void addListener(AnalogKeyboardListener listener) {
    listeners.add(listener);
  }

  /**
   * Removes an event listener to the list of event listeners.
   *
   * @param listener The event listener to be removed.
   */
  public void removeListener(AnalogKeyboardListener listener) {
    listeners.remove(listener);
  }

  /**
   * Called when an HID device is attached. If the device is an analog keyboard, it will be added to
   * the list of supported devices. An event is fired to notify listeners.
   *
   * @param event The HID detach event.
   */
  @Override
  public void hidDeviceAttached(HidServicesEvent event) {
    Optional<AnalogKeyboardPlugin> optionalKeyboard =
        AnalogKeyboardPluginUtil.getAvailablePlugin(event.getHidDevice(), listeners);
    if (optionalKeyboard.isPresent()) {
      AnalogKeyboardPlugin keyboard = optionalKeyboard.get();
      listeners.fireKeyboardAdded(keyboard.getDevice());
      keyboardDevices.add(keyboard);
    }
  }

  /**
   * Called when an HID device is detached. If the device is a supported analog keyboard, it will be
   * closed and removed from the list of active devices. An event is fired to notify listeners.
   *
   * @param event The HID detach event.
   */
  @Override
  public void hidDeviceDetached(HidServicesEvent event) {
    for (AnalogKeyboardPlugin keyboardDevice : keyboardDevices) {
      if (keyboardDevice.isResponsible()) {
        if (!keyboardDevice.isClosed()) {
          keyboardDevice.close();
        }
        keyboardDevices.remove(keyboardDevice);
        listeners.fireKeyboardRemoved(keyboardDevice.getDevice());
      }
    }
  }

  /**
   * Called when a HID failure occurs. The event is handed over to the listeners to notify them
   * about any failures.
   *
   * @param event The HID failure event.
   */
  @Override
  public void hidFailure(HidServicesEvent event) {
    HidDevice keyboard = event.getHidDevice();
    listeners.fireKeyboardError(keyboard, keyboard.getLastErrorMessage());
  }

  /**
   * Called when data is received from an HID device. Data is read from the devices directly, so
   * this must not do anything.
   *
   * @param event The HID data event.
   */
  @Override
  public void hidDataReceived(HidServicesEvent event) {}
}
