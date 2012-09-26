package org.livingplace.actions.api;

public interface IActionQualifier {
    String getFullQualifier();

    String getActionName();

    String getActionNamespace();

    @Override
    String toString();
}
