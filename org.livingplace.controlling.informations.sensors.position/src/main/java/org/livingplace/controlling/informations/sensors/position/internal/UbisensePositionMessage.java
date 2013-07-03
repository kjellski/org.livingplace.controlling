package org.livingplace.controlling.informations.sensors.position.internal;

public class UbisensePositionMessage {

    private String UbisenseTagId;
    private String Unit;
    private String Version;
    private String Id;
    private String Ontology;
    private Position NewPosition;

    public String getUbisenseTagId() {
        return UbisenseTagId;
    }

    public void setUbisenseTagId(String ubisenseTagId) {
        UbisenseTagId = ubisenseTagId;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getOntology() {
        return Ontology;
    }

    public void setOntology(String ontology) {
        Ontology = ontology;
    }

    public Position getNewPosition() {
        return NewPosition;
    }

    public void setNewPosition(Position NewPosition) {
        this.NewPosition = NewPosition;
    }
}
