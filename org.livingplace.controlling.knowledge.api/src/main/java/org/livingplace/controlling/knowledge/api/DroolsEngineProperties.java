package org.livingplace.controlling.knowledge.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DroolsEngineProperties extends Properties{

  DroolsEngineProperties() {

    Properties defaults = new Properties();
    InputStream in = this.getClass().getResourceAsStream("droolsengine.properties");

    try {
      defaults.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (defaults != null)
      this.defaults = defaults;

    System.out.println(this.defaults.toString());
  }
}
