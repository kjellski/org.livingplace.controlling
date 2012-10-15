package org.livingplace.controlling.actions.api.providers;

import org.livingplace.controlling.actions.api.IActorQualifier;
import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.api.providers.Qualifier;

public class ActorQualifier extends Qualifier implements IActorQualifier {

  public ActorQualifier(IQualifier qualifier) {
    super(qualifier.getNamespace(), qualifier.getName(), qualifier.getVersion());
  }

  public ActorQualifier(String actorNamespaceName, String actorName, String actorVersion) {
    super(actorNamespaceName, actorName, actorVersion);
  }

  @Override
  public String getPrefix() {
    return PREFIX;
  }
}
