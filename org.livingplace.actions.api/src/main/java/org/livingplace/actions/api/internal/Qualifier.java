package org.livingplace.actions.api.internal;

import org.livingplace.actions.api.IQualifier;

public abstract class Qualifier implements IQualifier {
  private final String name;
  private final String namespaceName;
  private final String version;

  public Qualifier(String namespaceName, String name, String version) {
    if (name == null || namespaceName == null || version == null)
      throw new IllegalArgumentException("An ActionIQualifier may never be initialized with empty qualification strings.");

    this.name = name;
    this.namespaceName = namespaceName;
    this.version = version;
  }

  public abstract String getFullQualifier();
  public abstract String getPrefix();

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getNamespace() {
    return this.namespaceName;
  }

  @Override
  public String getVersion() {
    return this.version;
  }

  @Override
  public String toString() {
    return getFullQualifier();
  }

  /*
  * private to ensure that the qualifier couldn't be instantiated useless
  */
  @SuppressWarnings("unused")
  private Qualifier() {
    // throws runtime exception to ensure it's never used anyways
    this(null, null, null);
  }
}
