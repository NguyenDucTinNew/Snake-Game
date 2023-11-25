package com.example;

import com.example.Gamecontroller;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.DatabaseConnection;
import com.example.usercrud;
import com.example.Gamecontroller;

/**
 * JavaFX App
 */
public class App extends Application {

    // Call game controller1 như là snake và gọi 2 còn rắn
    boolean gameOverSnake1 = false;
    boolean gameOverSnake2 = false;
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private GraphicsContext gc;
    private int currentDirection;
    private int currentDirection2;
    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final double SQUARE_SIZE = WIDTH / ROWS;// đơn vị ô 40x40px
    private static final int RIGHT2 = 0;
    private static final int LEFT2 = 1;
    private static final int UP2 = 2;
    private static final int DOWN2 = 3;
    private static Scene scene;
    public static Stage gamestage;
    private boolean running = true;
    private static final Object moveLock = new Object();
    private static final Object moveLock2 = new Object();
    private volatile boolean moveUp1 = false;
    private volatile boolean moveUp2 = false;
    private volatile boolean moveDown1 = false;
    private volatile boolean moveDown2 = false;
    private volatile boolean moveRight1 = false;
    private volatile boolean moveRight2 = false;
    private volatile boolean moveLeft1 = false;
    private volatile boolean moveLeft2 = false;
    private Point2D snakehead1;
    private Point2D snakehead2;
    Gamecontroller snake1 = new Gamecontroller();
    Gamecontroller snake2 = new Gamecontroller();

    @Override
    // creter a screen with another fxml file
    public void start(Stage stage) throws IOException {
       
    
     scene = new Scene(loadFXML("menugame"), 640, 480);
     stage.setScene(scene);
     stage.show();
     
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {

        launch();
    }

}