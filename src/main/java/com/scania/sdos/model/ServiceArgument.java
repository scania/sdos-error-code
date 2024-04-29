package com.scania.sdip.model;

import org.springframework.stereotype.Component;


/**
 * This Singleton hold the serviceId submitted at startup.
 */

@Component
public final class ServiceArgument {

  private static ServiceArgument instance;
  private String serviceId;

  private String envname;

  private ServiceArgument() {
  }

  /**
   * Returns the one and only instance of Service.
   * @return the Service class instance that holds information related to the service.
   */
  public static ServiceArgument getInstance() {
    if (instance == null) {
      instance = new ServiceArgument();
    }
    return instance;
  }

  /**
   * Returns the ServiceId parameter that been set from commandline for this service
   * @return the serviceId string
   */
  public String getServiceId() {
    return serviceId;
  }

  /**
   * Insert the ServiceId parameter that have been set in commandline for this service
   * @param serviceId the serviceId parameter from {@Link Argument }
   */
  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }


  public String getEnvname() {
    return envname;
  }

  public void setEnvname(String envname) {
    this.envname = envname;
  }
}
