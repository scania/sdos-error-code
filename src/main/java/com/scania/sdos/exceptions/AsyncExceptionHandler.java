package com.scania.sdip.exceptions;


import java.lang.reflect.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

@Component
  public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private final Logger logger = LogManager.getLogger(AsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {

      if(ex instanceof IncidentException){
        ((IncidentException)ex).printToLog();
      }else {
        logger.error("Error occured for unknown reason - "+ ex.getMessage());
      }
    }
}


