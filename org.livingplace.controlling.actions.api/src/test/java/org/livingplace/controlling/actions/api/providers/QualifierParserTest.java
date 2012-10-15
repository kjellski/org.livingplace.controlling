package org.livingplace.controlling.actions.api.providers;

import org.junit.Assert;
import org.junit.Test;
import org.livingplace.controlling.actions.api.IActionQualifier;

public class QualifierParserTest {

  @Test
  public void testActionParse() throws Exception {
    IActionQualifier qualifier = ActionQualifierParser.parseActionQualifier(ActionQualifierTest.NAMESPACE
            + "." + ActionQualifierTest.NAME
            + ":" + ActionQualifierTest.VERSION);

    Assert.assertEquals(qualifier.getNamespace(), ActionQualifierTest.NAMESPACE);
    Assert.assertEquals(qualifier.getName(), ActionQualifierTest.NAME);
    Assert.assertEquals(qualifier.getVersion(), ActionQualifierTest.VERSION);
  }

}
