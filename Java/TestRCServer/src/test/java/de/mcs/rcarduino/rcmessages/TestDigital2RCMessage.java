package de.mcs.rcarduino.rcmessages;

import static org.junit.Assert.*;

import org.junit.Test;

import de.mcs.rcarduino.rcmessages.Digital1RCMessage;
import de.mcs.rcarduino.rcmessages.Digital2RCMessage;
import de.mcs.rcarduino.rcmessages.IllegalChannelException;
import de.mcs.rcarduino.rcmessages.IllegalChannelValueException;
import de.mcs.rcarduino.rcmessages.PrioRCMessage;
import de.mcs.rcarduino.rcmessages.RCMessage;

public class TestDigital2RCMessage extends AbstractTestRCMessage {

  private static final byte[] MESSAGE1 = new byte[] { (byte) 0xdf, (byte) 0x81, // RCARduino Message
      (byte) 0x00, (byte) 0x41, // Prio message
      (byte) 0x00, (byte) 0x00, // digital Channel 197..392
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

  private static final byte[] MESSAGE2 = new byte[] { (byte) 0xdf, (byte) 0x81, // RCARduino Message
      (byte) 0x00, (byte) 0x41, // Prio message
      (byte) 0xFF, (byte) 0xFF, // digital Channel 1..392
      (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
      (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
      (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

  @Test
  public void testInjectAnalogChannels1() {
    byte[] message = buildMessageFromTemplate(MESSAGE1);

    RCMessage rcMessage = new Digital2RCMessage(message);

    int[] analog = new int[16];

    rcMessage.injectAnalogChannels(analog);

    for (int i : analog) {
      assertEquals(0x0000, i);
    }
  }

  @Test
  public void testInjectAnalogChannels2() {
    byte[] message = buildMessageFromTemplate(MESSAGE2);

    RCMessage rcMessage = new Digital2RCMessage(message);

    int[] analog = new int[16];

    rcMessage.injectAnalogChannels(analog);

    for (int i : analog) {
      assertEquals(0x0000, i);
    }
  }

  @Test
  public void testInjectDigitalChannels1() {
    byte[] message = buildMessageFromTemplate(MESSAGE1);

    RCMessage rcMessage = new Digital2RCMessage(message);

    boolean[] digital = new boolean[1024];

    rcMessage.injectDigitalChannels(digital);

    for (boolean b : digital) {
      assertFalse(b);
    }
  }

  @Test
  public void testInjectDigitalChannels2() {
    byte[] message = buildMessageFromTemplate(MESSAGE2);

    RCMessage rcMessage = new Digital2RCMessage(message);

    boolean[] digital = new boolean[1024];

    rcMessage.injectDigitalChannels(digital);

    for (int i = 0; i < digital.length; i++) {
      if (i >= 192 && i < 384) {
        assertTrue(digital[i]);
      } else {
        assertFalse(digital[i]);
      }
    }
  }

  @Test
  public void testMessageHeader() throws IllegalChannelValueException, IllegalChannelException {
    Digital2RCMessage message = new Digital2RCMessage();

    byte[] datagramm = message.getDatagramm();

    assertEquals(0xdf, (int) datagramm[0] & 0x00FF);
    assertEquals(0x81, (int) datagramm[1] & 0x00FF);
    assertEquals(0, datagramm[2]);
    assertEquals(0x42, (int) datagramm[3] & 0x00FF);
  }

  @Test
  public void testSetIllegalAnalogChannel() throws IllegalChannelValueException, IllegalChannelException {
    RCMessage message = new Digital2RCMessage();

    for (int channel = -1; channel < 16; channel++) {
      try {
        message.setAnalogChannel(channel, 4096);
        fail(String.format("%s must be thrown", IllegalChannelValueException.class.getName()));
      } catch (IllegalChannelException e) {
      }
    }
  }

  @Test
  public void testSetIllegalDigitalChannel() throws IllegalChannelValueException, IllegalChannelException {
    RCMessage message = new Digital2RCMessage();

    try {
      message.setDigitalChannel(-1, true);
      fail(String.format("%s must be thrown", IllegalChannelException.class.getName()));
    } catch (IllegalChannelException e) {
    }

    try {
      message.setDigitalChannel(191, true);
      fail(String.format("%s must be thrown", IllegalChannelException.class.getName()));
    } catch (IllegalChannelException e) {
    }
    try {
      message.setDigitalChannel(384, true);
      fail(String.format("%s must be thrown", IllegalChannelException.class.getName()));
    } catch (IllegalChannelException e) {
    }
  }

  @Test
  public void testSetDigitalChannelValue() throws IllegalChannelValueException, IllegalChannelException {
    AbstractRCMessage message = new Digital2RCMessage();

    for (int channel = 192; channel < 384; channel++) {
      message.setDigitalChannel(channel, true);
      byte[] datagramm = message.getDatagramm();

      int index = (channel - 192) / 8 + message.getDigitalChannelIndexStart();
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
