package com.scania.sdip.exceptions;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
/**
 * This class is responsible for handling all exceptions globally.
 */
@RestControllerAdvice
public class RestExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

  /**
   * Handles the exception by printing it to the log and returning its HTTP error response.
   * @param exception the exception to be handled.
   * @param request the request that caused the exception.
   * @return an HTTP error response.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Incident> handleAllExceptions(Exception exception, WebRequest request) {
    ServletWebRequest servletWebRequest;

    if (!(request instanceof ServletWebRequest)) {
      servletWebRequest = null;
    } else {
      servletWebRequest = (ServletWebRequest) request;
    }
    ResponseEntity<Incident> responseEntity = getException(exception, servletWebRequest) ;
    return responseEntity;
  }

  private ResponseEntity<Incident> getException(Exception exception, ServletWebRequest servletWebRequest){
    ResponseEntity<Incident> responseEntityException = null ;
    if (exception instanceof IncidentException) {
      responseEntityException = handleIncidentException((IncidentException) exception, servletWebRequest);
    } else if (exception instanceof HttpMessageNotReadableException) {
      if (exception.getCause() instanceof JsonParseException) {
        responseEntityException = handleIncidentException(
                createJsonException((JsonParseException) exception.getCause()), servletWebRequest);
      } else if (exception.getCause() instanceof JsonMappingException) {
        responseEntityException = handleIncidentException(
                createJsonMappingException((Exception) exception.getCause()), servletWebRequest);
      } else if (exception.getMessage().toLowerCase()
              .startsWith("required request body is missing")) {
        responseEntityException = handleIncidentException(
                createBodyEmptyException(),
                servletWebRequest);
      } else if (exception.getMessage().toLowerCase()
              .startsWith("json parse error:")) {
        responseEntityException = handleIncidentException(
                createHttpNotReadableException((Exception) exception.getCause()), servletWebRequest);
      } else {
        responseEntityException = handleIncidentException(createFallbackIncidentExceptionFromOtherException(exception),
                servletWebRequest);
      }
    } else if (exception instanceof NoHandlerFoundException) {
      responseEntityException = handleIncidentException(createNotFoundException((NoHandlerFoundException) exception),
              servletWebRequest);
    } else if (exception instanceof HttpMediaTypeNotSupportedException) {
      responseEntityException = handleIncidentException(
              createMediaTypeNotSupportedException((HttpMediaTypeNotSupportedException) exception),
              servletWebRequest);
    } else if (exception instanceof HttpMediaTypeNotAcceptableException) {
      responseEntityException = handleIncidentException(
              createNotAcceptableException((HttpMediaTypeNotAcceptableException) exception),
              servletWebRequest);
    } else if (exception instanceof MethodArgumentNotValidException) {
      responseEntityException = handleIncidentException(
              createNotValidException((MethodArgumentNotValidException) exception), servletWebRequest);
    } else if (exception instanceof ConstraintViolationException) {
      responseEntityException = handleIncidentException(createNotValidException1((ConstraintViolationException) exception), servletWebRequest);
    } else {
      responseEntityException = handleIncidentException(createFallbackIncidentExceptionFromOtherException(exception),
              servletWebRequest);
    }

    return responseEntityException;
  }
  private IncidentException createBodyEmptyException() {
    return new IncidentException(SdipErrorCode.NO_BODY, logger);
  }

  private IncidentException createNotValidException(MethodArgumentNotValidException exception) {
    BindingResult bindingResult = exception.getBindingResult();

    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    StringBuilder httpMessageArgument = new StringBuilder("'");
    StringBuilder logMessageArgument = new StringBuilder("'");

    for (FieldError fieldError : fieldErrors) {
      httpMessageArgument.append(fieldError.getField());
      httpMessageArgument.append("', '");

      logMessageArgument
              .append(fieldError.getField() + "' had value '" + fieldError.getRejectedValue() + "', '");
    }

    httpMessageArgument.delete(httpMessageArgument.length() - 4, httpMessageArgument.length());
    logMessageArgument.delete(logMessageArgument.length() - 4, logMessageArgument.length());

    return new IncidentException(SdipErrorCode.INVALID_FIELD, logger,
            httpMessageArgument.toString(), logMessageArgument.toString());
  }

  private IncidentException createNotValidException1(ConstraintViolationException exception) {
    Set<ConstraintViolation<?>> result = exception.getConstraintViolations();

    StringBuilder httpMessageArgument = new StringBuilder("'");
    StringBuilder logMessageArgument = new StringBuilder("'");

    for (ConstraintViolation violation : result) {
      httpMessageArgument.append(violation.getMessage());
      httpMessageArgument.append("', '");

      logMessageArgument
              .append(violation.getMessage() + "' had value '" + violation.getInvalidValue() + "', '");
    }

    httpMessageArgument.delete(httpMessageArgument.length() - 4, httpMessageArgument.length());
    logMessageArgument.delete(logMessageArgument.length() - 4, logMessageArgument.length());

    return new IncidentException(SdipErrorCode.INVALID_FIELD, logger,
            httpMessageArgument.toString(), logMessageArgument.toString());
  }

  private IncidentException createMediaTypeNotSupportedException(
          HttpMediaTypeNotSupportedException exception) {
    return new IncidentException(SdipErrorCode.UNSUPPORTED_MEDIA_TYPE, logger,
            buildSupportedMediaTypesString(exception.getSupportedMediaTypes()));
  }

  private StringBuilder buildSupportedMediaTypesString(List<MediaType> mediaTypes) {
    StringBuilder supportedMediaTypes = new StringBuilder("[");

    for (MediaType mediaType : mediaTypes) {
      supportedMediaTypes.append("\"" + mediaType + "\"");
      supportedMediaTypes.append(", ");
    }

    if (supportedMediaTypes.length() > 1) {
      supportedMediaTypes.delete(supportedMediaTypes.length() - 2, supportedMediaTypes.length());
    }

    supportedMediaTypes.append("]");
    return supportedMediaTypes;
  }

  private IncidentException createNotAcceptableException(
          HttpMediaTypeNotAcceptableException exception) {
    return new IncidentException(SdipErrorCode.NOT_ACCEPTABLE, logger,
            buildSupportedMediaTypesString(exception.getSupportedMediaTypes()));
  }

  private IncidentException createNotFoundException(NoHandlerFoundException exception) {
    return new IncidentException(SdipErrorCode.PAGE_NOT_FOUND, logger,
            exception.getHeaders().getHost() + exception.getRequestURL(),
            exception.getHeaders().getHost() + "/sdcs/swagger-ui.html");
  }

  private IncidentException createHttpNotReadableException(Exception cause) {
    return new IncidentException(SdipErrorCode.DOCUMENT_NOT_JSON2, logger, cause.getMessage());
  }

  private IncidentException createJsonException(JsonParseException cause) {
    return new IncidentException(SdipErrorCode.DOCUMENT_NOT_JSON, logger, cause.getMessage(),
            cause.getRequestPayloadAsString());
  }

  private IncidentException createJsonMappingException(Exception cause) {
    return new IncidentException(SdipErrorCode.JSON_MAPPING_EXCEPTION, logger, cause.getMessage());
  }

  private IncidentException createFallbackIncidentExceptionFromOtherException(Exception exception) {
    return new IncidentException(SdipErrorCode.UNKNOWN_REASON_ERROR, logger,exception.getMessage(),
            SdipErrorParameter.SUPPORTMAIL);
  }

  private ResponseEntity<Incident> handleIncidentException(IncidentException e,
                                                           ServletWebRequest request) {
    e.setRequestUsed(request);
    e.printToLog();
    return e.createHttpErrorResponse();
  }
}
