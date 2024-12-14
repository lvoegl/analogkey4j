package org.voegl.analogkey4j.event;

import org.voegl.analogkey4j.key.HidKey;

/**
 * A simple record holding information about an analog key's pressed state.
 *
 * @param key The key that belongs to this state.
 * @param value The current pressed amount.
 */
public record AnalogKeyState(HidKey key, float value) {}
