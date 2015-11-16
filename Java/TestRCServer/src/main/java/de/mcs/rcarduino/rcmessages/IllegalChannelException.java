/**
 * 
 */
package de.mcs.rcarduino.rcmessages;

/**
 * @author w.klaas
 *
 */
public class IllegalChannelException extends Exception {

  /**
   * 
   */
  public IllegalChannelException() {
  }

  /**
   * @param message
   */
  public IllegalChannelException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public IllegalChannelException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public IllegalChannelException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public IllegalChannelException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
