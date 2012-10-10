package org.livingplace.actions.api.providers;

import org.livingplace.actions.api.IActorQualifier;

public class ActorQualifier extends Qualifier implements IActorQualifier {
  public ActorQualifier(String actorNamespaceName, String actorName, String actorVersion) {
    super(actorNamespaceName, actorName, actorVersion);
  }

  @Override
  public String getPrefix() {
    return PREFIX;
  }

  @Override
  public String getFullQualifier() {
    return PREFIX + ":" + getNamespace() + "." + getName() + ":" + getVersion();
  }
}
