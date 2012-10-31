package org.livingplace.controlling.informations.registry.impl.internal;

import org.livingplace.controlling.informations.api.providers.AbstractSensor;
import org.livingplace.controlling.informations.api.providers.InformationQualifier;
import org.livingplace.controlling.informations.api.providers.SensorQualifier;

/**
 * This class is just for testing purposes and is unusable in production. It just shows how to implement
 * the concrete type.
 */
public class TestSensor extends AbstractSensor {

  public static String NAMESPACE = "TestNamespace";
  public static String NAME = "TestName";
  public static String VERSION = "TestVersion";

  public static int INFORMATION_COUNT = 3;

  protected TestSensor() {
    super(new SensorQualifier(NAMESPACE, NAME, VERSION));

    for (int i = 0; i < INFORMATION_COUNT; i++) {
      this.informations.add(new TestInformation(this, new InformationQualifier(i + NAMESPACE, i + NAME, i + VERSION)));
    }
  }
}
