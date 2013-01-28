package org.livingplace.controlling.informations.api;

import java.util.EventListener;

public interface IInformationListener extends EventListener {
  void sensedInformation(final IInformation information);
}
