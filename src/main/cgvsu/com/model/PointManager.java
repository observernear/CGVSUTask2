package main.cgvsu.com.model;

import java.util.List;

public interface PointManager<T> {
    void addPoint(T point);
    void removePoint(T point);
    void removePoint(int index);
    void clearPoints();
    List<T> getPoints();
    int getPointCount();
    boolean hasEnoughPointsForCurve();
    T findNearestPoint(T target, double radius);
    boolean containsPoint(T point);
}