package com.example.proburok;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Configs {
    //protected String dbHost = "10.38.1.214";
    protected String dbHost = ConfigLoader.getProperty("dbHost");
    protected String dbPort = "3306";
    protected String dbUser = "TOPA";
    protected String dbPass = "300122";
    protected String dbName = "gorizont";

    protected String OUTPUT_PATH = ConfigLoader.getProperty("OUTPUT_PATH");
    protected String Put =  ConfigLoader.getProperty("Put");

    protected String Put_albom ="/com/example/proburok/docs/AlbomBurebai.pdf";
    protected String Put_geolog ="/com/example/proburok/docs/GeologiBurebai.pdf";
    protected String Put_instr ="/com/example/proburok/docs/instruk.pdf";
    protected String Put_othet ="/com/example/proburok/docs/Othet.pdf";

    protected String TEMPLATE_PATH1 = "/com/example/proburok/docs/template_1.docx";
    protected String TEMPLATE_PATH2 = "/com/example/proburok/docs/template_2.docx";
    protected String TEMPLATE_PATH3 = "/com/example/proburok/docs/template_3.docx";
    protected String TEMPLATE_PATH4 = "/com/example/proburok/docs/template_4.docx";
    protected String TEMPLATE_PATH4_4344 = "/com/example/proburok/docs/template_4_4344.docx";
    protected String TEMPLATE_PATH5 = "/com/example/proburok/docs/template_5.docx";
    protected String TEMPLATE_PATH6 = "/com/example/proburok/docs/template_6.docx";
    protected String TEMPLATE_PATH7 = "/com/example/proburok/docs/template_7.docx";
    protected String TEMPLATE_PATH8 = "/com/example/proburok/docs/template_8.docx";
    protected String TEMPLATE_PATH9 = "/com/example/proburok/docs/template_9.docx";
    protected String TEMPLATE_PATH10 = "/com/example/proburok/docs/template_10.docx";
    protected String TEMPLATE_PATH11 = "/com/example/proburok/docs/template_11.docx";
    protected String TEMPLATE_PATH12 = "/com/example/proburok/docs/template_12.docx";
    protected String TEMPLATE_PATH13 = "/com/example/proburok/docs/template_13.docx";
    protected String TEMPLATE_PATH14 = "/com/example/proburok/docs/template_14.docx";
    protected String TEMPLATE_PATH15 = "/com/example/proburok/docs/template_15.docx";
    protected String TEMPLATE_PATH16 = "/com/example/proburok/docs/template_16.docx";


    protected String TEMPLATE_PATH1_SOPR = "/com/example/proburok/docs/template_1S.docx";

    protected String TEMPLATE_PATH3_SOPR = "/com/example/proburok/docs/template_3S.docx";

    protected String TEMPLATE_PATH5_SOPR = "/com/example/proburok/docs/template_5S.docx";

    protected String TEMPLATE_PATH7_SOPR = "/com/example/proburok/docs/template_7S.docx";



protected String HABLON_PATH = "/com/example/proburok/hablon";

protected String HABLON_PATH_VID =HABLON_PATH+"/obvid";
    protected String HABLON_PATH_SOPR =HABLON_PATH+"/soprigenii";

protected String HABLON_PATH_ILIMENT =HABLON_PATH+"/ilement";

protected String HABLON_PATH_USTANOVKA =HABLON_PATH+"/ustanovka";
protected String HABLON_PATH_USTANOVKA_SOPR =HABLON_PATH+"/ustanovka_sopr";

public void openNewScene(String Window){

        // Загрузка нового окна
        FXMLLoader loader = new FXMLLoader();

        // Проверка пути к FXML-файлу
        URL fxmlUrl = getClass().getResource(Window);

        loader.setLocation(fxmlUrl);
        try {
            Parent root = loader.load(); // Загрузка FXML
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void OpenDok(String Put,String NemDok){
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                // Получаем поток входных данных для ресурса
                InputStream inputStream = getClass().getResourceAsStream(Put);
                if (inputStream == null) {
                    throw new FileNotFoundException("Ресурс не найден: " + Put);
                }
                // Создаем временный файл
                Path tempFile = Files.createTempFile(NemDok, ".pdf");
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Открываем временный файл
                desktop.open(tempFile.toFile());
                // Опционально: удаление временного файла после использования
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        Files.deleteIfExists(tempFile);
                    } catch (IOException e) {
                        System.err.println("Не удалось удалить временный файл: " + tempFile);
                    }
                }));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Ошибка при открытии документа", e);
            }
        }
    }
    void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    void openImage(String imagePath, ImageView VKL, ImageView VKLNE, String open) {
        File folder = new File(imagePath);

        // Проверяем, существует ли папка
        if (!folder.exists() || !folder.isDirectory()) {
            VKL.setVisible(false);
            VKLNE.setVisible(true);
            System.err.println("Папка не найдена: " + imagePath);
            return;
        }
        if (open.equals("yes")) {
            // Получаем список файлов в папке
            File[] files = folder.listFiles((dir, name) -> {
                // Фильтруем файлы по расширению (изображения)
                String lowerCaseName = name.toLowerCase();
                return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") ||
                        lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".gif");
            });

            // Проверяем, есть ли изображения в папке
            if (files != null && files.length > 0) {
                // Берем первый файл (первое изображение)
                File imageFile = files[0];

                try {
                    // Открываем изображение
                    Desktop.getDesktop().open(imageFile);
                    System.out.println("Открыто изображение: " + imageFile.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("Не удалось открыть изображение: " + e.getMessage());
                }
            } else {
                System.err.println("В папке нет изображений.");
            }
        }
    }
    void proverkaImage(String imagePath,ImageView VNS,ImageView VNSNE,ImageView VKL,ImageView VKLNE,ImageView OBNV) {
        File folder = new File(imagePath);
        // Проверяем, существует ли папка
        String PutPlan=Put + "//" + "План";
        String PutPoper=Put + "//" +  "Поперечный";
        String PutProdol=Put + "//" + "Продольный";
        String PutSxema=Put + "//" + "Схема";

        if ( PutPlan.equals(imagePath)||PutPoper.equals(imagePath)||PutProdol.equals(imagePath)||PutSxema.equals(imagePath)){

            VNS.setVisible(false); VNSNE.setVisible(true);
            VKL.setVisible(false);VKLNE.setVisible(true);OBNV.setVisible(false);
            return;
        }
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Папка не найдена: " + imagePath);
            VNS.setVisible(true); VNSNE.setVisible(false);
            VKL.setVisible(false);VKLNE.setVisible(true);OBNV.setVisible(false);
            return;
        }
        // Получаем список файлов в папке
        File[] files = folder.listFiles((dir, name) -> {
            // Фильтруем файлы по расширению (изображения)
            String lowerCaseName = name.toLowerCase();
            return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") ||
                    lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".gif");
        });
        // Проверяем, есть ли изображения в папке
        if (files != null && files.length > 0) {

            VNS.setVisible(false); VNSNE.setVisible(false);
            VKL.setVisible(true);VKLNE.setVisible(false);OBNV.setVisible(true);

        } else {
            System.err.println("В папке нет изображений.");
            VNS.setVisible(true); VNSNE.setVisible(false);
            VKL.setVisible(false);VKLNE.setVisible(true);OBNV.setVisible(false);
        }
    }
}


//protected String dbHost = "localhost";
//protected String dbPort = "3306";
//protected String dbUser = "root";
//protected String dbPass = "Kliptomen250598";
//protected String dbName = "icproger";
//protected String Put = "E://Изображения";

//protected String OUTPUT_PATH = "C://Горизонт_Уруп//Паспорта//";
//protected String Put =  "C://Горизонт_Уруп//Изображения";

//protected String Put =  "src//main//resources//com//example//proburok//Изображения";
//protected String TEMPLATE_PATH = "src//main//resources//com//example//proburok//template.docx";
////private static final String TEMPLATE_PATH = "E://ProbUrok//src//main//resources//com//example//proburok//template.docx";
//protected String OUTPUT_PATH = "C://Паспорта//";
//protected String HABLON_PATH = "src//main//resources//com//example//proburok//шаблоны";
//protected String HABLON_PATH_SXEMATEX =HABLON_PATH+"/горно-техфакт";
//protected String HABLON_PATH_VID =HABLON_PATH+"/общий вид крепи";
//protected String HABLON_PATH_ILIMENT =HABLON_PATH+"/элементы крепи";