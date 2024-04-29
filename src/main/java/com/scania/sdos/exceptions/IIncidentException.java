package com.scania.sdip.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;
/**
 * An interface for performing actions on an SDIP incident exception.
 */
public interface IIncidentException {
  /**
   * Returns a {@link ResponseEntity} object with {@link Incident} information and the worst error
   * status code.
   * @return a HTTP response with incident information and the worst error status code
   */
  ResponseEntity<Incident> createHttpErrorResponse();

  /**
   * Prints error codes and log messages to the log
   */
  void printToLog();

  /**
   * Specify what request caused the incident exception.
   * @param request the {@link ServletWebRequest} that caused the incident exception.
   */
  void setRequestUsed(ServletWebRequest request);

  /**
   * Throws this incident exception.
   */
  void throwIfAnyIncidents();

  /**
   * Add another incident in the form of an error binding
   * @param errorBinding a binding of error codes to arguments.
   */
  void addIncident(ErrorBinding errorBinding);

  /**
   * Exits the current program with a status code.
   */
  void runExit();
}
