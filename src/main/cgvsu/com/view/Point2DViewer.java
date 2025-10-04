package main.cgvsu.com.view;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

public class Point2DViewer implements InterpolationViewer<Point2D> {
    private static final int POINT_RADIUS = 5;
    private static final int DRAGGED_POINT_RADIUS = 7;
    private static final Color POINT_COLOR = Color.BLUE;
    private static final Color DRAGGED_POINT_COLOR = Color.RED;
    private static final Color POINT_STROKE_COLOR = Color.DARKBLUE;
    private static final Color DRAGGED_POINT_STROKE_COLOR = Color.DARKRED;
    private static final Color LINE_COLOR = Color.RED;
    private static final Color CURVE_COLOR = Color.BLUE;
    private static final Color TEXT_COLOR = Color.LIGHTSLATEGREY;
    private static final Color ERROR_COLOR = Color.RED;
    private static final Color DEBUG_COLOR = Color.BLACK;

    @Override
    public void drawControlPoints(Canvas canvas, List<Point2D> points, int draggedPointIndex) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);
            drawPoint(gc, point, i, draggedPointIndex);
            drawPointLabel(gc, point, i);
        }
    }

    @Override
    public void drawControlPolygon(Canvas canvas, List<Point2D> points) {
        if (points.size() < 2) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(LINE_COLOR);
        gc.setLineWidth(1);

        for (int i = 1; i < points.size(); i++) {
            Point2D p1 = points.get(i - 1);
            Point2D p2 = points.get(i);
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
    }

    @Override
    public void drawCurve(Canvas canvas, List<Point2D> curvePoints) {
        if (curvePoints.size() < 2) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(CURVE_COLOR);
        gc.setLineWidth(2);

        gc.beginPath();
        gc.moveTo(curvePoints.get(0).getX(), curvePoints.get(0).getY());

        for (int i = 1; i < curvePoints.size(); i++) {
            Point2D point = curvePoints.get(i);
            gc.lineTo(point.getX(), point.getY());
        }

        gc.stroke();
    }

    @Override
    public void clearCanvas(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    @Override
    public void drawErrorText(Canvas canvas, String message) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(ERROR_COLOR);
        gc.fillText(message, 10, canvas.getHeight() - 20);
    }

    @Override
    public void drawDebugInfo(Canvas canvas, String debugInfo) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(DEBUG_COLOR);

        String[] lines = debugInfo.split("\n");
        for (int i = 0; i < lines.length; i++) {
            gc.fillText(lines[i], 10, 20 + i * 20);
        }
    }

    private void drawPoint(GraphicsContext gc, Point2D point, int index, int draggedPointIndex) {
        double radius = draggedPointIndex == index ? DRAGGED_POINT_RADIUS : POINT_RADIUS;
        Color fillColor = draggedPointIndex == index ? DRAGGED_POINT_COLOR : POINT_COLOR;
        Color strokeColor = draggedPointIndex == index ? DRAGGED_POINT_STROKE_COLOR : POINT_STROKE_COLOR;

        gc.setFill(fillColor);
        gc.fillOval(point.getX() - radius, point.getY() - radius, radius * 2, radius * 2);

        gc.setStroke(strokeColor);
        gc.setLineWidth(1);
        gc.strokeOval(point.getX() - radius, point.getY() - radius, radius * 2, radius * 2);
    }

    private void drawPointLabel(GraphicsContext gc, Point2D point, int index) {
        gc.setFill(TEXT_COLOR);
        gc.fillText(String.valueOf(index + 1), point.getX() + 8, point.getY() - 8);
    }
}