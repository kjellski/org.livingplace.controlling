package org.livingplace.controlling.informations.sensors.time.internal;

import org.joda.time.DateTime;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.providers.AbstractInformation;
import org.livingplace.controlling.informations.api.providers.InformationQualifier;

public class UTCTimeInformation extends AbstractInformation implements IInformation {
  public UTCTimeInformation(ISensor source) {
    super(source, new InformationQualifier("time", "UTCTime", "1.0"));

    this.setInformation(this.getQualifier() + " - UTCTime: " + DateTime.now());
  }
}
