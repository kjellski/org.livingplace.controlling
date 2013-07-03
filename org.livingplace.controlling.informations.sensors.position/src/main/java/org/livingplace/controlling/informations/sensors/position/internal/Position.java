package org.livingplace.controlling.informations.sensors.position.internal;

public class Position {

    private float X;
    private float Y;
    private float Z;


    public Position() {
        X = 0.0f;
        Y = 0.0f;
        Z = 0.0f;
    }

    public Position(float x, float y, float z) {
        X = x;
        Y = y;
        Z = z;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public float getZ() {
        return Z;
    }

    public void setZ(float z) {
        Z = z;
    }

}
