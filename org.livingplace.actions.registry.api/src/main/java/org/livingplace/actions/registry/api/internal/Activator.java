package org.livingplace.actions.registry.api.internal;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Component
public class Activator {

    @Service
    ActionRegistryImpl registry = new ActionRegistryImpl();


}
