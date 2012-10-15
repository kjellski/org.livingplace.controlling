package org.livingplace.controlling.informations.api.providers;

import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.api.providers.Qualifier;
import org.livingplace.controlling.informations.api.IInformationQualifier;

/**
 * This class represents a qualifier for informations, any instance should identify the
 * information uniquely.
 */
public class InformationQualifier extends Qualifier implements IInformationQualifier {

  public InformationQualifier(IQualifier qualifier) {
    super(qualifier.getNamespace(), qualifier.getName(), qualifier.getVersion());
  }

  public InformationQualifier(String informationNamespaceName, String informationName, String informationVersion) {
    super(informationNamespaceName, informationName, informationVersion);
  }

  @Override
  public String getPrefix() {
    return PREFIX;
  }
}
