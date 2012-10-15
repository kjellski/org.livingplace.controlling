package org.livingplace.actions.api.providers;

import org.junit.Assert;
import org.junit.Test;
import org.livingplace.actions.api.IActionQualifier;

public class QualifierParserTest {

  @Test
  public void testActionParse() throws Exception {
    IActionQualifier qualifier = QualifierParser.parseActionQualifier(ActionQualifierTest.NAMESPACE
            + "." + ActionQualifierTest.NAME
            + ":" + ActionQualifierTest.VERSION);

    Assert.assertEquals(qualifier.getNamespace(), ActionQualifierTest.NAMESPACE);
    Assert.assertEquals(qualifier.getName(), ActionQualifierTest.NAME);
    Assert.assertEquals(qualifier.getVersion(), ActionQualifierTest.VERSION);
  }

}
