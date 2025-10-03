package main.cgvsu.com.view;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

public class Point2DViewer implements CurveViewer<Point2D> {
    private static final int POINT_RADIUS = 5;
    private static final Color POINT_COLOR = Color.BLUE;
    private static final Color LINE_COLOR = Color.RED;
    private static final Color CURVE_COLOR = Color.BLUE;

    @Override
    public void drawControlPoints(GraphicsContext gc, List<Point2D> points) {
        if (points.isEmpty()) return;

        gc.setFill(POINT_COLOR);

        for (Point2D point : points) {
            gc.fillOval(
                    point.getX() - POINT_RADIUS,
                    point.getY() - POINT_RADIUS,
                    2 * POINT_RADIUS,
                    2 * POINT_RADIUS
            );
        }
    }

    @Override
    public void drawControlPolygon(GraphicsContext gc, List<Point2D> points) {
        if (points.size() < 2) return;

        gc.setStroke(LINE_COLOR);
        gc.setLineWidth(1);

        for (int i = 1; i < points.size(); i++) {
            Point2D p1 = points.get(i - 1);
            Point2D p2 = points.get(i);
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
    }

    @Override
    public void drawCurve(GraphicsContext gc, List<Point2D> curvePoints) {
        if (curvePoints.size() < 2) return;

        gc.setStroke(CURVE_COLOR);
        gc.setLineWidth(2);

        for (int i = 1; i < curvePoints.size(); i++) {
            Point2D p1 = curvePoints.get(i - 1);
            Point2D p2 = curvePoints.get(i);
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
    }

    @Override
    public void clearCanvas(GraphicsContext gc, double width, double height) {
        gc.clearRect(0, 0, width, height);
    }
}