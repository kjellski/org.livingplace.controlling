package org.livingplace.controlling.informations.api;

import java.util.List;

public interface ISensor {

  ISensorQualifier getSensorQualifer();
  List<IInformation> getAllInformations();
}
