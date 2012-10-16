package org.livingplace.controlling.informations.api.providers;

import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationQualifier;

import java.util.EventObject;

public abstract class AbstractInformation extends EventObject implements IInformation {

  protected final IInformationQualifier qualifier;
  protected Object information;

  /**
   * Constructs a prototypical Event.
   *
   * @param source The object on which the Event initially occurred.
   * @throws IllegalArgumentException if source is null.
   */
  protected AbstractInformation(Object source, IInformationQualifier qualifier) {
    super(source);
    this.qualifier = qualifier;
  }

  public void setInformation(Object information){
    this.information = information;
  }

  public Object getInformation(){
    return this.information;
  }

  public abstract IInformationQualifier getQualifier();
}
