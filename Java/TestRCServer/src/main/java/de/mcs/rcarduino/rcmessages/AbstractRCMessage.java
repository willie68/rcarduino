/**
 * 
 */
package de.mcs.rcarduino.rcmessages;

/**
 * @author w.klaas
 *
 */
abstract public class AbstractRCMessage implements RCMessage {
  protected byte[] message;

  public AbstractRCMessage(byte[] message) {
    this.message = message;
  }

  abstract public void injectAnalogChannels(int[] analog);

  abstract public void injectDigitalChannels(boolean[] digital);

}
