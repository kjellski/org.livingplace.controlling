package org.livingplace.controlling.informations.api.providers;

import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.IInformationQualifier;

/**
 * This class is just for testing purposes and is unusable in production. It just shows how to implement
 * the concrete type.
 */
public class TestInformation extends Information implements IInformation {
  private String prefix;

  public TestInformation(ISensor sensor, String prefix) {
    super(sensor, new InformationQualifier(prefix + "testnamespace", prefix + "testname", prefix + "testversion"));
    this.prefix = prefix;
  }

  @Override
  public IInformationQualifier getQualifier() {
    return qualifier;
  }
}

