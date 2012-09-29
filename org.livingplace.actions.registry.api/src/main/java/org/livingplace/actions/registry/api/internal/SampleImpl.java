package org.livingplace.actions.registry.api.internal;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.livingplace.actions.registry.api.Sample;

//@Component
//@Service(value = Sample.class)
@Component(metatype = false, immediate = true)
@Service(value = org.livingplace.actions.registry.api.Sample.class)
public class SampleImpl implements Sample {

  @Activate
  void start() {
    System.out.println("Started SampleImpl.");
  }

  @Deactivate
  void stop() {
    System.out.println("Stopped SampleImpl.");
  }

  @Override
  public void sayHello() {
    System.out.println("Hello!");
  }
}
