package ir.farzinnasiri.Utils;

import java.sql.*;
import java.util.*;

public class DataBase {
    private Connection connection = null;
    private Statement statement = null;
//    private  ResultSet resultSet = null;


    public DataBase() {
        //            connection = DriverManager.getConnection(dbUrl + "?user=root&password=farzin@21114155");
//            Statement s = connection.createStatement();
//            int myResult = s.executeUpdate("CREATE DATABASE IF NOT EXISTS " + user);
//            creatTable();


//        printTable("players_datas");


    }

    private void printTable(String tableName) {

        try {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * from " + tableName);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print("  ");
                System.out.print(rsmd.getColumnName(i));
            }
            System.out.println();

            while (resultSet.next()) {

                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setConnection() throws SQLException {

        String dbUrl = "jdbc:mysql://localhost:3306/";
        String user = "chicken_invaders";
        String pass = "ci@21114155";

        connection = DriverManager.getConnection(dbUrl + user, user, pass);


    }

    public void creatTable() {
        String playersTable = "CREATE TABLE IF NOT EXISTS players_datas ("
                + "user_name VARCHAR(6) CHARACTER SET utf8,"
                + "scores VARCHAR(1024) CHARACTER SET utf8,"
                + "wavesPassed VARCHAR(1024) CHARACTER SET utf8,"
                + "durations VARCHAR(1024) CHARACTER SET utf8,"
                + "life INT,"
                + "missile INT,"
                + "fireLevel INT,"
                + "food INT,"
                + "maxHeat INT,"
                + "spaceshipColor VARCHAR(16) CHARACTER SET utf8,"
                + "engineColor VARCHAR(16) CHARACTER SET utf8,"
                + "weaponType VARCHAR(16) CHARACTER SET utf8)";


        String rankingTable = "CREATE TABLE IF NOT EXISTS ranking_table ("
                + "user_name VARCHAR(16) CHARACTER SET utf8,"
                + "score INT)";


        String waveInfoTable = "CREATE TABLE IF NOT EXISTS wave_info ("
                + "wave_number VARCHAR(16) CHARACTER SET utf8,"
                + "group_name VARCHAR(16) CHARACTER SET utf8)";


        try {
            statement = connection.createStatement();
            statement.executeUpdate(playersTable);
            statement.executeUpdate(rankingTable);
            statement.executeUpdate(waveInfoTable);

            System.out.println("table created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(String name, List<Integer> scores, List<Integer> wavesPassed, List<Long> durations,
                          int life,
                          int missile, int fireLevel, String weaponType, int food, int maxHeat,
                          String spaceshipColor,
                          String engineColor) {

        String query = " insert into players_datas (user_name, scores, wavesPassed, durations, life,missile,fireLevel,"
                + "food,maxHeat,spaceshipColor,engineColor,weaponType)"
                + " values (?, ?, ?, ?, ?,?,?,?,?,?,?,?)";


        String scoresString = Arrays.toString(scores.toArray());
        String wavesPassedString = Arrays.toString(wavesPassed.toArray());
        String durationsString = Arrays.toString(durations.toArray());


        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt = null;
        try {
            statement = connection.createStatement();
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, name);
            preparedStmt.setString(2, scoresString);
            preparedStmt.setString(3, wavesPassedString);
            preparedStmt.setString(4, durationsString);
            preparedStmt.setInt(5, life);
            preparedStmt.setInt(6, missile);
            preparedStmt.setInt(7, fireLevel);
            preparedStmt.setInt(8, food);
            preparedStmt.setInt(9, maxHeat);
            preparedStmt.setString(10, spaceshipColor);
            preparedStmt.setString(11, engineColor);
            preparedStmt.setString(12, weaponType);

            preparedStmt.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        printTable("players_datas");


    }


    public void updatePlayer(String name, List<Integer> scores, List<Integer> wavesPassed, List<Long> durations,
                             int life,
                             int missile, int fireLevel, String weaponType, int food, int maxHeat,
                             String spaceshipColor,
                             String engineColor) {

        String query = "UPDATE players_datas SET" +
                "  scores = ?," +
                "  wavesPassed = ?," +
                "  durations = ?," +
                "  life = ?," +
                "  missile = ?," +
                "  fireLevel = ?," +
                "  food = ?," +
                "  maxHeat = ?," +
                "  spaceshipColor = ?," +
                "  engineColor = ?," +
                "  weaponType = ?" +
                "WHERE user_name = ?";

        String scoresString = Arrays.toString(scores.toArray());
        String wavesPassedString = Arrays.toString(wavesPassed.toArray());
        String durationsString = Arrays.toString(durations.toArray());


        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt = null;
        try {
            statement = connection.createStatement();
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(12, name);
            preparedStmt.setString(1, scoresString);
            preparedStmt.setString(2, wavesPassedString);
            preparedStmt.setString(3, durationsString);
            preparedStmt.setInt(4, life);
            preparedStmt.setInt(5, missile);
            preparedStmt.setInt(6, fireLevel);
            preparedStmt.setInt(7, food);
            preparedStmt.setInt(8, maxHeat);
            preparedStmt.setString(9, spaceshipColor);
            preparedStmt.setString(10, engineColor);
            preparedStmt.setString(11, weaponType);

            preparedStmt.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        printTable("players_datas");


    }


    public void removePlayer(String name) {
        String query = "DELETE FROM players_datas WHERE user_name=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);

            preparedStatement.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        printTable("players_datas");


    }


    public void creatRankingTable(HashMap<String, Integer> playersScores) {

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM ranking_table");

            String query = " INSERT INTO ranking_table (user_name, score)"
                    + " VALUES (?, ?)";


            for (Map.Entry<String, Integer> entry : playersScores.entrySet()) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, entry.getKey());
                preparedStatement.setInt(2, entry.getValue());

                preparedStatement.execute();

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        printTable("ranking_table");


    }


    public ArrayList<String> getPlayersNames() {
        ArrayList<String> names = new ArrayList<>();
        try {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * from players_datas");

            while (resultSet.next()) {
                String columnValue = resultSet.getString(1);
                names.add(columnValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return names;

    }


    public void creatWaveInfo() {
        Statement statement = null;
        try {



            String query = " INSERT INTO wave_info (wave_number, group_name)"
                    + " VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            Scanner scanner = new Scanner(System.in);

            for (int i = 0; i < 20; i++) {
                preparedStatement.setString(1, String.valueOf(i + 1));
                preparedStatement.setString(2, scanner.nextLine());
                preparedStatement.execute();
                printTable("wave_info");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public String getWaveName(int wave_number){
        try {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * from " + "wave_info");



            while (resultSet.next()) {
                    String number = resultSet.getString(1);
                    if(number.equals(String.valueOf(wave_number))){
                        return resultSet.getString(2);
                    }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }


}
