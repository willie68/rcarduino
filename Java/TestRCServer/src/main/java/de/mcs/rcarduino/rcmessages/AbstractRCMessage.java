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

  public AbstractRCMessage(int messageID) {
    this.message = new byte[32];
    this.message[0] = (byte) 0xdf;
    this.message[1] = (byte) 0x81;
    this.message[2] = (byte) (messageID >> 8);
    this.message[3] = (byte) (messageID & 0x00FF);
  }

  public AbstractRCMessage(byte[] message) {
    this.message = message;
  }

  public void injectAnalogChannels(int[] analog) {
    for (int i = 0; i < getAnalogChannelCount(); i++) {
      int channelIndex = getAnalogChannelIndexStart() + (i * 2);
      int value = (message[channelIndex] << 8) + message[channelIndex + 1];
      analog[getAnalogChannelStart() + i] = value;
    }
  }

  int getAnalogChannelCount() {
    return 0;
  };

  int getAnalogChannelIndexStart() {
    return 0;
  };

  int getAnalogChannelStart() {
    return 0;
  };

  public void injectDigitalChannels(boolean[] digital) {
    for (int channel = 0; channel < getDigitalChannelCount(); channel++) {
      int channelIndex = getDigitalChannelIndexStart() + (channel / 8);
      int bitPosition = channel % 8;
      int value = message[channelIndex];

      digital[getDigitalChannelStart() + channel] = (value & (1 << bitPosition)) > 0;
    }
  };

  int getDigitalChannelCount() {
    return 0;
  };

  int getDigitalChannelIndexStart() {
    return 0;
  };

  int getDigitalChannelStart() {
    return 0;
  };

  public void setDigitalChannel(int channel, boolean on) throws IllegalChannelException, IllegalChannelValueException {
    if (channel < getDigitalChannelStart()) {
      throw new IllegalChannelException(String.format("illegal channelnumber %d. Channel must be between %d and %d",
          channel, getDigitalChannelStart(), getDigitalChannelStart() + getDigitalChannelCount()));
    }
    if (channel >= getDigitalChannelStart() + getDigitalChannelCount()) {
      throw new IllegalChannelException(String.format("illegal channelnumber %d. Channel must be between %d and %d",
          channel, getDigitalChannelStart(), getDigitalChannelStart() + getDigitalChannelCount()));
    }

    int channelIndex = (channel - getDigitalChannelStart()) / 8 + getDigitalChannelIndexStart();
    int bitPosition = channel % 8;

    int value = message[channelIndex];
    if (on) {
      value = (byte) (value | (byte) (1 << bitPosition));
    } else {
      value = (byte) (value & ~(1 << bitPosition));
    }
    message[channelIndex] = (byte) (value & 0x00FF);

  };

  public void setAnalogChannel(int channel, int value) throws IllegalChannelException, IllegalChannelValueException {
    if (channel < getAnalogChannelStart()) {
      throw new IllegalChannelException(String.format("illegal channelnumber %d. Channel must be between %d and %d",
          channel, getAnalogChannelStart(), getAnalogChannelStart() + getAnalogChannelCount()));
    }
    if (channel >= getAnalogChannelStart() + getAnalogChannelCount()) {
      throw new IllegalChannelException(String.format("illegal channelnumber %d. Channel must be between %d and %d",
          channel, getAnalogChannelStart(), getAnalogChannelStart() + getAnalogChannelCount()));
    }

    if (value < 0) {
      throw new IllegalChannelValueException(
          String.format("illegal channel value %d. Value must be between 0 and 4096", value));
    }
    if (value >= 4096) {
      throw new IllegalChannelValueException(
          String.format("illegal channel value %d. Value must be between 0 and 4096", value));
    }

    int channelIndex = getAnalogChannelIndexStart() + ((channel - getAnalogChannelStart()) * 2);

    this.message[channelIndex] = (byte) (value >> 8);
    this.message[channelIndex + 1] = (byte) (value & 0x00FF);
  }

  public byte[] getDatagramm() {
    calculateCrc();
    return message;
  }

  private void calculateCrc() {
    char lowCrc = 0;
    char highCrc = 0;

    int messageLength = message.length;

    for (int i = 0; i < (messageLength / 2); i++) {
      highCrc = (char) (highCrc ^ message[i * 2]);
      lowCrc = (char) (lowCrc ^ message[(i * 2) + 1]);
    }
    message[messageLength - 2] = (byte) highCrc;
    message[messageLength - 1] = (byte) lowCrc;
  }

}
