package com.example.proburok;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class statusCOD {
    @FXML private Button LoadData;
    @FXML private TableView<Baza> dataTable;

    @FXML private TableColumn<Baza, String> gorizontCtolb;
    @FXML private TableColumn<Baza, String> nameCtolb;
    @FXML private TableColumn<Baza, String> nomerCtolb;
    @FXML private TableColumn<Baza, String> privizkaCtolb;
    @FXML private TableColumn<Baza, String> PRIMCtolb;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private ComboBox<String> filterComboBox1;
    private DatabaseHandler dbHandler;
    @FXML
    void initialize() {

        dbHandler = new DatabaseHandler();
        initFilterComboBox();
        applyFilter();
        nomerCtolb.setCellValueFactory(new PropertyValueFactory<>("NOMER"));

        gorizontCtolb.setCellValueFactory(new PropertyValueFactory<>("GORIZONT"));

        nameCtolb.setCellValueFactory(new PropertyValueFactory<>("NAME_BD"));

        privizkaCtolb.setCellValueFactory(new PropertyValueFactory<>("PRIVIZKA"));

        PRIMCtolb.setCellValueFactory(new PropertyValueFactory<>("PRIM"));


        LoadData.setOnAction(actionEvent -> {
            applyFilter();
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

                // 2. Добавляем опцию "Все" для сброса фильтра
        filterComboBox1.getItems().addAll("Требуется геологическое описание","Все данные внесены");


        gorizonts.add(0, "Все горизонты");
        //ushatok.add(0, "Все участки");

        // 3. Заполняем ComboBox
        filterComboBox.setItems(gorizonts);
        filterComboBox.setValue("Все горизонты"); // Устанавливаем значение по умолчанию


        filterComboBox1.setValue("Требуется геологическое описание");

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
            boolean ushatokMatch =  ushatok == null || ushatok.equals(b.getPRIM());

            // Оба условия должны быть true
            return gorizontMatch && ushatokMatch;
        };

        // Применяем фильтр к данным
        ObservableList<Baza> filteredList = dbHandler.getAllBaza().filtered(predicate);
        dataTable.setItems(filteredList);
    }
}
