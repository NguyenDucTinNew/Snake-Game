package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

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

    public void saveScore2player(String username, String username2, int score, int score2) {
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

    public void saveGameSession(String username, String username2, int mapIndex, LocalDateTime gametime,
            LocalDateTime endtime) {
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

    public List<Ranking> getTopRankings(int count) {
        List<Ranking> topRankings = new ArrayList<>();
    
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT player_name, Max(score) as highest_score FROM player_history GROUP BY player_name ORDER BY highest_score DESC LIMIT ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, count);
            ResultSet resultSet = statement.executeQuery();
            
            // có nghĩa là nó sẽ chạy từ đâu cho tới khi chạy hết câu querry
            while (resultSet.next()) {
                // Lấy các cột trong kết quả querry row ở đây là số hàng hiện tại
                int rank = resultSet.getRow();
                // Lấy username bằng cách tìm trong câu querry cái labbel nào = name
                String username = resultSet.getString("player_name");
                //Tương tự
                int score = resultSet.getInt("highest_score");
                Ranking ranking = new Ranking(rank, username, score);
                topRankings.add(ranking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return topRankings;
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

    public void saveHistory(String username, LocalDateTime start, int mapIndex, int score) {
        if (!isUserExists(username)) {
            saveScore1player(username, score);
              try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO player_history (players_id, score, player_name, timestart, map_ids) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            // Lấy player_id của người chơi 1 từ bảng players
            int player1Id = getPlayerIdByUsername(username);

            // Lưu thông tin của người chơi 1
            statement.setInt(1, player1Id);
            statement.setInt(2, score);
            statement.setString(3, username);
            statement.setTimestamp(4, Timestamp.valueOf(start));
            statement.setInt(5, mapIndex);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý ngoại lệ, ví dụ: in ra thông báo lỗi
        }
            return;
        }
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO player_history (players_id, score, player_name, timestart, map_ids) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            // Lấy player_id của người chơi 1 từ bảng players
            int player1Id = getPlayerIdByUsername(username);

            // Lưu thông tin của người chơi 1
            statement.setInt(1, player1Id);
            statement.setInt(2, score);
            statement.setString(3, username);
            statement.setTimestamp(4, Timestamp.valueOf(start));
            statement.setInt(5, mapIndex);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý ngoại lệ, ví dụ: in ra thông báo lỗi
        }
    }

    private boolean isUserExists(String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT COUNT(*) FROM players WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý ngoại lệ, ví dụ: in ra thông báo lỗi
            return false;
        }
    }
    private boolean isHistorySaved ( String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT COUNT(*) FROM player_history WHERE player_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý ngoại lệ, ví dụ: in ra thông báo lỗi
            return false;
        }
    }
    


}