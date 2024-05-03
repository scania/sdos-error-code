package com.scania.sdip.testExceptions;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

import com.scania.sdip.exceptions.ErrorBinding;
import com.scania.sdip.exceptions.ErrorCode;
import org.junit.jupiter.api.Test;

public class ErrorBindingTest {
  @Test
  public void unmatchedBracketIsNoTemplateArgShouldThrowException() {
    Exception exception = mock(Exception.class);
    assertThrows(IllegalArgumentException.class,
        () -> new ErrorBinding(ErrorBindingTestFormatter.WEIRD_STATUS9, exception));
  }

  @Test
  public void tooManyArgsShouldThrowException() {
    assertThrows(IllegalArgumentException.class,
        () -> new ErrorBinding(ErrorBindingTestFormatter.WEIRD_STATUS, "arg", "arg2", "arg3",
            "arg4", "arg5"));
  }

  @Test
  public void correctNumberOfArgsShouldNotThrowException() {
    Exception exception = mock(Exception.class);
    try {
      new ErrorBinding(ErrorBindingTestFormatter.WEIRD_STATUS, "arg", "arg2", "arg3", "arg4",
          exception);
    } catch (Exception e) {
      fail();
    }

  }

  @Test
  public void correctNumberOfArgsWithExceptionArgShouldNotThrowException() {
    Exception exception = mock(Exception.class);
    try {
      new ErrorBinding(ErrorBindingTestFormatter.WEIRD_STATUS7, "arg", "arg2", "arg3", "arg4",
          exception);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void tooFewArgsShouldThrowException() {
    assertThrows(IllegalArgumentException.class,
        () -> new ErrorBinding(ErrorBindingTestFormatter.WEIRD_STATUS, "arg1", "arg2"));
  }

  public enum ErrorBindingTestFormatter implements ErrorCode {
    WEIRD_STATUS("{0} {1} {2}", "{2} {3}"), //
    WEIRD_STATUS7("{3} {3}", ""), //
    WEIRD_STATUS9("{0", "}");

    private final String logMessage;
    private final String httpMessage;


    private ErrorBindingTestFormatter(String httpMessage, String logMessage) {
      this.httpMessage = httpMessage;
      this.logMessage = logMessage;
    }

    @Override
    public int getHttpErrorCode() {
      return -1;
    }

    @Override
    public int getSdipErrorCode() {
      return -1;
    }

    @Override
    public String getLogMessage() {
      return logMessage;
    }

    @Override
    public String getHttpMessage() {
      return httpMessage;
    }

    @Override
    public int getExitCode() {
      return -1;
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

  }
}
