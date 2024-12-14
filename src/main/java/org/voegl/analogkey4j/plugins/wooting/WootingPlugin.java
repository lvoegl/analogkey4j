package org.voegl.analogkey4j.plugins.wooting;

import java.util.Set;
import org.hid4java.HidDevice;
import org.voegl.analogkey4j.event.AnalogKeyboardListenerList;
import org.voegl.analogkey4j.key.HidKeyMap;
import org.voegl.analogkey4j.parser.HidParser;
import org.voegl.analogkey4j.plugins.AnalogKeyboardPlugin;
import org.voegl.analogkey4j.plugins.SimpleAnalogKeyboard;

/**
 * Represents a Wooting analog keyboard device. This class extends {@link AnalogKeyboardPlugin} and
 * implements the specific functionality required to communicate with Wooting HE devices.
 */
public class WootingPlugin extends AnalogKeyboardPlugin {

  private static final Set<SimpleAnalogKeyboard> KEYBOARDS =
      Set.of(new SimpleAnalogKeyboard("Wooting 80HE", 0x31e3, 0x1402, 0xffffff54));
  private static final HidParser PARSER = new HidParser(HidKeyMap.getInstance());

  /**
   * Constructs a new {@link WootingPlugin} instance. This constructor initializes the device and
   * its associated parser, as well as the listeners for event notifications.
   *
   * @param device The HID device representing the Wooting keyboard.
   * @param listeners The list of listeners that will be notified of keyboard events.
   */
  public WootingPlugin(HidDevice device, AnalogKeyboardListenerList listeners) {
    super(device, PARSER, listeners);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isResponsible() {
    return KEYBOARDS.stream().anyMatch(k -> k.matchesHidDevice(device));
  }
}
