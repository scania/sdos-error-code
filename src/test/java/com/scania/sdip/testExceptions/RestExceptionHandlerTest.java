package com.scania.sdip.testExceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.scania.sdip.exceptions.Incident;
import com.scania.sdip.exceptions.IncidentException;
import com.scania.sdip.exceptions.RestExceptionHandler;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

public class RestExceptionHandlerTest {
  @Test
  public void ensureWeGetBackTheResponseEntityWeHaveInIncidentExceptionWhenIncidentExceptionIsThrown() {
    ServletWebRequest request = mock(ServletWebRequest.class);
    IncidentException incidentException = mock(IncidentException.class);
    @SuppressWarnings("unchecked")
    ResponseEntity<Incident> expectedResponse = mock(ResponseEntity.class);

    doReturn(expectedResponse).when(incidentException).createHttpErrorResponse();
    doReturn(HttpStatus.INTERNAL_SERVER_ERROR).when(expectedResponse).getStatusCode();

    assertStatusCode(request, incidentException, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  public void getTempErrorCode500WhenHttpMessageCouldNotBeReadForUnkownReason() {
    ServletWebRequest request = mock(ServletWebRequest.class);
    HttpMessageNotReadableException httpMessageNotReadableException =
        mock(HttpMessageNotReadableException.class);

    doReturn("blah").when(httpMessageNotReadableException).getMessage();

    mockIterator(request);

    assertStatusCode(request, httpMessageNotReadableException, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  public void ensure400AndInvalidJsonErrorCodeWhenHttpMessageNotReadableWithJsonParseExceptionAsCause() {

    ServletWebRequest request = mock(ServletWebRequest.class);
    JsonParseException jsonParseException = mock(JsonParseException.class);
    HttpMessageNotReadableException httpMessageNotReadableException =
        mock(HttpMessageNotReadableException.class);

    JsonLocation location = mock(JsonLocation.class);

    doReturn(1).when(location).getLineNr();
    doReturn(1).when(location).getColumnNr();
    doReturn(location).when(jsonParseException).getLocation();
    doReturn(jsonParseException).when(httpMessageNotReadableException).getCause();

    mockIterator(request);

    assertStatusCode(request, httpMessageNotReadableException, HttpStatus.BAD_REQUEST);
  }


  @Test
  public void ensure400WhenBodyIsMissing() {

    ServletWebRequest request = mock(ServletWebRequest.class);
    HttpMessageNotReadableException httpMessageNotReadableException =
        mock(HttpMessageNotReadableException.class);

    doReturn("required request body is missing").when(httpMessageNotReadableException).getMessage();

    mockIterator(request);

    assertStatusCode(request, httpMessageNotReadableException, HttpStatus.BAD_REQUEST);
  }


  @Test
  public void ensure404WhenNohandlerFoundExceptionIsCaught() {
    ServletWebRequest request = mock(ServletWebRequest.class);
    mockIterator(request);
    NoHandlerFoundException noHandlerFoundException = mock(NoHandlerFoundException.class);
    HttpHeaders httpHeaders = mock(HttpHeaders.class);

    doReturn(httpHeaders).when(noHandlerFoundException).getHeaders();
    doReturn(mock(InetSocketAddress.class)).when(httpHeaders).getHost();
    doReturn("").when(noHandlerFoundException).getRequestURL();

    assertStatusCode(request, noHandlerFoundException, HttpStatus.NOT_FOUND);
  }

  @Test
  public void ensure415WhenMediaNotSupportedIsCaught() {
    ServletWebRequest request = mock(ServletWebRequest.class);

    HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException =
        mock(HttpMediaTypeNotSupportedException.class);

    doReturn(Arrays.asList(new MediaType[] {MediaType.APPLICATION_JSON_UTF8}))
        .when(httpMediaTypeNotSupportedException).getSupportedMediaTypes();

    mockIterator(request);

    assertStatusCode(request, httpMediaTypeNotSupportedException,
        HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @Test
  public void ensure406WhenMediaTypeNotAcceptableIsCaught() {
    ServletWebRequest request = mock(ServletWebRequest.class);

    HttpMediaTypeNotAcceptableException httpMediaTypeNotAcceptableException =
        mock(HttpMediaTypeNotAcceptableException.class);

    mockIterator(request);

    assertStatusCode(request, httpMediaTypeNotAcceptableException, HttpStatus.NOT_ACCEPTABLE);
  }

  @Test
  public void ensure400WhenMethodArgumentNotValidException() {
    ServletWebRequest request = mock(ServletWebRequest.class);

    MethodArgumentNotValidException methodArgumentNotValidException =
        mock(MethodArgumentNotValidException.class);

    BindingResult bindingResult = mock(BindingResult.class);

    doReturn(bindingResult).when(methodArgumentNotValidException).getBindingResult();
    doReturn(Arrays.asList(new FieldError[] {mock(FieldError.class)})).when(bindingResult)
        .getFieldErrors();

    mockIterator(request);

    assertStatusCode(request, methodArgumentNotValidException, HttpStatus.BAD_REQUEST);

  }

  @Test
  public void ensure400WhenMethodArgumentNotValidException1() {
    ServletWebRequest request = mock(ServletWebRequest.class);

    ConstraintViolationException constraintViolationException =
        mock(ConstraintViolationException.class);

    ConstraintViolation constraintViolation = mock(ConstraintViolation.class);
    Set<ConstraintViolation> set = new HashSet<>();
    set.add(constraintViolation);

    doReturn(set).when(constraintViolationException).getConstraintViolations();

    doReturn("ERRORTEST").when(constraintViolation).getInvalidValue();
    doReturn("a error message ").when(constraintViolation).getMessage();
    mockIterator(request);

    assertStatusCode(request, constraintViolationException, HttpStatus.BAD_REQUEST);

  }

  @Test
  public void ensure500WhenRandomExceptionIsCaught() {
    ServletWebRequest request = mock(ServletWebRequest.class);

    Exception exception = mock(Exception.class);

    mockIterator(request);

    assertStatusCode(request, exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }


  private void mockIterator(ServletWebRequest request) {
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    @SuppressWarnings("unchecked")
    Iterator<String> iterator = mock(Iterator.class);
    doReturn(httpServletRequest).when(request).getRequest();
    doReturn(iterator).when(request).getHeaderNames();
    doReturn(false).when(iterator).hasNext();
  }

  private void assertStatusCode(ServletWebRequest request, Exception exception,
      HttpStatus desiredStatusCode) {
    RestExceptionHandler restExceptionHandler = new RestExceptionHandler();
    ResponseEntity<Incident> response =
        restExceptionHandler.handleAllExceptions(exception, request);
    assertEquals(desiredStatusCode, response.getStatusCode());
  }


}
