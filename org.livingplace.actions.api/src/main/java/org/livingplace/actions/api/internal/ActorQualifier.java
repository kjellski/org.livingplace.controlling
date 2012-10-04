package org.livingplace.actions.api.internal;

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
    return PREFIX + ":" + super.getNamespace() + "." + super.getName() + ":" + super.getVersion();
  }
}
