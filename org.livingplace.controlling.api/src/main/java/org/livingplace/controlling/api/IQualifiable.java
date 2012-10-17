package org.livingplace.controlling.api;

/**
 * Marks objects as qualifiable for a readable identification
 */
public interface IQualifiable {
  abstract IQualifier getQualifier();
}
