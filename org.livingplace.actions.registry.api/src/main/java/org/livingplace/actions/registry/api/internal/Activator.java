package org.livingplace.actions.registry.api.internal;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;

@Component
public class Activator {

    @Reference
    ActionRegistryImpl registry = null;

    void start(){
        System.out.println("Activator START!");
    }

    @Deactivate
    void stop(){
        System.out.println("Activator START!");
    }
}
