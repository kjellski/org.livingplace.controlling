package org.livingplace.controlling.informations.api.providers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.livingplace.controlling.informations.api.IInformationQualifier;

public class InformationQualifierTest {

  private IInformationQualifier testObject;
  public static final String VERSION = "1.0.0-SNAPSHOT";
  public static final String NAME = "TestName";
  public static final String NAMESPACE = "TestNamespace";

  @Before
  public void setup() {
    testObject = new InformationQualifier(NAMESPACE, NAME, VERSION);
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
