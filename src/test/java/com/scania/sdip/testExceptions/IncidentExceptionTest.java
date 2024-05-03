package com.scania.sdip.testExceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.scania.sdip.exceptions.ErrorBinding;
import com.scania.sdip.exceptions.ErrorCode;
import com.scania.sdip.exceptions.Incident;
import com.scania.sdip.exceptions.IncidentException;
import com.scania.sdip.exceptions.SdipErrorCode;
import com.scania.sdip.exceptions.SdipErrorParameter;
import java.text.Format;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

public class IncidentExceptionTest {

  private static final Logger LOGGER = LogManager.getLogger(IncidentException.class);

  private final ErrorCode WEIRD_STATUS_CODE = new ErrorCode() {
    private static final long serialVersionUID = 1L;

    @Override
    public int getSdipErrorCode() {
      return 6001;
    }

    @Override
    public String getLogMessage() {
      return "I'm weird";
    }

    @Override
    public String getHttpMessage() {
      return "I'm weird";
    }

    @Override
    public int getHttpErrorCode() {
      return 600;
    }

    @Override
    public int getExitCode() {
      return 6001;
    }

    /**
     * Set the HTTP message to the error code.
     *
     * @param httpMessage
     */
    @Override
    public void setHttpMessage(String httpMessage) {

    }

    /**
     * Set the Log message to the error code.
     *
     * @param logMessage
     */
    @Override
    public void setLogMessage(String logMessage) {

    }
  };


  @Test
  public void ensureIncidentExceptionConstructor() {
    Logger log4jlogger = mock(Logger.class);
    ErrorBinding errorBinding = mock(ErrorBinding.class);

    @SuppressWarnings("unchecked")
    List<ErrorBinding> list = mock(List.class);

    new IncidentException(log4jlogger);

    new IncidentException(SdipErrorCode.APPLICATION_UNCONFIGURED, log4jlogger);

    org.slf4j.Logger sl4jLogger = mock(org.slf4j.Logger.class);

    new IncidentException(sl4jLogger);

    new IncidentException(SdipErrorCode.APPLICATION_UNCONFIGURED, sl4jLogger);
  }

  @Test
  public void printErrorMessageOnHowIncidentExceptionIsUsedIfNoErrorBindingsIsSet() {
    org.apache.logging.log4j.Logger log4jlogger = mock(org.apache.logging.log4j.Logger.class);
    IncidentException incidentException = new IncidentException(log4jlogger);
    incidentException.printToLog();
    verify(log4jlogger).error(
        "No error bindings set within the IncidentException. You should not see this message. Ensure that you didn't forgot to add an errorBinding or that you didn't.");

  }

  @Test
  public void checkPrintLogErrorMessageReturnsActionDetails() {
    org.apache.logging.log4j.Logger log4jlogger = mock(org.apache.logging.log4j.Logger.class);
    IncidentException incidentException = new IncidentException("testLabel, testSubjectIri",
        SdipErrorCode.UNABLE_TO_COMMUNICATE_WITH_DATABASE, log4jlogger, "TestMessage",
        SdipErrorParameter.SUPPORTMAIL);
    incidentException.printToLog();
    verify(log4jlogger).error(
        ArgumentMatchers.contains("(Error occured in Action - testLabel, testSubjectIri)"));
  }

  @Test
  public void checkPrintLogErrorMessageReturnsPopulateErrorDetails() {
    org.apache.logging.log4j.Logger log4jlogger = mock(org.apache.logging.log4j.Logger.class);
    Exception exception = new Exception("This is to test populate error message");
    IncidentException incidentException = new IncidentException(exception,"label SubjectIri_test "
        + "(class-HttpActionModel, method-populate, line-160), ",
        SdipErrorCode.UNABLE_TO_COMMUNICATE_WITH_DATABASE, log4jlogger, "TestMessage",
        SdipErrorParameter.SUPPORTMAIL);
    incidentException.printToLog();
    verify(log4jlogger).error(
        ArgumentMatchers.contains("Error occured while populating label SubjectIri_test "
            + "(class-HttpActionModel, method-populate, line-160), "));
  }

  @Test
  public void checkIncidentExceptionforErrorTrace() {
    org.apache.logging.log4j.Logger log4jlogger = mock(org.apache.logging.log4j.Logger.class);
    Exception exception = new Exception();
    StackTraceElement element = new StackTraceElement(
        "com.scania.sdip.sdos.orchestration.model.TaskModel", "populate", "TaskModel.java",
        139);
    exception.setStackTrace(new StackTraceElement[]{element});
    IncidentException incidentException = new IncidentException(exception,
        SdipErrorCode.UNKNOWN_PARSING_ERROR, log4jlogger,
        "testing exception");
    incidentException.printToLog();
    verify(log4jlogger).error(
        ArgumentMatchers.contains(
            "com.scania.sdip.sdos.orchestration.model.TaskModel.populate(TaskModel.java:139)"));
  }

  @Test
  public void test() {
    org.slf4j.Logger sl4jLogger = mock(org.slf4j.Logger.class);
    IncidentException incidentException_ = new IncidentException(sl4jLogger);
    incidentException_.printToLog();
    verify(sl4jLogger).error(
        "No error bindings set within the IncidentException. You should not see this message. Ensure that you didn't forgot to add an errorBinding or that you didn't.");

  }

  @Test
  public void printAllErrorBindingsToLogOneByOneWithCorrectNumberOfArguments() {
    Logger logger = mock(Logger.class);

    for (SdipErrorCode errorCode : SdipErrorCode.values()) {
      StringBuilder sb = new StringBuilder();
      sb.append(errorCode.getHttpMessage());
      sb.append(errorCode.getLogMessage());
      Format[] mf = new MessageFormat(sb.toString()).getFormatsByArgumentIndex();

      int max = mf.length;

      List<String> args = new ArrayList<>();

      for (int i = 0; i < max; ++i) {
        args.add(Integer.toString(i));
      }

      IncidentException incidentException =
          new IncidentException(errorCode, logger, args.toArray());
      incidentException.printToLog();
    }
  }

  @Test
  public void printAllErrorBindingsWithTooFewArguments() {
    Logger logger = mock(Logger.class);

    for (SdipErrorCode errorCode : SdipErrorCode.values()) {
      int left = new MessageFormat(errorCode.getHttpMessage()).getFormats().length;
      int right = new MessageFormat(errorCode.getLogMessage()).getFormats().length;
      int min;
      if (left == right) {
        min = left - 1;
      } else {
        min = Math.min(left, right);
      }

      List<String> args = new ArrayList<>();

      if (min <= 0) {
        continue;
      }

      for (int i = 0; i < min; ++i) {
        args.add(i + "");
      }

      assertThrows(IllegalArgumentException.class,
          () -> new IncidentException(errorCode, logger, args.toArray()));
    }
  }

  @Test
  public void ensureSupportMailIsAddedAsArg() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);
    incidentException
        .addIncident(new ErrorBinding(SdipErrorCode.UNABLE_TO_COMMUNICATE_WITH_DATABASE
            , "error", SdipErrorParameter.SUPPORTMAIL));

    ResponseEntity<Incident> errorResponse = incidentException.createHttpErrorResponse();
    incidentException.printToLog();
    assertTrue(
        errorResponse.getBody().getMessages().toString().contains(SdipErrorParameter.SUPPORTMAIL));
  }


  @Test
  public void two400CodesShouldBecome400() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);
    incidentException.addIncident(new ErrorBinding(SdipErrorCode.INVALID_SHACL_DATA, "lol"));
    incidentException.addIncident(new ErrorBinding(SdipErrorCode.INVALID_CONTEXT_DATA, "lol"));

    ResponseEntity<Incident> errorResponse = incidentException.createHttpErrorResponse();
    incidentException.printToLog();
    assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
    assertEquals(2, errorResponse.getBody().getMessages().size());
  }

  @Test
  public void twoDifferent4xxCodesShouldBecome400() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);

    incidentException.addIncident(new ErrorBinding(SdipErrorCode.INVALID_ID_DATA));
    incidentException.addIncident(new ErrorBinding(SdipErrorCode.PAGE_NOT_FOUND, "lol", "nope"));

    ResponseEntity<Incident> errorResponse = incidentException.createHttpErrorResponse();
    assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
    assertEquals(2, errorResponse.getBody().getMessages().size());
  }

  @Test
  public void twoEqual4xxCodesShouldBecomeThat4xxCode() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);

    incidentException.addIncident(new ErrorBinding(SdipErrorCode.NO_DOMAINCONFIGURATIONS_EXIST));
    incidentException.addIncident(new ErrorBinding(SdipErrorCode.NO_DOMAINCONFIGURATIONS_EXIST));

    ResponseEntity<Incident> errorResponse = incidentException.createHttpErrorResponse();
    assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());
    assertEquals(2, errorResponse.getBody().getMessages().size());
  }

  @Test
  public void return500IfBothA5xxCodeAnd4xxCodeIsAdded() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);

    incidentException
        .addIncident(new ErrorBinding(SdipErrorCode.UNABLE_TO_COMMUNICATE_WITH_DATABASE, "lol.com",
            SdipErrorParameter.SUPPORTMAIL));
    incidentException.addIncident(new ErrorBinding(SdipErrorCode.APPLICATION_UNCONFIGURED));

    ResponseEntity<Incident> errorResponse = incidentException.createHttpErrorResponse();
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode());
    assertEquals(2, errorResponse.getBody().getMessages().size());
  }

  @Test
  public void return503IfTwo503ErrorCodesAreAdded() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);

    incidentException
        .addIncident(new ErrorBinding(SdipErrorCode.UNABLE_TO_COMMUNICATE_WITH_DATABASE, "lol.com",
            SdipErrorParameter.SUPPORTMAIL));
    incidentException
        .addIncident(new ErrorBinding(SdipErrorCode.UNABLE_TO_COMMUNICATE_WITH_DATABASE, "lol.com",
            SdipErrorParameter.SUPPORTMAIL));

    ResponseEntity<Incident> errorResponse = incidentException.createHttpErrorResponse();
    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, errorResponse.getStatusCode());
    assertEquals(2, errorResponse.getBody().getMessages().size());
  }

  @Test
  public void return500IfTwoDifferent5xxErrorCodesAreAdded() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);

    incidentException
        .addIncident(new ErrorBinding(SdipErrorCode.UNABLE_TO_COMMUNICATE_WITH_DATABASE, "lol.com",
            SdipErrorParameter.SUPPORTMAIL));
    incidentException.addIncident(
        new ErrorBinding(SdipErrorCode.UNKNOWN_REASON_ERROR, "Test",
            SdipErrorParameter.SUPPORTMAIL));

    ResponseEntity<Incident> errorResponse = incidentException.createHttpErrorResponse();
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode());
    assertEquals(2, errorResponse.getBody().getMessages().size());
  }

  @Test
  public void return500IfTwoDifferentFirstA503AndThenA600StatusCodeIsAdded() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);

    incidentException
        .addIncident(new ErrorBinding(SdipErrorCode.UNABLE_TO_COMMUNICATE_WITH_DATABASE, "lol.com",
            SdipErrorParameter.SUPPORTMAIL));
    incidentException.addIncident(new ErrorBinding(WEIRD_STATUS_CODE));

    ResponseEntity<Incident> errorResponse = incidentException.createHttpErrorResponse();
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode());
    assertEquals(2, errorResponse.getBody().getMessages().size());
  }

  @Test
  public void return500IfTwoDifferentFirstA404AndThenA600StatusCodeIsAdded() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);

    incidentException.addIncident(new ErrorBinding(SdipErrorCode.PAGE_NOT_FOUND, "lol.com/dsadsa",
        "lol.com/swagger-ui.html"));
    incidentException.addIncident(new ErrorBinding(WEIRD_STATUS_CODE));

    ResponseEntity<Incident> errorResponse = incidentException.createHttpErrorResponse();
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode());
    assertEquals(2, errorResponse.getBody().getMessages().size());
  }

  @Test
  public void checkExceptionreturnsActionDetails() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException("testLabel, testSubjectIri",
        SdipErrorCode.UNABLE_TO_COMMUNICATE_WITH_DATABASE, logger, "TestMessage",
        SdipErrorParameter.SUPPORTMAIL);

    ResponseEntity<Incident> errorResponse = incidentException.createHttpErrorResponse();
    assertTrue(errorResponse.getBody().getMessages().get(0)
        .startsWith("(Error occured in Action - testLabel, testSubjectIri)"));
  }

  @Test
  public void addExceptionToError() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);

    incidentException.addIncident(
        new ErrorBinding(SdipErrorCode.UNKNOWN_REASON_ERROR, logger, new Exception(),
            SdipErrorParameter.SUPPORTMAIL));
    incidentException.printToLog();

    verify(logger).error(ArgumentMatchers.contains("java.lang.Exception"));
  }

  @Test
  public void ensureThrowIfAnyIncidentThrowsExceptionIfAnyErrorBindingsBeenAdded() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);

    incidentException.addIncident(
        new ErrorBinding(SdipErrorCode.UNKNOWN_REASON_ERROR, logger, new Exception(),
            SdipErrorParameter.SUPPORTMAIL));

    assertThrows(IncidentException.class, () -> incidentException.throwIfAnyIncidents());
  }

  @Test
  public void ensureNoExceptionThrownIfNoErrorBindingsAreAdded() { // NOSONAR
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);

    incidentException.throwIfAnyIncidents(); // shouldnt throw any exception
  }

  @Test
  public void ensureWeCanWriteToSlf4jLogger() {
    org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
    IncidentException incidentException = new IncidentException(logger);

    incidentException.addIncident(new ErrorBinding(SdipErrorCode.UNKNOWN_REASON_ERROR,
        SdipErrorParameter.SUPPORTMAIL, "test"));

    incidentException.printToLog();

    verify(logger).error(ArgumentMatchers.contains("SDIP_5001"));
  }

  @Test
  public void ensureWeCanWriteToSlf4jLoggerWhenAddingIncidentDirectlyInConstructor() {
    org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
    IncidentException incidentException =
        new IncidentException(SdipErrorCode.APPLICATION_UNCONFIGURED, logger, new Exception());

    incidentException.printToLog();

    verify(logger).error(ArgumentMatchers.contains("SDIP_40050"));
  }


  @Test
  public void ensureExitsWithFallbackCode1000WhenNoIncidentsAreAdded() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);
    ExitAssertion.assertExitCode(1000, () -> incidentException.runExit());
  }

  @Test
  public void ensureExitsWithFallbackCode1000WhenTwoIncidentsAreAdded() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);
    incidentException.addIncident(new ErrorBinding(SdipErrorCode.INVALID_CONTEXT_DATA, "asd"));
    incidentException.addIncident(new ErrorBinding(SdipErrorCode.INVALID_ONTOLOGY_DATA, "asd"));
    ExitAssertion.assertExitCode(1000, () -> incidentException.runExit());
  }

  @Test
  public void addRequestShouldAddRequestInformationToOutput() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);
    ServletWebRequest request = mock(ServletWebRequest.class);
    HttpServletRequest httpRequest = mock(HttpServletRequest.class);
    Iterator<String> headerNames = mock(Iterator.class);

    when(headerNames.hasNext()).thenReturn(true).thenReturn(false);
    when(headerNames.next()).thenReturn("Content-Type");

    doReturn(httpRequest).when(request).getRequest();

    doReturn(headerNames).when(request).getHeaderNames();

    doReturn("application/json").when(request).getHeader(anyString());

    doReturn("GET").when(httpRequest).getMethod();

    doReturn("lol.com").when(httpRequest).getRequestURI();

    incidentException.setRequestUsed(request);

    ResponseEntity<Incident> httpResponse = incidentException.createHttpErrorResponse();
    incidentException.printToLog();

    verify(logger).error(ArgumentMatchers.contains("Content-Type: application/json"));
    verify(logger).error(ArgumentMatchers.contains("GET lol.com"));
  }

  @Test
  public void ensureExceptionArgumentCanBePlacedWhereverWithinErrorBindingWithoutShowingUpInHttResponseEntity() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);
    incidentException
        .addIncident(new ErrorBinding(SdipErrorCode.UNKNOWN_REASON_ERROR, "Test", "lol"));

    ResponseEntity<Incident> httpErrorResponse = incidentException.createHttpErrorResponse();

    Incident incident = httpErrorResponse.getBody();

    assertEquals(
        "The service was unable to fulfill the request for an unknown reason Test, contact support on mail lol if the issue persists.",
        incident.getMessages().get(0));
  }

  @Test
  public void ensureErrorMessageReplacesTabsAndNewLineWithNothing() {
    Logger logger = mock(Logger.class);
    IncidentException incidentException = new IncidentException(logger);
    incidentException.addIncident(
        new ErrorBinding(SdipErrorCode.DOCUMENT_NOT_JSON, "{\n\rjsonthingy}", "ignore"));
    ResponseEntity<Incident> httpErrorResponse = incidentException.createHttpErrorResponse();
    Incident incident = httpErrorResponse.getBody();
    assertEquals("The provided document is not JSON(https://www.json.org/), {jsonthingy}",
        incident.getMessages().get(0));
  }
}
