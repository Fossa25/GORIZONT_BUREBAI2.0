package com.example.proburok;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

import java.io.IOException;

public class KrepController {

    @FXML
    private TextField widthField;

    @FXML
    private TextField heightField;

    @FXML
    private Pane krepContainer;

    @FXML
    private Label infoLabel;

    @FXML
    private Button drawButton;

    private Canvas canvas;
    private GraphicsContext gc;

    // Текущие размеры выработки
    private double currentWidth = 0;
    private double currentHeight = 0;

    @FXML
    public void initialize() {
        System.out.println("Инициализация контроллера крепи...");

        // Создаем Canvas для рисования
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();

        // Добавляем Canvas в контейнер
        krepContainer.getChildren().add(canvas);

        // Привязываем размеры Canvas к размерам контейнера
        canvas.widthProperty().bind(krepContainer.widthProperty());
        canvas.heightProperty().bind(krepContainer.heightProperty());

        // Добавляем слушатели изменений размеров
        krepContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (currentWidth > 0 && currentHeight > 0) {
                drawKrep(currentWidth, currentHeight);
            }
        });

        krepContainer.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (currentWidth > 0 && currentHeight > 0) {
                drawKrep(currentWidth, currentHeight);
            }
        });

        // Устанавливаем начальные значения
        widthField.setText("2.0");
        heightField.setText("2.0");

        // Очищаем canvas при инициализации
        clearCanvas();
    }

    @FXML
    private void drawKrepAction() {
        try {
            double width = Double.parseDouble(widthField.getText());
            double height = Double.parseDouble(heightField.getText());

            if (width <= 0 || height <= 0) {
                infoLabel.setText("Ошибка: значения должны быть больше 0");
                return;
            }

            if (width > 8 || height > 8) {
                infoLabel.setText("Ошибка: значения не должны превышать 8 метров");
                return;
            }

            currentWidth = width;
            currentHeight = height;

            drawKrep(width, height);
            infoLabel.setText("Крепь нарисована. Ширина: " + width + " м, Высота: " + height + " м");

        } catch (NumberFormatException e) {
            infoLabel.setText("Ошибка: введите числовые значения");
        }
    }

    @FXML
    private void clearCanvas() {
        clearCanvasArea();
        infoLabel.setText("Введите параметры выработки и нажмите 'Нарисовать крепь'");
        currentWidth = 0;
        currentHeight = 0;
    }

    private void clearCanvasArea() {
        if (gc != null) {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            // Рисуем белый фон
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    private void drawKrep(double width, double height) {
        if (gc == null) {
            System.err.println("Графический контекст не инициализирован!");
            return;
        }

        // Очищаем Canvas
        clearCanvasArea();
        double PIXELS_PER_METER ;
        // Рассчитываем размеры в пикселях с фиксированным масштабом
        // Масштаб: 100 пикселей на 1 метр
        if (height <=4){
         PIXELS_PER_METER = 100.0;}
        else {PIXELS_PER_METER = 70.0;}

        double widthPixels = width * PIXELS_PER_METER;
        double heightPixels = height * PIXELS_PER_METER;

        // Рассчитываем смещение для центрирования
        double offsetX = (canvas.getWidth() - widthPixels) / 2;
        double offsetY = (canvas.getHeight() - heightPixels) / 2;

        // Рисуем основную прямоугольную выработку
        drawMainTunnel(offsetX, offsetY, widthPixels, heightPixels);

        // Добавляем текстовые метки с размерами
        drawDimensionLabels(offsetX, offsetY, widthPixels, heightPixels, width, height);
    }

    // Рисование основной прямоугольной выработки
    private void drawMainTunnel(double offsetX, double offsetY, double widthPixels, double heightPixels) {
        double hr = widthPixels / 3.0;
        double alfaR = Math.atan((2.0 * hr) / widthPixels);
        double alfa = Math.toDegrees(alfaR);
        double beta = 90 - alfa;

        double sinA = Math.sin(alfaR);
        double cosA = Math.cos(alfaR);
        double sin2A = Math.sin(alfaR * 2);

        double r = (widthPixels * (((2.0/3.0) * sinA) + cosA - 1)) / (2 * (sinA + cosA - 1));
        double R = (widthPixels * ((1.0/3.0) - (cosA/(2*(1-sinA))))) / (1 - cosA - (sin2A/(2*(1-sinA))));

        // Установка цвета линии
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        // Рисуем стены и пол
        gc.strokeLine(offsetX, offsetY + hr, offsetX, offsetY + heightPixels);
        gc.strokeLine(offsetX + widthPixels, offsetY + hr, offsetX + widthPixels, offsetY + heightPixels);
        gc.strokeLine(offsetX, offsetY + heightPixels, offsetX + widthPixels, offsetY + heightPixels);

        // Рисуем дуги
        gc.strokeArc(offsetX, offsetY + hr - r, r * 2, r * 2, 180 - beta, beta, ArcType.OPEN);
        gc.strokeArc(offsetX + widthPixels - r * 2, offsetY + hr - r, r * 2, r * 2, 0, beta, ArcType.OPEN);
        gc.strokeArc(offsetX + (widthPixels/2) - R, offsetY, R * 2, R * 2, beta, 180 - 2 * beta, ArcType.OPEN);
    }

    // Добавление текстовых меток с размерами
    private void drawDimensionLabels(double offsetX, double offsetY, double widthPixels, double heightPixels, double width, double height) {
        gc.setFill(Color.BLACK);

        // Подпись ширины
        String widthText = String.format("Ширина: %.1f м", width);
        gc.fillText(widthText, offsetX + 10, offsetY - 15);

        // Подпись высоты
        String heightText = String.format("Высота: %.1f м", height);
        gc.fillText(heightText, offsetX - 70, offsetY + 25);

        // Стрелки для обозначения размеров
        drawDimensionArrow(offsetX, offsetY - 10, offsetX + widthPixels, offsetY - 10, true); // Ширина
        drawDimensionArrow(offsetX - 10, offsetY, offsetX - 10, offsetY + heightPixels, false); // Высота
    }

    // Рисование стрелок для обозначения размеров
    private void drawDimensionArrow(double startX, double startY, double endX, double endY, boolean isHorizontal) {
        gc.setStroke(Color.DARKBLUE);
        gc.setLineWidth(1);

        // Линия размера
        gc.strokeLine(startX, startY, endX, endY);

        // Стрелки на концах
        double arrowSize = 5;
        if (isHorizontal) {
            // Горизонтальная линия - вертикальные стрелки
            gc.strokeLine(startX, startY - arrowSize, startX, startY + arrowSize);
            gc.strokeLine(endX, endY - arrowSize, endX, endY + arrowSize);
        } else {
            // Вертикальная линия - горизонтальные стрелки
            gc.strokeLine(startX - arrowSize, startY, startX + arrowSize, startY);
            gc.strokeLine(endX - arrowSize, endY, endX + arrowSize, endY);
        }
    }

    public static void showKrepEditor() {
        try {
            FXMLLoader loader = new FXMLLoader(KrepController.class.getResource("krep_editor.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Редактор крепи горной выработки");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

        } catch (IOException e) {
            System.err.println("Ошибка загрузки FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}