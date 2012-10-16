package org.livingplace.controlling.informations.api;

import org.livingplace.controlling.api.IQualifiable;

public interface IInformation extends IQualifiable {
  void setInformation(Object information);
  Object getInformation();
  IInformationQualifier getQualifier();
}
