package main.cgvsu.com;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ProtoCurveController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    private ArrayList<Point2D> points = new ArrayList<>();

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> {
            canvas.setWidth(newValue.doubleValue());
            redrawCanvas();
        });
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> {
            canvas.setHeight(newValue.doubleValue());
            redrawCanvas();
        });

        canvas.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> handlePrimaryClick(canvas.getGraphicsContext2D(), event);
                case SECONDARY -> handleSecondaryClick();
            }
        });
    }

    private void handlePrimaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());

        points.add(clickPoint);

        redrawCanvas();

        if (points.size() >= 2) {
            drawBezierCurve(graphicsContext);
        }
    }

    private void handleSecondaryClick() {
        points.clear();
        redrawCanvas();
    }

    private void redrawCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawPointsAndLines(gc);

        if (points.size() >= 2) {
            drawBezierCurve(gc);
        }
    }

    private void drawPointsAndLines(GraphicsContext gc) {
        final int POINT_RADIUS = 5;

        // рисуем точки и соединяющие линии
        for (int i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);

            gc.setFill(Color.BLUE);
            gc.fillOval(point.getX() - POINT_RADIUS, point.getY() - POINT_RADIUS,
                    2 * POINT_RADIUS, 2 * POINT_RADIUS);

            if (i > 0) {
                Point2D prevPoint = points.get(i - 1);
                gc.setStroke(Color.RED);
                gc.setLineWidth(1);
                gc.strokeLine(prevPoint.getX(), prevPoint.getY(), point.getX(), point.getY());
            }
        }
    }

    private void drawBezierCurve(GraphicsContext gc) {
        if (points.size() < 2) return;

        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);

        List<Point2D> curvePoints = calculateBezierCurve(points, 1000);

        for (int i = 1; i < curvePoints.size(); i++) {
            Point2D p1 = curvePoints.get(i - 1);
            Point2D p2 = curvePoints.get(i);
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
    }

    private List<Point2D> calculateBezierCurve(List<Point2D> controlPoints, int segments) {
        List<Point2D> curvePoints = new ArrayList<>();
        int n = controlPoints.size() - 1;

        for (int i = 0; i <= segments; i++) {
            double t = (double) i / segments;
            Point2D point = calculateBezierPointCasteljo(controlPoints, t, n);
            // Point2D point = calculateBezierPointBernstein(controlPoints, t);
            curvePoints.add(point);
        }

        return curvePoints;
    }

    // Кастельжо
    private Point2D calculateBezierPointCasteljo(List<Point2D> points, double t, int n) {
        if (n == 0) {
            return points.getFirst();
        }

        Point2D[] temp = new Point2D[n + 1];
        for (int i = 0; i <= n; i++) {
            temp[i] = points.get(i);
        }

        for (int k = 1; k <= n; k++) {
            for (int i = 0; i <= n - k; i++) {
                double x = (1 - t) * temp[i].getX() + t * temp[i + 1].getX();
                double y = (1 - t) * temp[i].getY() + t * temp[i + 1].getY();
                temp[i] = new Point2D(x, y);
            }
        }

        return temp[0];
    }

    // Бернштейн
    private Point2D calculateBezierPointBernstein(List<Point2D> points, double t) {
        int n = points.size() - 1;
        double x = 0;
        double y = 0;

        for (int i = 0; i <= n; i++) {
            double binomial = binomialCoefficient(n, i);
            double bernstein = binomial * Math.pow(t, i) * Math.pow(1 - t, n - i);

            x += bernstein * points.get(i).getX();
            y += bernstein * points.get(i).getY();
        }

        return new Point2D(x, y);
    }

    private double binomialCoefficient(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1;

        double result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - k + i) / i;
        }
        return result;
    }
}