package de.mcs.rcarduino;
/**
 * MCS Media Computer Software
 * Copyright 2015 by Wilfried Klaas
 * Project: TestRCServer
 * File: MainRCServer.java
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

/**
 * @author wklaa_000
 *
 */
public class MainRCServer {

  /**
   * @param args
   */
  public static void main(String[] args) {
    MultiThreadedServer server = new MultiThreadedServer(3456);
    Thread thread = new Thread(server);
    thread.start();

    try {
      Thread.sleep(600 * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Stopping Server");
    server.stop();
    while (thread.isAlive()) {
      Thread.yield();
    }
    System.exit(0);
  }

}
