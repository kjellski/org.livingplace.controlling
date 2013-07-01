package org.livingplace.controlling.informations.sensors.doorcontrol.internal;

import org.haw.door.objects.LPResource;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.providers.Information;
import org.livingplace.controlling.informations.api.providers.InformationQualifier;

/**
 * Created with IntelliJ IDEA.
 * User: sven
 * Date: 01.07.13
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */
public class DoorInformations extends Information implements IInformation{
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException
     *          if source is null.
     */
    public DoorInformations(ISensor source, LPResource door) {
        super(source, new InformationQualifier("Door", "DoorInformations", "1.0"));
        this.setInformation(door);
    }

    public void setDoor(LPResource door){
        this.setInformation(door);
    }
}
