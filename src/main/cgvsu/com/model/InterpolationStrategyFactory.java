package main.cgvsu.com.model;

import javafx.geometry.Point2D;

public class InterpolationStrategyFactory {

    public enum StrategyType {
        BEZIER,
        LAGRANGE,
        SPLINE
    }

    public static InterpolationStrategyImpl<Point2D> createStrategy(StrategyType type) {
        return switch (type) {
            case BEZIER -> new BezierStrategy();
            case LAGRANGE -> new LagrangeStrategy();
            case SPLINE -> new CubicSplineStrategy();
            default -> throw new IllegalArgumentException("Unknown strategy type: " + type);
        };
    }
}