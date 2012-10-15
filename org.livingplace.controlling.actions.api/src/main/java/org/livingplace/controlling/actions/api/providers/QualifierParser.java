package org.livingplace.controlling.actions.api.providers;

import org.livingplace.controlling.actions.api.IQualifier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QualifierParser {

  public static ActorQualifier parseActorQualifier(String fullActionQualifier) {
    IQualifier qualifier = parseQualifier(fullActionQualifier);
    return new ActorQualifier(qualifier);
  }

  public static ActionQualifier parseActionQualifier(String fullActionQualifier) {
    IQualifier qualifier = parseQualifier(fullActionQualifier);
    return new ActionQualifier(qualifier);
  }

  private static Qualifier parseQualifier(String fullQualifier) {
    // matches <namespace>.<name>:<version>
    Pattern pattern = Pattern.compile("^([\\w+\\.*]+)\\.([\\w]+):([\\w\\.\\-]+)$");
    Matcher matcher = pattern.matcher(fullQualifier);

    if (matcher.find()) {
      return new Qualifier(matcher.group(1), matcher.group(2), matcher.group(3)) {
        @Override
        public String getPrefix() {
          return "ERROR: This should never reach the outside of a qualifier class!";
        }
      };
    } else
      throw new IllegalArgumentException("The Qualifier must be formatted in the following way: \"<namespace>.<name>:<version>\"");
  }
}

