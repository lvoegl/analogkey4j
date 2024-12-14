package org.voegl.analogkey4j.plugins.wooting;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hid4java.HidDevice;
import org.junit.jupiter.api.Test;
import org.voegl.analogkey4j.event.AnalogKeyboardListenerList;

class WootingDeviceTests {

  private AnalogKeyboardListenerList LISTENER_MOCK = mock(AnalogKeyboardListenerList.class);

  private static HidDevice createMockDevice(int vendorId, int productId, int usagePage) {
    HidDevice device = mock(HidDevice.class);
    when(device.getVendorId()).thenReturn(vendorId);
    when(device.getProductId()).thenReturn(productId);
    when(device.getUsagePage()).thenReturn(usagePage);

    return device;
  }

  @Test
  void testIsResponsibleFor80HE() {
    HidDevice w80he = createMockDevice(0x31e3, 0x1402, 0xffffff54);
    WootingDevice plugin = new WootingDevice(w80he, LISTENER_MOCK);

    assertThat(plugin.isResponsible()).isTrue();
  }

  @Test
  void testIsResponsibleForRandomVendor() {
    HidDevice device = createMockDevice(0x12a5, 0x1402, 0xffffff54);
    WootingDevice plugin = new WootingDevice(device, LISTENER_MOCK);

    assertThat(plugin.isResponsible()).isFalse();
  }

  @Test
  void testIsResponsibleForRandomProduct() {
    HidDevice device = createMockDevice(0x31e3, 0xb513, 0xffffff54);
    WootingDevice plugin = new WootingDevice(device, LISTENER_MOCK);

    assertThat(plugin.isResponsible()).isFalse();
  }

  @Test
  void testIsResponsibleForRandomUsage() {
    HidDevice device = createMockDevice(0x31e3, 0x1402, 0x89e);
    WootingDevice plugin = new WootingDevice(device, LISTENER_MOCK);

    assertThat(plugin.isResponsible()).isFalse();
  }
}
