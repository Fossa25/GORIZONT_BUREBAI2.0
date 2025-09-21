package com.example.proburok;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;


public class ProhodCOD extends Configs{
    @FXML private DatePicker data;
    @FXML private ComboBox<String> sehenbox;
    @FXML private TextField gorizont;
    @FXML private TextField sehen;
    @FXML private TextField nameProhod;

    @FXML private TextField nomer;
    @FXML private TextField privizka;

    @FXML private TextField idi;
    @FXML private Button singUpButtun;
    private String NOMER;
    private Date DATA;
    private String SEHEN;
    private String GORIZONT;
    private String NAME;
    private String PRIVIZKA;
    private String HIFR;
    @FXML private TableView<Probnik> Tablshen;

    @FXML private TableView<Probnik1> Tablshen1;

    @FXML private TableColumn<Probnik, String> stolb1;
    @FXML private TableColumn<Probnik, Double> stolb2;
    @FXML private TableColumn<Probnik, Double> stolb3;

    @FXML private TableColumn<Probnik1, String> stolb11;
    @FXML private TableColumn<Probnik1, String> stolb21;
    @FXML private TableColumn<Probnik, Double> stolb31;

    @FXML private ImageView instr;
    @FXML private ImageView dokumGeolog;
    @FXML private ImageView dokumGeolog11;
    @FXML private ImageView pethat;
    @FXML private ImageView tabl;
    @FXML private ImageView othet;
    @FXML private ComboBox<String> ushatok;

    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;
    @FXML private ImageView ProdolVKL;
    @FXML private ImageView ProdolVKLNe;
    @FXML private ImageView planVNS;
    @FXML private ImageView planVNSNE;
    @FXML private ImageView planobnov;
    @FXML private ImageView poperVNS;
    @FXML private ImageView poperVNSNE;
    @FXML private ImageView poperobnov;
    @FXML private ImageView prodolVNS;
    @FXML private ImageView prodolVNSNE;
    @FXML private ImageView prodolobnov;
    @FXML
    void initialize() {
        setupTooltips(); // Добавляем подсказки
        setupCursor();   // Настраиваем курсор
        setupImageHandlers();

        idi.setVisible(false);
        tabltrabl();
        data.setValue(LocalDate.now());
        pethat.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/Pehat.fxml"));
        tabl.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/app.fxml"));

        dokumGeolog.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_geolog,"Геология Октябрьского месторождения_");
        });
        dokumGeolog11.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_albom,"АЛЬБОМ ТИПОВЫХ ПАСПОРТОВ КРЕПЛЕНИЯ_");
        });
        instr.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_instr,"Инструкция_");
        });
        othet.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_othet,"Отчет_");
        });


        ushatok.getItems().addAll("1 участок", "2 участок", "Участок ГКР ", "Участок КГВ");

        ushatok.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && data.getValue() != null) {
                String year = String.valueOf(data.getValue().getYear());
                String prefix = "";
                if (newValue.equals("1 участок")) prefix = "1";
                else if (newValue.equals("2 участок")) prefix = "2";
                else if (newValue.equals("Участок ГКР ")) prefix = "ГКР";
                else if (newValue.equals("Участок КГВ")) prefix = "КГВ";

                // Получаем следующий порядковый номер из БД
                DatabaseHandler dbHandler = new DatabaseHandler();
                int nextNumber = dbHandler.getNextSequenceNumber(prefix, year);

                // Устанавливаем номер в формате "1-5-2025"
                nomer.setText(prefix + "-" + nextNumber + "-" + year);
                proverkaImage(Put + "/" + nomer.getText() + "/" + "План",planVNS,planVNSNE, PlanVKL,PlanVKLNe,planobnov);
                proverkaImage(Put + "/" + nomer.getText() + "/" + "Поперечный",poperVNS,poperVNSNE,PoperVKL,PoperVKLNe,poperobnov);
                proverkaImage(Put + "/" + nomer.getText() + "/" + "Продольный",prodolVNS,prodolVNSNE,ProdolVKL,ProdolVKLNe,prodolobnov);
            }
        });
        singUpButtun.setOnMouseClicked(mouseEvent -> {
            proverkaImage(Put + "/" + nomer.getText() + "/" + "План",planVNS,planVNSNE, PlanVKL,PlanVKLNe,planobnov);
            proverkaImage(Put + "/" + nomer.getText() + "/" + "Поперечный",poperVNS,poperVNSNE,PoperVKL,PoperVKLNe,poperobnov);
            proverkaImage(Put + "/" + nomer.getText() + "/" + "Продольный",prodolVNS,prodolVNSNE,ProdolVKL,ProdolVKLNe,prodolobnov);

            NOMER = nomer.getText() != null ? nomer.getText().trim() : "";
            SEHEN = sehen.getText() != null ? sehen.getText().trim() : "";
            GORIZONT = gorizont.getText() != null ? gorizont.getText().trim() : "";
            PRIVIZKA = privizka.getText() != null ? privizka.getText().trim() : "";
            NAME = nameProhod.getText() != null ? nameProhod.getText().trim() : "";
            HIFR = idi.getText() != null ? idi.getText().trim() : "";
            LocalDate selectedDate = data.getValue();
            try {
                StringBuilder errors = new StringBuilder();
                if (NOMER.isEmpty()) errors.append("- Не заполнен номер\n");
                if (selectedDate == null) {
                    errors.append("- Не выбрана дата\n");
                } else {
                    DATA = Date.valueOf(selectedDate); // Преобразуем только если дата выбрана
                }
                if (SEHEN.isEmpty()) errors.append("- Не выбрано сечение\n");
                if (GORIZONT.isEmpty()) errors.append("- Не заполнен горизонт\n");
                if (PRIVIZKA.isEmpty()) errors.append("- Не заполнена привязка\n");
                if (NAME.isEmpty()) errors.append("- Не заполнено название выработки\n");

                if (planVNS.isVisible() || planVNSNE.isVisible() ) {errors.append("- Не внесён план\n");}
               // if (poperVNS.isVisible() || poperVNSNE.isVisible() ) {errors.append("- Не внесён поперечный разрез\n");}
               // if (prodolVNS.isVisible() || prodolVNSNE.isVisible() ) {errors.append("- Не внесён продольный разрез\n");}
                if (errors.length() > 0) {
                    showAlert("Заполните обязательные поля:\n" + errors);
                    return;
                }
                String nameBD = NAME + " № " + NOMER;
                String prim =  "Требуется геологическое описание" ;

                // Все данные валидны - сохраняем
                DatabaseHandler Tabl = new DatabaseHandler();
                Tabl.Dobavlenie(NOMER, DATA, SEHEN, GORIZONT, PRIVIZKA, nameBD,NAME,HIFR,ushatok.getValue(),prim);
                ohistka();

            } catch (DateTimeException e) {
                showAlert("Ошибка в формате даты!");
            } catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });
    }
    private void ohistka() {
        ushatok.setValue("");
        nomer.setText("");
        sehen.setText("");
        gorizont.setText("");
        privizka.setText("");
        nameProhod.setText("");
        idi.setText("");
        PlanVKL.setVisible(false);PlanVKLNe.setVisible(true);planobnov.setVisible(false);
        planVNS.setVisible(false); planVNSNE.setVisible(true);
        PoperVKL.setVisible(false);PoperVKLNe.setVisible(true);poperobnov.setVisible(false);
        poperVNS.setVisible(false); poperVNSNE.setVisible(true);
        ProdolVKL.setVisible(false);ProdolVKLNe.setVisible(true);prodolobnov.setVisible(false);
        prodolVNS.setVisible(false); prodolVNSNE.setVisible(true);
    }
    private void tabltrabl(){
        stolb1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVirobotka()));
        stolb2.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPlohad()).asObject());
        stolb3.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getId()).asObject());
        // Для второй таблицы
        stolb11.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVirobotka1()));
        stolb21.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNety()));
        stolb31.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getId()).asObject());

        ObservableList<Probnik> tabl = FXCollections.observableArrayList(
                new Probnik("Выработка откаточная (двупутевая)", 13.8,1),
                new Probnik("Камера ЛС", 12.1,7),
                new Probnik("Выработка отрезной панели", 12.0,9),
                new Probnik("Выработка отрезной панели", 10.9,13),
                new Probnik("Выработка откаточная (однопутевая)", 10.6,2),
                new Probnik("Выработка буровая", 9.0,8),
                new Probnik("Выработка буровая", 8.4,12),
                new Probnik("Выработка разведочная" , 6.8,6),
                new Probnik("Выработка скреперования, вентиляционная", 5.0,3),
                new Probnik("Сбойка, ходок, ниша воронки", 4.0,4),
                new Probnik("Выработка скреперования (выпуск рудной массы)", 4.0,5),
                new Probnik("Вентиляционный восстающий, рудоспуск, отрезной восстающий", 4.0,10),
                new Probnik("Вентиляционно-ходовой восстающий", 4.0,11)
        );
        ObservableList<Probnik1> tabl1 = FXCollections.observableArrayList(
                new Probnik1 ("Скреперные и погрузочные выработки, ходки, сбойки (Т-образное)","5,0/5,0",14),
                new Probnik1 ("Скреперные (погрузочные) выработки,ниши выпускных воронок, ниши ВХВ (Т-образное)","5,0/4,0",15),
                new Probnik1 ("Откаточные выработки / откаточные выработки (У-образное,правостороннее)","10,6/10,6",16),
                new Probnik1 ("Откаточные выработки / откаточные выработки (У-образное,левостороннее)","10,6/10,6",17)
        );

        stolb1.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
            }
        });
        stolb2.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(item));
                setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
            }
        });
        stolb11.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
            }
        });
        stolb21.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
            }
        });
        Tablshen.setItems(tabl);
        Tablshen1.setItems(tabl1);

        Tablshen.setOnMouseClicked(mouseEvent -> {
            Probnik selectedPerson = Tablshen.getSelectionModel().getSelectedItem();
            if (selectedPerson != null) {
                // Отображаем значение из первого столбца ("Имя") в TextField
                sehen.setText(String.valueOf(selectedPerson.getPlohad()));
                idi.setText(String.valueOf(selectedPerson.getId()));
            }
        });
        Tablshen1.setOnMouseClicked(mouseEvent -> {
            Probnik1 selectedPerson = Tablshen1.getSelectionModel().getSelectedItem();
            if (selectedPerson != null) {
                // Отображаем значение из первого столбца ("Имя") в TextField
                sehen.setText(String.valueOf(selectedPerson.getNety()));
                idi.setText(String.valueOf(selectedPerson.getId()));
            }
        });
    }
    private void setupImageHandlers() {
        openImageHandler(PlanVKL, "План",PlanVKLNe);
        openImageHandler(PoperVKL, "Поперечный",PoperVKLNe);
        openImageHandler(ProdolVKL, "Продольный",ProdolVKLNe);

        obnovaImageHandler(planobnov, "План",planobnov);
        obnovaImageHandler(poperobnov, "Поперечный",planobnov);
        obnovaImageHandler(prodolobnov, "Продольный",planobnov);

        sozdaniiImageHandler(planVNS, "План",PlanVKL,PlanVKLNe,planVNS,planobnov,planobnov);
        sozdaniiImageHandler(poperVNS, "Поперечный",PoperVKL,PoperVKLNe,poperVNS,poperobnov,planobnov);
        sozdaniiImageHandler(prodolVNS, "Продольный",ProdolVKL,ProdolVKLNe,prodolVNS,prodolobnov,planobnov);
    }

    protected void openImageHandler(ImageView imageView, String folder, ImageView neimage) {
        imageView.setOnMouseClicked(e -> {
            if (nomer.getText().isEmpty()) return;
            openImage(Put + "/" + nomer.getText() + "/" + folder,imageView,neimage,"yes");
        });
    }
    protected void obnovaImageHandler(ImageView imageView, String folder,ImageView im) {
        imageView.setOnMouseClicked(e -> {
            if (nomer.getText().isEmpty()) return;
            Sosdatpapky(nomer.getText(),folder,im);
        });
    }
    protected void sozdaniiImageHandler(ImageView imageView, String folder, ImageView image1, ImageView image2, ImageView image3, ImageView image4,ImageView im) {
        imageView.setOnMouseClicked(e -> {
            if (nomer.getText().isEmpty()) return;
            Sosdatpapky(nomer.getText(),folder,im);
            image1.setVisible(true); image2.setVisible(false);
            image3.setVisible(false); image4.setVisible(true);
        });}
    public void Sosdatpapky(String gor, String papka,ImageView pl){

        String path = Put + "/" + gor + "/" + papka;
        File newFolder = new File(path);

        // Создаем папку, если её нет
        if (!newFolder.exists()) {
            newFolder.mkdirs();
        }

        // Удаляем все существующие файлы в папке
        File[] existingFiles = newFolder.listFiles();
        if (existingFiles != null) {
            for (File file : existingFiles) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }
        // Диалог выбора файла
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Window mainWindow = pl.getScene().getWindow(); // Получаем текущее окно
        File selectedFile = fileChooser.showOpenDialog(mainWindow);

        if (selectedFile != null) {
            try {
                File destinationFile = new File(newFolder.getAbsolutePath() + "/" + selectedFile.getName());
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Файл скопирован: " + destinationFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Ошибка при копировании файла: " + e.getMessage());
            }
        }
    }



    protected void setupTooltips() {
        Tooltip.install(planVNS, createTooltip("Внести изображение плана"));
        Tooltip.install(PlanVKL, createTooltip("Показать план"));
        Tooltip.install(PlanVKLNe, createTooltip("План не внесён"));
        Tooltip.install(planobnov, createTooltip("Обновить изображение плана"));

        Tooltip.install(poperVNS, createTooltip("Внести изображение поперечного разреза"));
        Tooltip.install(PoperVKL, createTooltip("Показать поперечный разрез"));
        Tooltip.install(PoperVKLNe, createTooltip("Поперечный разрез не внесён"));
        Tooltip.install(poperobnov, createTooltip("Обновить изображение поперечного разреза"));

        Tooltip.install(prodolVNS, createTooltip("Внести изображение продольного разреза"));
        Tooltip.install(ProdolVKL, createTooltip("Показать продольный разрез"));
        Tooltip.install(ProdolVKLNe, createTooltip("Продольный разрез не внесён"));
        Tooltip.install(prodolobnov, createTooltip("Обновить изображение продольного разреза"));

    }
    protected Tooltip createTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.millis(300));
        tooltip.setStyle("-fx-font-size: 14; -fx-background-color: #aa9455;");
        return tooltip;
    }
    protected void setupCursor() {
        String hoverStyle = "-fx-cursor: hand;";//рука
        planVNS.setStyle(hoverStyle);
        PlanVKL.setStyle(hoverStyle);
        planVNSNE.setStyle(hoverStyle);
        PlanVKLNe.setStyle(hoverStyle);
        planobnov.setStyle(hoverStyle);

        poperVNS.setStyle(hoverStyle);
        PoperVKL.setStyle(hoverStyle);
        poperVNSNE.setStyle(hoverStyle);
        PoperVKLNe.setStyle(hoverStyle);
        poperobnov.setStyle(hoverStyle);

        prodolVNS.setStyle(hoverStyle);
        ProdolVKL.setStyle(hoverStyle);
        prodolVNSNE.setStyle(hoverStyle);
        ProdolVKLNe.setStyle(hoverStyle);
        prodolobnov.setStyle(hoverStyle);
    }


}






