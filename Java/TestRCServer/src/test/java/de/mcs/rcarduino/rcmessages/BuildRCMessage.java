package de.mcs.rcarduino.rcmessages;

import static org.junit.Assert.*;

import org.junit.Test;

public class BuildRCMessage {

  @Test
  public void test() throws IllegalChannelException, IllegalChannelValueException {
    PrioRCMessage message = new PrioRCMessage();
    for (int i = 0; i < 4; i++) {
      message.setAnalogChannel(i, 1024);
    }
    boolean on = true;
    for (int i = 0; i < 64; i++) {
      on = !on;
      message.setDigitalChannel(i, on);
    }
    byte[] datagramm = message.getDatagramm();

    for (int i = 0; i < datagramm.length; i++) {
      System.out.print(String.format(", 0x%H", (datagramm[i] & 0x00ff)));
    }

  }

}
