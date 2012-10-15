package org.livingplace.controlling.api;

public interface IQualifier {
  String getFullQualifier();

  String getName();

  String getNamespace();

  String getVersion();

  String getPrefix();

  @Override
  String toString();
}
