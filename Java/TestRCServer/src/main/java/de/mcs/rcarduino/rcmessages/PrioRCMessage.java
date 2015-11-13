/**
 * MCS Media Computer Software
 * Copyright 2015 by Wilfried Klaas
 * Project: TestRCServer
 * File: PrioRCMessage.java
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
public class PrioRCMessage extends AbstractRCMessage implements RCMessage {

  public static final int MESSAGEID = 0x0011;
  private static final int ANALOG_START = 4;
  private static final int ANALOG_CHANNELS = 4;
  private static final int DIGITAL_CHANNELS = 64;
  private static final int DIGITAL_START = 12;

  public PrioRCMessage(byte[] message) {
    super(message);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.mcs.rcarduino.RCMessage#injectAnalogChannels(int[])
   */
  public void injectAnalogChannels(int[] analog) {
    for (int i = 0; i < ANALOG_CHANNELS; i++) {
      int channelIndex = ANALOG_START + (i * 2);
      int value = (message[channelIndex] << 8) + message[channelIndex + 1];
      analog[i] = value;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.mcs.rcarduino.RCMessage#injectDigitalChannels(boolean[])
   */
  public void injectDigitalChannels(boolean[] digital) {
    for (int i = 0; i < DIGITAL_CHANNELS; i++) {
      int channelIndex = DIGITAL_START + (i / 8);
      int bitPosition = i % 8;
      int value = message[channelIndex];

      digital[i] = (value & (1 << bitPosition)) > 0;
    }
  }

}
