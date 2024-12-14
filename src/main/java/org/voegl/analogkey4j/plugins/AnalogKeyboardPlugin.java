package org.voegl.analogkey4j.plugins;

import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import org.hid4java.HidDevice;
import org.voegl.analogkey4j.event.AnalogKeyState;
import org.voegl.analogkey4j.event.AnalogKeyboardListenerList;
import org.voegl.analogkey4j.parser.HidParser;

/**
 * Provides an abstract implementation for an analog keyboard plugin. It provides generic methods to
 * interact with analog keyboard, such as opening, reading and closing.
 */
public abstract class AnalogKeyboardPlugin {
  @Getter protected final HidDevice device;
  private final AnalogKeyboardListenerList listeners;
  /* NOTE: this may not always be 48 byte */
  private static final int HID_MESSAGE_SIZE = 48;
  private Thread readThread;
  private final HidParser parser;
  @Getter private boolean readDone = true;

  /**
   * Constructs a new analog keyboard plugin with device-specific functionality.
   *
   * @param device The underlying {@link HidDevice}.
   * @param parser A parser specific to this device.
   * @param listeners Event listeners listening for device changes.
   */
  public AnalogKeyboardPlugin(
      HidDevice device, HidParser parser, AnalogKeyboardListenerList listeners) {
    this.device = device;
    this.parser = parser;
    this.listeners = listeners;
  }

  /**
   * Gets a {@link Runnable} that continuously reads data from the underlying device until the
   * {@link #close()} method is called.
   *
   * @return A {@link Runnable} that can read from the device.
   */
  private synchronized Runnable getReadRunnable() {
    return () -> {
      readDone = false;
      while (!readDone) {
        byte[] data = new byte[HID_MESSAGE_SIZE];
        int val = device.read(data, 1000);

        switch (val) {
          case -1:
            // error
            listeners.fireKeyboardError(device, device.getLastErrorMessage());
            break;
          case 0:
            // nothing to read
            break;
          default:
            // read success
            Set<AnalogKeyState> keyStates = parser.parse(data, val);
            listeners.fireKeyPressed(device, keyStates);
            break;
        }
      }
      device.close();
      listeners.fireKeyboardClosed(device);
    };
  }

  /**
   * Checks whether the current device is closed.
   *
   * @return {@code true} if the device is closed, otherwise {@code false}.
   */
  public boolean isClosed() {
    return readThread == null || !readThread.isAlive();
  }

  /**
   * Opens the underlying device and starts reading from it directly. Sends an opened event to all
   * listeners. Open must only be called once.
   */
  public void open() {
    if (!isClosed()) {
      throw new IllegalStateException("Device is already open.");
    }

    device.open();
    device.setNonBlocking(true);

    readThread = new Thread(getReadRunnable());
    readThread.setDaemon(true);
    readThread.setName("analog keyboard reader");
    readThread.start();
    listeners.fireKeyboardOpened(device);
  }

  /**
   * gracefully closes the underlying device after the next read has finished. Close must only be
   * called once, use {@link #isClosed()} to check.
   */
  public void close() {
    if (isClosed()) {
      throw new IllegalStateException("Device is not open.");
    }
    readDone = true;
  }

  /**
   * Checks if the keyboard's HID device matches a keyboard supported by this plugin.
   *
   * @return {@code true} if the device is a supported keyboard, {@code false} otherwise.
   */
  public abstract boolean isResponsible();

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    AnalogKeyboardPlugin that = (AnalogKeyboardPlugin) o;
    return Objects.equals(device, that.device);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(device);
  }
}
