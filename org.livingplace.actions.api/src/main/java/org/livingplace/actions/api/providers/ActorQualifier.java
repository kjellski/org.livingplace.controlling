package org.livingplace.actions.api.providers;

import org.livingplace.actions.api.IActorQualifier;
import org.livingplace.actions.api.IQualifier;

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
