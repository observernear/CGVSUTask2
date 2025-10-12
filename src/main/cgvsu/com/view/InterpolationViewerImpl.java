package main.cgvsu.com.view;

import javafx.scene.canvas.Canvas;

import java.util.List;

public interface InterpolationViewerImpl<T> {
    void drawControlPoints(Canvas canvas, List<T> points, int draggedPointIndex);
    void drawControlPolygon(Canvas canvas, List<T> points);
    void drawCurve(Canvas canvas, List<T> curvePoints);
    void clearCanvas(Canvas canvas);
    void drawErrorText(Canvas canvas, String message);
    void drawDebugInfo(Canvas canvas, String debugInfo);
}