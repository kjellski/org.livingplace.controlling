package org.livingplace.controlling.informations.registry.impl.internal;

import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationQualifier;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.providers.Information;

/**
 * This class is just for testing purposes and is unusable in production. It just shows how to implement
 * the concrete type.
 */
public class TestInformation extends Information implements IInformation {
  /**
   * Constructs a prototypical Event.
   *
   * @param source The object on which the Event initially occurred.
   * @throws IllegalArgumentException if source is null.
   */
  protected TestInformation(ISensor source, IInformationQualifier qualifier) {
    super(source, qualifier);
  }
}

