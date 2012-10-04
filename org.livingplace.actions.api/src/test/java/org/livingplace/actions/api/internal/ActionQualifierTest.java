package org.livingplace.actions.api.internal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.livingplace.actions.api.IActionQualifier;

public class ActionQualifierTest {

  private IActionQualifier testObject;
  String VERSION = "1.0.0-SNAPSHOT";
  String NAME = "TestName";
  String NAMESPACE = "TestNamespace";

  @Before
  public void setup() {
    testObject = new ActionQualifier(NAMESPACE, NAME, VERSION);
  }

  @Test
  public void testGetFullQualifier() throws Exception {
    Assert.assertEquals(testObject.getFullQualifier(), testObject.getPrefix() + ":" + NAMESPACE + "." + NAME + ":" + VERSION);
  }

  @Test
  public void testGetName() throws Exception {
    Assert.assertSame(testObject.getName(), NAME);
  }

  @Test
  public void testGetNamespace() throws Exception {
    Assert.assertSame(testObject.getNamespace(), NAMESPACE);
  }

  @Test
  public void testGetVersion() throws Exception {
    Assert.assertSame(testObject.getVersion(), VERSION);
  }

  @Test
  public void testToString() throws Exception {
    Assert.assertEquals(testObject.getFullQualifier(), testObject.toString());
  }
}
