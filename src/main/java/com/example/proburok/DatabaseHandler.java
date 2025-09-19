package com.example.proburok;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DatabaseHandler extends Configs {
    public Connection getDbConnection() throws SQLException {  //Этот метод создает и возвращает соединение с базой данных MySQL.
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        return DriverManager.getConnection(connectionString, dbUser, dbPass); //Создает соединение с базой данных, используя строку подключения, имя пользователя (dbUser) и пароль
    }
    //добавление для проходчика в БД
    public void Dobavlenie(String nomer, Date data, String sehen, String gorizont, String privizka,String nazvanie,String nazvanie_bd,String idi,String uhastok,String primihanie) {
        String stroka = "INSERT INTO " + Const.BAZA_TABLE + "(" + Const.BAZA_NOMER +
                "," + Const.BAZA_DATA + "," + Const.BAZA_SHENIE + "," + Const.BAZA_GORIZONT
                + "," + Const.BAZA_PRIVIZKA + "," + Const.BAZA_NAZVANIE + "," + Const.BAZA_NAZVANIE_BD+ ","+ Const.BAZA_IDI  +"," + Const.BAZA_UHASTOK+ "," + Const.BAZA_PRIM   + ")" + "VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (Connection connection = getDbConnection(); // подключение к бд

             PreparedStatement prSt = connection.prepareStatement(stroka)) {  //что именно передаем

            prSt.setString(1,nomer );
            prSt.setDate(2, data);
            prSt.setString(3, sehen);
            prSt.setString(4, gorizont);
            prSt.setString(5, privizka);
            prSt.setString(6, nazvanie);
            prSt.setString(7, nazvanie_bd);
            prSt.setString(8, idi);
            prSt.setString(9, uhastok);
            prSt.setString(10, primihanie);
            prSt.executeUpdate(); //выполнить передачу
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to sign up user: " + e.getMessage(), e);
        }
    }

    public void DobavlenieGEOLOG(String kategoriya,  String opisanie,String tippas,String dlina,String gorizont, String nazvanie,String primihanie) {
        String query = "UPDATE " + Const.BAZA_TABLE + " SET " + Const.BAZA_KATIGORII + " = ?, "
                       + Const.BAZA_OPISANIE + " = ?, " + Const.BAZA_TIPPAS + " = ?, " + Const.BAZA_DLINA + " = ?, "+ Const.BAZA_PRIM + " = ? "+ "WHERE "
                       + Const.BAZA_GORIZONT + " = ? AND " + Const.BAZA_NAZVANIE + " = ?";

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(query)) {

            prSt.setString(1, kategoriya);
            prSt.setString(2, opisanie);

            prSt.setString(3, tippas);
            prSt.setString(4, dlina);
            prSt.setString(5, primihanie);
            prSt.setString(6, gorizont);
            prSt.setString(7, nazvanie);

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка обновления данных: " + e.getMessage(), e);
        }
    }
    public void DobavleniePRIM(String prim,String gorizont, String nazvanie) {
        String query = "UPDATE " + Const.BAZA_TABLE + " SET " + Const.BAZA_PRIM + " = ? "
                + "WHERE " + Const.BAZA_GORIZONT + " = ? AND " + Const.BAZA_NAZVANIE + " = ?";

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(query)) {

            prSt.setString(1, prim);
            prSt.setString(2, gorizont);
            prSt.setString(3, nazvanie);

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка обновления данных: " + e.getMessage(), e);
        }
    }
    public void DobavlenieGEOMEX(String faktor,String tippas, String gorizont, String nazvanie) {
        String query = "UPDATE " + Const.BAZA_TABLE + " SET " + Const.BAZA_FAKTOR + " = ?, "
                + Const.BAZA_TIPPAS + " = ? " + "WHERE " + Const.BAZA_GORIZONT + " = ? AND " + Const.BAZA_NAZVANIE + " = ?";

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(query)) {

            prSt.setString(1, faktor);
            prSt.setString(2, tippas);
            prSt.setString(3, gorizont);
            prSt.setString(4, nazvanie);

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка обновления данных: " + e.getMessage(), e);
        }
    }
    //Добавление регистрации в юзер
    public void singUpUser(User user) {
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" + Const.USER_FIRSTNAME +
                "," + Const.USER_LASTNAME + "," + Const.USER_USERNAME + "," + Const.USER_PASSWORD
                + "," + Const.USER_LOCATION +  ")" + "VALUES(?,?,?,?,?)";

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(insert)) {  //что именно передаем
            prSt.setString(1, user.getFirstname());
            prSt.setString(2, user.getLastname());
            prSt.setString(3, user.getUsername());
            prSt.setString(4, user.getPassword());
            prSt.setString(5, user.getLocation());

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to sign up user: " + e.getMessage(), e);
        }
    }
    public User getUser(String username, String password) {
        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " +
                Const.USER_USERNAME + "=? AND " + Const.USER_PASSWORD + "=?"; // "SELECT * FROM " + Const.USER_TABLE  выбрать всё из этой базы  " WHERE " + Const.USER_USERNAME + "=? где конст равен чему то

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(select)) {  // что вставляем в БД
            prSt.setString(1, username);
            prSt.setString(2, password);

            try (ResultSet resSet = prSt.executeQuery()) { //prSt.executeQuery-получаем даные из БД
                if (resSet.next()) {
                    User user = new User();
                    user.setFirstname(resSet.getString(Const.USER_FIRSTNAME));
                    user.setLastname(resSet.getString(Const.USER_LASTNAME));
                    user.setUsername(resSet.getString(Const.USER_USERNAME));
                    user.setPassword(resSet.getString(Const.USER_PASSWORD));
                    user.setLocation(resSet.getString(Const.USER_LOCATION));

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Пользователь не найден
    }

    public ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList(); //создает список
        String select = "SELECT * FROM " + Const.USER_TABLE; // извлечение всей инфы из бд

        try (Connection connection = getDbConnection(); // подключаемся к бд
             PreparedStatement prSt = connection.prepareStatement(select); // вставляем запрос
             ResultSet resSet = prSt.executeQuery()) { // получаем что в БД

            while (resSet.next()) { //перебираем все строки в бд
                User user = new User();
                user.setFirstname(resSet.getString(Const.USER_FIRSTNAME));
                user.setLastname(resSet.getString(Const.USER_LASTNAME));
                user.setUsername(resSet.getString(Const.USER_USERNAME));
                users.add(user); // добавляем всё в юзера
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users; // метод возвращает заполненный список
    }

    public ObservableList<Baza> getAllBaza() {
        ObservableList<Baza> bazas = FXCollections.observableArrayList(); //создает список
        String select = "SELECT * FROM " + Const.BAZA_TABLE; // извлечение всей инфы из бд

        try (Connection connection = getDbConnection(); // подключаемся к бд
             PreparedStatement prSt = connection.prepareStatement(select); // вставляем запрос
             ResultSet resSet = prSt.executeQuery()) { // получаем что в БД

            while (resSet.next()) { //перебираем все строки в бд
                Baza infa = new Baza();
                infa.setDATA(resSet.getDate(Const.BAZA_DATA));
                infa.setGORIZONT(resSet.getString(Const.BAZA_GORIZONT));
                infa.setNAME(resSet.getString(Const.BAZA_NAZVANIE));
                infa.setNOMER(resSet.getString(Const.BAZA_NOMER));
                infa.setSEHEN(resSet.getString(Const.BAZA_SHENIE));
                infa.setKATEGORII(resSet.getString(Const.BAZA_KATIGORII));
                infa.setOPISANIE(resSet.getString(Const.BAZA_OPISANIE));
                infa.setFAKTOR(resSet.getString(Const.BAZA_FAKTOR));
                infa.setTIPPAS(resSet.getString(Const.BAZA_TIPPAS));
                infa.setPRIVIZKA(resSet.getString(Const.BAZA_PRIVIZKA));
                infa.setUGOL(resSet.getString(Const.BAZA_UGOL));
                infa.setDLINA(resSet.getString(Const.BAZA_DLINA));
                infa.setUHASTOK(resSet.getString(Const.BAZA_UHASTOK));
                infa.setPRIM(resSet.getString(Const.BAZA_PRIM));
                infa.setNAME_BD(resSet.getString(Const.BAZA_NAZVANIE_BD));
                bazas.add(infa);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bazas; // метод возвращает заполненный список
    }
    public ObservableList<Baza> poiskName(String gor) {
        ObservableList<Baza> bazas = FXCollections.observableArrayList();
        String select = "SELECT * FROM " + Const.BAZA_TABLE + " WHERE " + Const.BAZA_GORIZONT + "=?";

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(select)) {
            prSt.setString(1, gor);
            try (ResultSet resSet = prSt.executeQuery()) {
                while (resSet.next()) {
                    Baza infa = new Baza();
                    infa.setDATA(resSet.getDate(Const.BAZA_DATA));
                    infa.setGORIZONT(resSet.getString(Const.BAZA_GORIZONT));
                    infa.setNAME(resSet.getString(Const.BAZA_NAZVANIE));
                    infa.setNOMER(resSet.getString(Const.BAZA_NOMER));
                    infa.setSEHEN(resSet.getString(Const.BAZA_SHENIE));
                    bazas.add(infa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bazas;
    }
    public Baza danii(String gor, String name) {
        String select = "SELECT * FROM " + Const.BAZA_TABLE + " WHERE " +
                Const.BAZA_GORIZONT + "=? AND " + Const.BAZA_NAZVANIE + "=?"; // "SELECT * FROM " + Const.USER_TABLE  выбрать всё из этой базы  " WHERE " + Const.USER_USERNAME + "=? где конст равен чему то

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(select)) {  // что вставляем в БД
            prSt.setString(1, gor);
            prSt.setString(2, name);

            try (ResultSet resSet = prSt.executeQuery()) { //prSt.executeQuery-получаем даные из БД
                if (resSet.next()) {
                    Baza infa = new Baza();
                    infa.setDATA(resSet.getDate(Const.BAZA_DATA));
                    infa.setGORIZONT(resSet.getString(Const.BAZA_GORIZONT));
                    infa.setNAME(resSet.getString(Const.BAZA_NAZVANIE));
                    infa.setNOMER(resSet.getString(Const.BAZA_NOMER));
                    infa.setSEHEN(resSet.getString(Const.BAZA_SHENIE));
                    infa.setKATEGORII(resSet.getString(Const.BAZA_KATIGORII));
                    infa.setOPISANIE(resSet.getString(Const.BAZA_OPISANIE));
                    infa.setFAKTOR(resSet.getString(Const.BAZA_FAKTOR));
                    infa.setTIPPAS(resSet.getString(Const.BAZA_TIPPAS));
                    infa.setPRIVIZKA(resSet.getString(Const.BAZA_PRIVIZKA));
                    infa.setUGOL(resSet.getString(Const.BAZA_UGOL));
                    infa.setDLINA(resSet.getString(Const.BAZA_DLINA));
                    infa.setUHASTOK(resSet.getString(Const.BAZA_UHASTOK));
                    infa.setPRIM(resSet.getString(Const.BAZA_PRIM));
                    infa.setNAME_BD(resSet.getString(Const.BAZA_NAZVANIE_BD));
                    infa.setIDI(resSet.getString(Const.BAZA_IDI));

                    return infa;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Пользователь не найден
    }
    // В классе DatabaseHandler
    public int getNextSequenceNumber(String prefix, String year) {
        String pattern = prefix + "-" + "%" + "-" + year;
        int count = 0;

        String select = "SELECT COUNT(*) AS count FROM " + Const.BAZA_TABLE + " WHERE " + Const.BAZA_NOMER +  " LIKE ?";

        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {
            prSt.setString(1, pattern);
            ResultSet resultSet = prSt.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count + 1; // Следующий номер = количество записей + 1
    }
    public ObservableList<String> getUniqueGorizonts() {
        ObservableList<String> gorizonts = FXCollections.observableArrayList();

        String query = "SELECT DISTINCT ГОРИЗОНТ FROM " + Const.BAZA_TABLE;

        try (Connection connection = getDbConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                gorizonts.add(resultSet.getString("ГОРИЗОНТ"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gorizonts;
    }
    public ObservableList<String> getUniqueUSHATOK() {
        ObservableList<String> ushatok = FXCollections.observableArrayList();

        String query = "SELECT DISTINCT УЧАСТОК FROM " + Const.BAZA_TABLE;

        try (Connection connection = getDbConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                ushatok.add(resultSet.getString("УЧАСТОК"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ushatok;
    }

}


