package com.github.jsqry.cli;

import lombok.SneakyThrows;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
  @SneakyThrows
  public static void main(String[] args) {
    //    System.out.println("Hello Java!");

    URL jsFileResource =
        Thread.currentThread().getContextClassLoader().getResource("jsqry.js");

    if (jsFileResource == null) {
      throw new IllegalStateException("no js lib file");
    }

    String jsFileContent = new String(Files.readAllBytes(Paths.get(jsFileResource.toURI())));

    try (Context context = Context.create()) {
      //      context.eval("js", "print('Hello JavaScript!');");

      context.eval("js", jsFileContent);
      Value queryFunction = context.eval("js", "jsqry.query");
      System.out.println(queryFunction);
    }
  }
}
