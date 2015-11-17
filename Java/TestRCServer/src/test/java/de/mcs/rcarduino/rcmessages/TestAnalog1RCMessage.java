package de.mcs.rcarduino.rcmessages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TestAnalog1RCMessage extends AbstractTestRCMessage {

  private static final byte[] MESSAGE1 = new byte[] { (byte) 0xdf, (byte) 0x81, // RCARduino
                                                                                // Message
      (byte) 0x00, (byte) 0x81, // Prio message
      (byte) 0x00, (byte) 0x00, // analog Channel 1
      (byte) 0x00, (byte) 0x00, // analog Channel 2
      (byte) 0x00, (byte) 0x00, // analog Channel 3
      (byte) 0x00, (byte) 0x00, // analog Channel 4
      (byte) 0x00, (byte) 0x00, // analog Channel 5
      (byte) 0x00, (byte) 0x00, // analog Channel 6
      (byte) 0x00, (byte) 0x00, // analog Channel 7
      (byte) 0x00, (byte) 0x00 // analog Channel 8
  };

  private static final byte[] MESSAGE2 = new byte[] { (byte) 0xdf, (byte) 0x81, // RCARduino
                                                                                // Message
      (byte) 0x00, (byte) 0x81, // Prio message
      (byte) 0x12, (byte) 0x34, // analog Channel 1
      (byte) 0x12, (byte) 0x34, // analog Channel 2
      (byte) 0x12, (byte) 0x34, // analog Channel 3
      (byte) 0x12, (byte) 0x34, // analog Channel 4
      (byte) 0x12, (byte) 0x34, // analog Channel 5
      (byte) 0x12, (byte) 0x34, // analog Channel 6
      (byte) 0x12, (byte) 0x34, // analog Channel 7
      (byte) 0x12, (byte) 0x34 // analog Channel 8
  };

  @Test
  public void testInjectAnalogChannels1() {
    byte[] message = buildMessageFromTemplate(MESSAGE1);

    RCMessage rcMessage = new Analog1RCMessage(message);

    int[] analog = new int[16];

    rcMessage.injectAnalogChannels(analog);

    for (int i : analog) {
      assertEquals(0x0000, i);
    }
  }

  @Test
  public void testInjectAnalogChannels2() {
    byte[] message = buildMessageFromTemplate(MESSAGE2);

    RCMessage rcMessage = new Analog1RCMessage(message);

    int[] analog = new int[16];

    rcMessage.injectAnalogChannels(analog);

    for (int i = 0; i < analog.length; i++) {
      int value = analog[i];
      if (i < 8) {
        assertEquals(0x1234, value);
      } else {
        assertEquals(0x0000, value);
      }
    }
  }

  @Test
  public void testInjectDigitalChannels1() {
    byte[] message = buildMessageFromTemplate(MESSAGE1);

    RCMessage rcMessage = new Analog1RCMessage(message);

    boolean[] digital = new boolean[1024];

    rcMessage.injectDigitalChannels(digital);

    for (boolean b : digital) {
      assertFalse(b);
    }
  }

  @Test
  public void testInjectDigitalChannels2() {
    byte[] message = buildMessageFromTemplate(MESSAGE2);

    RCMessage rcMessage = new Analog1RCMessage(message);

    boolean[] digital = new boolean[1024];

    rcMessage.injectDigitalChannels(digital);

    for (int i = 0; i < digital.length; i++) {
      assertFalse(digital[i]);
    }
  }

  @Test
  public void testMessageHeader() throws IllegalChannelValueException, IllegalChannelException {
    Analog1RCMessage message = new Analog1RCMessage();

    byte[] datagramm = message.getDatagramm();

    assertEquals(0xdf, (int) datagramm[0] & 0x00FF);
    assertEquals(0x81, (int) datagramm[1] & 0x00FF);
    assertEquals(0, datagramm[2]);
    assertEquals(0x81, (int) datagramm[3] & 0x00FF);
  }

  @Test
  public void testSetIllegalAnalogChannel() throws IllegalChannelValueException, IllegalChannelException {
    RCMessage message = new Analog1RCMessage();

    try {
      message.setAnalogChannel(-1, 2048);
      fail(String.format("%s must be thrown", IllegalChannelException.class.getName()));
    } catch (IllegalChannelException e) {
    }

    try {
      message.setAnalogChannel(8, 2048);
      fail(String.format("%s must be thrown", IllegalChannelException.class.getName()));
    } catch (IllegalChannelException e) {
    }
  }

  @Test
  public void testSetIllegalAnalogChannelValue() throws IllegalChannelValueException, IllegalChannelException {
    RCMessage message = new Analog1RCMessage();

    try {
      message.setAnalogChannel(4, -1);
      fail(String.format("%s must be thrown", IllegalChannelValueException.class.getName()));
    } catch (IllegalChannelValueException e) {
    }

    try {
      message.setAnalogChannel(4, 4096);
      fail(String.format("%s must be thrown", IllegalChannelValueException.class.getName()));
    } catch (IllegalChannelValueException e) {
    }
  }

  @Test
  public void testSetAnalogChannelValue() throws IllegalChannelValueException, IllegalChannelException {
    RCMessage message = new Analog1RCMessage();

    for (int channel = 0; channel < 8; channel++) {
      for (int value = 0; value < 4096; value++) {
        message.setAnalogChannel(channel, value);
        byte[] datagramm = message.getDatagramm();
        int index = (channel * 2) + 4;
        int newValue = (((int) (datagramm[index] & 0x00FF)) << 8) + ((int) datagramm[index + 1] & 0x00FF);
        assertEquals(value, newValue);
      }
    }
  }

  @Test
  public void testIllegalDigitalChannelValue() throws IllegalChannelValueException, IllegalChannelException {
    AbstractRCMessage message = new Analog1RCMessage();

    for (int channel = 0; channel < 1024; channel++) {
      try {
        message.setDigitalChannel(channel, true);
        fail(String.format("%s must be thrown", IllegalChannelException.class.getName()));
      } catch (IllegalChannelException e) {
      }
    }
  }
}
