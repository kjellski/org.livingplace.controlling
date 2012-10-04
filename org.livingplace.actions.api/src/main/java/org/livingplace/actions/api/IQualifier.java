package org.livingplace.actions.api;

public interface IQualifier {
  String getFullQualifier();

  String getName();

  String getNamespace();

  String getVersion();

  String getPrefix();

  @Override
  String toString();
}
