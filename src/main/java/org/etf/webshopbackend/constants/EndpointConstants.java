package org.etf.webshopbackend.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class EndpointConstants {

  public static final String SINGLE_PATH = "/*";
  public static final String ALL_PATHS = "/**";
  public static final String USERS = "/users";
  public static final String LOGIN = "/login";
  public static final String REGISTER = "/register";
  public static final String PIN = "/pins";
  public static final String WHOAMI = "/user/me";
}
