package org.livingplace.controlling.informations.api.providers;

import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.ISensorQualifier;

import java.util.ArrayList;
import java.util.List;

public class AbstractSensor implements ISensor {
  protected ISensorQualifier qualifier;
  protected List<IInformation> informations = new ArrayList<IInformation>();

  protected AbstractSensor(ISensorQualifier qualifier) {
    this.qualifier = qualifier;
  }

  /*
     * this one should never ever be used.
     */
  @SuppressWarnings("unused")
  private AbstractSensor() {
    throw new IllegalAccessError("A childclass of the " + AbstractSensor.class.getName()
            + " was enforced to be instantiated in the wrong way.");
  }

  @Override
  public ISensorQualifier getQualifier() {
    return qualifier;
  }

  @Override
  public List<IInformation> getAllInformations() {
    return informations;
  }

  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();
    b.append("[" + this.qualifier.getFullQualifier() + "]: ");

    if (this.informations.size() > 0) {
      for (IInformation information : informations) {
        b.append(information.toString());
      }
    } else {
      b.append("No informations for this Sensor.");
    }
    return b.toString();
  }
}
