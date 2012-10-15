package org.livingplace.controlling.informations.api.providers;

import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationQualifier;

import java.util.Observable;

public abstract class AbstractInformation extends Observable implements IInformation {
  protected final IInformationQualifier qualifier;

  protected AbstractInformation(IInformationQualifier qualifier) {
    this.qualifier = qualifier;
  }

}
