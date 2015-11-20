/**
 * MCS Media Computer Software
 * Copyright 2015 by Wilfried Klaas
 * Project: TestRCServer
 * File: TestRCArduinoTranceiver.java
 * EMail: W.Klaas@gmx.de
 * Created: 20.11.2015 wklaa_000
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.mcs.rcarduino.RCArduinoTranceiver;
import de.mcs.rcarduino.RCArduinoTranceiver.MESSAGE_STATE;

/**
 * @author wklaa_000
 *
 */
public class TestRCArduinoTranceiver {

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for
   * {@link de.mcs.rcarduino.RCArduinoTranceiver#RCArduinoTranceiver()}.
   */
  @Test
  public void testRCArduinoTranceiver() {
    RCArduinoTranceiver tranceiver = new RCArduinoTranceiver();

    assertEquals(MESSAGE_STATE.OK, tranceiver.getMessageError());
  }

  @Test
  public void testParseMessage() throws IllegalChannelException, IllegalChannelValueException {
    PrioRCMessage message = new PrioRCMessage();
    message.setAnalogChannel(1, 2048);
    message.setAnalogChannel(2, 1024);

    message.setDigitalChannel(12, true);
    message.setDigitalChannel(8, true);

    RCArduinoTranceiver tranceiver = new RCArduinoTranceiver();

    assertTrue(tranceiver.parseMessage(message.getDatagramm()));

    assertEquals(MESSAGE_STATE.OK, tranceiver.getMessageError());

    assertEquals(2048, tranceiver.getAnalogChannel(1));
    assertEquals(1024, tranceiver.getAnalogChannel(2));

    assertTrue(tranceiver.getDigitalChannel(12));
    assertTrue(tranceiver.getDigitalChannel(8));

  }

  @Test(expected = IllegalChannelException.class)
  public void testAnalogChannelUpperBounds() throws IllegalChannelException, IllegalChannelValueException {
    RCArduinoTranceiver tranceiver = new RCArduinoTranceiver();
    try {
      tranceiver.getAnalogChannel(15);
    } catch (IllegalChannelException e) {
      fail("wrong exception caught. Channel 15");
    }
    tranceiver.getAnalogChannel(16);
  }

  @Test(expected = IllegalChannelException.class)
  public void testAnalogChannelLowerBounds() throws IllegalChannelException, IllegalChannelValueException {
    RCArduinoTranceiver tranceiver = new RCArduinoTranceiver();
    try {
      tranceiver.getAnalogChannel(0);
    } catch (IllegalChannelException e) {
      fail("wrong exception caught. Channel 0");
    }
    tranceiver.getAnalogChannel(-1);
  }

  @Test(expected = IllegalChannelException.class)
  public void testDigitalChannelUpperBounds() throws IllegalChannelException, IllegalChannelValueException {
    RCArduinoTranceiver tranceiver = new RCArduinoTranceiver();
    try {
      tranceiver.getDigitalChannel(1023);
    } catch (IllegalChannelException e) {
      fail("wrong exception caught. Channel 1023");
    }
    tranceiver.getDigitalChannel(1024);
  }

  @Test(expected = IllegalChannelException.class)
  public void testDigitalChannelLowerBounds() throws IllegalChannelException, IllegalChannelValueException {
    RCArduinoTranceiver tranceiver = new RCArduinoTranceiver();
    try {
      tranceiver.getDigitalChannel(0);
    } catch (IllegalChannelException e) {
      fail("wrong exception caught. Channel 0");
    }
    tranceiver.getDigitalChannel(-1);
  }

  @Test
  public void testWrongMessageType() throws IllegalChannelException, IllegalChannelValueException {
    PrioRCMessage message = new PrioRCMessage();
    byte[] datagramm = message.getDatagramm();
    datagramm[3] = 0x7e;

    injectCRC(datagramm);

    RCArduinoTranceiver tranceiver = new RCArduinoTranceiver();

    assertFalse(tranceiver.parseMessage(datagramm));
    assertEquals(MESSAGE_STATE.ERROR_UNKNOWN_MESSAGE_TYPE, tranceiver.getMessageError());
  }

  private void injectCRC(byte[] message) {
    byte lowCrc = 0;
    byte highCrc = 0;

    int messageLength = message.length;

    for (int i = 0; i < (messageLength / 2) - 1; i++) {
      highCrc = (byte) (highCrc ^ message[i * 2]);
      lowCrc = (byte) (lowCrc ^ message[(i * 2) + 1]);
    }
    message[messageLength - 2] = (byte) highCrc;
    message[messageLength - 1] = (byte) lowCrc;
  }

  @Test
  public void testWrongMessageLength() throws IllegalChannelException, IllegalChannelValueException {
    PrioRCMessage message = new PrioRCMessage();
    byte[] datagramm = message.getDatagramm();
    datagramm[0] = 0x7e;

    byte[] wrongDatagram = new byte[30];
    for (int i = 0; i < wrongDatagram.length; i++) {
      wrongDatagram[i] = datagramm[i];
    }
    RCArduinoTranceiver tranceiver = new RCArduinoTranceiver();

    assertFalse(tranceiver.parseMessage(wrongDatagram));
    assertEquals(MESSAGE_STATE.ERROR_WRONG_MESSAGE_LENGTH, tranceiver.getMessageError());
  }

  @Test
  public void testWrongMessageID() throws IllegalChannelException, IllegalChannelValueException {
    PrioRCMessage message = new PrioRCMessage();
    byte[] datagramm = message.getDatagramm();
    datagramm[0] = 0x7e;

    RCArduinoTranceiver tranceiver = new RCArduinoTranceiver();

    assertFalse(tranceiver.parseMessage(datagramm));
    assertEquals(MESSAGE_STATE.ERROR_WRONG_MESSAGE_ID, tranceiver.getMessageError());
  }

  @Test
  public void testWrongMessageCRC() throws IllegalChannelException, IllegalChannelValueException {
    PrioRCMessage message = new PrioRCMessage();
    byte[] datagramm = message.getDatagramm();
    datagramm[31] = 0x7e;

    RCArduinoTranceiver tranceiver = new RCArduinoTranceiver();

    assertFalse(tranceiver.parseMessage(datagramm));
    assertEquals(MESSAGE_STATE.ERROR_CRC_CHECK, tranceiver.getMessageError());
  }

  /**
   * Test method for
   * {@link de.mcs.rcarduino.RCArduinoTranceiver#getMessageError()}.
   */
  @Test
  public void testGetMessageError() {
    RCArduinoTranceiver tranceiver = new RCArduinoTranceiver();
    tranceiver.setMessageError(MESSAGE_STATE.ERROR_CRC_CHECK);

    assertEquals(MESSAGE_STATE.ERROR_CRC_CHECK, tranceiver.getMessageError());

    tranceiver.setMessageError(null);

    assertEquals(MESSAGE_STATE.OK, tranceiver.getMessageError());
  }

}
