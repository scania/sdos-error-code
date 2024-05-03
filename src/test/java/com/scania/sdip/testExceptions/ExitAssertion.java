package com.scania.sdip.testExceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Permission;

public class ExitAssertion {
  private static final ExitAssertionSecurityManager exitAssertionSecurityManager =
      new ExitAssertionSecurityManager();

  public static void assertExitCode(int expected, Runnable runnable) {
    SecurityManager oldSecurityManager = System.getSecurityManager();
    System.setSecurityManager(exitAssertionSecurityManager);
    try {
      runnable.run();
    } catch (SystemExitException exception) {
      assertEquals(expected, exception.getStatusCode());
    } finally {
      System.setSecurityManager(oldSecurityManager);
    }
  }

  private static class SystemExitException extends RuntimeException {
    private final int statusCode;

    public SystemExitException(int statusCode) {
      this.statusCode = statusCode;
    }

    public int getStatusCode() {
      return statusCode;
    }
  }

  private static class ExitAssertionSecurityManager extends SecurityManager {
    @Override
    public void checkExit(int status) {
      throw new SystemExitException(status);
    }

    @Override
    public void checkPermission(Permission perm) {
      // Allow everything, actually we wanna allow to setSecurityManager, but
      // why prohibit anything else, maybe we can decorate the default
    }
  }
}
