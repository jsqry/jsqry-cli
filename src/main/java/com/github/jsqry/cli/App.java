package com.github.jsqry.cli;

import lombok.SneakyThrows;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class App {
  @SneakyThrows
  public static void main(String[] args) {
    String inputJsonStr = new String(System.in.readAllBytes(), StandardCharsets.UTF_8);
    String query = args[0];

    List<String> scripts = new ArrayList<>();

    String[] assets = {"jsqry.js", "app.js"};

    for (String asset : assets) {
      URL jsFileResource = Thread.currentThread().getContextClassLoader().getResource(asset);

      if (jsFileResource == null) {
        throw new IllegalStateException("no js file: " + asset);
      }

      scripts.add(new String(Files.readAllBytes(Paths.get(jsFileResource.toURI()))));
    }

    try (Context context = Context.create()) {
      for (String script : scripts) {
        context.eval("js", script);
      }

      Value doWork = context.eval("js", "doWork");

      doWork.executeVoid(inputJsonStr, query);
    }
  }
}
