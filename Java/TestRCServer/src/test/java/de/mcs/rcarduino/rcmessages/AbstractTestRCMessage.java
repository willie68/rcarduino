package de.mcs.rcarduino.rcmessages;
/**
 * 
 */

/**
 * @author w.klaas
 *
 */
public abstract class AbstractTestRCMessage {

  final byte[] buildMessageFromTemplate(byte[] messageTemplate) {
    byte[] message = new byte[32];
    for (int i = 0; i < messageTemplate.length; i++) {
      message[i] = messageTemplate[i];
    }

    injectCrc(message);
    return message;
  }

  final void injectCrc(byte[] message) {
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
