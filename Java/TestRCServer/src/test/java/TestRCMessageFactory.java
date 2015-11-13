
/**
 * 
 */
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.mcs.rcarduino.rcmessages.Analog1RCMessage;
import de.mcs.rcarduino.rcmessages.Analog2RCMessage;
import de.mcs.rcarduino.rcmessages.Digital1RCMessage;
import de.mcs.rcarduino.rcmessages.Digital2RCMessage;
import de.mcs.rcarduino.rcmessages.Mix1RCMessage;
import de.mcs.rcarduino.rcmessages.Mix2RCMessage;
import de.mcs.rcarduino.rcmessages.PrioRCMessage;
import de.mcs.rcarduino.rcmessages.RCMessage;
import de.mcs.rcarduino.rcmessages.RCMessageFactory;

/**
 * @author w.klaas
 *
 */
public class TestRCMessageFactory {
  private static final byte[] MESSAGE1 = new byte[] { (byte) 0xdf, (byte) 0x81, // RCARduino Message
      (byte) 0x00, (byte) 0x11, // Prio message
      (byte) 0x00, (byte) 0x00, // analog Channel 1
      (byte) 0x00, (byte) 0x00, // analog Channel 2
      (byte) 0x00, (byte) 0x00, // analog Channel 3
      (byte) 0x00, (byte) 0x00, // analog Channel 4
      (byte) 0x00, (byte) 0x00, // digital Channel 129..256
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

  @Test
  public void testFactory() {
    byte[] message = buildMessageFromTemplate(MESSAGE1);

    RCMessage rcMessage = RCMessageFactory.getRCMessage(message);

    assertTrue(rcMessage instanceof PrioRCMessage);

    message[3] = (byte) 0x21;

    rcMessage = RCMessageFactory.getRCMessage(message);

    assertTrue(rcMessage instanceof Mix1RCMessage);

    message[3] = (byte) 0x22;

    rcMessage = RCMessageFactory.getRCMessage(message);

    assertTrue(rcMessage instanceof Mix2RCMessage);

    message[3] = (byte) 0x41;

    rcMessage = RCMessageFactory.getRCMessage(message);

    assertTrue(rcMessage instanceof Digital1RCMessage);

    message[3] = (byte) 0x42;

    rcMessage = RCMessageFactory.getRCMessage(message);

    assertTrue(rcMessage instanceof Digital2RCMessage);

    message[3] = (byte) 0x81;

    rcMessage = RCMessageFactory.getRCMessage(message);

    assertTrue(rcMessage instanceof Analog1RCMessage);

    message[3] = (byte) 0x82;

    rcMessage = RCMessageFactory.getRCMessage(message);

    assertTrue(rcMessage instanceof Analog2RCMessage);
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
