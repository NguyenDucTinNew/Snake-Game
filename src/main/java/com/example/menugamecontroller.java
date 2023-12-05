package com.example;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class menugamecontroller {

    @FXML
    private ImageView grassbackgound;

    public void initialize() {
        String imagePath = "/grassbackground.png";
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        grassbackgound.setImage(image);
        // Tạo các cột
        // tạo các cột Ranking mang các giá trị tương ứng
        // lấy các giá trị của Ranking tương ứng (property) và gén nó cho cột
        TableColumn<Ranking, Integer> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));

        TableColumn<Ranking, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Ranking, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        tbview.setStyle("-fx-background-color: #ffffff;");
        // Chỉnh màu cho các hàng
        tbview.setRowFactory(tv -> new TableRow<Ranking>() {
            @Override
            protected void updateItem(Ranking item, boolean empty) {
                super.updateItem(item, empty);
                if (getIndex() % 2 == 0) {
                    setStyle("-fx-background-color: #2E8B57;"); // Màu nền cho hàng chẵn
                } else {
                    setStyle("-fx-background-color: #006400;"); // Màu nền cho hàng lẻ
                }
            }
        });
        // chỉnh màu cho các cột
        rankColumn.setStyle("-fx-text-fill: #A52A2A;"); // Màu chữ cho cột "Rank"
        usernameColumn.setStyle("-fx-text-fill: #DAA520;"); // Màu chữ cho cột "Username"
        scoreColumn.setStyle("-fx-text-fill: #1E90FF;"); // Màu chữ cho cột "Score"
        

        // Thêm các cột vào TableView
        tbview.getColumns().addAll(rankColumn, usernameColumn, scoreColumn);

        // Khởi tạo danh sách ObservableList để lưu trữ dữ liệu
        ObservableList<Ranking> rankings = FXCollections.observableArrayList();

        // Lấy dữ liệu từ cơ sở dữ liệu của 5 người chơi cao điểm nhất
        DatabaseConnection databaseConnection = new DatabaseConnection();
        List<Ranking> topRankings = databaseConnection.getTopRankings(5);

        // Thêm dữ liệu vào danh sách ObservableList
        rankings.addAll(topRankings);

        // Gán danh sách ObservableList làm dữ liệu nguồn cho TableView
        tbview.setItems(rankings);
        // gán giá trị cho các cột
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

    }

    @FXML
    private ImageView logogame;

    @FXML
    private TableView<Ranking> tbview;

    @FXML
    void btn1player(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("pregame.fxml"));
        Parent parent = loader.load();
        pregamecontroller pregameController = loader.getController();
        pregameController.initialize();
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        Stage stage = (Stage) grassbackgound.getScene().getWindow();
        stage.setScene(scene);

        stage.show();
    }

    @FXML
    void btn2player(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pregame2.fxml"));
        Parent parent = loader.load();
        pregamecontroller2 pregameController = loader.getController();
        pregameController.initialize();
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        Stage stage = (Stage) grassbackgound.getScene().getWindow();
        stage.setScene(scene);

        stage.show();
    }

}
