package com.hedgehogkb.stage;

import java.awt.geom.Point2D;

public class Camera {
    private Point2D cameraPos;
    private double zoom;

    public Camera(Point2D cameraPos, double zoom) {
        this.cameraPos = cameraPos;
        this.zoom = zoom;
    }

    public Point2D getCameraPos() {
        return this.cameraPos;
    }
    public void getCameraPos(Point2D cameraPos) {
        this.cameraPos = cameraPos;
    }

    public double getZoom() {
        return this.zoom;
    }
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
}
