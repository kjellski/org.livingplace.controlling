package org.livingplace.controlling.informations.api.providers;

/**
 * This class is just for testing purposes and is unusable in production. It just shows how to implement
 * the concrete type.
 */
public class TestSensor extends AbstractSensor {

  public static final int INFORMATION_COUNT = 3;

  public TestSensor() {
    super(new SensorQualifier("testnamespace", "testname", "testversion"));

    for (int i = 0; i < INFORMATION_COUNT; i++) {
      this.informations.add(new TestInformation(this, "" + i));
    }
  }
}
