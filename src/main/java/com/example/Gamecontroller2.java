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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.example.DatabaseConnection;
import com.example.usercrud;
import com.example.Gamecontroller;

/**
 * JavaFX App
 */
public class Gamecontroller2 {

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
    private String username2;
    private String username;
    private int mapindex;
    private Point2D snakeHead;
    Gamecontroller snake1 = new Gamecontroller();
    Gamecontroller snake2 = new Gamecontroller();
    private LocalDateTime endTime=null;
    private LocalDateTime startTime=null;
    
    public Gamecontroller2(String Username, int mapid) {
        this.username = Username;
        this.mapindex = mapid;
    }

    public Gamecontroller2(String Username, String Username2, int mapid) {
        this.username = Username;
        this.mapindex = mapid;
        this.username2 = Username2;
    }


    // creter a screen with another fxml file
    public void startgame() {
        //set thời gian start time và end time
        startTime = LocalDateTime.now();
      
        // Cấu hình gamestage và logic game
        try {
            gamestage = new Stage();

            gamestage.setTitle("snakegame");
            Group root = new Group();
            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            root.getChildren().add(canvas);
            Scene scene = new Scene(root);
            gamestage.setScene(scene);
            gc = canvas.getGraphicsContext2D();
            gamestage.show();

            Gamecontroller drawmap = new Gamecontroller();
            Gamecontroller food = new Gamecontroller();
            Gamecontroller scoresnake1 = new Gamecontroller();
            Gamecontroller scoresnake2 = new Gamecontroller();
            // Hàm luôn được gọi khi người dùng nhấp vào phím trên màn hình
            scene.setOnKeyPressed(event -> {
                KeyCode keyCode = event.getCode();

                if (keyCode == KeyCode.W) {
                    if (currentDirection != DOWN) {
                        currentDirection = UP;
                    }
                } else if (keyCode == KeyCode.S) {
                    if (currentDirection != UP) {
                        currentDirection = DOWN;
                    }
                } else if (keyCode == KeyCode.A) {
                    if (currentDirection != RIGHT) {
                        currentDirection = LEFT;
                    }
                } else if (keyCode == KeyCode.D) {
                    if (currentDirection != LEFT) {
                        currentDirection = RIGHT;
                    }
                } else if (keyCode == KeyCode.UP) {
                    if (currentDirection2 != DOWN2) {
                        currentDirection2 = UP2;
                    }
                } else if (keyCode == KeyCode.DOWN) {
                    if (currentDirection2 != UP2) {
                        currentDirection2 = DOWN2;
                    }
                } else if (keyCode == KeyCode.LEFT) {
                    if (currentDirection2 != RIGHT2) {
                        currentDirection2 = LEFT2;
                    }
                } else if (keyCode == KeyCode.RIGHT) {
                    if (currentDirection2 != LEFT2) {
                        currentDirection2 = RIGHT2;
                    }
                }
            });

            for (int i = 0; i < 3; i++) {
                snake1.getbodysnake().add(new Point2D(5, ROWS / 2));
            }
            snake1.setSnakeHead();

            for (int i = 0; i < 3; i++) {
                snake2.getbodysnake().add(new Point2D(10, ROWS / 2));

            }
            snake2.setSnakeHead();

            Maps mapsnake = new Maps();
            DatabaseConnection connection = new DatabaseConnection();
            mapsnake.setdatamap(connection.getMapData(mapindex));

            mapsnake.Drawmap();
            food.generateFood(mapsnake);

            AnimationTimer timer = new AnimationTimer() {
                private long lastUpdate = 0;
                private final long frameDelay = 300_000_000; // Thời gian chờ giữa các khung hình (tính bằng
                                                               // nanosecond)

                @Override
                public void handle(long now) {
                    // Cập nhật và vẽ các yếu tố liên quan đến đồ họa
                    // ...
                    if (now - lastUpdate >= frameDelay) {

                        gc.clearRect(0, 0, WIDTH, HEIGHT);

                        drawmap.drawBackground(gc);
                        food.drawFood(gc);
                        snake1.drawSnake(gc);
                        snake2.drawSnake2(gc);
                        // vẽ score cho rắn 1
                        gc.setFill(Color.WHITE);
                        gc.setFont(new Font("Digital-7", 35));
                        gc.fillText("Score: " + scoresnake1.score, 10, 35);
                        // vẽ score cho rắn 2
                        gc.setFill(Color.RED);
                        gc.setFont(new Font("Digital-7", 35));
                        gc.fillText("Score: " + scoresnake2.score, 600, 35);
                        snake1.run(gc);
                        snake2.run(gc);

                        // Xử lý phím nhấn và cập nhật hướng di chuyển của snake1
                        switch (currentDirection) {
                            case RIGHT:
                                snake1.upRight();
                                break;
                            case LEFT:
                                snake1.upLeft();
                                break;
                            case UP:
                                snake1.upUp();
                                break;
                            case DOWN:
                                snake1.upDown();
                                break;
                        }
                        snake1.setbodysnake(0, snake1.getSnakeHead());

                        // Xử lý phím nhấn và cập nhật hướng di chuyển của snake2
                        switch (currentDirection2) {
                            case RIGHT:
                                snake2.upRight();
                                break;
                            case LEFT:
                                snake2.upLeft();
                                break;
                            case UP:
                                snake2.upUp();
                                break;
                            case DOWN:
                                snake2.upDown();
                                break;
                        }

                        snake2.setbodysnake(0, snake2.getSnakeHead());

                        // Vẽ lại các yếu tố liên quan đến đồ họa

                        if (snake1.getXSnakeHead() < 0 || snake1.getYSnakeHead() < 0
                                || snake1.getXSnakeHead() * SQUARE_SIZE >= WIDTH
                                || snake1.getYSnakeHead() * SQUARE_SIZE >= HEIGHT) {

                            gameOverSnake1 = true;
                        }
                        if (snake2.getXSnakeHead() < 0 || snake2.getYSnakeHead() < 0
                                || snake2.getXSnakeHead() * SQUARE_SIZE >= WIDTH
                                || snake2.getYSnakeHead() * SQUARE_SIZE >= HEIGHT) {

                            gameOverSnake2 = true;
                        }
                        if (checkCollisionSnake1()) {
                            gameOverSnake1 = true;
                        }

                        if (checkCollisionSnake2()) {
                            gameOverSnake2 = true;
                        }
                        // hàm ăn
                        if (snake1.getXSnakeHead() == food.foodX && snake1.getYSnakeHead() == food.foodY) {
                            snake1.addbody();
                            food.generateFood(mapsnake);
                            scoresnake1.score += 5;
                        }
                        // ăn
                        if (snake2.getXSnakeHead() == food.foodX && snake2.getYSnakeHead() == food.foodY) {
                            snake2.addbody();
                            food.generateFood(mapsnake);
                            scoresnake2.score += 5;
                        }
                        // vat can
                        if (snake1.getXSnakeHead() < 0 || snake1.getYSnakeHead() < 0
                                || snake1.getXSnakeHead() * SQUARE_SIZE >= WIDTH
                                || snake1.getYSnakeHead() * SQUARE_SIZE >= HEIGHT
                                || checkCollisionWithWalls1(mapsnake)) {
                            gameOverSnake1 = true;
                        }
                        if (snake2.getXSnakeHead() < 0 || snake2.getYSnakeHead() < 0
                                || snake2.getXSnakeHead() * SQUARE_SIZE >= WIDTH
                                || snake2.getYSnakeHead() * SQUARE_SIZE >= HEIGHT
                                || checkCollisionWithWalls2(mapsnake)) {
                            gameOverSnake2 = true;
                        }
                        //đụng nhau chết
                        if(checkCollisionwithanothersnake())
                        {
                        gameOverSnake1=true;
                        }
                        
                        // gamae over
                        if (gameOverSnake1 || gameOverSnake2) {
                            gc.setFill(Color.RED);
                            gc.setFont(new Font("Digital-7", 70));
                            gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2);
                            endTime = LocalDateTime.now();
                            DatabaseConnection gamesave = new DatabaseConnection();
                            gamesave.saveScore2player(username, username2, scoresnake1.score,scoresnake2.score);
                            gamesave.saveGameSession(username, username2, mapindex, startTime, endTime);
                            stop();
                        }

                        

                        lastUpdate = now;

                    }
                }

            };
            timer.start();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private boolean checkCollisionSnake1() {
        for (int i = 1; i < snake1.getBodysize(); i++) {
            if (snake1.getXSnakeHead() == snake1.getXsnakebody(i)
                    && snake1.getYSnakeHead() == snake1.getYsnakebody(i)) {
                return true; // Nếu có va chạm, trả về true
            }
        }
        return false; // Nếu không có va chạm, trả về false
    }

    private boolean checkCollisionSnake2() {
        for (int i = 1; i < snake2.getBodysize(); i++) {
            if (snake2.getXSnakeHead() == snake2.getXsnakebody(i)
                    && snake2.getYSnakeHead() == snake2.getYsnakebody(i)) {
                return true; // Nếu có va chạm, trả về true
            }
        }
        return false; // Nếu không có va chạm, trả về false
    }

    private boolean checkCollisionWithWalls1(Maps mapsnake) {
        // Kiểm tra xem đầu rắn có va chạm với các tường trong bản đồ hay không
        double wall = 0;
        int x = (int) snake1.getXSnakeHead();
        int y = (int) snake1.getYSnakeHead();
        if (mapsnake.map[x][y] == 1) {
            return true; // Đầu rắn va chạm với tường
        }
        return false; // Đầu rắn không va chạm với tường
    }

    private boolean checkCollisionWithWalls2(Maps mapsnake) {
        // Kiểm tra xem đầu rắn có va chạm với các tường trong bản đồ hay không
        double wall = 0;
        int x = (int) snake2.getXSnakeHead();
        int y = (int) snake2.getYSnakeHead();
        if (mapsnake.map[x][y] == 1) {
            return true; // Đầu rắn va chạm với tường
        }
        return false; // Đầu rắn không va chạm với tường
    }
      private boolean checkCollisionwithanothersnake() {
        for (int i = 1; i < snake1.getBodysize(); i++) {
            if (snake2.getXSnakeHead() == snake1.getXsnakebody(i)
                    && snake2.getYSnakeHead() == snake1.getYsnakebody(i)) {
                return true; // Nếu có va chạm, trả về true
            }
        }
         for (int i = 1; i < snake2.getBodysize(); i++) {
            if (snake1.getXSnakeHead() == snake2.getXsnakebody(i)
                    && snake1.getYSnakeHead() == snake2.getYsnakebody(i)) {
                return true; // Nếu có va chạm, trả về true
            }
        }
        
        return false; // Nếu không có va chạm, trả về false
    }
    
    

    /*
     * scene = new Scene(loadFXML("menugame"), 640, 480);
     * stage.setScene(scene);
     * stage.show();
     */

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

}