package com.example.proburok;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PehatCOD extends ProhodCOD {

    private String Photosxemtefat;
    private String Photosxemtefat2;
    private String Photovidkrepi;
    private String Photovidkrepi2;
    private String Photoilement;
    private String Photoilement2;
    private String Photosxemprovet;

    private String ankerPM;
    private String ankerR;
    private String setkaPM;
    private String setkaR;
    private String torkretPM;
    private String torkretR;

    private String ankerPM1;
    private String ankerR1;
    private String ankerPM2;
    private String ankerR2;
    private String ampulaPM;
    private String ampulaR;
    private String ankerPM3;
    private String ankerR3;
    private String ankerPM4;
    private String ankerR4;

    List<String> soprigenii = Arrays.asList("14", "15", "16", "17");
    List<String> obhvid2 = Arrays.asList("19", "31");
    List<String> sxemust2 = Arrays.asList("9", "21","33", "43","10", "22", "34","44",
            "35","45", "36", "46","38", "48","39", "49", "40","50", "41","51","68","70","69","71");

    List<String> rasht_3temp = Arrays.asList( "33","34", "35", "36", "38","39", "40", "41",  "45","46", "48", "49", "50", "51","68","70","69","71");
    List<String> rasht_4344 = Arrays.asList(  "43", "44");
    List<String> rasht_11temp = Arrays.asList( "19","31");
    private static final Map<String, String> PLACEHOLDER_MAP;

    static {
        PLACEHOLDER_MAP = new HashMap<>();
        PLACEHOLDER_MAP.put("${nomer}", "nomer");
        PLACEHOLDER_MAP.put("${name}", "name");
        PLACEHOLDER_MAP.put("${gorizont}", "gorizont");
        PLACEHOLDER_MAP.put("${opisanie}", "opisanie");
        PLACEHOLDER_MAP.put("${kategorii}", "kategorii");
        PLACEHOLDER_MAP.put("${faktor}", "faktor");
        PLACEHOLDER_MAP.put("${plan}", "plan");
        PLACEHOLDER_MAP.put("${poper}", "poper");
        PLACEHOLDER_MAP.put("${prodol}", "prodol");
        PLACEHOLDER_MAP.put("${dlina}", "dlina");
        PLACEHOLDER_MAP.put("${tipovoi}","tipovoi");
        PLACEHOLDER_MAP.put("${sxematexfakt}", "sxematexfakt");
        PLACEHOLDER_MAP.put("${obvid}", "obvid");
        PLACEHOLDER_MAP.put("${konstrk}", "konstrk");
        PLACEHOLDER_MAP.put("${obvid2}", "obvid2");
        PLACEHOLDER_MAP.put("${sxematexfakt2}", "sxematexfakt2");
        PLACEHOLDER_MAP.put("${konstrk2}", "konstrk2");
        PLACEHOLDER_MAP.put("${sxema}", "sxema");

    }
    @FXML private ImageView instr;

    @FXML private TextField cehen;
    @FXML private TextField bdname;
    @FXML private ComboBox<String> gorbox;
    @FXML private TextField katigoria;
    @FXML private ComboBox<String> namebox;
    @FXML private TextField nomer;
    @FXML private TextField nomer1;
    @FXML private TextArea opisanie;
    @FXML private Button singUpButtun;
    @FXML private TextField texfak;
    @FXML private TextField idi;
  //  @FXML private ComboBox<String> krepbox;
    @FXML  private Label krep;
    @FXML private TextField dlina;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    @FXML
    private TextArea primhanie;
    @FXML
    private CheckBox cb;
    @FXML private Button singUpButtun1;
    @FXML private ImageView sxemaVKL;
    @FXML private ImageView sxemaVKLNe;
    @FXML private ImageView sxemaVNS;
    @FXML private ImageView sxemaVNSNE;
    @FXML private ImageView sxemaobnov;

    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView planVNS;
    @FXML private ImageView planVNSNE;
    @FXML private ImageView planobnov;

    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;
    @FXML private ImageView poperVNS;
    @FXML private ImageView poperVNSNE;
    @FXML private ImageView poperobnov;
    @FXML private ImageView ProdolVKL;
    @FXML private ImageView ProdolVKLNe;
    @FXML private ImageView prodolVNS;
    @FXML private ImageView prodolVNSNE;
    @FXML private ImageView prodolobnov;
    @FXML
    void initialize() {
        primhanie.visibleProperty().bind(cb.selectedProperty());
        singUpButtun1.visibleProperty().bind(cb.selectedProperty());

        singUpButtun1.setOnMouseClicked(mouseEvent -> {
            String selectedGor = gorbox.getValue();
            String selectedName = namebox.getValue();

            try {
                // Проверка полей по очереди
                StringBuilder errors = new StringBuilder();
                if (selectedGor == null || selectedGor.isEmpty()) {errors.append("- Не выбран горизонт\n");}
                if (selectedName == null || selectedName.isEmpty()) {errors.append("- Не выбрано название выработки\n");}

                // Если есть ошибки - показываем их
                if (errors.length() > 0) {showAlert("Заполните обязательные поля:\n" + errors.toString());
                    return;
                }

                new DatabaseHandler().DobavleniePRIM(primhanie.getText(), selectedGor, selectedName);
                clearFields();
                //gorbox.setValue(null);
            }  catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }

        });
        setupComboBoxes();
        setupButtonAction();

        setupTooltips(); // Добавляем подсказки
        setupCursor();   // Настраиваем курсор
        setupTooltips_Sxema();
        setupCursor_Sxema();
        setupImageHandlers();
        singUpButtun.setVisible(false);
        instr.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_instr,"Инструкция_");
        });

    }

    private void setupComboBoxes() {
        ObservableList<String> horizons = FXCollections.observableArrayList(
                dbHandler.getAllBaza().stream()
                        .map(Baza::getGORIZONT)
                        .distinct()
                        .collect(Collectors.toList())
        );
        gorbox.setItems(horizons);

        gorbox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateNamesComboBox(newVal);
            }
        });

        namebox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && gorbox.getValue() != null) {
                populateFields(gorbox.getValue(), newVal);

            }
        });

        addTextChangeListener(nomer);
        addTextChangeListener(bdname);
        addTextChangeListener(katigoria);
        addTextChangeListener(cehen);
        addTextChangeListener(nomer1);
        addTextChangeListener(texfak);
        addTextChangeListener(dlina);
    }
    private void addTextChangeListener(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            checkFieldsAndUpdateButton();
        });
    }

    private void checkFieldsAndUpdateButton() {
        boolean allValid = validateRequiredFields() &&
                gorbox.getValue() != null &&
                namebox.getValue() != null;

        singUpButtun.setDisable(!allValid);
    }
    private void updateNamesComboBox(String horizon) {
        ObservableList<String> names = FXCollections.observableArrayList(
                dbHandler.poiskName(horizon).stream()
                        .map(Baza::getNAME)
                        .distinct()
                        .collect(Collectors.toList())
        );
        clearFields();
        namebox.setItems(names);

    }

    private void updateUI(Baza data) {
        nomer.setText(data.getNOMER());
        bdname.setText(data.getNAME_BD());
        katigoria.setText(data.getKATEGORII());
        opisanie.setText(data.getOPISANIE());
        cehen.setText(data.getSEHEN());
        texfak.setText(data.getPRIVIZKA());
        nomer1.setText(data.getTIPPAS());
        dlina.setText(data.getDLINA());
        primhanie.setText(data.getPRIM());
        idi.setText(data.getIDI());
        proverkaImage(Put + "/" + nomer.getText() + "/" + "План",planVNS,planVNSNE, PlanVKL,PlanVKLNe,planobnov);
        proverkaImage(Put + "/" + nomer.getText() + "/" + "Поперечный",poperVNS,poperVNSNE,PoperVKL,PoperVKLNe,poperobnov);
        proverkaImage(Put + "/" + nomer.getText() + "/" + "Продольный",prodolVNS,prodolVNSNE,ProdolVKL,ProdolVKLNe,prodolobnov);
        proverkaImage(Put + "/" + nomer.getText() + "/" + "Схема",sxemaVNS,sxemaVNSNE, sxemaVKL,sxemaVKLNe,sxemaobnov);

        // Проверяем заполненность полей после обновления
        if (!validateRequiredFields()) {
            showAlert("Неполные данные", "Не все данные заполнены для выбранной записи!");
            singUpButtun.setVisible(false);
            checkFieldsAndUpdateButton();
        } else {
            singUpButtun.setVisible(true);
        }
    }
    private boolean validateRequiredFields() {
        return isFieldValid(nomer) &&
                isFieldValid(bdname) &&
                isFieldValid(katigoria) &&
                isFieldValid(cehen) &&
                isFieldValid(nomer1) &&
                isFieldValid(dlina) &&
                isFieldValid(texfak);
    }

    private boolean isFieldValid(TextField field) {
        return field != null && field.getText() != null && !field.getText().trim().isEmpty();
    }

    private void populateFields(String horizon, String name) {
        Baza data = dbHandler.danii(horizon, name);
        if (data != null) {
            updateUI(data);


        } else {
            clearFields();

            singUpButtun.setVisible(false);
        }
    }
    private void clearFields() {
        namebox.setValue(null);
        nomer.clear();
        bdname.clear();
        katigoria.clear();
        opisanie.setText("");
        cehen.clear();
        nomer1.clear();
        texfak.clear();
        dlina.clear();
        primhanie.clear();
        cb.setSelected(false);
        idi.clear();

      this.Photosxemtefat = "";
        this.Photosxemtefat2= "";
        this.Photovidkrepi= "";
        this.Photovidkrepi2= "";
        this.Photoilement= "";
        this.Photoilement2= "";
        this.Photosxemprovet="";

        this.ankerPM= "";
        this.ankerR= "";
        this.setkaPM= "";
        this.setkaR= "";
        this.torkretPM= "";
        this.torkretR= "";

        this.ankerPM1= "";
        this.ankerR1= "";
        this.ankerPM2= "";
        this.ankerR2= "";
        this.ampulaPM= "";
        this.ampulaR= "";
        this.ankerPM3= "";
        this.ankerR3= "";
        this.ankerPM4= "";
        this.ankerR4= "";
        proverkaImage(Put + "/" + nomer.getText() + "/" + "Схема",sxemaVNS,sxemaVNSNE, sxemaVKL,sxemaVKLNe,sxemaobnov);


        proverkaImage(Put + "/" + nomer.getText() + "/" + "План",planVNS,planVNSNE, PlanVKL,PlanVKLNe,planobnov);
        proverkaImage(Put + "/" + nomer.getText() + "/" + "Поперечный",poperVNS,poperVNSNE,PoperVKL,PoperVKLNe,poperobnov);
        proverkaImage(Put + "/" + nomer.getText() + "/" + "Продольный",prodolVNS,prodolVNSNE,ProdolVKL,ProdolVKLNe,prodolobnov);
    }



    private void setupButtonAction() {
        singUpButtun.setOnAction(event -> handleDocumentGeneration());

    }

    private void handleDocumentGeneration() {
        String selectedGor = gorbox.getValue();
        String selectedName = namebox.getValue();
        String kategoriyaValue = katigoria.getText();
        String opisanieValue = opisanie.getText();
        String selectPrivizka = texfak.getText();
        String dlinaValue = dlina.getText();

        proverkaImage(Put + "/" + nomer.getText() + "/" + "Схема",sxemaVNS,sxemaVNSNE, sxemaVKL,sxemaVKLNe,sxemaobnov);


        proverkaImage(Put + "/" + nomer.getText() + "/" + "План",planVNS,planVNSNE, PlanVKL,PlanVKLNe,planobnov);
        proverkaImage(Put + "/" + nomer.getText() + "/" + "Поперечный",poperVNS,poperVNSNE,PoperVKL,PoperVKLNe,poperobnov);
        proverkaImage(Put + "/" + nomer.getText() + "/" + "Продольный",prodolVNS,prodolVNSNE,ProdolVKL,ProdolVKLNe,prodolobnov);
        try {
            // Проверка полей по очереди
            StringBuilder errors = new StringBuilder();
            if (selectedGor == null || selectedGor.isEmpty()) {errors.append("- Не выбран горизонт\n");}
            if (selectedName == null || selectedName.isEmpty()) {errors.append("- Не выбрано название выработки\n");}
            if (selectPrivizka == null || selectPrivizka.isEmpty()) {errors.append("- Не заполнена привязка\n");}
            if (dlinaValue == null || dlinaValue.isEmpty()) {errors.append("- Не заполнена длина выработки\n");}

            if (kategoriyaValue == null || kategoriyaValue.isEmpty()) { errors.append("- Не заполнено поле категория \n"); }
            if (opisanieValue == null || opisanieValue.isEmpty()) {errors.append("- Не заполнено поле описание\n");}

            if (sxemaVNS.isVisible() || sxemaVNSNE.isVisible() ) {errors.append("- Не внесена схема вентиляции\n");}
            if (poperVNS.isVisible() || poperVNSNE.isVisible() ) {errors.append("- Не внесён поперечный разрез\n");}
            if (prodolVNS.isVisible() || prodolVNSNE.isVisible() ) {errors.append("- Не внесён продольный разрез\n");}
            if (planVNS.isVisible() || planVNSNE.isVisible() ) {errors.append("- Не внесён план\n");}
            // Если есть ошибки - показываем их
            if (errors.length() > 0) {showAlert("Заполните обязательные поля:\n" + errors.toString());
                return;
            }
        String vidkripi= nomer1.getText()+".jpg";
        if (soprigenii.contains(idi.getText())) {

            this.Photovidkrepi=getRESURS(HABLON_PATH_SOPR, vidkripi);
            this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA_SOPR, vidkripi);
            this.Photoilement=getIlement_SOPR(nomer1.getText());
            this.Photosxemprovet=getPoto(Put + "/" + nomer.getText() + "/" + "Схема",0);
            if (nomer1.getText().equals("13")){
              this.Photosxemtefat2=getRESURS(HABLON_PATH_USTANOVKA_SOPR, "13_1.jpg");
            }
           else if (nomer1.getText().equals("17")){
                this.Photosxemtefat2=getRESURS(HABLON_PATH_USTANOVKA_SOPR, "17_1.jpg");
            }
        } else {
            this.Photovidkrepi=getRESURS(HABLON_PATH_VID, vidkripi);
            this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, vidkripi);
            getIlement(nomer1.getText());
            this.Photosxemprovet=getPoto(Put + "/" + nomer.getText() + "/" + "Схема",0);

            if (obhvid2.contains(nomer1.getText()))
            { switch (nomer1.getText()) {
                case "19" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "20.jpg"); break;
                case "31" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "32.jpg"); break;
                default : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, null); break;
            }}

            if (sxemust2.contains(nomer1.getText()))
            { String sxema2= nomer1.getText()+"_1.jpg";
                this.Photosxemtefat2=getRESURS(HABLON_PATH_USTANOVKA, sxema2);
            }

        }
        try {
            if (validateInput()) {
                generateWordDocument(
                        nomer.getText(),
                       bdname.getText(),
                        gorbox.getValue(),
                        katigoria.getText(),
                        opisanie.getText(),
                        texfak.getText(),
                        getPoto(Put + "/" + nomer.getText() + "/" + "План",0),
                        getPoto(Put + "/" + nomer.getText() + "/" + "Поперечный",0),
                        getPoto(Put + "/" + nomer.getText() + "/" + "Продольный",0),
                        dlina.getText(),
                        nomer1.getText(),
                        this.Photosxemtefat,
                        this.Photovidkrepi,
                        this.Photoilement,
                        this.Photovidkrepi2,
                        this.Photosxemtefat2,
                        this.Photoilement2,
                        this.Photosxemprovet
                );
            }
        } catch (IOException | InvalidFormatException e) {
            handleError(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        }  catch (Exception e) {
            showAlert("Произошла ошибка: " + e.getMessage());
        }
    }
    private boolean validateInput() {
        if (gorbox.getValue() == null || namebox.getValue() == null) {
            showAlert("Ошибка", "Выберите горизонт и название!");
            return false;
        }
        return true;
    }
    private void rashet(String list) throws ParseException {
        // Проверка входных данных
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Значение list не может быть пустым");
        }

        if (dlina.getText() == null || dlina.getText().trim().isEmpty()) {
            throw new ParseException("Значение длины не может быть пустым", 0);
        }

        switch (list) {
            case "7","8","18","30","42", "52",
                 "60", "61","62","63","72","73" ->{this.ankerPM = "0";this.setkaPM = "0";this.torkretPM = "0";}
            case "3","4","13","16","55", "56",
                 "57" ->{this.ankerPM = "5";this.setkaPM = "0";this.torkretPM = "0";}
            case "64" -> {this.ankerPM = "6.25";this.setkaPM = "0";this.torkretPM = "0.0";}
            case "65" -> {this.ankerPM = "7.5";this.setkaPM = "0";this.torkretPM = "0.0";}
            case "11","12" ->{this.ankerPM = "5";this.setkaPM = "2.8";this.torkretPM = "0";}
            case "25","37","47" ->{this.ankerPM = "5";this.setkaPM = "3.0";this.torkretPM = "1.1";}
            case "28" -> {this.ankerPM = "5.7";this.setkaPM = "0";this.torkretPM = "0.0";}
            case "40", "50" -> {this.ankerPM = "5.7";this.setkaPM = "3.0";this.torkretPM = "0";
                this.ankerPM1 = "0.15";this.ankerPM2 = "0.9";}
            case "14" -> {this.ankerPM = "6.2";this.setkaPM = "3.8";this.torkretPM = "0.0";}
            case "5", "17","58" -> {this.ankerPM = "6.2";this.setkaPM = "0.0";this.torkretPM = "0.0";}
            case "2","54" -> {this.ankerPM = "7.5";this.setkaPM = "0.0";this.torkretPM = "0.0";}
            case "10"-> {this.ankerPM = "7.5";this.setkaPM = "4.5";this.torkretPM = "0.0";}

            case "29","66","67" -> {this.ankerPM = "8.6";this.setkaPM = "0.0";this.torkretPM = "0.0";}

            case "41","51" -> {this.ankerPM = "8.6";this.setkaPM = "3.0";this.torkretPM = "0";
                this.ankerPM1 = "0.2";this.ankerPM2 = "1.2";}
            case "24", "23","35", "45"->{this.ankerPM = "8.6";this.setkaPM = "4.0";this.torkretPM = "0";
                this.ankerPM1 = "0.325";this.ankerPM2 = "1.9";}

            case "68", "70"->{this.ankerPM = "8.6";this.setkaPM = "4.0";this.torkretPM = "0";
                this.ankerPM1 = "0.15";this.ankerPM2 = "1.5";}

            case  "36","46" ->{this.ankerPM = "8.6";this.setkaPM = "4.0";this.torkretPM = "0";
                this.ankerPM1 = "0.275";this.ankerPM2 = "1.6";}

            case "15"-> {this.ankerPM = "8.7";this.setkaPM = "5.7";this.torkretPM = "0.0";}
            case "6","59"-> {this.ankerPM = "8.7";this.setkaPM = "0.0";this.torkretPM = "0";}
            case "19"-> {this.ankerPM = "8.9";this.setkaPM = "8.3";this.torkretPM = "0.0";
                this.ankerPM1 = "4.2";this.ankerPM2 = "1.0";}
            case "9"-> {this.ankerPM = "10.0";this.setkaPM = "5.8";this.torkretPM = "0.46";}
            case "1","53" ->{this.ankerPM = "10.0";this.setkaPM = "0.0";this.torkretPM = "0";}
            case "26","38","48" -> {this.ankerPM = "11.4";this.setkaPM = "5.7";this.torkretPM = "0.0";
                this.ankerPM1 = "0.36";this.ankerPM2 = "2.1";}
            case "31" ->{this.ankerPM = "12.5";this.setkaPM = "8.3";this.torkretPM = "0";
                this.ankerPM1 = "4.2";this.ankerPM2 = "1.0";}

            case "69","71" ->{this.ankerPM = "10.0";this.setkaPM = "3.0";this.torkretPM = "0";
                this.ankerPM1 = "0.2";this.ankerPM2 = "2.6";}

            case "22","34","44"-> {this.ankerPM = "14.2";this.setkaPM = "6.0";this.torkretPM = "0.44";
                this.ankerPM1 = "0.455";this.ankerPM2 = "2.7";}

            case "21","33","43" ->{this.ankerPM = "15.7";this.setkaPM = "7.2";this.torkretPM = "0.46";
                this.ankerPM1 = "0.5";this.ankerPM2 = "3.0";}
            case "27","39","49" ->{this.ankerPM = "15.7";this.setkaPM = "7.7";this.torkretPM = "0.0";
                this.ankerPM1 = "0.46";this.ankerPM2 = "2.8";}
            default -> throw new IllegalStateException("Unexpected value: " + list);
        }

        // Обработка ввода с заменой запятых на точки
        String input = dlina.getText().trim().replace(',', '.');
        if (rasht_11temp.contains(nomer1.getText())) {
            try {
                double dlina_Dobl = Double.parseDouble(input);
                double ankerPM_Dobl = Double.parseDouble(this.ankerPM);
                double setkaPM_Dobl = Double.parseDouble(this.setkaPM);
                double ampulaPM_Dobl =ankerPM_Dobl*3;

                double ankerR_Doble = dlina_Dobl * ankerPM_Dobl;
                double setkaR_Doble = dlina_Dobl * setkaPM_Dobl;
                double ampulaR_Doble = dlina_Dobl *ampulaPM_Dobl;

                double ankerPM_Dobl1 =Math.ceil(dlina_Dobl)/4.2;

                double ankerPM_Dobl3 =Math.ceil(dlina_Dobl) * 4;

                double ankerR_Doble1 = Math.ceil(dlina_Dobl) * 0.35;
                double ankerR_Doble2 = Math.ceil(dlina_Dobl) * 1.6;



                this.ankerR = String.format(Locale.US,"%.0f", Math.ceil(ankerR_Doble));
                this.setkaR = String.format(Locale.US,"%.1f", setkaR_Doble);
                this.ampulaR = String.format(Locale.US,"%.0f",Math.ceil (ampulaR_Doble));
                this.ampulaPM = String.format(Locale.US,"%.1f", ampulaPM_Dobl);

                this.ankerPM1 = String.format(Locale.US,"%.0f", Math.ceil(ankerPM_Dobl1));
                this.ankerPM2 = String.format(Locale.US,"%.0f", Math.ceil(dlina_Dobl));
                this.ankerPM3 = String.format(Locale.US,"%.0f", Math.ceil(ankerPM_Dobl3));
                this.ankerR1 = String.format(Locale.US,"%.1f", Math.ceil(ankerR_Doble1));
                this.ankerR2 = String.format(Locale.US,"%.1f", Math.ceil(ankerR_Doble2));


            } catch (NumberFormatException e) {
                throw new ParseException("Некорректный числовой формат", 0);
            }}
        else if (rasht_4344.contains(nomer1.getText())) {
            try {
                double dlina_Dobl = Double.parseDouble(input);
                double ankerPM_Dobl = Double.parseDouble(this.ankerPM);
                double setkaPM_Dobl = Double.parseDouble(this.setkaPM);
                double ampulaPM_Dobl =ankerPM_Dobl*3;
                double ankerPM_Dobl1 = Double.parseDouble(this.ankerPM1);
                double ankerPM_Dobl2= Double.parseDouble(this.ankerPM2);

                double ankerPM_Dobl3 =  Math.ceil(dlina_Dobl) * 2.66;
                double ankerR_Doble3 =  Math.ceil(dlina_Dobl) * 3.99;
                double ankerR_Doble4 =  Math.ceil(dlina_Dobl) * 5.32;
                double ankerPM_Dobl4 =  Math.ceil(dlina_Dobl) *1.33;

                double ankerR_Doble1 =  Math.ceil(dlina_Dobl) * ankerPM_Dobl1;
                double ankerR_Doble2 =  Math.ceil(dlina_Dobl) * ankerPM_Dobl2;

                double ankerR_Doble = dlina_Dobl * ankerPM_Dobl;
                double setkaR_Doble = dlina_Dobl * setkaPM_Dobl;
                double ampulaR_Doble = dlina_Dobl * ampulaPM_Dobl;



                this.ankerR = String.format(Locale.US,"%.0f", Math.ceil(ankerR_Doble));
                this.setkaR = String.format(Locale.US,"%.1f", setkaR_Doble);
                this.ampulaPM = String.format(Locale.US,"%.1f", ampulaPM_Dobl);
                this.ampulaR = String.format(Locale.US,"%.0f",Math.ceil (ampulaR_Doble));

                this.ankerR1 = String.format(Locale.US,"%.1f", ankerR_Doble1);
                this.ankerR2 = String.format(Locale.US,"%.1f", ankerR_Doble2);

                this.ankerPM3 = String.format(Locale.US, "%.0f", Math.ceil(ankerPM_Dobl3));
                this.ankerR3 = String.format(Locale.US, "%.0f", Math.ceil(ankerR_Doble3));
                this.ankerPM4 = String.format(Locale.US, "%.0f", Math.ceil(ankerPM_Dobl4));
                this.ankerR4 = String.format(Locale.US, "%.0f", Math.ceil(ankerR_Doble4));

            } catch (NumberFormatException e) {
                throw new ParseException("Некорректный числовой формат", 0);
            }
            }else if (rasht_3temp.contains(nomer1.getText())) {
            try {
                double dlina_Dobl = Double.parseDouble(input);
                double ankerPM_Dobl = Double.parseDouble(this.ankerPM);
                double setkaPM_Dobl = Double.parseDouble(this.setkaPM);
                double ampulaPM_Dobl =ankerPM_Dobl*3;
                double ankerPM_Dobl1 = Double.parseDouble(this.ankerPM1);
                double ankerPM_Dobl2= Double.parseDouble(this.ankerPM2);

                double ankerPM_Dobl3 =  Math.ceil(dlina_Dobl) * 2;
                double ankerR_Doble3 =  Math.ceil(dlina_Dobl) * 3;
                double ankerR_Doble4 =  Math.ceil(dlina_Dobl) * 4;
                double ankerPM_Dobl4 =  Math.ceil(dlina_Dobl) *1;

                double ankerR_Doble1 =  Math.ceil(dlina_Dobl) * ankerPM_Dobl1;
                double ankerR_Doble2 =  Math.ceil(dlina_Dobl) * ankerPM_Dobl2;

                double ankerR_Doble = dlina_Dobl * ankerPM_Dobl;
                double setkaR_Doble = dlina_Dobl * setkaPM_Dobl;
                double ampulaR_Doble = dlina_Dobl * ampulaPM_Dobl;



                this.ankerR = String.format(Locale.US,"%.0f", Math.ceil(ankerR_Doble));
                this.setkaR = String.format(Locale.US,"%.1f", setkaR_Doble);
                this.ampulaPM = String.format(Locale.US,"%.1f", ampulaPM_Dobl);
                this.ampulaR = String.format(Locale.US,"%.0f",Math.ceil (ampulaR_Doble));

                this.ankerR1 = String.format(Locale.US,"%.1f", ankerR_Doble1);
                this.ankerR2 = String.format(Locale.US,"%.1f", ankerR_Doble2);

                this.ankerPM3 = String.format(Locale.US, "%.0f", Math.ceil(ankerPM_Dobl3));
                this.ankerR3 = String.format(Locale.US, "%.0f", Math.ceil(ankerR_Doble3));
                this.ankerPM4 = String.format(Locale.US, "%.0f", Math.ceil(ankerPM_Dobl4));
                this.ankerR4 = String.format(Locale.US, "%.0f", Math.ceil(ankerR_Doble4));

            } catch (NumberFormatException e) {
                throw new ParseException("Некорректный числовой формат", 0);
            }
        }else{
            try {
            double dlina_Dobl = Double.parseDouble(input);
            double ankerPM_Dobl = Double.parseDouble(this.ankerPM);
            double setkaPM_Dobl = Double.parseDouble(this.setkaPM);
            double torkretPM_Dobl = Double.parseDouble(this.torkretPM);
                double ampulaPM_Dobl =ankerPM_Dobl*3;
            double ankerR_Doble = dlina_Dobl * ankerPM_Dobl;
            double setkaR_Doble = dlina_Dobl * setkaPM_Dobl;
            double torkretR_Doble = dlina_Dobl * torkretPM_Dobl;
                double ampulaR_Doble = dlina_Dobl * ampulaPM_Dobl;

            this.ankerR = String.format(Locale.US,"%.0f", Math.ceil(ankerR_Doble));
            this.setkaR = String.format(Locale.US,"%.1f", setkaR_Doble);
            this.torkretR = String.format(Locale.US,"%.1f", torkretR_Doble);
                this.ampulaPM = String.format(Locale.US,"%.1f", ampulaPM_Dobl);
                this.ampulaR = String.format(Locale.US,"%.0f", Math.ceil(ampulaR_Doble));
        } catch (NumberFormatException e) {
            throw new ParseException("Некорректный числовой формат", 0);
        }}
    }
    private void rashet_SOPR(String list) throws ParseException {
        // Проверка входных данных
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Значение list не может быть пустым");
        }
        switch (list) {
            case "1","5","6","19" -> {this.ankerR = "28";this.setkaR = "13.9";this.torkretR = "0";
                this.ankerPM = "84";}
            case "2" -> {this.ankerR = "32";this.setkaR = "0";this.torkretR = "0";
                this.ankerPM = "96";}
            case "9","13","17" -> {this.ankerR = "36";this.setkaR = "17.6";this.ankerPM = "108";
                this.ankerR1 = "25.4";this.ankerR2 = "5";}
            case "10","14","18","20" -> {this.ankerR = "40";this.setkaR = "15.4";this.torkretR = "20";
                this.ankerPM = "120";}

            case "3","4","7","8" -> {this.ankerR = "177";this.setkaR = "153";this.torkretR = "15";
              this.ankerPM = "531";}

            case "11","12"-> {this.ankerR = "306";this.setkaR = "196";this.torkretR = "15";
                this.ankerPM = "918";}

            case "15","16" -> {this.ankerR = "306";this.setkaR = "196";this.torkretR = "58";
                this.ankerPM = "918";}
        }
    }
    private void generateWordDocument(String... params) throws IOException, InvalidFormatException, ParseException {

        if (soprigenii.contains(idi.getText())) {
            rashet_SOPR(nomer1.getText());
        }else{ rashet(nomer1.getText());}

            Map<String, String> tableData = new HashMap<>();
            tableData.put("${table.ankPM}", this.ankerPM);
            tableData.put( "${table.ankR}",this.ankerR);
            tableData.put( "${table.stkPM}" ,this.setkaPM);
            tableData.put( "${table.stkR}",this.setkaR);
            tableData.put("${table.torPM}",this.torkretPM);
            tableData.put("${table.torR}",this.torkretR);
            tableData.put( "${table.ankR1}",this.ankerR1);
            tableData.put("${table.ankPM1}", this.ankerPM1);
            tableData.put("${table.ankPM2}", this.ankerPM2);
            tableData.put( "${table.ankR2}",this.ankerR2);

            tableData.put("${table.ampulaPM}", this.ampulaPM);
            tableData.put("${table.ampulaR}",this.ampulaR);

            tableData.put("${table.ankPM3}", this.ankerPM3);
            tableData.put( "${table.ankR3}",this.ankerR3);
            tableData.put("${table.ankPM4}", this.ankerPM4);
            tableData.put( "${table.ankR4}",this.ankerR4);



        String outputFileName = OUTPUT_PATH + nomer.getText() + "_" + gorbox.getValue() + ".docx";
        File outputFile = new File(outputFileName);
        try {
            TemplateResource templateResource;
        // Получаем поток входных данных для ресурса
        //InputStream inputStream = getClass().getResourceAsStream(TEMPLATE_PATH);
            if (soprigenii.contains(idi.getText())) {
                 templateResource = getDokHablon_SOPR(nomer1.getText());
            }else{  templateResource = getDokHablon(nomer1.getText());}

            InputStream inputStream = templateResource.getInputStream();
            String templatePath = templateResource.getTemplatePath();
            System.err.println("Берем шаблон: " + templatePath);
            if (inputStream == null) {
            throw new FileNotFoundException("Ресурс не найден: " + templatePath);
        }

        // Создаем временный файл
        Path tempFile = Files.createTempFile("hablon_", ".docx");
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

        try (FileInputStream template = new FileInputStream(tempFile.toFile());
             XWPFDocument document = new XWPFDocument(template)) {

            replacePlaceholders(document, params);

            replaceTablePlaceholders(document, tableData);

            try (FileOutputStream out = new FileOutputStream(outputFile)) {
                document.write(out);
            }
            // Автоматическое открытие файла после сохранения
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (outputFile.exists()) {
                    desktop.open(outputFile);
                } else {
                    System.err.println("Файл не найден: " + outputFile.getAbsolutePath());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка генерации документа: " + e.getMessage());
        }
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
        String prim ="Паспорт создан";
        new DatabaseHandler().DobavleniePRIM(prim, gorbox.getValue(), namebox.getValue());

        clearFields();
    }
    private void replaceTablePlaceholders(XWPFDocument doc, Map<String, String> tableData) {
        // Перебираем все таблицы в документе
        for (XWPFTable table : doc.getTables()) {
            // Перебираем все строки в таблице
            for (XWPFTableRow row : table.getRows()) {
                // Перебираем все ячейки в строке
                for (XWPFTableCell cell : row.getTableCells()) {
                    // Перебираем все абзацы в ячейке
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        // Перебираем все плейсхолдеры
                        for (Map.Entry<String, String> entry : tableData.entrySet()) {
                            String placeholder = entry.getKey();
                            String value = entry.getValue();

                            // Если абзац содержит плейсхолдер - заменяем его
                            if (paragraph.getText().contains(placeholder)) {
                                replaceTextInParagraph(paragraph, placeholder, value);
                            }
                        }
                    }
                }
            }
        }
    }

    private void replacePlaceholders(XWPFDocument doc, String[] values) {
        // Списки плейсхолдеров для разных типов замен
        Set<String> textPlaceholders = Set.of(
                "${nomer}", "${name}", "${gorizont}",
                "${opisanie}", "${kategorii}", "${faktor}","${tipovoi}", "${dlina}");
        Set<String> imagePlaceholders = Set.of("${plan}", "${poper}", "${prodol}");
        Set<String> imageSXPlaceholders = Set.of("${sxema}");
        Set<String> hablonPlaceholders = Set.of("${konstrk}","${konstrk2}");
        Set<String> VidhablonPlaceholders = Set.of("${obvid2}","${obvid}");
        Set<String> SxmHablonPlaceholders = Set.of("${sxematexfakt}",  "${sxematexfakt2}");
        // Обработка текстовых плейсхолдеров
        textPlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String value = getValueByFieldName(fieldName, values);
                    System.out.println("[ТЕКСТ] Замена " + placeholder + " → " + value);
                    replaceTextInParagraph(p, placeholder, value);
                }
            }
        });
        // Обработка плейсхолдеров изображений
        imagePlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String imagePath = getValueByFieldName(fieldName, values);
                    //System.out.println("[ИЗОБРАЖЕНИЕ] Замена " + placeholder + " → " + imagePath);
                    replaceImageInParagraph(p, placeholder, imagePath,470,340,false);
                }
            }
        });
        imageSXPlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String imagePath = getValueByFieldName(fieldName, values);
                    //System.out.println("[ИЗОБРАЖЕНИЕ] Замена " + placeholder + " → " + imagePath);
                    replaceImageInParagraph(p,  placeholder, imagePath,470,600,false);
                }
            }
        });
        // Обработка плейсхолдеров hablon
        hablonPlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String imagePath = getValueByFieldName(fieldName, values);
                    System.out.println("[ШАБЛОН] Замена " + placeholder + " → " + imagePath);
                    replaceImageInParagraph(p,  placeholder, imagePath,470,320,true);
                }
            }
        });
        VidhablonPlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String imagePath = getValueByFieldName(fieldName, values);
                    System.out.println("[Vid] Замена " + placeholder + " → " + imagePath);
                    replaceImageInParagraph(p,  placeholder, imagePath,600,410,true);
                }
            }
        });
        SxmHablonPlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String imagePath = getValueByFieldName(fieldName, values);
                    System.out.println("[Sxm] Замена " + placeholder + " → " + imagePath);
                    //replaceTextInParagraph(p, placeholder, "");
                    replaceImageInParagraph(p,  placeholder, imagePath,470,700,true);
                }
            }
        });
    }
    private String getValueByFieldName(String name, String[] values) {
        return switch (name) { //зависимости от ключа возвращает значение из массива который вводим
            case "nomer" -> values[0];
            case "name" -> values[1];
            case "gorizont" -> values[2];
            case "kategorii" -> values[3];
            case "opisanie" -> values[4];
            case "faktor" -> values[5];
            case "plan" -> values[6];
            case "poper" -> values[7];
            case "prodol" -> values[8];
            case "dlina" -> values[9];
            case "tipovoi" -> values[10];
            case "sxematexfakt" -> values[11];
            case "obvid" -> values[12];
            case "konstrk" -> values[13];
            case "obvid2" -> values[14];
            case "sxematexfakt2" -> values[15];
            case "konstrk2" -> values[16];
            case "sxema" -> values[17];
            default -> "";
        };
    }
    private void replaceTextInParagraph(XWPFParagraph p, String placeholder, String replacement) {
        String text = p.getText(); // получаем весь текст параметра
        p.getRuns().forEach(r -> r.setText("", 0)); //удаляем существующий из всех ронов и заменяем полностью
        XWPFRun newRun = p.createRun(); // создаем новый для нового текста
        newRun.setText(text.replace(placeholder, replacement)); // заменяем "${nomer}", "123",
    }
    private void replaceImageInParagraph(XWPFParagraph p, String placeholder, String imagePath, int width, int height, boolean isResource) {
        replaceTextInParagraph(p, placeholder, "");

        if (imagePath == null || imagePath.isEmpty()) {
            System.err.println("Путь к изображению пуст");
            return;
        }
        try (InputStream is = isResource
                ? getClass().getResourceAsStream(imagePath.startsWith("/") ? imagePath : "/" + imagePath)
                : new FileInputStream(imagePath)) {
            if (is == null) {
                System.err.println("Источник не найден: " + imagePath);
                return;
            }
            byte[] imageBytes = IOUtils.toByteArray(is);
            PictureData.PictureType type = isResource
                    ? determineImageType(imageBytes)
                    : getImageType(imagePath);

            XWPFRun run = p.createRun();
            run.addPicture(
                    new ByteArrayInputStream(imageBytes),
                    type.ordinal(),
                    "image",
                    Units.toEMU(width),
                    Units.toEMU(height)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private PictureData.PictureType determineImageType(byte[] imageData) {
        if (imageData.length >= 4 &&
                imageData[0] == (byte)0xFF && imageData[1] == (byte)0xD8) {
            return PictureData.PictureType.JPEG;
        } else if (imageData.length >= 8 &&
                new String(imageData, 0, 8).equals("\211PNG\r\n\032\n")) {
            return PictureData.PictureType.PNG;
        }
        return PictureData.PictureType.JPEG; // fallback
    }
    // Определение типа изображения
    private PictureData.PictureType getImageType(String imagePath) {
        String ext = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();
        return switch (ext) {
            case "jpg", "jpeg" -> PictureData.PictureType.JPEG;
            case "png" -> PictureData.PictureType.PNG;
            case "gif" -> PictureData.PictureType.GIF;
            case "emf" -> PictureData.PictureType.EMF;
            case "wmf" -> PictureData.PictureType.WMF;
            default -> throw new IllegalArgumentException("Unsupported image type: " + ext);
        };
    }

        // Сформируйте полный путь к ресурсу
        public String getRESURS(String resourcePath, String fileName) {
            // Возвращаем путь в формате "/папка/файл"
            if (fileName == null) return "";
            return (resourcePath.startsWith("/") ? "" : "/") +
                    resourcePath.replace("\\", "/") +
                    "/" + fileName;
        }
public void getIlement (String list){

     switch (list) {

        case  "35", "45", "36", "46", "38",
             "48", "39", "49" -> {
            this.Photoilement=getRESURS(HABLON_PATH_ILIMENT, "3.jpg");
            this.Photoilement2= getRESURS(HABLON_PATH_ILIMENT, "10.jpg");
        }
         case "33", "34" -> {
             this.Photoilement=getRESURS(HABLON_PATH_ILIMENT, "3.jpg");
             this.Photoilement2= getRESURS(HABLON_PATH_ILIMENT, "2.jpg");
         }
        case "43","44"-> {
            this.Photoilement=  getRESURS(HABLON_PATH_ILIMENT, "4.jpg");
            this.Photoilement2 = getRESURS(HABLON_PATH_ILIMENT, "2.jpg");
        }
        case "40", "41","68","69" -> {
            this.Photoilement=getRESURS(HABLON_PATH_ILIMENT, "7.jpg");
            this.Photoilement2 = getRESURS(HABLON_PATH_ILIMENT, "10.jpg");
        }
        case "50", "51","70","71" -> {
            this.Photoilement=getRESURS(HABLON_PATH_ILIMENT, "8.jpg");
            this.Photoilement2 = getRESURS(HABLON_PATH_ILIMENT, "10.jpg");
        }

        case "1","53","2","54"-> this.Photoilement= getRESURS(HABLON_PATH_ILIMENT, "1.jpg");

        case  "9", "21","10", "22" -> this.Photoilement= getRESURS(HABLON_PATH_ILIMENT, "2.jpg");
        case "19", "31" -> this.Photoilement=getRESURS(HABLON_PATH_ILIMENT, "9.jpg");

        case "11", "23","12","24","25","37",
             "47","14","26","15","27" -> this.Photoilement=getRESURS(HABLON_PATH_ILIMENT, "10.jpg");

        case "3", "55","4","56", "13","57","5",
             "58", "6", "59","16", "28","17","29",
             "64","65","66","67"-> this.Photoilement=getRESURS(HABLON_PATH_ILIMENT, "11.jpg");

        case "20", "32","60","61", "7","18","30",
             "42", "52", "62","8", "63","72","73" -> {}

//
        };
    }
    public String getIlement_SOPR (String list){

        return switch (list) {
            case "5","9","10","14","18"-> getRESURS(HABLON_PATH_ILIMENT, "10.jpg");
            case "1","19","2","6","20"-> getRESURS(HABLON_PATH_ILIMENT, "11.jpg");

            case "13","17" -> getRESURS(HABLON_PATH_ILIMENT, "3.jpg");
            case "3","4" -> getRESURS(HABLON_PATH_ILIMENT, "5.jpg");

            case "7","11","15", "8","12","16"-> getRESURS(HABLON_PATH_ILIMENT, "6.jpg");

            default -> "Unsupported image type: " ;
        };
    }
    public TemplateResource getDokHablon (String list){
        return switch (list) {

            case "1","53","2","54" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH1), TEMPLATE_PATH1);
            case "40","50","41","51","68","70","69","71" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH10), TEMPLATE_PATH10);
            case "19","31" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH11), TEMPLATE_PATH11);


            case "9","21","10","22" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH2), TEMPLATE_PATH2);

            case "11","23", "12","24","14","26","15","27" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH3), TEMPLATE_PATH3);

            case "33","34" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH4), TEMPLATE_PATH4);
            case "43", "44" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH4_4344), TEMPLATE_PATH4_4344);

            case  "35","45","36","38","48","39", "49","46" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH5), TEMPLATE_PATH5);

            case  "3","55","4","56","13","57","5","58","6","59","16","28","17","29",
                  "64","65","66","67" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH6), TEMPLATE_PATH6);

            case  "25","37","47" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH7), TEMPLATE_PATH7);

            case  "60","61","7", "18","30","42","52","62","8","63","72","73" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH9), TEMPLATE_PATH9);


            default -> throw new IllegalStateException("Unexpected value: " + list);
        };
    }
    public TemplateResource getDokHablon_SOPR (String list){
        return switch (list) {

            case  "5","9"  -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH12), TEMPLATE_PATH12);

            case "3","4" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH13), TEMPLATE_PATH13);

            case "7","11","8","12"-> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH14), TEMPLATE_PATH14);

            case "13","17"-> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH15), TEMPLATE_PATH15);
            case "14","10","15","16","18"-> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH16), TEMPLATE_PATH16);

            case "1","19","2","6","20" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH8), TEMPLATE_PATH8);

            default -> throw new IllegalStateException("Unexpected value: " + list);
        };
    }
    String getPoto(String imagePath,int i) {
        if (imagePath == null || imagePath.isEmpty()) return "";
        try {
            File folder = new File(imagePath);

        // Проверяем, существует ли папка
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Папка не найдена: " + imagePath);
            return "";
        }

        // Получаем список файлов в папке
        File[] files = folder.listFiles((dir, name) -> {
            // Фильтруем файлы по расширению (изображения)
            String lowerCaseName = name.toLowerCase();
            return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") ||
                    lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".gif");
        });

        // Проверяем, есть ли изображения в папке
        if (files == null || files.length == 0) {
            System.err.println("Нет изображений в: " + imagePath);
            return "";
        }

        return files[i].getAbsolutePath();
        } catch (SecurityException e) {
            showAlert("Ошибка", "Нет доступа к папке: " + imagePath);
            return "";
        }
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



        openImageHandler(sxemaVKL, "Схема",sxemaVKLNe);
        obnovaImageHandler(sxemaobnov, "Схема",sxemaobnov);
        sozdaniiImageHandler(sxemaVNS, "Схема",sxemaVKL,sxemaVKLNe,sxemaVNS,sxemaobnov,sxemaobnov);
    }



    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void handleError(Exception e) {
        e.printStackTrace();
        showAlert("Ошибка", "Произошла ошибка: " + e.getMessage());
    }
    protected void setupTooltips_Sxema() {
        Tooltip.install(sxemaVNS, createTooltip("Внести изображение схемы вентиляции"));
        Tooltip.install(sxemaVKL, createTooltip("Показать схему вентиляции"));
        Tooltip.install(sxemaVKLNe, createTooltip("Схема вентиляции не внесена"));
        Tooltip.install(sxemaobnov, createTooltip("Обновить изображение схемы вентиляции"));

    }

    protected void setupCursor_Sxema() {
        String hoverStyle = "-fx-cursor: hand;";//рука
        sxemaVNS.setStyle(hoverStyle);
        sxemaVKL.setStyle(hoverStyle);
        sxemaVNSNE.setStyle(hoverStyle);
        sxemaVKLNe.setStyle(hoverStyle);
        sxemaobnov.setStyle(hoverStyle);

    }
}