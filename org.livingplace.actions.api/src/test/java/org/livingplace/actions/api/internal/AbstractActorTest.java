package org.livingplace.actions.api.internal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.livingplace.actions.api.IActor;
import org.livingplace.actions.api.IActorQualifier;

public class AbstractActorTest {
  IActor actor;

  @Before
  public void setup() {
    actor = new TestActor();
  }

  @Test
  public void testGetActorQualifer() throws Exception {
    Assert.assertNotNull(actor.getActorQualifer());
    Assert.assertNotSame(actor.getActorQualifer(), IActorQualifier.PREFIX + ":.:");
  }

  @Test
  public void testGetAllActions() throws Exception {
    Assert.assertEquals(actor.getAllActions().size(), TestActor.ACTION_COUNT);
  }
}
