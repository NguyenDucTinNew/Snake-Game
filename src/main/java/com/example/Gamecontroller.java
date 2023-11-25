package com.example;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import java.util.Map;

import com.example.DatabaseConnection;
import com.example.usercrud;
import com.example.Maps;

/**
 * JavaFX App
 */
public class Gamecontroller {
    private String username2;
    private String username;
    private int mapindex;
    private Point2D snakeHead;

    public Gamecontroller(String Username) {
        this.username = Username;
    }

    // ????
    public Point2D getSnakeHead() {
        return snakeHead;
    }

    public Gamecontroller(String Username, int mapid) {
        this.username = Username;
        this.mapindex = mapid;
    }

    public Gamecontroller(String Username, String Username2, int mapid) {
        this.username = Username;
        this.mapindex = mapid;
        this.username2 = Username2;
    }

    public Gamecontroller(GraphicsContext gc) {
        this.gc = gc;
    }

    public Gamecontroller() {
    }

    public String Getusername() {
        return username;
    }

    public void upRight() {
        Point2D moverighthead = new Point2D(snakeHead.getX() + 1, snakeHead.getY());
        snakeHead = moverighthead;

    }

    public double getXSnakeHead()
    {
        return  snakeHead.getX();
    }
     public double getYSnakeHead()
    {
        return  snakeHead.getY();
    }

    public int getBodysize() {
        return snakeBody.size();
    }

    public List<Point2D> getbodysnake() {
        return snakeBody;
    }

    public void setbodysnake(List<Point2D> snakebody1) {
        snakeBody = snakebody1;
    }

    public void setbodysnake(int i, Point2D temp) {
        snakeBody.set(i, temp);
    }

    // lấy vị trí trừ 1 trong snake
    public double getXsnakebodyn1(int i) {
        return snakeBody.get(i - 1).getX();
    }// lấy Y từ vị trí i

    public double getYsnakebody(int i) {
        return snakeBody.get(i).getY();
    }
       public double getXsnakebody(int i) {
        return snakeBody.get(i).getX();
    }


    public double getYsnakebodyn1(int i) {
        return snakeBody.get(i - 1).getY();
    }

    // gén đầu rắn
    public void setSnakeHead() {
        snakeHead = snakeBody.get(0);
    }

    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final double SQUARE_SIZE = WIDTH / ROWS;// đơn vị ô 40x40px
    private static final String[] FOODS_IMAGE = new String[] { "/ic_orange.png", "/ic_apple.png",
            "/ic_cherry.png",
            "/ic_berry.png", "/ic_coconut_.png", "/ic_peach.png", "/ic_watermelon.png",
            "/ic_orange.png",
            "/ic_pomegranate.png" };

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private static final int RIGHT2 = 0;
    private static final int LEFT2 = 1;
    private static final int UP2 = 2;
    private static final int DOWN2 = 3;
    private static final Image wall = new Image("/wall.png");
    // private static double [][] map = new double[ROWS][COLUMNS];

    private GraphicsContext gc;
    private List<Point2D> snakeBody = new ArrayList();
    private List<Point2D> snakeBody2 = new ArrayList();

    private Point2D snakeHead2;
    private Image foodImage;
    public int foodX;
    public int foodY;
    private boolean gameOver;
    private int currentDirection;
    private int currentDirection2;
    public int score = 0;
    private int score2 = 0;
    public static Stage gamestage;
    private Maps mapsnake; // Biến thành viên để lưu trữ đối tượng Maps

    // creter a screen with another fxml file
    public void startgame() {

        // generateFood();
    }

    /*
     * scene = new Scene(loadFXML("test"), 640, 480);
     * stage.setScene(scene);
     * stage.show();
     */
    // vẽ foodImage vào vị trí foodx foody với square là đơn vị hàng (Weight / rows
    // == đơn vị cho 1 ô , 2 cái cuối là kích thươc add kích thước nó bằng với 1 ô
    // map)
    public void drawFood(GraphicsContext gc) {

        gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    public void run(GraphicsContext gc) {
          
        for(int i = snakeBody.size() -1 ; i>=1 ;i--)
        {
            Point2D tempbody = new Point2D(snakeBody.get(i-1).getX(), snakeBody.get(i-1).getY());
            snakeBody.set(i, tempbody);
        }
    
    
    }

    public void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.web("4674E9"));// lay mau
        // 2 cái đuôi 35 dưới đẻ lấy bo tròn , kích thước -1 để đảm bảo rắng không vượt
        // quá 1 ô (đơn vị) ô = weight / row
        // khác với fillrect là chỉ fill full hình chữ nhật thì fillroundrect vẽ bo tròn
        // với setting được kích thước fill màu
        // nhân vào để tính đơn vị ô lúc này tính đang ở ô mấy
        // mảng bang đầu đã chia thành đơn vị ô để dễ fill dự và SQuare_Size
        gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE - 1,
                SQUARE_SIZE - 1, 35, 35);
        // quét full body của rắn tình sao khi đã fill đầu răng thì tô từ đầu rắn về
        // đuôi chạy từ 1 lý do là đầu răng đã được fill đầu rắng là snakebody(0)
        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE,
                    SQUARE_SIZE - 1, SQUARE_SIZE - 1, 30, 30);
        }

    }
     public void drawSnake2(GraphicsContext gc) {
        gc.setFill(Color.web("#000000"));// lay mau
        // 2 cái đuôi 35 dưới đẻ lấy bo tròn , kích thước -1 để đảm bảo rắng không vượt
        // quá 1 ô (đơn vị) ô = weight / row
        // khác với fillrect là chỉ fill full hình chữ nhật thì fillroundrect vẽ bo tròn
        // với setting được kích thước fill màu
        // nhân vào để tính đơn vị ô lúc này tính đang ở ô mấy
        // mảng bang đầu đã chia thành đơn vị ô để dễ fill dự và SQuare_Size
        gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE - 1,
                SQUARE_SIZE - 1, 35, 35);
        // quét full body của rắn tình sao khi đã fill đầu răng thì tô từ đầu rắn về
        // đuôi chạy từ 1 lý do là đầu răng đã được fill đầu rắng là snakebody(0)
        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE,
                    SQUARE_SIZE - 1, SQUARE_SIZE - 1, 30, 30);
        }

    }

    public void drawBackground(GraphicsContext gc) {

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (mapsnake.map[i][j] == 0) {
                    // Ô trống
                    if ((i + j) % 2 == 0) {
                        gc.setFill(Color.web("AAD751"));
                        gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    } else {
                        gc.setFill(Color.web("A2D149"));
                        gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    }
                } else if (mapsnake.map[i][j] == 1) {
                    // Ô chứa vật cản
                    gc.drawImage(wall, i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                }

            }
        }
    }
    /*
     * // fill màu cho map
     * private void drawBackground(GraphicsContext gc)
     * {
     * for (int i = 0; i < ROWS; i++) {
     * for (int j = 0; j < COLUMNS; j++) {
     * if ((i + j) % 2 == 0) {
     * gc.setFill(Color.web("AAD751"));
     * } else {
     * gc.setFill(Color.web("A2D149"));
     * }
     * gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
     * }
     * }
     * }
     */

    // hàm tạo thức ăn thui và nó sẽ được recall liên tục đó
    public void generateFood(Maps mapsnake) {
        start: while (true) {
            // gán giá trị ngẫu nhiên từ 0 đến 20 rùi ép kiểu int
            foodX = (int) (Math.random() * ROWS);
            foodY = (int) (Math.random() * COLUMNS);
            // kiểm tra coi nó phải vật cảng không
            if (mapsnake.map[foodX][foodY] == 1) {
                continue start;
            }

            // check coi cái food mới respone ra có trung với con rằng không
            // chạy hết người con rắn coi coi nó có đồ ăn mới tạo ở trong đó không
            for (Point2D snake : snakeBody) {
                if (snake.getX() == foodX && snake.getY() == foodY) {
                    continue start;
                }
            }

            // lấy 1 cái hình từ file img hàm ở dưới là lấy đại 1 cái hình ngẫy nhiên trong
            // mảng FOODS_IMAGES
            foodImage = new Image(FOODS_IMAGE[(int) (Math.random() * FOODS_IMAGE.length)]);

            break;
        }
    }

    private void moveRight() {
        Point2D moverighthead = new Point2D(snakeHead.getX() + 1, snakeHead.getY());
        snakeHead = moverighthead;
    }

    private boolean checkCollisionWithWalls(Maps mapsnake) {
        // Kiểm tra xem đầu rắn có va chạm với các tường trong bản đồ hay không
        double wall = 0;
        int x = (int) snakeHead.getX();
        int y = (int) snakeHead.getY();
        if (mapsnake.map[x][y] == 1) {
            return true; // Đầu rắn va chạm với tường
        }
        return false; // Đầu rắn không va chạm với tường
    }

    private void moveLeft() {
        Point2D moveLefthead = new Point2D(snakeHead.getX() - 1, snakeHead.getY());
        snakeHead = moveLefthead;
    }

    public void upLeft() {
        Point2D moveLefthead = new Point2D(snakeHead.getX() - 1, snakeHead.getY());
        snakeHead = moveLefthead;

    }

    private void moveUp() {
        Point2D moveUphead = new Point2D(snakeHead.getX(), snakeHead.getY() - 1);
        snakeHead = moveUphead;
    }

    public void upUp() {
        Point2D moveUphead = new Point2D(snakeHead.getX(), snakeHead.getY() - 1);
        snakeHead = moveUphead;
    }

    private void moveDown() {
        Point2D moveDownhead = new Point2D(snakeHead.getX(), snakeHead.getY() + 1);
        snakeHead = moveDownhead;
    }

    public void upDown() {
        Point2D moveDownhead = new Point2D(snakeHead.getX(), snakeHead.getY() + 1);
        snakeHead = moveDownhead;

    }

    // cho đụng nó chết
    // sao này mà gét map thì thêm tọa độ chướng ngại vật vào map nếu cái đàu nó
    // đụng dzo thì ón die hoyy
    // tạo thêm luông logic để cho thim 1 con nữa và lưu điểm tạo map
    public void gameOver() {
        if (snakeHead.getX() < 0 || snakeHead.getY() < 0 || snakeHead.getX() * SQUARE_SIZE >= WIDTH
                || snakeHead.getY() * SQUARE_SIZE >= HEIGHT) {

            gameOver = true;
               
        }

        // vat can
        if (snakeHead.getX() < 0 || snakeHead.getY() < 0 || snakeHead.getX() * SQUARE_SIZE >= WIDTH
                || snakeHead.getY() * SQUARE_SIZE >= HEIGHT || checkCollisionWithWalls(mapsnake)) {
            gameOver = true;
              
        }

        // destroy itself
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.getX() == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY()) {
                gameOver = true;
                
                break;
            }
             
             
        }
     
    }

    public void eatFood() {
        if (snakeHead.getX() == foodX && snakeHead.getY() == foodY) {
            snakeBody.add(new Point2D(-1, -1));
            generateFood(mapsnake);
            score += 5;
        }
    }

    public void drawScore(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);

    }
      public void drawScore2(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 350);

    }

    public void addbody()
    {
        snakeBody.add(new Point2D(-1, -1));
    }


    // Hàm xử lí mỗi khung hình đồng bọ trên kia là hàm xử lí khi có sự kiện nhấn
    // nút

}
