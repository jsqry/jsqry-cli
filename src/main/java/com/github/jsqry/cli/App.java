package com.github.jsqry.cli;

import org.graalvm.polyglot.Context;

public class App {
  public static void main(String[] args) {
    System.out.println("Hello Java!");

    try (Context context = Context.create()) {
      context.eval("js", "print('Hello JavaScript!');");
    }
  }
}
