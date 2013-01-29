package org.livingplace.controlling.actions.api.providers;

import org.livingplace.controlling.actions.api.IActionQualifier;
import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.api.providers.QualifierParser;

public class ActionQualifierParser extends QualifierParser {

  public static ActionQualifier parseActionQualifier(String fullActionQualifier) {
    // if necessary, remove the "ACTION:
    if (fullActionQualifier.startsWith(IActionQualifier.PREFIX + ":"))
      fullActionQualifier = fullActionQualifier.replace(IActionQualifier.PREFIX + ":","");

    IQualifier qualifier = parseQualifier(fullActionQualifier);
    return new ActionQualifier(qualifier);
  }
}
