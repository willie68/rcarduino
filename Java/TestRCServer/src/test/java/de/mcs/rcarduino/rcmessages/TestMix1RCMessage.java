package de.mcs.rcarduino.rcmessages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TestMix1RCMessage extends AbstractTestRCMessage {

  private static final byte[] MESSAGE1 = new byte[] { (byte) 0xdf, (byte) 0x81, // RCARduino
                                                                                // Message
      (byte) 0x00, (byte) 0x11, // Prio message
      (byte) 0x00, (byte) 0x00, // analog Channel 1
      (byte) 0x00, (byte) 0x00, // analog Channel 2
      (byte) 0x00, (byte) 0x00, // analog Channel 3
      (byte) 0x00, (byte) 0x00, // analog Channel 4
      (byte) 0x00, (byte) 0x00, // digital Channel 1..128
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

  private static final byte[] MESSAGE2 = new byte[] { (byte) 0xdf, (byte) 0x81, // RCARduino
                                                                                // Message
      (byte) 0x00, (byte) 0x11, // Prio message
      (byte) 0x12, (byte) 0x34, // analog Channel 1
      (byte) 0x12, (byte) 0x34, // analog Channel 2
      (byte) 0x12, (byte) 0x34, // analog Channel 3
      (byte) 0x12, (byte) 0x34, // analog Channel 4
      (byte) 0xFF, (byte) 0xFF, // digital Channel 1..128
      (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
      (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

  @Test
  public void testInjectAnalogChannels1() {
    byte[] message = buildMessageFromTemplate(MESSAGE1);

    RCMessage rcMessage = new Mix1RCMessage(message);

    int[] analog = new int[16];

    rcMessage.injectAnalogChannels(analog);

    for (int i : analog) {
      assertEquals(0x0000, i);
    }
  }

  @Test
  public void testInjectAnalogChannels2() {
    byte[] message = buildMessageFromTemplate(MESSAGE2);

    RCMessage rcMessage = new Mix1RCMessage(message);

    int[] analog = new int[16];

    rcMessage.injectAnalogChannels(analog);

    for (int i = 0; i < analog.length; i++) {
      int value = analog[i];
      if (i < 4) {
        assertEquals(0x1234, value);
      } else {
        assertEquals(0x0000, value);
      }
    }
  }

  @Test
  public void testInjectDigitalChannels1() {
    byte[] message = buildMessageFromTemplate(MESSAGE1);

    RCMessage rcMessage = new Mix1RCMessage(message);

    boolean[] digital = new boolean[1024];

    rcMessage.injectDigitalChannels(digital);

    for (boolean b : digital) {
      assertFalse(b);
    }
  }

  @Test
  public void testInjectDigitalChannels2() {
    byte[] message = buildMessageFromTemplate(MESSAGE2);

    RCMessage rcMessage = new Mix1RCMessage(message);

    boolean[] digital = new boolean[1024];

    rcMessage.injectDigitalChannels(digital);

    for (int i = 0; i < digital.length; i++) {
      if (i < 128) {
        assertTrue(digital[i]);
      } else {
        assertFalse(digital[i]);
      }
    }
  }

  @Test
  public void testMessageHeader() throws IllegalChannelValueException, IllegalChannelException {
    Mix1RCMessage message = new Mix1RCMessage();

    byte[] datagramm = message.getDatagramm();

    assertEquals(0xdf, (int) datagramm[0] & 0x00FF);
    assertEquals(0x81, (int) datagramm[1] & 0x00FF);
    assertEquals(0, datagramm[2]);
    assertEquals(0x21, (int) datagramm[3] & 0x00FF);
  }

  @Test
  public void testSetIllegalAnalogChannel() throws IllegalChannelValueException, IllegalChannelException {
    RCMessage message = new Mix1RCMessage();

    try {
      message.setAnalogChannel(-1, 2048);
      fail(String.format("%s must be thrown", IllegalChannelException.class.getName()));
    } catch (IllegalChannelException e) {
    }

    try {
      message.setAnalogChannel(4, 2048);
      fail(String.format("%s must be thrown", IllegalChannelException.class.getName()));
    } catch (IllegalChannelException e) {
    }
  }

  @Test
  public void testSetIllegalAnalogChannelValue() throws IllegalChannelValueException, IllegalChannelException {
    RCMessage message = new Mix1RCMessage();

    try {
      message.setAnalogChannel(0, -1);
      fail(String.format("%s must be thrown", IllegalChannelValueException.class.getName()));
    } catch (IllegalChannelValueException e) {
    }

    try {
      message.setAnalogChannel(0, 4096);
      fail(String.format("%s must be thrown", IllegalChannelValueException.class.getName()));
    } catch (IllegalChannelValueException e) {
    }
  }

  @Test
  public void testSetAnalogChannelValue() throws IllegalChannelValueException, IllegalChannelException {
    RCMessage message = new Mix1RCMessage();

    for (int channel = 0; channel < 4; channel++) {
      for (int value = 0; value < 4096; value++) {
        message.setAnalogChannel(channel, value);
        byte[] datagramm = message.getDatagramm();

        int newValue = (((int) (datagramm[(channel * 2) + 4] & 0x00FF)) << 8)
            + ((int) datagramm[(channel * 2) + 5] & 0x00FF);
        assertEquals(value, newValue);
      }
    }
  }

  @Test
  public void testSetIllegalDigitalChannel() throws IllegalChannelValueException, IllegalChannelException {
    RCMessage message = new Mix1RCMessage();

    try {
      message.setDigitalChannel(-1, true);
      fail(String.format("%s must be thrown", IllegalChannelException.class.getName()));
    } catch (IllegalChannelException e) {
    }

    try {
      message.setDigitalChannel(128, false);
      fail(String.format("%s must be thrown", IllegalChannelException.class.getName()));
    } catch (IllegalChannelException e) {
    }
  }

  @Test
  public void testSetDigitalChannelValue() throws IllegalChannelValueException, IllegalChannelException {
    AbstractRCMessage message = new Mix1RCMessage();

    for (int channel = 0; channel < 128; channel++) {
      message.setDigitalChannel(channel, true);
      byte[] datagramm = message.getDatagramm();

      int index = channel / 8 + message.getDigitalChannelIndexStart();
      int bit = channel % 8;
      boolean newValue = (datagramm[index] & (1 << bit)) > 0;
      assertTrue(newValue);

      message.setDigitalChannel(channel, false);
      datagramm = message.getDatagramm();

      newValue = (datagramm[index] & (1 << bit)) > 0;
      assertFalse(newValue);
    }
  }
}
