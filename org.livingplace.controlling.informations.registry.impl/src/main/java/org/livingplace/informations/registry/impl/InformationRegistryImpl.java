package org.livingplace.informations.registry.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.livingplace.controlling.api.providers.Registry;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;
import org.livingplace.controlling.informations.api.IInformation;

@Component
@Service
public class InformationRegistryImpl extends Registry<IInformation> implements IInformationRegistry {

}
