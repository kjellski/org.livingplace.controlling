package org.livingplace.controlling.actions.registry.impl.internal;

import org.livingplace.controlling.actions.api.providers.AbstractActor;
import org.livingplace.controlling.actions.api.providers.ActorQualifier;

/**
 * This class is just for testing purposes and is unusable in production. It just shows how to implement
 * the concrete type.
 */
public class TestActor extends AbstractActor {

  public static final int ACTION_COUNT = 3;

  public static String NAMESPACE = "TestActorNamespace";
  public static String NAME = "TestActorNamespace";
  public static String VERSION = "TestActorNamespace";

  public TestActor() {
    super(new ActorQualifier(NAMESPACE, NAME, VERSION));

    for (int i = 0; i < ACTION_COUNT; i++) {
      this.actions.add(new TestAction("" + i));
    }
  }
}
