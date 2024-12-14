package org.voegl.analogkey4j.parser;

import static com.google.common.truth.Truth.assertThat;

import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.voegl.analogkey4j.event.AnalogKeyState;
import org.voegl.analogkey4j.key.HidKeyMap;

class HidParserTest {

  private static final int HID_MESSAGE_SIZE = 64;
  private static final HidKeyMap KEY_MAP = HidKeyMap.getInstance();
  private HidParser parser;

  @BeforeEach
  void setup() {
    // This stores a last state so make sure to get a new one every time
    parser = new HidParser(KEY_MAP);
  }

  private byte hexToByte(int hex) {
    if (hex < 0 || hex > Math.abs(Byte.MIN_VALUE) + Math.abs(Byte.MAX_VALUE)) {
      throw new AssertionError("Invalid hex value: " + hex);
    }

    return (byte) hex;
  }

  private byte[] hexToBytes(int[] hex) {
    byte[] result = new byte[HID_MESSAGE_SIZE];

    for (int i = 0; i < hex.length; i++) {
      result[i] = hexToByte(hex[i]);
    }

    return result;
  }

  @Test
  void testParseEmptyHid() {
    /* use empty message (all zeros) */
    int[] hidMessageInt = {};
    byte[] hidMessage = hexToBytes(hidMessageInt);

    Set<AnalogKeyState> expectedStates = Collections.emptySet();

    assertThat(parser.parse(hidMessage, hidMessage.length)).isEqualTo(expectedStates);
  }

  @Test
  void testParseSingleKeyHid() {
    int[] hidMessageInt = {0x00, 0xe1, 0xFF};
    byte[] hidMessage = hexToBytes(hidMessageInt);

    Set<AnalogKeyState> expectedStates =
        Set.of(new AnalogKeyState(KEY_MAP.getKey((byte) 0xe1), 1.0f));

    assertThat(parser.parse(hidMessage, 3)).isEqualTo(expectedStates);
  }

  @Test
  void testParseTwoKeyHid() {
    int[] hidMessageInt = {0x00, 0x17, 0xFF, 0x00, 0xe4, 0x00};
    byte[] hidMessage = hexToBytes(hidMessageInt);

    Set<AnalogKeyState> expectedStates =
        Set.of(
            new AnalogKeyState(KEY_MAP.getKey((byte) 0x17), 1.0f),
            new AnalogKeyState(KEY_MAP.getKey((byte) 0xe4), 0.0f));

    assertThat(parser.parse(hidMessage, 6)).isEqualTo(expectedStates);
  }

  @Test
  void testDifferentHidMessage() {
    int[] hidMessageInt = {0x01};
    byte[] hidMessage = hexToBytes(hidMessageInt);

    Set<AnalogKeyState> expectedStates = Collections.emptySet();

    assertThat(parser.parse(hidMessage, 3)).isEqualTo(expectedStates);
  }
}
