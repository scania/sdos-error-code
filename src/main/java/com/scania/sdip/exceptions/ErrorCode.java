package com.scania.sdip.exceptions;

import java.io.Serializable;
/**
 * An interface for retrieval of information assigned to the error codes used in SDIP.
 */
public interface ErrorCode extends Serializable {
  /**
   * Returns the HTTP error code assigned to the error code.
   * @return the HTTP error code.
   */
  int getHttpErrorCode();

  /**
   * Returns the unique SDIP error code.
   * @return the unique SDIP error code assigned to the error code.
   */
  int getSdipErrorCode();

  /**
   * Returns the log message.
   * @return the log message assigned to the error code.
   */
  String getLogMessage();

  /**
   * Returns the HTTP message.
   * @return the HTTP message assigned to the error code.
   */
  String getHttpMessage();

  /**
   * Returns the exit code.
   * @return the exit code assigned to the error code.
   */
  int getExitCode();

  /**
   * Set the HTTP message to the error code.
   */
  void setHttpMessage(String httpMessage);

  /**
   * Set the Log message to the error code.
   */
  void setLogMessage(String logMessage);
}
