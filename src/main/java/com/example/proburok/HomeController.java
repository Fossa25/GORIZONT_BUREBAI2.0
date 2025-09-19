package com.example.proburok;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class HomeController {
    @FXML private Button LoadData;
    @FXML private TableView<Baza> dataTable;
    @FXML private TableColumn<Baza, Date> dataCtolb;
    @FXML private TableColumn<Baza, String> gorizontCtolb;
    @FXML private TableColumn<Baza, String> nameCtolb;
    @FXML private TableColumn<Baza, String> nomerCtolb;
    @FXML private TableColumn<Baza, String> sehenCtolb;
  //  @FXML private TableColumn<Baza, String> faktorCtolb;
    @FXML private TableColumn<Baza, String> kftegoriiCtolb;
    @FXML private TableColumn<Baza, String> opisanieCtolb;
    @FXML private TableColumn<Baza, String> tippasCtolb;
    @FXML private TableColumn<Baza, String> privizkaCtolb;
    @FXML private TableColumn<Baza, String> dlinaCtolb;
    @FXML private TableColumn<Baza, String> PRIMCtolb;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private ComboBox<String> filterComboBox1;
    private DatabaseHandler dbHandler;
    @FXML
    void initialize() {

         dbHandler = new DatabaseHandler();
        initFilterComboBox();
        nomerCtolb.setCellValueFactory(new PropertyValueFactory<>("NOMER"));
        dataCtolb.setCellValueFactory(new PropertyValueFactory<>("DATA"));
        gorizontCtolb.setCellValueFactory(new PropertyValueFactory<>("GORIZONT"));
        sehenCtolb.setCellValueFactory(new PropertyValueFactory<>("SEHEN"));
        nameCtolb.setCellValueFactory(new PropertyValueFactory<>("NAME_BD"));
        kftegoriiCtolb.setCellValueFactory(new PropertyValueFactory<>("KATEGORII"));
        opisanieCtolb.setCellValueFactory(new PropertyValueFactory<>("OPISANIE"));
    //    faktorCtolb.setCellValueFactory(new PropertyValueFactory<>("UGOL"));
        tippasCtolb.setCellValueFactory(new PropertyValueFactory<>("TIPPAS"));
        privizkaCtolb.setCellValueFactory(new PropertyValueFactory<>("PRIVIZKA"));
        dlinaCtolb.setCellValueFactory(new PropertyValueFactory<>("DLINA"));
        PRIMCtolb.setCellValueFactory(new PropertyValueFactory<>("PRIM"));
        LoadData();
         LoadData.setOnAction(actionEvent -> {
             LoadData();
         });
    }
    @FXML
    public void LoadData() {
        // Получение данных из базы данных
         ObservableList<Baza> bazas = dbHandler.getAllBaza();
        // Загрузка данных в TableView
        dataTable.setItems(bazas);
    }
    private void initFilterComboBox() {
        // 1. Получаем уникальные значения GORIZONT из базы данных
        ObservableList<String> gorizonts = dbHandler.getUniqueGorizonts();

        ObservableList<String> ushatok = dbHandler.getUniqueUSHATOK();

        // 2. Добавляем опцию "Все" для сброса фильтра
        gorizonts.add(0, "Все горизонты");
        ushatok.add(0, "Все участки");

        // 3. Заполняем ComboBox
        filterComboBox.setItems(gorizonts);
        filterComboBox.setValue("Все горизонты"); // Устанавливаем значение по умолчанию

        filterComboBox1.setItems(ushatok);
        filterComboBox1.setValue("Все участки");

        // 4. Добавляем обработчик изменений
        filterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            applyFilter();
        });
        filterComboBox1.valueProperty().addListener((obs, oldVal, newVal) -> {
            applyFilter();
        });
    }

    private void applyFilter() {
        String gorizont = filterComboBox.getValue();
        String ushatok = filterComboBox1.getValue();

        // Создаем предикат, который будет учитывать оба фильтра
        Predicate<Baza> predicate = b -> {
            // Для горизонта: если выбрано "Все" - пропускаем, иначе сравниваем
            boolean gorizontMatch = "Все горизонты".equals(gorizont) || gorizont == null || gorizont.equals(b.getGORIZONT());

            // Для участка: если выбрано "Все" - пропускаем, иначе сравниваем
            boolean ushatokMatch = "Все участки".equals(ushatok) || ushatok == null || ushatok.equals(b.getUHASTOK());

            // Оба условия должны быть true
            return gorizontMatch && ushatokMatch;
        };

        // Применяем фильтр к данным
        ObservableList<Baza> filteredList = dbHandler.getAllBaza().filtered(predicate);
        dataTable.setItems(filteredList);
    }
}
