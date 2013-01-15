package org.livingplace.controlling.api;

/**
 * Classes implementing this interface are completely qualifiable in the domain of
 * org.livingplace.controlling.*
 *
 * Note: This is used to qualify by namespace, name and version of a specific class (FQDNV).
 */
public interface IQualifier {
  /**
   * Returns String representation of the FQDNV
   * @return String representation of this Qualifier
   */
  String getFullQualifier();

  /**
   * Returns only the Name of the Qualifier
   * @return Only the Name of this Qualifier
   */
  String getName();

  /**
   * Returns only the Namespace of the Qualifier
   * @return Only the Namespace of this Qualifier
   */
  String getNamespace();

  /**
   * Returns only the Version of the Qualifier
   * @return Only the Version of this Qualifier
   */
  String getVersion();

  /**
   * Returns only the Prefix of the Qualifier
   * @return Only the Prefix of this Qualifier
   */
  String getPrefix();

  /**
   * @see IQualifier.getFullQualifier()
   * @return getFullQualifier()
   */
  @Override
  String toString();
}
