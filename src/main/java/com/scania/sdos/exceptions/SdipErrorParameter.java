package com.scania.sdip.exceptions;

/**
 * The SdipErrorParameter enum is used to easily see and update dependent variables on
 * error code messages.
 *
 * @author ferdgd
 *
 */
public class SdipErrorParameter {
  public static final String ERRORCODE_PREFIX = "SDIP_";
  public static final String SUPPORTMAIL = "DL7167@scania.com";
  public static final String EMPTY_ENVIRONMENT_VAR = " LOCAL";
  public static final String UNABLE_TO_CONVERT_DOC_TO_UTF_8_ERROR_MESSAGE =
      "The supplied document for is not a valid UTF-8 standard.";

  public static final String TOKEN_INVALID =  "This is not valid token.";
  public static final String UNSUPPORTED_GRAPH_TYPE =  "This graph type not supported. ";

  private SdipErrorParameter() {}
}

