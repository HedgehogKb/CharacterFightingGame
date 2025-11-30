package com.hedgehogkb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class TubeHitbox {
    private Point2D circle1;
    private Point2D circle2;
    private double radius;

    public TubeHitbox(Point2D circle1, Point2D circle2, double radius) {
        this.circle1 = circle1;
        this.circle2 = circle2;
        this.radius = radius;
    }

    public TubeHitbox(double x1, double y1, double x2, double y2, double radius) {
        this.circle1 = new Point2D.Double(x1, y1);
        this.circle2 = new Point2D.Double(x2, y2);
        this.radius = radius;
    }

    public void draw(Graphics g) {
        int x1 = (int) circle1.getX() - (int) (radius);
        int y1 = (int) circle1.getY() - (int) (radius);
    
        int x2 = (int) circle2.getX() - (int) (radius);
        int y2 = (int) circle2.getY() - (int) (radius);

        g.drawOval(x1, y1, (int) (radius * 2), (int) (radius * 2));
        g.drawOval(x2, y2, (int) (radius * 2), (int) (radius * 2));

        double theta = Math.atan2(circle2.getY() - circle1.getY(), circle2.getX() - circle1.getX());
        double dx = radius * Math.sin(theta);
        double dy = radius * Math.cos(theta);

        int[] xPoints = {
            (int) (circle1.getX() + dx),
            (int) (circle2.getX() + dx),
            (int) (circle2.getX() - dx),
            (int) (circle1.getX() - dx)
        };
        int[] yPoints = {
            (int) (circle1.getY() - dy),
            (int) (circle2.getY() - dy),
            (int) (circle2.getY() + dy),
            (int) (circle1.getY() + dy)
        };
        g.drawPolygon(xPoints, yPoints, 4);
    }

    public boolean intersects(TubeHitbox o) {
        //TODO: add bounding circle check for optimization

        //Circle Intersection
        if (circle1.distance(o.circle1) <= this.radius + o.radius) return true;
        if (circle1.distance(o.circle2) <= this.radius + o.radius) return true;
        if (circle2.distance(o.circle1) <= this.radius + o.radius) return true;
        if (circle2.distance(o.circle2) <= this.radius + o.radius) return true;

        //Line Intersections
        double theta1 = Math.atan2(circle2.getY() - circle1.getY(), circle2.getX() - circle1.getX());
        double dx1 = radius * Math.sin(theta1);
        double dy1 = radius * Math.cos(theta1);
        Point2D[] points1 = {
            new Point2D.Double(circle1.getX() + dx1, circle1.getY() - dy1),
            new Point2D.Double(circle2.getX() + dx1, circle2.getY() - dy1),
            new Point2D.Double(circle2.getX() - dx1, circle2.getY() + dy1),
            new Point2D.Double(circle1.getX() - dx1, circle1.getY() + dy1)
        };
        double theta2 = Math.atan2(o.circle2.getY() - o.circle1.getY(), o.circle2.getX() - o.circle1.getX());
        double dx2 = o.radius * Math.sin(theta2);
        double dy2 = o.radius * Math.cos(theta2);
        Point2D[] points2 = {
            new Point2D.Double(o.circle1.getX() + dx2, o.circle1.getY() - dy2),
            new Point2D.Double(o.circle2.getX() + dx2, o.circle2.getY() - dy2),
            new Point2D.Double(o.circle2.getX() - dx2, o.circle2.getY() + dy2),
            new Point2D.Double(o.circle1.getX() - dx2, o.circle1.getY() + dy2)
        };

        for (int i = 0; i < 4; i+=2) {
            Point2D a1 = points1[i];
            Point2D a2 = points1[i+1];
            for (int v = 0; v < 4; v+=2) {
                Point2D b1 = points2[v];
                Point2D b2 = points2[v+1];
                if (linesIntersect(a1, a2, b1, b2)) {
                    return true;
                }
            }
        }

        //Cirle to Tube Body Intersections

        // Check each circle of this tube against the other tube's center line segment
        Point2D[] thisCircles = {circle1, circle2};
        for (Point2D c : thisCircles) {
            double closestX = closestLineX(o.circle1, o.circle2, c);
            double distance = lineDistance(o.circle1, o.circle2, c);
            if (distance <= radius + o.radius
            && closestX >= Math.min(o.circle1.getX(), o.circle2.getX())
            && closestX <= Math.max(o.circle1.getX(), o.circle2.getX())) {
            return true;
            }
        }
        // Check each circle of the other tube against this tube's center line segment
        Point2D[] otherCircles = {o.circle1, o.circle2};
        for (Point2D c : otherCircles) {
            double closestX = closestLineX(circle1, circle2, c);
            double distance = lineDistance(circle1, circle2, c);
            if (distance <= radius + o.radius
            && closestX >= Math.min(circle1.getX(), circle2.getX())
            && closestX <= Math.max(circle1.getX(), circle2.getX())) {
            return true;
            }
        }
        

        return false;
    }

    public boolean linesIntersect(Point2D p1, Point2D p2, Point2D q1, Point2D q2) {
        double m1 = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
        double m2 = (q2.getY() - q1.getY()) / (q2.getX() - q1.getX());
        double b1 = p1.getY() - m1 * p1.getX();
        double b2 = q1.getY() - m2 * q1.getX();

        if (m1 == m2) {
            return b1 == b2; // Parallel lines
        }

        double xIntersect = (b1 - b2) / (m2 - m1);
        return xIntersect >= Math.min(p1.getX(), p2.getX()) && xIntersect <= Math.max(p1.getX(), p2.getX()) &&
               xIntersect >= Math.min(q1.getX(), q2.getX()) && xIntersect <= Math.max(q1.getX(), q2.getX());
    }

    public double lineDistance(Point2D p1, Point2D p2, Point2D o) {
        double slope = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
        double a = slope;
        double b = -1;
        double c = -1 * slope * p1.getX() + p1.getY();

        return Math.abs(a*o.getX() + b*o.getY() + c) / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    public double closestLineX(Point2D p1, Point2D p2, Point2D o) {
        double slope = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
        double a = -1;
        double b = slope;
        double c = -1 * slope * p1.getX() + p1.getY();

        return o.getX() - a * (a*o.getX() + b*o.getY() + c) / (Math.pow(a, 2) + Math.pow(b, 2));
    }

    //GETTERS AND SETTERS
    public double getRadius() {
        return radius;
    }

}
