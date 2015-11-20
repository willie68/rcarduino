/**
 * MCS Media Computer Software
 * Copyright 2015 by Wilfried Klaas
 * Project: TestRCServer
 * File: RCArduinoTranceiver.java
 * EMail: W.Klaas@gmx.de
 * Created: 13.11.2015 wklaa_000
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package de.mcs.rcarduino;

import de.mcs.rcarduino.rcmessages.IllegalChannelException;
import de.mcs.rcarduino.rcmessages.RCMessage;
import de.mcs.rcarduino.rcmessages.RCMessageFactory;

/**
 * @author wklaa_000
 *
 */
public class RCArduinoTranceiver {

  public static final int MAX_ANALOG_CHANNELS = 16;
  public static final int MAX_DIGITAL_CHANNELS = 1024;
  public static final int MAX_ANALOG_VALUE = 4096;
  public static final int NULL_ANALOG_VALUE = 2048;
  public static final int MIN_ANALOG_VALUE = 0;
  private static final int RCARDUINO_MESSAGE_LENGTH = 32;

  public static enum MESSAGE_STATE {
    ERROR_WRONG_MESSAGE_ID, OK, ERROR_CRC_CHECK, ERROR_WRONG_MESSAGE_LENGTH, ERROR_UNKNOWN_MESSAGE_TYPE
  };

  private int[] analog;
  private boolean[] digital;
  private MESSAGE_STATE messageError;

  public RCArduinoTranceiver() {
    analog = new int[MAX_ANALOG_CHANNELS];
    digital = new boolean[MAX_DIGITAL_CHANNELS];
    initChannels();
    setMessageError(MESSAGE_STATE.OK);
  }

  private void initChannels() {
    for (int i = 0; i < analog.length; i++) {
      analog[i] = NULL_ANALOG_VALUE;
    }
    for (int i = 0; i < digital.length; i++) {
      digital[i] = false;
    }
  }

  public boolean parseMessage(byte[] message) {
    if (message.length != RCARDUINO_MESSAGE_LENGTH) {
      setMessageError(MESSAGE_STATE.ERROR_WRONG_MESSAGE_LENGTH);
      return false;
    }
    int messageID = ((message[0] & 0xff) << 8) + (message[1] & 0xff);
    if (messageID != 0xdf81) {
      setMessageError(MESSAGE_STATE.ERROR_WRONG_MESSAGE_ID);
      return false;
    }

    if (!checkCrc(message)) {
      setMessageError(MESSAGE_STATE.ERROR_CRC_CHECK);
      return false;
    }

    RCMessage rcMessage = RCMessageFactory.getRCMessage(message);
    if (rcMessage == null) {
      setMessageError(MESSAGE_STATE.ERROR_UNKNOWN_MESSAGE_TYPE);
      return false;
    }

    rcMessage.injectAnalogChannels(this.analog);
    rcMessage.injectDigitalChannels(this.digital);

    return true;
  }

  private boolean checkCrc(byte[] message) {
    byte lowCrc = 0;
    byte highCrc = 0;

    int messageLength = message.length;

    for (int i = 0; i < (messageLength / 2) - 1; i++) {
      highCrc = (byte) (highCrc ^ message[i * 2]);
      lowCrc = (byte) (lowCrc ^ message[(i * 2) + 1]);
    }

    int crc = ((highCrc & 0xff) << 8) + (lowCrc & 0xff);
    int crcOrg = ((message[messageLength - 2] & 0xff) << 8) + (message[messageLength - 1] & 0xff);

    return crc == crcOrg;
  }

  public MESSAGE_STATE getMessageError() {
    return messageError;
  }

  public void setMessageError(MESSAGE_STATE messageError) {
    if (messageError == null) {
      this.messageError = MESSAGE_STATE.OK;
    } else {
      this.messageError = messageError;
    }
  }

  public int getAnalogChannel(int channel) throws IllegalChannelException {
    if ((channel < 0) || (channel >= MAX_ANALOG_CHANNELS)) {
      throw new IllegalChannelException(String.format("illegal channelnumber %d. Channel must be between %d and %d",
          channel, 0, MAX_ANALOG_CHANNELS));
    }
    return analog[channel];
  }

  public boolean getDigitalChannel(int channel) throws IllegalChannelException {
    if ((channel < 0) || (channel >= MAX_DIGITAL_CHANNELS)) {
      throw new IllegalChannelException(String.format("illegal channelnumber %d. Channel must be between %d and %d",
          channel, 0, MAX_DIGITAL_CHANNELS));
    }
    return digital[channel];
  }

}
