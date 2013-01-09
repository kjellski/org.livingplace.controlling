package org.livingplace.controlling.informations.registry.impl.internal;

import org.junit.Before;
import org.junit.Test;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;

public class InformationRegistryImplTest {
  ISensor Sensor;
  IInformationRegistry registry;

  @Before
  public void setUp() throws Exception {
    Sensor = new TestSensor();
    registry = new InformationRegistryImpl();
  }

  @Test
  public void testSenseInformation() throws Exception {

  }
}