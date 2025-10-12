package main.cgvsu.com.model;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Point2DManager implements PointManagerImpl<Point2D> {
    private final List<Point2D> points;

    public Point2DManager() {
        this.points = new ArrayList<>();
    }

    @Override
    public void addPoint(Point2D point) {
        points.add(point);
    }

    @Override
    public void insertPoint(int index, Point2D point) {
        if (index >= 0 && index <= points.size()) {
            points.add(index, point);
        }
    }

    @Override
    public void updatePoint(int index, Point2D newPoint) {
        if (index >= 0 && index < points.size()) {
            points.set(index, newPoint);
        }
    }

    @Override
    public void removePoint(Point2D point) {
        points.remove(point);
    }

    @Override
    public void removePoint(int index) {
        if (index >= 0 && index < points.size()) {
            points.remove(index);
        }
    }

    @Override
    public void clearPoints() {
        points.clear();
    }

    @Override
    public List<Point2D> getPoints() {
        return new ArrayList<>(points);
    }

    @Override
    public int getPointCount() {
        return points.size();
    }

    @Override
    public boolean hasEnoughPointsForCurve() {
        return points.size() >= 2;
    }

    @Override
    public Point2D findNearestPoint(Point2D target, double radius) {
        Point2D nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Point2D point : points) {
            double distance = point.distance(target);
            if (distance <= radius && distance < minDistance) {
                minDistance = distance;
                nearest = point;
            }
        }
        return nearest;
    }

    @Override
    public int findNearestPointIndex(Point2D target, double radius) {
        int nearestIndex = -1;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < points.size(); i++) {
            double distance = points.get(i).distance(target);
            if (distance <= radius && distance < minDistance) {
                minDistance = distance;
                nearestIndex = i;
            }
        }
        return nearestIndex;
    }

    @Override
    public boolean containsPoint(Point2D point) {
        return points.contains(point);
    }
}