package org.livingplace.controlling.informations.registry.impl.internal;

import org.junit.Before;
import org.junit.Test;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;
import org.mockito.Mockito;
import org.osgi.service.log.LogService;

public class InformationRegistryImplTest {
  ISensor Sensor;
  IInformationRegistry registry;

  @Before
  public void setUp() throws Exception {
    Sensor = new TestSensor();
    registry = new InformationRegistryImpl(Mockito.mock(LogService.class));
  }

  @Test
  public void testSenseInformation() throws Exception {
    
  }
}