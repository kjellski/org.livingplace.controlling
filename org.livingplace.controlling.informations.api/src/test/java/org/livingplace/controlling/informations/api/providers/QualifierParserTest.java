package org.livingplace.controlling.informations.api.providers;

import org.junit.Assert;
import org.junit.Test;
import org.livingplace.controlling.informations.api.IInformationQualifier;

public class QualifierParserTest {

  @Test
  public void testInformationParse() throws Exception {
    IInformationQualifier qualifier = InformationQualifierParser.parseInformationQualifier(InformationQualifierTest.NAMESPACE
            + "." + InformationQualifierTest.NAME
            + ":" + InformationQualifierTest.VERSION);

    Assert.assertEquals(qualifier.getNamespace(), InformationQualifierTest.NAMESPACE);
    Assert.assertEquals(qualifier.getName(), InformationQualifierTest.NAME);
    Assert.assertEquals(qualifier.getVersion(), InformationQualifierTest.VERSION);
  }

}
