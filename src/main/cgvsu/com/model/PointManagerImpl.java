package main.cgvsu.com.model;

import java.util.List;

public interface PointManagerImpl<T> {
    void addPoint(T point);
    void insertPoint(int index, T point);
    void updatePoint(int index, T newPoint);
    void removePoint(T point);
    void removePoint(int index);
    void clearPoints();
    List<T> getPoints();
    int getPointCount();
    boolean hasEnoughPointsForCurve();
    T findNearestPoint(T target, double radius);
    int findNearestPointIndex(T target, double radius);
    boolean containsPoint(T point);
}