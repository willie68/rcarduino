/**
 * MCS Media Computer Software
 * Copyright 2015 by Wilfried Klaas
 * Project: TestRCServer
 * File: RCArduinoReceiver.java
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

import de.mcs.rcarduino.rcmessages.RCMessage;
import de.mcs.rcarduino.rcmessages.RCMessageFactory;

/**
 * @author wklaa_000
 *
 */
public class RCArduinoReceiver {

  public static final int MAX_ANALOG_CHANNELS = 16;
  public static final int MAX_DIGITAL_CHANNELS = 1024;
  public static final int MAX_ANALOG_VALUE = 4096;
  public static final int NULL_ANALOG_VALUE = 2048;
  public static final int MIN_ANALOG_VALUE = 0;
  private static final int RCARDUINO_MESSAGE_LENGTH = 32;

  public static enum MESSAGE_STATE {
    ERROR_WRONG_MESSAGE_ID, OK, ERROR_CRC_CHECK, ERROR_WRONG_MESSAGE_LENGTH
  };

  private int[] analog;
  private boolean[] digital;
  private MESSAGE_STATE messageError;

  public RCArduinoReceiver() {
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
    int messageID = message[0] << 8 + message[1];
    if (messageID != 0xdf81) {
      setMessageError(MESSAGE_STATE.ERROR_WRONG_MESSAGE_ID);
      return false;
    }

    if (!checkCrc(message)) {
      setMessageError(MESSAGE_STATE.ERROR_CRC_CHECK);
      return false;
    }

    RCMessage rcMessage = RCMessageFactory.getRCMessage(message);

    rcMessage.injectAnalogChannels(this.analog);
    rcMessage.injectDigitalChannels(this.digital);

    return true;
  }

  private boolean checkCrc(byte[] message) {
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
    int crc = highCrc << 8 + lowCrc;
    int crcOrg = message[messageLength - 2] << 8 + message[messageLength - 1];

    return crc == crcOrg;
  }

  public MESSAGE_STATE getMessageError() {
    return messageError;
  }

  public void setMessageError(MESSAGE_STATE messageError) {
    this.messageError = messageError;
  }
}
