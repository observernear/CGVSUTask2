package main.cgvsu.com.controller;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
            switch (event.getButton()) {
                case PRIMARY -> handlePrimaryClick(event);
                case SECONDARY -> handleSecondaryClick();
            }
        });
    }

    private void handlePrimaryClick(MouseEvent event) {
        Point2D clickPoint = new Point2D(event.getX(), event.getY());
        pointManager.addPoint(clickPoint);
        redrawCanvas();
    }

    private void handleSecondaryClick() {
        pointManager.clearPoints();
        redrawCanvas();
    }

    private void redrawCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        curveRenderer.clearCanvas(gc, canvas.getWidth(), canvas.getHeight());

        List<Point2D> points = pointManager.getPoints();

        curveRenderer.drawControlPoints(gc, points);
        curveRenderer.drawControlPolygon(gc, points);

        if (pointManager.hasEnoughPointsForCurve()) {
            List<Point2D> curvePoints = curveCalculator.calculateCurve(points, 1000);
            curveRenderer.drawCurve(gc, curvePoints);
        }
    }
}