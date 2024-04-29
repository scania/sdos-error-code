package com.scania.sdip.exceptions;

import java.text.Format;
import java.text.MessageFormat;
/**
 * The ErrorBinding class holds information about the bindings of error codes to arguments. An error
 * binding is created at instantiation of an {@link IncidentException}.
 */
public class ErrorBinding {
  private final ErrorCode errorCode;
  private final Object[] args;

  /**
   * Constructs a new error binding with an error code and arguments.
   * @param errorCode {@link SdipErrorCode} SDIP error code.
   * @param args Arguments required for the template strings in Http message and Log message.
   */
  public ErrorBinding(ErrorCode errorCode, Object... args) {
    validateArgs(errorCode, args);
    this.errorCode = errorCode;
    this.args = args;
  }

  /**
   * Returns the error code of the error binding.
   * @return an SDIP error code {@link SdipErrorCode}
   */
  public ErrorCode getErrorCode() {
    return errorCode;
  }

  /**
   * Returns the arguments of the error binding.
   * @return Arguments used in the template strings in Http message and Log message.
   */
  public Object[] getArgs() {
    return args;
  }

  private void validateArgs(ErrorCode errorCode, Object... args) {
    int numberOfException = 0;
    for (Object arg : args) {
      if (arg instanceof Exception) {
        ++numberOfException;
      }
    }
    if (!isRealArgsCounterInTemplateArgs(errorCode, (args.length - numberOfException))) {
      throw new IllegalArgumentException("Number of arguments required by template strings '"
          + errorCode.getHttpMessage() + "' and '" + errorCode.getLogMessage()
          + "' doesnt match the number of arguments given [" + toCommaSeperated(args) + "].");
    }
  }

  private boolean isRealArgsCounterInTemplateArgs(ErrorCode errorCode, int countedArgs) {
    int correctArg = 0;
    StringBuilder sbHttpMessage = new StringBuilder();
    StringBuilder sbLogMessage = new StringBuilder();
    sbHttpMessage.append(errorCode.getHttpMessage());
    sbLogMessage.append(errorCode.getLogMessage());
    Format[] httpNumber = new MessageFormat(sbHttpMessage.toString()).getFormatsByArgumentIndex();
    Format[] logNumber = new MessageFormat(sbLogMessage.toString()).getFormatsByArgumentIndex();

    if (httpNumber.length >= logNumber.length) {
      correctArg = httpNumber.length;
    } else {
      correctArg = logNumber.length;
    }

    return (correctArg == countedArgs);

  }

  private String toCommaSeperated(Object[] args) {
    StringBuilder sb = new StringBuilder("");
    for (Object object : args) {
      sb.append(object);
      sb.append(",");
    }
    if (sb.length() > 0) {
      sb.delete(sb.length() - 1, sb.length());
    }
    return sb.toString();
  }
}
