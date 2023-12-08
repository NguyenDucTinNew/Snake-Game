package com.example;

import java.util.Optional;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

public class pregamecontroller {
    private int mapindex;
    private String username;
    private String difficulty = "easy";
    private int hardmode=0;
    private BooleanProperty isHardMode = new SimpleBooleanProperty(false);
    public void setUsername(String username) {
        tftusername.setText(username);
    }

    @FXML
    private ImageView imggrass;

    public String getusername() {
        return username;
    }

    @FXML
    private ImageView imgmap1;

    private BooleanProperty isImgMap1Clicked = new SimpleBooleanProperty(false);

    @FXML
    private ImageView imgmap2;

    private BooleanProperty isImgMap2Clicked = new SimpleBooleanProperty(false);

    @FXML
    private AnchorPane anchorpane;

    @FXML
    private ImageView imgmap3;

    private BooleanProperty isImgMap3Clicked = new SimpleBooleanProperty(false);

    @FXML
    private Label lboptionmap;

    @FXML
    private Label lbusername;

    @FXML
    private TextField tftusername;

    @FXML
    private Pane paneboder1;

    @FXML
    private Pane paneboder2;

    @FXML
    private Pane paneboder3;

    @FXML
    void btnhandleonclickstart(ActionEvent event) {
        String username = tftusername.getText();
        if (isValidUsername(username)) {
            Game1player gamecontroller = new Game1player(username, mapindex,hardmode);
            gamecontroller.startgame();
        } else {
            // Tạo một Dialog tùy chỉnh
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Lỗi");

            // Thêm nội dung và trường nhập tên người dùng
            Label label = new Label(
                    "Tên người dùng phải là số điện thoại hợp lệ gồm 10 số và đầu số thuộc Việt Nam. Vui lòng nhập lại:");
            dialog.getDialogPane().setContent(new VBox(label));

            // Thiết lập nút OK và xử lý sự kiện khi người dùng nhấp vào
            ButtonType buttonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(buttonType);
            // Hiển thị dialog và chờ người dùng nhập tên mới
            dialog.showAndWait();

        }
    }

    @FXML
    void btnimgsgetmapid10(MouseEvent event) {

    }

    @FXML
    void btnimgsgetmapid11(MouseEvent event) {

    }

    @FXML
    private ToggleButton toggetonoffbutton;

    @FXML
    void btnimgsgetmapid12(MouseEvent event) {

    }

    private void handlePaneClick(Pane pane, BooleanProperty isClicked, int mapIndex) {
        if (!isClicked.get()) {
            this.mapindex = mapIndex;
            pane.setStyle("-fx-border-color: yellow; -fx-border-width: 6px; -fx-border-style: solid;");
            isClicked.set(true);

            // Ẩn border của các Pane khác
            if (pane != paneboder1) {
                paneboder1.setStyle("");
                isImgMap1Clicked.set(false);
            }
            if (pane != paneboder2) {
                paneboder2.setStyle("");
                isImgMap2Clicked.set(false);
            }
            if (pane != paneboder3) {
                paneboder3.setStyle("");
                isImgMap3Clicked.set(false);
            }
        } else {
            this.mapindex = 0;
            pane.setStyle("");
            isClicked.set(false);
        }
        this.mapindex = mapIndex;
    }

    @FXML
    public void handleToggle(ActionEvent event) {
        if (toggetonoffbutton.isSelected()) {
            difficulty = "hard";
            imgmap1.setImage(new Image("map1hard.png"));
            imgmap2.setImage(new Image("map2hard.png"));
            imgmap3.setImage(new Image("map3hard.png"));
            anchorpane.setStyle("-fx-background-color: #680000;");
            tftusername.setStyle("-fx-background-color:  #F08080");
            lbusername.setStyle("-fx-text-fill: #b74141;");
            lboptionmap.setStyle("-fx-text-fill: #b74141;");
            imggrass.setVisible(false);
            hardmode=1;


        } else {
            difficulty = "easy";
            imgmap1.setImage(new Image("map1.png"));
            imgmap2.setImage(new Image("map2.png"));
            imgmap3.setImage(new Image("map3.png"));
            anchorpane.setStyle("-fx-background-color: transparent;");
            tftusername.setStyle("-fx-background-color: white;");
            lbusername.setStyle("-fx-text-fill: black;");
            lboptionmap.setStyle("-fx-text-fill: black;");
             imggrass.setVisible(true);
             hardmode=0;

        }
        isHardMode.set(!isHardMode.get());
        anchorpane.getStyleClass().add("toggled");
        toggetonoffbutton.getStyleClass().add("toggled");
        imgmap1.getStyleClass().add("toggled");
        

    }

    @FXML
    public void initialize() {
        paneboder1.setOnMouseClicked(event -> handlePaneClick(paneboder1, isImgMap1Clicked, 10));
        paneboder2.setOnMouseClicked(event -> handlePaneClick(paneboder2, isImgMap2Clicked, 11));
        paneboder3.setOnMouseClicked(event -> handlePaneClick(paneboder3, isImgMap3Clicked, 13));
        if (difficulty.equals("easy")) {
            toggetonoffbutton.setSelected(false);
        } else {
            toggetonoffbutton.setSelected(true);
        }
        StringBinding buttonTextBinding = Bindings.when(isHardMode)
                .then("Khó")
                .otherwise("Dễ");
        toggetonoffbutton.textProperty().bind(buttonTextBinding);
    }

    private boolean isValidUsername(String username) {
        // Kiểm tra username có đáp ứng yêu cầu hợp lệ hay không
        // Ví dụ: Kiểm tra username không rỗng, chỉ chứa chữ số và bắt đầu bằng "03",
        // "05", "07" ,"08" hoặc "09"
        return username.matches("(03|05|07|08|09)\\d{8}");
    }
}