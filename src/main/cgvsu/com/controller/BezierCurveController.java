package main.cgvsu.com.controller;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import main.cgvsu.com.model.BezierCurve2D;
import main.cgvsu.com.model.CurveCalculator;
import main.cgvsu.com.model.Point2DManager;
import main.cgvsu.com.model.PointManager;
import main.cgvsu.com.view.CurveViewer;
import main.cgvsu.com.view.Point2DViewer;

import java.util.List;

public class BezierCurveController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    private final PointManager<Point2D> pointManager = new Point2DManager();
    private final CurveCalculator<Point2D> curveCalculator = new BezierCurve2D();
    private final CurveViewer<Point2D> curveRenderer = new Point2DViewer();

    private boolean isDragging = false;
    private int draggedPointIndex = -1;
    private int lastDeletePointIndex = -1;
    private static final double DRAG_RADIUS = 10.0;

    @FXML
    private void initialize() {
        setupCanvasResizeListeners();
        setupMouseHandlers();
    }

    private void setupCanvasResizeListeners() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> {
            canvas.setWidth(newValue.doubleValue());
            redrawCanvas();
        });

        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> {
            canvas.setHeight(newValue.doubleValue());
            redrawCanvas();
        });
    }

    private void setupMouseHandlers() {
        canvas.setOnMouseClicked(event -> {
            if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                handlePrimaryClick(event);
            } else if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                handleSecondaryClick(event);
            }
        });

        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);

        // Добавляем обработчик движения мыши для смены курсора
        canvas.setOnMouseMoved(this::handleMouseMoved);
    }

    private void handleMouseMoved(MouseEvent event) {
        Point2D mousePoint = new Point2D(event.getX(), event.getY());
        int nearestIndex = pointManager.findNearestPointIndex(mousePoint, DRAG_RADIUS);

        if (nearestIndex != -1 && !isDragging) {
            canvas.setCursor(Cursor.HAND);
        } else {
            canvas.setCursor(Cursor.DEFAULT);
        }
    }

    private void handlePrimaryClick(MouseEvent event) {
        Point2D clickPoint = new Point2D(event.getX(), event.getY());

        // Добавляем точку только если не кликнули рядом с существующей
        if (pointManager.findNearestPoint(clickPoint, DRAG_RADIUS) == null) {
            pointManager.addPoint(clickPoint);
            redrawCanvas();
        }
    }

    private void handleSecondaryClick(MouseEvent event) {
        Point2D clickPoint = new Point2D(event.getX(), event.getY());
        int nearestIndex = pointManager.findNearestPointIndex(clickPoint, DRAG_RADIUS);

        if (nearestIndex != -1) {
            // Удаляем ближайшую точку
            pointManager.removePoint(nearestIndex);
            lastDeletePointIndex = nearestIndex;
            resetDragging();
        } else {
            // Если нет точек рядом - очищаем все
            pointManager.clearPoints();
            resetDragging();
        }
        redrawCanvas();
    }

    private void handleMousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            Point2D clickPoint = new Point2D(event.getX(), event.getY());
            draggedPointIndex = pointManager.findNearestPointIndex(clickPoint, DRAG_RADIUS);

            if (draggedPointIndex != -1) {
                isDragging = true;
                canvas.setCursor(Cursor.CLOSED_HAND);
            }
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (isDragging && draggedPointIndex != -1) {
            Point2D newPosition = new Point2D(event.getX(), event.getY());

            // Обновляем позицию точки
            pointManager.updatePoint(draggedPointIndex, newPosition);

            redrawCanvas();
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        resetDragging();
    }

    private void resetDragging() {
        isDragging = false;
        draggedPointIndex = -1;
        canvas.setCursor(Cursor.DEFAULT);
    }

    private void redrawCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        curveRenderer.clearCanvas(gc, canvas.getWidth(), canvas.getHeight());

        List<Point2D> points = pointManager.getPoints();

        // Рисуем кривую Безье если достаточно точек
        if (pointManager.hasEnoughPointsForCurve()) {
            List<Point2D> curvePoints = curveCalculator.calculateCurve(points, 1000);
            curveRenderer.drawCurve(gc, curvePoints);
        }

        curveRenderer.drawControlPolygon(gc, points);

        drawPointsWithHighlight(gc, points);

        drawDebugInfo(gc);
    }

    private void drawPointsWithHighlight(GraphicsContext gc, List<Point2D> points) {
        for (int i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);

            if (isDragging && i == draggedPointIndex) {
                // Перетаскиваемая точка - красная
                drawPoint(gc, point, Color.RED, Color.DARKRED, 7);
            } else {
                // Обычные точки - синие
                drawPoint(gc, point, Color.BLUE, Color.DARKBLUE, 5);
            }
        }
    }

    private void drawPoint(GraphicsContext gc, Point2D point, Color fillColor, Color strokeColor, double radius) {
        gc.setFill(fillColor);
        gc.fillOval(point.getX() - radius, point.getY() - radius, radius * 2, radius * 2);

        gc.setStroke(strokeColor);
        gc.setLineWidth(1);
        gc.strokeOval(point.getX() - radius, point.getY() - radius, radius * 2, radius * 2);
    }

    private void drawDebugInfo(GraphicsContext gc) {
        // Отладочная информация о количестве точек
        gc.setFill(Color.BLACK);
        gc.fillText("Точек: " + pointManager.getPointCount(), 10, 20);

        if (isDragging) {
            gc.fillText("Перетаскивание точки: " + draggedPointIndex, 10, 40);
        } if (lastDeletePointIndex != -1) {
            gc.fillText("Удалена точка: " + lastDeletePointIndex, 10, 60);
        }
    }
}