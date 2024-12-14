package org.voegl.analogkey4j.plugins;

import lombok.ToString;
import org.hid4java.HidDevice;

/**
 * Represents a simple analog keyboard. This class is mostly used to specify a specific keyboard by
 * hardware information.
 */
@ToString
public class SimpleAnalogKeyboard {

  private final String name;
  private final int vendorId;
  private final int productId;
  private final int usagePage;

  /**
   * Constructs a new simple analog keyboard from the given hardware information.
   *
   * @param name A user specified name.
   * @param vendorId The vendor id (VID) for the device.
   * @param productId The product id (PID) for the device.
   * @param usagePage The HID usage page for the device.
   */
  public SimpleAnalogKeyboard(String name, int vendorId, int productId, int usagePage) {
    this.name = name;
    this.vendorId = vendorId;
    this.productId = productId;
    this.usagePage = usagePage;
  }

  /**
   * Checks whether this device is the same hardware device as the given {@link HidDevice}.
   *
   * @param o The {@link HidDevice} to match against.
   * @return {@code true} if the current instance and the parameter refer to the same hardware
   *     device, {@code false} otherwise.
   */
  public boolean matchesHidDevice(HidDevice o) {
    return productId == o.getProductId()
        && vendorId == o.getVendorId()
        && usagePage == o.getUsagePage();
  }
}
