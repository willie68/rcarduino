/**
 * MCS Media Computer Software
 * Copyright 2015 by Wilfried Klaas
 * Project: TestRCServer
 * File: Digital1RCMessage.java
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
package de.mcs.rcarduino.rcmessages;

/**
 * @author wklaa_000
 *
 */
public class Digital1RCMessage extends AbstractRCMessage implements RCMessage {

  public static final int MESSAGEID = 0x0041;
  private static final int DIGITAL_CHANNELS = 192;
  private static final int DIGITAL_START_INDEX = 4;
  private static final int DIGITAL_START_CHANNEL = 0;

  public Digital1RCMessage(byte[] message) {
    super(message);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.mcs.rcarduino.RCMessage#injectAnalogChannels(int[])
   */
  public void injectAnalogChannels(int[] analog) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.mcs.rcarduino.RCMessage#injectDigitalChannels(boolean[])
   */
  public void injectDigitalChannels(boolean[] digital) {
    for (int i = 0; i < DIGITAL_CHANNELS; i++) {
      int channelIndex = DIGITAL_START_INDEX + (i / 8);
      int bitPosition = i % 8;
      int value = message[channelIndex];

      digital[DIGITAL_START_CHANNEL + i] = (value & (1 << bitPosition)) > 0;
    }
  }

}
