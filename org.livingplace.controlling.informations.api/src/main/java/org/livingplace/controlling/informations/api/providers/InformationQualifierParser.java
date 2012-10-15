package org.livingplace.controlling.informations.api.providers;

import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.api.providers.QualifierParser;

public class InformationQualifierParser extends QualifierParser {

  public static InformationQualifier parseInformationQualifier(String fullInformationQualifier) {
    IQualifier qualifier = parseQualifier(fullInformationQualifier);
    return new InformationQualifier(qualifier);
  }
}
