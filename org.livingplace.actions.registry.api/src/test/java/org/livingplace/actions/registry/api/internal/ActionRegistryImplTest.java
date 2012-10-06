package org.livingplace.actions.registry.api.internal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.livingplace.actions.api.IAction;
import org.livingplace.actions.api.IActor;
import org.livingplace.actions.registry.api.IActionRegistry;
import org.mockito.Mockito;
import org.osgi.service.log.LogService;

public class ActionRegistryImplTest {
  IActor actor;
  IActionRegistry registry;

  @Before
  public void setUp() throws Exception {
    actor = new TestActor();
    registry = new ActionRegistryImpl();
    ((ActionRegistryImpl) registry).setLog(Mockito.mock(LogService.class));
  }

  @Test
  public void testExecuteActionWithActionProperties() throws Exception {

  }

  @Test
  public void testExecuteAction() throws Exception {

  }

  @Test
  public void testRegisterActor() throws Exception {
    int actionCount = registry.getAllRegisteredActions().size();
    registry.registerActor(actor);
    Assert.assertEquals(actionCount + TestActor.ACTION_COUNT, registry.getAllRegisteredActions().size());
  }

  @Test
  public void testUnregisterActor() throws Exception {
    int actionCount = registry.getAllRegisteredActions().size();
    registry.registerActor(actor);
    Assert.assertEquals(actionCount + TestActor.ACTION_COUNT, registry.getAllRegisteredActions().size());
    registry.unregisterActor(actor);
    Assert.assertEquals(actionCount, registry.getAllRegisteredActions().size());
  }

  @Test
  public void testExecuteActionOnActorWithActionProperties() throws Exception {

  }

  @Test
  public void testExecuteActionOnActor() throws Exception {

  }

  @Test
  public void testGetAllRegisteredActors() throws Exception {

  }

  @Test
  public void testRegisterActions() throws Exception {

  }

  @Test
  public void testUnregisterActions() throws Exception {

  }

  @Test
  public void testGetAllRegisteredActions() throws Exception {
    int actionCount = registry.getAllRegisteredActions().size();
    Assert.assertTrue(actionCount >= 0);

    if (actionCount > 0) {
      for (IAction iAction : registry.getAllRegisteredActions()) {
        Assert.assertTrue(iAction != null);
        Assert.assertTrue(iAction.getActionQualifier() != null);
      }
    }
  }
}

