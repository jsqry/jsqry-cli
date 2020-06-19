package com.github.jsqry.cli;

class Constants {
  public static final String UTILITY_NAME = "jsqry";
  public static final String UTILITY_HELP_LINE = "echo $JSON | " + UTILITY_NAME + " 'query'";

  private static final String VERSION = "%APP_VERSION%";
  private static final String VERSION_EXT = " (%GRAAL_VERSION%, %JAVA_VERSION%)";

  // via getter such that the compiler won't inline it
  public static String getVersion() {
    return VERSION;
  }

  // via getter such that the compiler won't inline it
  public static String getFullVersion() {
    return VERSION + VERSION_EXT;
  }
}
