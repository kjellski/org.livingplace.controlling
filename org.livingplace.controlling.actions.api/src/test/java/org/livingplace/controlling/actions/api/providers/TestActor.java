package org.livingplace.controlling.actions.api.providers;

/**
 * This class is just for testing purposes and is unusable in production. It just shows how to implement
 * the concrete type.
 */
public class TestActor extends AbstractActor {

  public static final int ACTION_COUNT = 3;

  public TestActor() {
    super(new ActorQualifier("testnamespace", "testname", "testversion"));

    for (int i = 0; i < ACTION_COUNT; i++) {
      this.actions.add(new TestAction("" + i));
    }
  }
}
