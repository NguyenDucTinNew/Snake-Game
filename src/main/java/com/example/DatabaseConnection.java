package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/snakegame";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void saveScore1player(String username, int score) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO players (name, scorest) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setInt(2, score);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý ngoại lệ, ví dụ: in ra thông báo lỗi
        }
    }

    public void saveScore2player (String username , String username2 , int score , int score2)
    {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO players (name, scorest) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            
            // Lưu thông tin của người chơi 1
            statement.setString(1, username);
            statement.setInt(2, score);
            statement.executeUpdate();
    
            // Lưu thông tin của người chơi 2
            statement.setString(1, username2);
            statement.setInt(2, score2);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý ngoại lệ, ví dụ: in ra thông báo lỗi
        }
    }

    
   
    public void saveMap(String datamap) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO maps (map_data) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, datamap);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getMapData(int dataId) {
        String mapData = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT map_data FROM maps WHERE map_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, dataId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                mapData = resultSet.getString("map_data");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapData;
    }

    public void saveGameSession(String username, String username2, int mapIndex ,LocalDateTime gametime, LocalDateTime endtime) {
    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        String query = "INSERT INTO game_sessions (player1_id, player2_id, map_id, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);

        // Lấy player_id của người chơi 1 từ bảng players
        int player1Id = getPlayerIdByUsername(username);

        // Lấy player_id của người chơi 2 từ bảng players
        int player2Id = getPlayerIdByUsername(username2);

        // Lưu thông tin của người chơi 1
        statement.setInt(1, player1Id);
        statement.setInt(2, player2Id);
        statement.setInt(3, mapIndex);
        statement.setTimestamp(4, Timestamp.valueOf(gametime));
        statement.setTimestamp(5, Timestamp.valueOf(endtime));
        
        statement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace(); // Xử lý ngoại lệ, ví dụ: in ra thông báo lỗi
    }
}

private int getPlayerIdByUsername(String username) {
    int playerId = -1;
    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        String query = "SELECT player_id FROM players WHERE name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            playerId = resultSet.getInt("player_id");
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Xử lý ngoại lệ, ví dụ: in ra thông báo lỗi
    }
    return playerId;
}
     

}