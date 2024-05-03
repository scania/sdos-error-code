package com.scania.sdip.testModel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.scania.sdip.model.ServiceArgument;
import org.junit.jupiter.api.Test;
public class ServiceArgumentTest {

  @Test public void testServiceArgumentSetterGetter() {
    //Arrange
    ServiceArgument argument = ServiceArgument.getInstance();
    argument.setEnvname("env");
    argument.setServiceId("id");
    //Act&Assert
    assertEquals("id", ServiceArgument.getInstance().getServiceId());
    assertEquals("env", ServiceArgument.getInstance().getEnvname());

  }

}
