package org.livingplace.controlling.actions.api;

import org.livingplace.controlling.api.IQualifier;

/**
 * Action specific qualifier
 */
public interface IActionQualifier extends IQualifier {
  /**
   * Prefix to be combined with the full qualifier in order to
   * make the qualifier distinguishable
   */
  public static final String PREFIX = "ACTION";
}

