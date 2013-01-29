package org.livingplace.controlling.informations.api.providers;

import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.api.providers.QualifierParser;
import org.livingplace.controlling.informations.api.IInformationQualifier;

public class InformationQualifierParser extends QualifierParser {

  public static InformationQualifier parseInformationQualifier(String fullInformationQualifier) {
    // if necessary, remove the "ACTION:
    if (fullInformationQualifier.startsWith(IInformationQualifier.PREFIX + ":"))
      fullInformationQualifier = fullInformationQualifier.replace(IInformationQualifier.PREFIX + ":","");
    IQualifier qualifier = parseQualifier(fullInformationQualifier);
    return new InformationQualifier(qualifier);
  }
}
