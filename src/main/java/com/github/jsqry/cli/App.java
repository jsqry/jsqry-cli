package com.github.jsqry.cli;

import lombok.SneakyThrows;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
  @SneakyThrows
  public static void main(String[] args) {
    //    System.out.println("Hello Java!");

    URL jsFileResource = Thread.currentThread().getContextClassLoader().getResource("jsqry.js");

    if (jsFileResource == null) {
      throw new IllegalStateException("no js lib file");
    }

    String jsFileContent = new String(Files.readAllBytes(Paths.get(jsFileResource.toURI())));

    String inputJsonStr = new String(System.in.readAllBytes(), StandardCharsets.UTF_8);

    try (Context context = Context.create()) {
      //      context.eval("js", "print('Hello JavaScript!');");

      context.eval("js", jsFileContent);

      //      Value queryFunction = context.eval("js", "jsqry.query");
      //      System.out.println(queryFunction);

      Value logicFunction =
          context.eval(
              "js",
              "(jsonStr, queryStr) => { "
                  + "let json;"
                  + "try {"
                  + " json = JSON.parse(jsonStr);"
                  + "} catch (e) {"
                  + " print('Wrong JSON');"
                  + " return"
                  + "}"
                  + "const res = jsqry.query(json, queryStr);"
                  + "print(JSON.stringify(res, null, 2));"
                  + " }");

      logicFunction.executeVoid(inputJsonStr, args[0]);
    }
  }
}
