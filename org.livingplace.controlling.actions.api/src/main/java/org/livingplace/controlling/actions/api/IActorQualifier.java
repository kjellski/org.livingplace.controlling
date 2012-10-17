package org.livingplace.controlling.actions.api;

import org.livingplace.controlling.api.IQualifier;

/**
 * Actor specific qualifier
 */
public interface IActorQualifier extends IQualifier {
  /**
   * Prefix to be combined with the full qualifier in order to
   * make the qualifier distinguishable
   */
  public static final String PREFIX = "ACTOR";
}
