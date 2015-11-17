/**
 * MCS Media Computer Software
 * Copyright 2015 by Wilfried Klaas
 * Project: TestRCServer
 * File: WorkerRunnable.java
 * EMail: W.Klaas@gmx.de
 * Created: 11.11.2015 wklaa_000
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wklaa_000
 *
 */
public class WorkerRunnable implements Runnable {

  protected Socket clientSocket = null;
  protected String serverText = null;

  public WorkerRunnable(Socket clientSocket, String serverText) {
    this.clientSocket = clientSocket;
    this.serverText = serverText;
  }

  public void run() {
    try {
      byte[] buffer = new byte[32];
      List<Integer> inputBuffer = new ArrayList<Integer>(32);
      InputStream input = clientSocket.getInputStream();
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      while (clientSocket.isConnected()) {
        if (input.available() > 0) {
          int value = input.read();
          inputBuffer.add(value);
          if (inputBuffer.size() >= 32) {

            if (inputBuffer.get(0) == 0xdf) {
              if (inputBuffer.get(1) == 0x81) {
                for (int i = 0; i < buffer.length; i++) {
                  buffer[i] = (byte) (inputBuffer.get(0) & 0x00FF);
                  inputBuffer.remove(0);
                }
                outputMessage(buffer);
              } else {
                inputBuffer.remove(0);
                inputBuffer.remove(0);
              }
            } else {
              inputBuffer.remove(0);
            }
          }
        }
      }
      input.close();
      outputMessage(buffer);
    } catch (IOException e) {
      // report exception somewhere.
      e.printStackTrace();
    }
  }

  private void outputMessage(byte[] bytes) {
    StringBuilder b = new StringBuilder();
    long time = System.currentTimeMillis();
    byte lowCrc = 0;
    byte highCrc = 0;
    for (int i = 0; i < (bytes.length - 2); i++) {
      if ((i % 2) == 0) {
        lowCrc = (byte) (lowCrc ^ bytes[i]);
      } else {
        highCrc = (byte) (highCrc ^ bytes[i]);

      }
      b.append(String.format("%02x ", bytes[i]));
    }
    byte lowCrcOrg = bytes[bytes.length - 2];
    byte highCrcOrg = bytes[bytes.length - 1];
    b.append(String.format("%02x(%02x) ", lowCrcOrg, lowCrc));
    b.append(String.format("%02x(%02x) ", highCrcOrg, highCrc));

    if (lowCrcOrg != lowCrc || highCrcOrg != highCrc) {
      b.append(" CRC Error");
    }
    b.append("\r\n");
    b.append(String.format("Request processed: %d", time));
    System.out.println(b.toString());
  }
}
