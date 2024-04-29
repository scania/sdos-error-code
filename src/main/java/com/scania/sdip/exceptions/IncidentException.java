package com.scania.sdip.exceptions;

import com.scania.sdip.model.ServiceArgument;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * The IncidentException class represents a runtime exception in SDIP. It holds information about
 * the {@link ServletWebRequest} that caused the exception, the relevant SDIP error code and the
 * arguments supplied for said error code as an {@link ErrorBinding}. Provided methods for logging
 * and throwing incidents are used by the {@link RestExceptionHandler}.
 */

public class IncidentException extends RuntimeException implements IIncidentException {

  private static final long serialVersionUID = 1L;
  private final List<ErrorBinding> errorBindings = new ArrayList<>();
  private final Logger sl4jLogger;
  private final org.apache.logging.log4j.Logger log4jlogger;
  private ServletWebRequest request;
  private String actionDetails;
  private Exception exception;
  private String executionId;
  private String populateErrorMsg;

  public String getPopulateErrorMsg() { return populateErrorMsg; }

  public void setPopulateErrorMsg(String populateErrorMsg) {
    this.populateErrorMsg = populateErrorMsg; }

  public String getExecutionId() { return executionId; }

  public void setExecutionId(String executionId) { this.executionId = executionId; }

  public List<ErrorBinding> getErrorBindings() {
    return errorBindings;
  }

  public String getActionDetails() {
    return actionDetails;
  }

  /**
   * Constructs a new incident exception with the specified SLF4J logger.
   *
   * @param sl4jLogger a SLF4J logger.
   */
  public IncidentException(Logger sl4jLogger) {
    this.sl4jLogger = sl4jLogger;
    log4jlogger = null;
  }

  /**
   * Constructs a new incident exception with the specified log4j logger.
   *
   * @param log4jlogger a log4j logger.
   */
  public IncidentException(org.apache.logging.log4j.Logger log4jlogger) {
    this.log4jlogger = log4jlogger;
    sl4jLogger = null;
  }

  /**
   * Constructs a new incident exception with the specified SDIP error code, SLF4J logger and
   * arguments for the SDIP error code.
   *
   * @param errorCode {@link SdipErrorCode} SDIP error code
   * @param logger    a SLF4J logger.
   * @param args      Arguments required for the template strings in Http message and Log message.
   */
  public IncidentException(ErrorCode errorCode, Logger logger, Object... args) {
    sl4jLogger = logger;
    log4jlogger = null;
    errorBindings.add(new ErrorBinding(errorCode, args));
  }

  /**
   * Constructs a new incident exception with the specified SDIP error code, log4j logger and
   * arguments for the SDIP error code.
   *
   * @param errorCode {@link SdipErrorCode} SDIP error code
   * @param logger    a log4j logger.
   * @param args      Arguments required for the template strings in Http message and Log message.
   */
  public IncidentException(ErrorCode errorCode, org.apache.logging.log4j.Logger logger,
      Object... args) {
    log4jlogger = logger;
    sl4jLogger = null;
    errorBindings.add(new ErrorBinding(errorCode, args));
  }

  /**
   * Constructs a new incident exception with the specified SDIP error code, log4j logger and
   * arguments for the SDIP error code.
   *
   * @param action    required to add action id with error message
   * @param errorCode {@link SdipErrorCode} SDIP error code
   * @param logger    a log4j logger.
   * @param args      Arguments required for the template strings in Http message and Log message.
   */
  public IncidentException(String action, ErrorCode errorCode,
      org.apache.logging.log4j.Logger logger,
      Object... args) {
    actionDetails = action;
    log4jlogger = logger;
    sl4jLogger = null;
    errorBindings.add(new ErrorBinding(errorCode, args));
  }

  /**
   * Constructs a new incident exception with the specified Exception, SDIP error code, log4j logger
   * and arguments for the SDIP error code.
   *
   * @param ex        required to print the stack trace
   * @param errorCode {@link SdipErrorCode} SDIP error code
   * @param logger    a log4j logger.
   * @param args      Arguments required for the template strings in Http message and Log message.
   */
  public IncidentException(Exception ex, ErrorCode errorCode,
      org.apache.logging.log4j.Logger logger,
      Object... args) {
    super(ex.getMessage());
    this.exception = ex;
    log4jlogger = logger;
    sl4jLogger = null;
    errorBindings.add(new ErrorBinding(errorCode, args));
  }

  /**
   * Constructs a new incident exception with the specified Exception, SDIP error code, log4j logger
   * and arguments for the SDIP error code.
   *
   * @param ex        required to print the stack trace
   * @param populateErrorMessage required to add populate error messagealong with exception message
   * @param errorCode {@link SdipErrorCode} SDIP error code
   * @param logger    a log4j logger.
   * @param args      Arguments required for the template strings in Http message and Log message.
   */
  public IncidentException(Exception ex,String populateErrorMessage, ErrorCode errorCode,
      org.apache.logging.log4j.Logger logger,
      Object... args) {
    super(ex.getMessage());
    this.exception = ex;
    this.populateErrorMsg = populateErrorMessage;
    log4jlogger = logger;
    sl4jLogger = null;
    errorBindings.add(new ErrorBinding(errorCode, args));
  }

  @Override
  public void addIncident(ErrorBinding errorBinding) {
    errorBindings.add(errorBinding);
  }

  private Object[] filterExceptions(Object[] args) {
    return Arrays.stream(args).filter(object -> !(object instanceof Throwable))
        .map(object -> String.valueOf(object).replaceAll("\r|\n", "")).toArray();
  }

  @Override
  public ResponseEntity<Incident> createHttpErrorResponse() {
    Incident errorResponse = new Incident();
    int worstStatusCode = 0;
    for (ErrorBinding errorBinding : errorBindings) {
      int statusCode = errorBinding.getErrorCode().getHttpErrorCode();
      errorResponse.addSdipErrorCode(
          SdipErrorParameter.ERRORCODE_PREFIX + errorBinding.getErrorCode().getSdipErrorCode());
      errorResponse.addMessage(MessageFormat.format(errorBinding.getErrorCode().getHttpMessage(),
          filterExceptions(errorBinding.getArgs())));
      worstStatusCode = pickWorstStatusCode(worstStatusCode, statusCode);
    }
    errorResponse.setTimestamp(System.currentTimeMillis());
    if (request != null) {
      errorResponse.setRequest(buildRequestString(request));
    } else {
      errorResponse.setRequest("REQUEST DATA NOT AVAILABLE");
    }

    if (this.actionDetails != null && !this.actionDetails.isEmpty()) {
      String error = errorResponse.getMessages().get(0);
      error = "(Error occured in Action - " + this.actionDetails + ") " + error;
      List message = new ArrayList();
      message.add(error);
      errorResponse.setMessages(message);
      this.actionDetails = null;
    }
    // if this if statement is true we have mixed up status codes and error codes, we then put 500
    // just so the program can continue to run, WARN?
    if (!is4xx(worstStatusCode) && !is5xx(worstStatusCode)) {
      worstStatusCode = 500;
    }
    return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(worstStatusCode));
  }

  private int pickWorstStatusCode(int worstStatusCode, int statusCode) {
    if (is4xx(worstStatusCode)) {
      if (is4xx(statusCode)) {
        if (statusCode != worstStatusCode) {
          worstStatusCode = 400;
        }
      } else {
        worstStatusCode = 500;
      }
    } else if (is5xx(worstStatusCode)) {
      if (is5xx(statusCode)) {
        if (statusCode != worstStatusCode) {
          worstStatusCode = 500;
        }
      } else {
        worstStatusCode = 500;
      }
    } else {
      worstStatusCode = statusCode;
    }
    return worstStatusCode;
  }

  @Override
  public void printToLog() {
    if (errorBindings.size() == 0) {
      printEntryToLog(
          "No error bindings set within the IncidentException. You should not see this message. Ensure that you didn't forgot to add an errorBinding or that you didn't.");
    }
    List<String> errorCodes = new ArrayList<>();
    List<String> messages = new ArrayList<>();
    List<Throwable> exceptions = new ArrayList<>();

    iterateErrorBindings(errorBindings, exceptions, messages, errorCodes);

    StringBuilder logEntry = new StringBuilder();
    logEntry.append(String.join(", ", errorCodes) + "\n\n");
    for (String message : messages) {
      logEntry.append(message + "\n");
    }
    logEntry.append("\n");

    appendHttpRequestInformation(logEntry);
    appendExceptions(exceptions, logEntry);
    printEntryToLog(logEntry.toString());
    if (exception != null) {
      StringWriter errors = new StringWriter();
      exception.printStackTrace(new PrintWriter(errors));
      printEntryToLog(errors.toString());
    }
  }

  private void iterateErrorBindings(List<ErrorBinding> errorBindings, List<Throwable> exceptions,
                                    List<String> messages, List<String> errorCodes){

    for (ErrorBinding errorBinding : errorBindings) {
      List<Object> arguments = new ArrayList<>();
      errorBindingsArgs(arguments ,errorBinding, exceptions);

      ServiceArgument serviceArgument = ServiceArgument.getInstance();
      StringBuilder sb = new StringBuilder();
      sb.append(serviceArgument.getEnvname());
      sb.append("_");
      sb.append(serviceArgument.getServiceId());
      sb.append("_");
      String errorMessage = errorBinding.getErrorCode().getLogMessage();

      if(this.populateErrorMsg!=null && !this.populateErrorMsg.isEmpty()){
        errorMessage =
                "Error occured while populating " + this.populateErrorMsg + errorBinding.getErrorCode()
                        .getLogMessage();
      }

      if (this.actionDetails != null && !this.actionDetails.isEmpty()) {
        errorMessage =
                "(Error occured in Action - " + this.actionDetails + ") " + errorBinding.getErrorCode()
                        .getLogMessage();
      }
      errorCodes.add(sb + SdipErrorParameter.ERRORCODE_PREFIX + errorBinding.getErrorCode()
              .getSdipErrorCode());
      messages.add(
              sb + SdipErrorParameter.ERRORCODE_PREFIX + errorBinding.getErrorCode().getSdipErrorCode()
                      + ": "
                      + MessageFormat.format(errorMessage, arguments.toArray()));
      if(executionId!=null && !executionId.equals("")){
        log4jlogger.error(executionId +"-"+ SdipErrorParameter.ERRORCODE_PREFIX +
                errorBinding.getErrorCode().getSdipErrorCode() +"-"+
                MessageFormat.format(errorMessage, arguments.toArray()));
      }
    }
  }

  private void  errorBindingsArgs(List<Object> arguments , ErrorBinding errorBinding,
                                  List<Throwable> exceptions){
    for (Object arg : errorBinding.getArgs()) {
      if (arg instanceof Throwable) {
        exceptions.add((Throwable) arg);
      } else {
        arguments.add(arg);
      }
    }
  }
  private void appendHttpRequestInformation(StringBuilder logEntry) {
    if (request == null) {
      return;
    }
    StringBuilder headers = new StringBuilder();
    Iterator<String> headerNames = request.getHeaderNames();
    while (headerNames.hasNext()) {
      String headerName = headerNames.next();
      headers.append("        " + headerName + ": " + request.getHeader(headerName) + "\n");
    }
    String httpRequestMessage;
    httpRequestMessage = "    " + buildRequestString(request) + "\n" + headers;
    logEntry.append(httpRequestMessage);
  }

  private void appendExceptions(Collection<Throwable> throwables, StringBuilder logEntry) {
    for (Throwable throwable : throwables) {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      PrintStream s = new PrintStream(out);
      throwable.printStackTrace(s);
      logEntry.append("    " + out.toString());
    }
  }


  public void setActionDetails(String actionDetails){
    this.actionDetails = actionDetails;
  }

  @Override
  public void setRequestUsed(ServletWebRequest request) {
    this.request = request;
  }

  private boolean is4xx(int statusCode) {
    return (statusCode >= 400) && (statusCode < 500);
  }

  private boolean is5xx(int statusCode) {
    return (statusCode >= 500) && (statusCode < 600);
  }

  private void printEntryToLog(String entry) {
    if (sl4jLogger != null) {
      sl4jLogger.error(entry);
    } else {
      log4jlogger.error(entry);
    }
  }

  private String buildRequestString(ServletWebRequest request) {
    return request.getRequest().getMethod() + " " + request.getRequest().getRequestURI()
        + Optional.ofNullable(request.getRequest().getQueryString()).orElse("");
  }

  @Override
  public void throwIfAnyIncidents() {
    if (!errorBindings.isEmpty()) {
      throw this;
    }
  }

  @Override
  public void runExit() {
    if (errorBindings.size() == 1) {
      System.exit(errorBindings.get(0).getErrorCode().getExitCode());
    } else {
      System.exit(1000);
    }
  }

}
