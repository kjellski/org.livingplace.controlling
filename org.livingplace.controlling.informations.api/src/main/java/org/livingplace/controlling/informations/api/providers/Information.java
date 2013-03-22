package org.livingplace.controlling.informations.api.providers;

import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationQualifier;
import org.livingplace.controlling.informations.api.ISensor;

import java.util.EventObject;

public class Information extends EventObject implements IInformation {

  protected final IInformationQualifier qualifier;
  protected Object information;

  /**
   * Constructs a prototypical Event.
   *
   * @param source The object on which the Event initially occurred.
   * @throws IllegalArgumentException if source is null.
   */
  protected Information(ISensor source, IInformationQualifier qualifier) {
    super(source);
    this.qualifier = qualifier;
  }

  public void setInformation(Object information) {
    this.information = information;
  }

  public Object getInformation() {
    return this.information;
  }

  public IInformationQualifier getQualifier() {
    return this.qualifier;
  }

  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();

    if (this.information != null)
      try {
        b.append(qualifier.getFullQualifier() + ":" + this.information.toString());
      } catch (Exception ex) {
        b.append(qualifier.getFullQualifier() + ":" + "[ERROR] information.toString() threw an Exception: " + ex.getMessage());
      }
    else
      b.append(qualifier.getFullQualifier() + ":" + "null");

    return b.toString();
  }
}
