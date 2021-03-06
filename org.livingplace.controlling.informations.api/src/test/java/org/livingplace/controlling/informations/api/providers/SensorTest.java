package org.livingplace.controlling.informations.api.providers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.ISensorQualifier;

public class SensorTest {
  ISensor sensor;

  @Before
  public void setup() {
    sensor = new TestSensor();
  }

  @Test
  public void testGetSensorQualifer() throws Exception {
    Assert.assertNotNull(sensor.getQualifier());
    Assert.assertNotSame(sensor.getQualifier(), ISensorQualifier.PREFIX + ":.:");
  }

  @Test
  public void testGetAllInformations() throws Exception {
    Assert.assertEquals(sensor.getAllInformations().size(), TestSensor.INFORMATION_COUNT);
  }
}
