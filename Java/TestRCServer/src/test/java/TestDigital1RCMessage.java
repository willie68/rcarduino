import static org.junit.Assert.*;

import org.junit.Test;

import de.mcs.rcarduino.rcmessages.Digital1RCMessage;
import de.mcs.rcarduino.rcmessages.PrioRCMessage;
import de.mcs.rcarduino.rcmessages.RCMessage;

public class TestDigital1RCMessage {

  private static final byte[] MESSAGE1 = new byte[] { (byte) 0xdf, (byte) 0x81, // RCARduino Message
      (byte) 0x00, (byte) 0x41, // Prio message
      (byte) 0x00, (byte) 0x00, // digital Channel 1..192
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

  private static final byte[] MESSAGE2 = new byte[] { (byte) 0xdf, (byte) 0x81, // RCARduino Message
      (byte) 0x00, (byte) 0x41, // Prio message
      (byte) 0xFF, (byte) 0xFF, // digital Channel 1..192
      (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
      (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
      (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

  @Test
  public void testInjectAnalogChannels1() {
    byte[] message = buildMessageFromTemplate(MESSAGE1);

    RCMessage rcMessage = new Digital1RCMessage(message);

    int[] analog = new int[16];

    rcMessage.injectAnalogChannels(analog);

    for (int i : analog) {
      assertEquals(0x0000, i);
    }
  }

  @Test
  public void testInjectAnalogChannels2() {
    byte[] message = buildMessageFromTemplate(MESSAGE2);

    RCMessage rcMessage = new Digital1RCMessage(message);

    int[] analog = new int[16];

    rcMessage.injectAnalogChannels(analog);

    for (int i : analog) {
      assertEquals(0x0000, i);
    }
  }

  @Test
  public void testInjectDigitalChannels1() {
    byte[] message = buildMessageFromTemplate(MESSAGE1);

    RCMessage rcMessage = new Digital1RCMessage(message);

    boolean[] digital = new boolean[1024];

    rcMessage.injectDigitalChannels(digital);

    for (boolean b : digital) {
      assertFalse(b);
    }
  }

  @Test
  public void testInjectDigitalChannels2() {
    byte[] message = buildMessageFromTemplate(MESSAGE2);

    RCMessage rcMessage = new Digital1RCMessage(message);

    boolean[] digital = new boolean[1024];

    rcMessage.injectDigitalChannels(digital);

    for (int i = 0; i < digital.length; i++) {
      if (i < 192) {
        assertTrue(digital[i]);
      } else {
        assertFalse(digital[i]);
      }
    }
  }

  private byte[] buildMessageFromTemplate(byte[] messageTemplate) {
    byte[] message = new byte[32];
    for (int i = 0; i < messageTemplate.length; i++) {
      message[i] = messageTemplate[i];
    }

    injectCrc(message);
    return message;
  }

  private void injectCrc(byte[] message) {
    byte lowCrc = 0;
    byte highCrc = 0;

    int messageLength = message.length;

    for (int i = 0; i < (messageLength - 2); i++) {
      if ((i % 2) == 0) {
        lowCrc = (byte) (lowCrc ^ message[i]);
      } else {
        highCrc = (byte) (highCrc ^ message[i]);
      }
    }
    message[messageLength - 2] = highCrc;
    message[messageLength - 1] = lowCrc;
  }
}
