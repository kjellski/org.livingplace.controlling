package org.livingplace.controlling.actions.api.providers;

import org.livingplace.controlling.actions.api.IActorQualifier;
import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.api.providers.QualifierParser;

public class ActorQualifierParser extends QualifierParser {

  public static ActorQualifier parseActorQualifier(String fullActorQualifier) {
    // if necessary, remove the "ACTION:
    if (fullActorQualifier.startsWith(IActorQualifier.PREFIX + ":"))
      fullActorQualifier = fullActorQualifier.replace(IActorQualifier.PREFIX + ":","");

    IQualifier qualifier = parseQualifier(fullActorQualifier);
    return new ActorQualifier(qualifier);
  }
}
