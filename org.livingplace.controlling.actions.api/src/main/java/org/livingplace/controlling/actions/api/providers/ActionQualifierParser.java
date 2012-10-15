package org.livingplace.controlling.actions.api.providers;

import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.api.providers.QualifierParser;

public class ActionQualifierParser extends QualifierParser {

  public static ActionQualifier parseActionQualifier(String fullActionQualifier) {
    IQualifier qualifier = parseQualifier(fullActionQualifier);
    return new ActionQualifier(qualifier);
  }
}
