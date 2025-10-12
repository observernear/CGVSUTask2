package main.cgvsu.com.model;

import java.util.List;

public interface InterpolationStrategyImpl<T> {
    List<T> calculate(List<T> controlPoints, int segments);
    T calculatePoint(List<T> points, double t);
}