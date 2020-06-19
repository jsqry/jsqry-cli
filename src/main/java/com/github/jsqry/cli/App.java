package com.github.jsqry.cli;

import lombok.SneakyThrows;
import org.apache.commons.cli.*;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

  @SneakyThrows
  public static void main(String[] args) {
    Options options = new Options();

    Option version = new Option("v", "version", false, "print version and exit");
    version.setRequired(false);
    options.addOption(version);

    Option help = new Option("h", "help", false, "print help and exit");
    help.setRequired(false);
    options.addOption(help);

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd;

    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      throw printHelpAndExit(e.getMessage(), options);
    }

    if (cmd.hasOption(help.getLongOpt())) {
      System.out.println(Constants.UTILITY_NAME + " ver. " + Constants.getFullVersion());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(Constants.UTILITY_HELP_LINE, options);
      System.exit(0);
    }

    if (cmd.hasOption(version.getLongOpt())) {
      System.out.println(Constants.getVersion());
      System.exit(0);
    }

    List<String> argList = cmd.getArgList();

    String query = argList.isEmpty() ? "" : argList.get(0);

    List<String> scripts = new ArrayList<>();

    String[] assets = {"jsqry.js", "app.js"};

    for (String asset : assets) {
      URL jsFileResource = Thread.currentThread().getContextClassLoader().getResource(asset);

      if (jsFileResource == null) {
        throw new IllegalStateException("no js file: " + asset);
      }

      //      For some reason with native-image this fails:
      // java.nio.file.FileSystemNotFoundException: Provider "resource" not installed
      //      scripts.add(new String(Files.readAllBytes(Paths.get(jsFileResource.toURI()))));

      scripts.add(new Scanner(jsFileResource.openStream()).useDelimiter("\\A").next());
    }

    try (Context context = Context.create()) {
      for (String script : scripts) {
        context.eval("js", script);
      }

      Value doWork = context.eval("js", "doWork");

      String inputJsonStr = new String(System.in.readAllBytes(), StandardCharsets.UTF_8);
      Value result = doWork.execute(inputJsonStr, query);
      if (!result.asBoolean()) {
        System.exit(1);
      }
    }
  }

  private static IllegalStateException printHelpAndExit(String message, Options options) {
    if (message != null) {
      System.err.println(message);
    }
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(Constants.UTILITY_HELP_LINE, options);
    System.exit(1);
    return new IllegalStateException();
  }
}
