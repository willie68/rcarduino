/**
 * MCS Media Computer Software
 * Copyright 2015 by Wilfried Klaas
 * Project: TestRCServer
 * File: RCMessageFactory.java
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
public class RCMessageFactory {

  public static RCMessage getRCMessage(byte[] message) {
    RCMessage rcMessage = null;
    int messageType = (((int) message[3] & 0xff) << 8) + (int) message[3] & 0xff;

    if (messageType == PrioRCMessage.MESSAGEID) {
      rcMessage = new PrioRCMessage(message);
    } else if (messageType == Analog1RCMessage.MESSAGEID) {
      rcMessage = new Analog1RCMessage(message);
    } else if (messageType == Analog2RCMessage.MESSAGEID) {
      rcMessage = new Analog2RCMessage(message);
    } else if (messageType == Digital1RCMessage.MESSAGEID) {
      rcMessage = new Digital1RCMessage(message);
    } else if (messageType == Digital2RCMessage.MESSAGEID) {
      rcMessage = new Digital2RCMessage(message);
    } else if (messageType == Mix1RCMessage.MESSAGEID) {
      rcMessage = new Mix1RCMessage(message);
    } else if (messageType == Mix2RCMessage.MESSAGEID) {
      rcMessage = new Mix2RCMessage(message);
    }
    return rcMessage;
  }

}
