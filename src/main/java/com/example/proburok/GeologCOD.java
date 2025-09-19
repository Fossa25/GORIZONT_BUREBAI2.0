package com.example.proburok;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GeologCOD extends Configs {

    @FXML private Button B1;
    @FXML private Button B2;
    @FXML private Button B3;
    @FXML private Button B4;
    @FXML private Button B5;
    @FXML private Button B6;

    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;
    @FXML private ImageView ProdolVKL;
    @FXML private ImageView ProdolVKLNe;



    @FXML private TextArea opisanie;
    @FXML private TextField date;
    @FXML private TextField katigoria;
    @FXML private TextField nomer;
    @FXML private Button singUpButtun;
    @FXML private ComboBox<String> gorbox;
    @FXML private ComboBox<String> namebox;

    @FXML private TextField idi;
    @FXML private ImageView instr;
    @FXML private ImageView dokumGeolog;
    @FXML private ImageView dokumGeolog11;
    @FXML private TextField dlina;
    @FXML private ImageView tabl;
    @FXML private ImageView pethat;
    private String tippas;

    public String blak= " -fx-border-color: #00000000;-fx-background-color:#00000000;-fx-border-width: 0px";
    public String red = "-fx-border-color: #14b814;-fx-background-color:#00000000;-fx-border-width: 3px";

    List<String> soprigenii = Arrays.asList("12.8/12.8", "3.7/3.7", "3.7/2.5", "14.7/14.7", "8.5/8.5", "5.8/5.8", "12.8|12.8");

    public String tex1 = "Очень прочный, крупноблочный, угловатый массив. После отпалки поверхность рваная. Поверхность трещин шероховатая, цепкая, с редким заполнителем. Блоки зажаты." +
            " При простукивании геологическим молотком или оборке заколов издается резкий звенящий звук.";
    public String tex2 = "Прочный, крупноблочный массив с закругленными гранями. Поверхность трещин гладкая, скользкая, с глинистым заполнителем. Склонность к выскальзыванию блоков.";
    public String tex3 = "Непрочный, мелкоблочный, микротрещиноватый, перемятый массив. Наличие крупных разломов, пересекающих все сечение выработки. " +
            "Разломы и трещины заполнены глинистым чешуйчатым материалом. Заполнитель набухает и размягчается при обводнении.  Склонен к заколообразованию. ";
    public String tex4 = "Чередование вязких, тонкослоистых мягких пород светло-серого цвета с непрочными разрыхлёнными блоками. Наличие вкрапленной минерализации. " +
            "Геологический молоток или другой острый инструмент легко втыкается в массив. При обводнении массив набухает и переходит в ползучее состояние.";
    public String tex5 = "Чередование тонких прослоев сильно трещиноватой руды и глинистого материала с минерализацией. " +
            "Геологический молоток или другой острый инструмент легко втыкается в глинистые прослои. При обводнении массив набухает и переходит в ползучее состояние.";
    public String tex6 = "Прочный, слоистый массив руды с поперечными трещинами. Вдоль напластования присутствуют редкие прослои глинистого материала. " +
            "При простукивании геологическим молотком издается резкий звенящий звук.";
    @FXML
    void initialize() {

        setupImageHandlers();

        dlina.setTextFormatter(new TextFormatter<>(change -> {
            // Всегда корректируем start и end
            int start = Math.min(change.getRangeStart(), change.getRangeEnd());
            int end = Math.max(change.getRangeStart(), change.getRangeEnd());
            change.setRange(start, end);

            String newText = change.getControlNewText();

            if (newText.isEmpty() || newText.matches("[0-9]*([.,][0-9]*)?")) {
                return change;
            }

            // Отклоняем невалидные изменения
            return null;
        }));
        viborKatigorii();
        tabl.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/app.fxml"));
        pethat.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/Pehat.fxml"));

        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<Baza> bazas = dbHandler.getAllBaza();
        List<String> nom = bazas.stream() //заполнение базы
                .map(Baza::getGORIZONT)
                .distinct()
                .collect(Collectors.toList());

        gorbox.setItems(FXCollections.observableArrayList(nom));
        gorbox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                ObservableList<Baza> namespisok = dbHandler.poiskName(newValue);
                List<String> imi = namespisok.stream() //заполнение базы
                        .map(Baza::getNAME)
                        .distinct()
                        .toList();
                ohistka();
                namebox.setItems(FXCollections.observableArrayList(imi));
            }
        });
        namebox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null  && !newValue.equals(oldValue)) {
                poisk(gorbox.getValue(),newValue);
                viborKatigorii();
            }
        });

        singUpButtun.setOnMouseClicked(mouseEvent -> {
            String selectedGor = gorbox.getValue();
            String selectedName = namebox.getValue();
            String kategoriyaValue = katigoria.getText();
            String opisanieValue = opisanie.getText();
            String selectPrivizka = date.getText();
            String dlinaValue = dlina.getText();

            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "План", PlanVKL,PlanVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Поперечный",PoperVKL,PoperVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Продольный",ProdolVKL,ProdolVKLNe);
            try {
                // Проверка полей по очереди
                StringBuilder errors = new StringBuilder();
                if (selectedGor == null || selectedGor.isEmpty()) {errors.append("- Не выбран горизонт\n");}
                if (selectedName == null || selectedName.isEmpty()) {errors.append("- Не выбрано название выработки\n");}
                if (selectPrivizka == null || selectPrivizka.isEmpty()) {errors.append("- Не заполнена привязка\n");}
                if (dlinaValue == null || dlinaValue.isEmpty()) {errors.append("- Не заполнена длина выработки\n");}

                if (kategoriyaValue == null || kategoriyaValue.isEmpty()) { errors.append("- Не заполнено поле категория \n"); }
                if (opisanieValue == null || opisanieValue.isEmpty()) {errors.append("- Не заполнено поле описание\n");}


                // Если есть ошибки - показываем их
                if (errors.length() > 0) {showAlert("Заполните обязательные поля:\n" + errors.toString());
                    return;
                }
                // Все данные валидны - выполняем сохранение
                getPas(kategoriyaValue);
                String prim =  "Все данные внесены" ;
                new DatabaseHandler().DobavlenieGEOLOG(kategoriyaValue, opisanieValue,tippas,dlinaValue, selectedGor, selectedName,prim);
                System.out.println("ТИПОВОЙ ПАСПОРТ= "+tippas);
                ohistka();
              // gorbox.setValue(null);
            }  catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });
        dokumGeolog.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_geolog,"Геология Октябрьского месторождения_");
        });
        dokumGeolog11.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_albom,"АЛЬБОМ ТИПОВЫХ ПАСПОРТОВ КРЕПЛЕНИЯ_");
        });
        instr.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_instr,"Инструкция_");
        });
    }
    private void viborKatigorii(){
        if (soprigenii.contains(idi.getText())) {
            klikKatigorii(B1,"1","no",tex1,"no");
            klikKatigorii(B2,"2","no",tex2,"yes");
            klikKatigorii(B3,"3","no",tex3,"yes");
            klikKatigorii(B4,"4","no",tex4,"no");
            klikKatigorii(B5,"5","no",tex5,"no");
            klikKatigorii(B6,"6","no",tex6,"no");
        }else {
            klikKatigorii(B1,"1","no",tex1,"no");
            klikKatigorii(B2,"2","yes",tex2,"yes");
            klikKatigorii(B3,"3","no",tex3,"yes");
            klikKatigorii(B4,"4","no",tex4,"no");
            klikKatigorii(B5,"5","no",tex5,"no");
            klikKatigorii(B6,"6","no",tex6,"no");
        }
    }

    private void klikKatigorii(Button imageView, String kat, String x, String tx,String krigik  ) {
        imageView.setOnMouseClicked(e -> {
            obnul(kat,tx);
            imageView.setStyle(red);
        });
    }

    private void setupImageHandlers() {
        openImageHandler(PlanVKL, "План",PlanVKLNe);
        openImageHandler(PoperVKL, "Поперечный",PoperVKLNe);
        openImageHandler(ProdolVKL, "Продольный",ProdolVKLNe);

    }

    protected void openImageHandler(ImageView imageView, String folder, ImageView neimage) {
        imageView.setOnMouseClicked(e -> {
            if (nomer.getText().isEmpty()) return;
            openImage(Put + "/" + nomer.getText() + "/" + folder,imageView,neimage,"yes");
        });
    }

    public void obnul(String x,String tx){
        if (x.equals("x")) {
            B1.setStyle(blak);
            B2.setStyle(blak);
            B3.setStyle(blak);
            B4.setStyle(blak);
            B5.setStyle(blak);
            B6.setStyle(blak);

            opisanie.setText(tx);

        }else {
            B1.setStyle(blak);
            B2.setStyle(blak);
            B3.setStyle(blak);
            B4.setStyle(blak);
            B5.setStyle(blak);
            B6.setStyle(blak);

            katigoria.setText(x);
            opisanie.setText(tx);
        }
    }

    private void poisk (String viborGOR, String viborName) {

        Platform.runLater(() -> {
        DatabaseHandler dbHandler = new DatabaseHandler();
        Baza poluh = dbHandler.danii(viborGOR, viborName);

        if (poluh != null) {
            nomer.setText(poluh.getNOMER());
            idi.setText(poluh.getIDI());
            date.setText(poluh.getPRIVIZKA());

            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "План", PlanVKL,PlanVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Поперечный",PoperVKL,PoperVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Продольный",ProdolVKL,ProdolVKLNe);


            if (poluh.getDLINA() != null) {
                dlina.setText(poluh.getDLINA());
            } else {
                dlina.setText("");
            }
            if (poluh.getOPISANIE() != null) {
                opisanie.setText(poluh.getOPISANIE());
            } else {
                opisanie.setText("");
            }
            if (poluh.getKATEGORII() != null) {
                katigoria.setText(poluh.getKATEGORII());
            } else {
                katigoria.setText("");
            }

            if ((katigoria.getText() ==null) || katigoria.getText().isEmpty() ){
                obnul("x","");
                System.out.println("Категории нет ");
            }else {
                switch (katigoria.getText()){
                    case"1" -> {obnul("x",opisanie.getText());B1.setStyle(red);}
                    case"2" -> {obnul("x",opisanie.getText());B2.setStyle(red);}
                    case"3" -> {obnul("x",opisanie.getText());B3.setStyle(red);}
                    case"4" -> {obnul("x",opisanie.getText());B4.setStyle(red);}
                    case"5" ->{obnul("x",opisanie.getText());B5.setStyle(red);}
                    case"6" -> {obnul("x",opisanie.getText());B6.setStyle(red);}

                }}

        } else {

            System.out.println("Данные не найдены для " + viborGOR + " - " + viborName);
            nomer.clear();
            date.clear(); // Пользователь не найден
        }
        });

    }
    private void ohistka(){
        obnul("x","");
        //namebox.setValue(null);
        nomer.setText("");
        date.setText("");
        katigoria.setText("");
        opisanie.setText("");
        dlina.setText("");
        tippas="";
        PlanVKL.setVisible(false);PlanVKLNe.setVisible(true);
        PoperVKL.setVisible(false);PoperVKLNe.setVisible(true);
        ProdolVKL.setVisible(false);ProdolVKLNe.setVisible(true);
    }

    void proverkaImageGeolg(String imagePath,ImageView VKL,ImageView VKLNE) {
        File folder = new File(imagePath);
        // Проверяем, существует ли папка
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Папка не найдена: " + imagePath);

            VKL.setVisible(false);VKLNE.setVisible(true);
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


            VKL.setVisible(true);VKLNE.setVisible(false);

        } else {
            System.err.println("В папке нет изображений.");

            VKL.setVisible(false);VKLNE.setVisible(true);
        }
    }


    private void getPas (String kategor) {
        String kat = katigoria.getText();
        String id=idi.getText();
        String Nopas = "Типовой паспорт не разработан";
        if (id == null) {
            tippas = "Типовой паспорт не разработан";
            return;
        }
        if (!kat.isEmpty()){

            switch (id) {
                case "1" -> TP_1_6(kat,"1","9","21","33","43","53");
                case "2" ->TP_1_6(kat,"2","10","22","34","44","54");
                case "3" ->TP_1_6(kat,"3","11","23","35","45","55");
                case "4" ->TP_1_6(kat,"4","12","24","36","46","56");
                case "5" ->TP_1_6(kat,Nopas,"13","25","37","47","57");
                case "6" ->TP_1_6(kat,"5","14","26","38","48","58");
                case "7" ->TP_1_6(kat,"6","15","27","39","49","59");
                case "8" ->TP_1_6(kat,Nopas,"16","28","40","50","60");
                case "9" ->TP_1_6(kat,Nopas,"17","29","41","51","61");
                case "10" ->TP_1_6(kat,"7","18","30","42","52","62");
                case "11" ->TP_1_6(kat,"8","19","31",Nopas,Nopas,"63");
                case "12" ->TP_1_6(kat,Nopas,"64","66","68","70","72");
                case "13" ->TP_1_6(kat,Nopas,"65","67","69","71","73");

                case "14" ->TP_1_6(kat,"1","5","9","13","17","19");
                case "15" ->TP_1_6(kat,"2","6","10","14","18","20");
                case "16" ->TP_1_6(kat,"3","7","11","15",Nopas,Nopas);
                case "17" ->TP_1_6(kat,"4","8","12","16",Nopas,Nopas);

                default -> throw new IllegalStateException("Unexpected value: " + kategor);
            }}
        }

    private void TP_1_6 (String kat,String x1,String x2,String x3,String x4,String x5,String x6) {

        switch (kat) {
            case "1" -> tippas=x1;
            case "2" ->tippas=x2;
            case "3" ->tippas=x3;
            case "4" -> tippas=x4;
            case "5" ->tippas=x5;
            case "6" -> tippas=x6;

            default -> tippas = "Типовой паспорт не разработан";

        }
    }
    private void TP_SOPR (String SH,String x12_8_12_8,String x3_7_3_7,String x3_7_2_5,String x14_7_14_7,String x8_5_8_5,String x5_8_5_8,String x12_8012_8) {

        switch (SH) {

            case "12.8/12.8" -> tippas=x12_8_12_8;
            case "3.7/3.7" -> tippas=x3_7_3_7;
            case "3.7/2.5" -> tippas=x3_7_2_5;
            case "14.7/14.7" -> tippas=x14_7_14_7;
            case "8.5/8.5" -> tippas=x8_5_8_5;
            case "5.8/5.8" -> tippas=x5_8_5_8;
            case "12.8|12.8" -> tippas=x12_8012_8;

            default -> tippas = "Типовой паспорт не разработан";

        }
    }

    }

