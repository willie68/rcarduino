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
public class Mix2RCMessage extends AbstractRCMessage implements RCMessage {

  public static final int MESSAGEID = 0x0022;
  private static final int ANALOG_START_CHANNEL = 4;
  private static final int ANALOG_START = 4;
  private static final int ANALOG_CHANNELS = 4;
  private static final int DIGITAL_CHANNELS = 128;
  private static final int DIGITAL_START_INDEX = 12;
  private static final int DIGITAL_START_CHANNEL = 128;

  public Mix2RCMessage() {
    super(MESSAGEID);
  }

  public Mix2RCMessage(byte[] message) {
    super(message);
  }

  @Override
  int getAnalogChannelCount() {
    return ANALOG_CHANNELS;
  }

  @Override
  int getAnalogChannelIndexStart() {
    return ANALOG_START;
  }

  @Override
  int getAnalogChannelStart() {
    return ANALOG_START_CHANNEL;
  }

  @Override
  int getDigitalChannelCount() {
    return DIGITAL_CHANNELS;
  };

  @Override
  int getDigitalChannelIndexStart() {
    return DIGITAL_START_INDEX;
  };

  @Override
  int getDigitalChannelStart() {
    return DIGITAL_START_CHANNEL;
  }
}
