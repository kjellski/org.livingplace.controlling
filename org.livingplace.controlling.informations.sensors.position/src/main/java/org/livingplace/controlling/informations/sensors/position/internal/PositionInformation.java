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
  public PositionInformation(ISensor source) {
    super(source, new InformationQualifier("Position", "Position", "1.0"));
    this.setInformation(new Position(0,0));
  }

  public void setPostition(Position position){
    this.setInformation(position);
  }
}
