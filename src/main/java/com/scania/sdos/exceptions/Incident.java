package com.scania.sdip.exceptions;

import java.util.ArrayList;
import java.util.List;
/**
 * The Incident class holds information from an {@link IncidentException} to be returned to the user
 * as a HTTP response.
 */
public class Incident {
  private List<String> sdipErrorCodes = new ArrayList<>();
  private List<String> messages = new ArrayList<>();
  private long timestamp;
  private String request;

  /**
   * Adds an error code to the incident.
   * @param sdipErrorCode an error code
   */
  public void addSdipErrorCode(String sdipErrorCode) {
    sdipErrorCodes.add(sdipErrorCode);
  }

  /**
   * Returns a list of the error codes assigned to the incident.
   * @return a list of error codes.
   */
  public List<String> getSdipErrorCodes() {
    return sdipErrorCodes;
  }

  /**
   * Replace the list of error codes to the incident.
   * @param sdipErrorCodes a list of error codes
   */
  public void setSdipErrorCodes(List<String> sdipErrorCodes) {
    this.sdipErrorCodes = sdipErrorCodes;
  }

  /**
   * Returns a list of the messages for the error codes assigned to the incident.
   * @return a list of error code HTTP messages.
   */
  public List<String> getMessages() {
    return messages;
  }

  /**
   * Replace the list of messages to the incident.
   * @param messages a list of messages
   */
  public void setMessages(List<String> messages) {
    this.messages = messages;
  }

  /**
   * Adds a HTTP message.
   * @param message an HTTP message.
   */
  public void addMessage(String message) {
    messages.add(message);
  }

  /**
   * Returns a long timestamp
   * @return a timestamp of type long.
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Replace the timestamp to the incident.
   * @param timestamp a long timestamp
   */
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Returns the request that caused the incident.
   * @return the request that caused the incident.
   */
  public String getRequest() {
    return request;
  }

  /**
   * Assign the incident with the request that caused it.
   * @param request the request that caused the incident.
   */
  public void setRequest(String request) {
    this.request = request;
  }
}
