package org.livingplace.controlling.actions.api.providers;

import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.api.providers.QualifierParser;

public class ActorQualifierParser extends QualifierParser {

  public static ActorQualifier parseActorQualifier(String fullActionQualifier) {
    IQualifier qualifier = parseQualifier(fullActionQualifier);
    return new ActorQualifier(qualifier);
  }
}
