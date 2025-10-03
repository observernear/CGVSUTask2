package main.cgvsu.com.model;

import java.util.List;

public interface CurveCalculator<T> {
    List<T> calculateCurve(List<T> controlPoints, int segments);
    T calculatePoint(List<T> points, double t);
}