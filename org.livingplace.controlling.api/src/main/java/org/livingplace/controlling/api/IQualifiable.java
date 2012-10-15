package org.livingplace.controlling.api;

/**
 * Marks object as qualifiable to get them registered
 */
public interface IQualifiable {
  IQualifier getQualifier();
}
