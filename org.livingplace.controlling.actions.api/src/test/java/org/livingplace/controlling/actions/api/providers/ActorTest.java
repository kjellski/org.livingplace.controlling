package org.livingplace.controlling.actions.api.providers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.livingplace.controlling.actions.api.IActor;
import org.livingplace.controlling.actions.api.IActorQualifier;

public class ActorTest {
  IActor actor;

  @Before
  public void setup() {
    actor = new TestActor();
  }

  @Test
  public void testGetActorQualifer() throws Exception {
    Assert.assertNotNull(actor.getQualifier());
    Assert.assertNotSame(actor.getQualifier(), IActorQualifier.PREFIX + ":.:");
  }

  @Test
  public void testGetAllActions() throws Exception {
    Assert.assertEquals(actor.getAllActions().size(), TestActor.ACTION_COUNT);
  }
}
