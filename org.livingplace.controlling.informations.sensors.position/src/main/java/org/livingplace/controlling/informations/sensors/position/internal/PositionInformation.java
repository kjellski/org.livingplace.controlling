package org.livingplace.controlling.informations.sensors.position.internal;

import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.providers.Information;
import org.livingplace.controlling.informations.api.providers.InformationQualifier;

public class PositionInformation extends Information implements IInformation {
  /**
   * Constructs a prototypical Event.
   *
   * @param source The object on which the Event initially occurred.
   * @throws IllegalArgumentException
   *          if source is null.
   */
  public PositionInformation(ISensor source, UbisensePositionMessage msg) {
    super(source, new InformationQualifier("Position", "Position", "1.0"));
    this.setInformation(msg);
  }

  public void setPostition(UbisensePositionMessage msg){
    this.setInformation(msg);
  }
}
