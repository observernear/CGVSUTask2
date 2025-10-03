package main.cgvsu.com.view;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public interface CurveViewer<T> {
    void drawControlPoints(GraphicsContext gc, List<T> points);
    void drawControlPolygon(GraphicsContext gc, List<T> points);
    void drawCurve(GraphicsContext gc, List<T> curvePoints);
    void clearCanvas(GraphicsContext gc, double width, double height);
}