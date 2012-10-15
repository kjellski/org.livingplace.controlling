package org.livingplace.controlling.informations.api;

import org.livingplace.controlling.api.IQualifiable;

import java.util.Observer;

public interface IInformation extends IQualifiable{

  void addObserver(Observer observer);
  void deleteObserver(Observer observer);

  IInformationQualifier getQualifier();
}
