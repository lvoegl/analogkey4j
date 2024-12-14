package org.voegl.analogkey4j.plugins;

import java.util.Optional;
import java.util.Set;
import org.hid4java.HidDevice;
import org.voegl.analogkey4j.event.AnalogKeyboardListenerList;
import org.voegl.analogkey4j.plugins.wooting.WootingPlugin;

/**
 * A utility class for managing available analog keyboard plugins. This class is responsible for
 * detecting compatible devices and selecting the appropriate plugin to manage them.
 */
public class AnalogKeyboardPluginUtil {

  /** Class must not be instantiated. */
  private AnalogKeyboardPluginUtil() {
    throw new UnsupportedOperationException();
  }

  /**
   * Retrieves a set of available analog keyboard plugins for the given HID device.
   *
   * @param device The HID device to check for available analog keyboard plugins.
   * @param listeners A list of listeners that will be notified of events related to the devices.
   * @return A set of analog keyboard plugins that correspond to the given HID device.
   */
  private static Set<AnalogKeyboardPlugin> getDevices(
      HidDevice device, AnalogKeyboardListenerList listeners) {
    return Set.of(new WootingPlugin(device, listeners));
  }

  /**
   * Attempts to find the first (and only) available analog keyboard plugin for the given HID
   * device. The method checks if any plugin is responsible for handling the device.
   *
   * @param device The HID device to check for available analog keyboard plugins.
   * @param listeners A list of listeners to notify about events related to the device.
   * @return An {@link Optional} containing the first responsible analog keyboard device, or an
   *     empty {@link Optional} if no compatible plugin is found.
   */
  public static Optional<AnalogKeyboardPlugin> getAvailablePlugin(
      HidDevice device, AnalogKeyboardListenerList listeners) {
    return getDevices(device, listeners).stream()
        .filter(AnalogKeyboardPlugin::isResponsible)
        .findFirst();
  }
}
