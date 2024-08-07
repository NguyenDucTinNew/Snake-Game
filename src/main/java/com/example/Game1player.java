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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
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
import java.time.LocalDateTime;

/**
 * JavaFX App
 */
public class Game1player {
    private String username2;
    private String username;
    private int mapindex;
    private Point2D snakeHead;
    private int hardmode;

    public Game1player(String Username) {
        this.username = Username;
    }

    public Point2D getSnakeHead() {
        return snakeHead;
    }

    public Game1player(String Username, int mapid, int modehard) {
        this.username = Username;
        this.mapindex = mapid;
        hardmode = modehard;
    }

    public Game1player(String Username, int mapid) {
        this.username = Username;
        this.mapindex = mapid;
    }

    public Game1player(String Username, String Username2, int mapid) {
        this.username = Username;
        this.mapindex = mapid;
        this.username2 = Username2;
    }

    public Game1player(GraphicsContext gc) {
        this.gc = gc;
    }

    public Game1player() {
    }

    public String Getusername() {
        return username;
    }

    public void upRight() {
        Point2D moverighthead = new Point2D(snakeHead.getX() + 1, snakeHead.getY());
        snakeHead = moverighthead;
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

    // gén đầu rắn
    public void setSnakeHead(Point2D snakeheadd) {
        snakeBody.set(0, snakeheadd);
    }

    private LocalDateTime startTime = null;
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

    private Timeline timeline;
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private static final int RIGHT2 = 0;
    private static final int LEFT2 = 1;
    private static final int UP2 = 2;
    private static final int DOWN2 = 3;
    private static final Image wall = new Image("/wall.png");
    private static final Image headup = new Image("/daulen.png");
    private static final Image headown = new Image("/dauxuong.png");
    private static final Image headright = new Image("/dauphai.png");
    private static final Image headleft = new Image("/dautrai.png");
    private int Health = 3;
    private static final Image Healthimg = new Image("/NewPiskel.png");

    private static final Image boomImage = new Image("/bom.png");
    private static final Image lavaImage = new Image("/lava.png");
    private int bodysnakeleght = 3;
    private GraphicsContext gc;
    private List<Point2D> snakeBody = new ArrayList();
    private List<Point2D> snakeBody2 = new ArrayList();

    private Point2D snakeHead2;
    private Image foodImage;
    private String collisionMessagewall = "Bạn đã va chạm vào tường!";
    private String collisionMessagewallmap = "Bạn đã va chạm vào vật cản!";
    private String collisionMessagebody = "Bạn đã va chạm vào bản thân";
    private String collisionMessageboom = "Bạn đã va chạm vào boom!";
    private int notificationCount = 0;
    private int foodX;
    private int foodY;
    private int boomX;
    private int boomY;
    private boolean gameOver;
    private int currentDirection;
    private int currentDirection2;
    private int score = 0;
    private int scorefollow = 3;
    private int countboom = 0;
    public static Stage gamestage;
    private Maps mapsnake; // Biến thành viên để lưu trữ đối tượng Maps
    Group root = new Group();
    private Button restartButton = new Button("Chơi lại");

    // creter a screen with another fxml file
    public void startgame() {

        try {
            startTime = LocalDateTime.now();
            gamestage = new Stage();
            gamestage.setTitle("snakegame");
            Group root = new Group();
            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            root.getChildren().add(canvas);
            restartButton.setOnAction(event -> {
                // Xử lý sự kiện khi nút chơi lại được nhấn
                Restartgamewhengameover(); // Phương thức để khởi động lại trò chơi
            });

            // Thêm nút "Chơi lại" vào pane
            root.getChildren().add(restartButton);

            // Đặt vị trí nút chơi lại dưới chữ "Game Over"
            restartButton.setTranslateX(WIDTH / 2.3);
            restartButton.setTranslateY(HEIGHT / 2 + 50); // Cộng thêm 50 để di chuyển nút xuống dưới
            restartButton.setStyle("-fx-background-color: orange; -fx-font-size: 24px; -fx-text-fill: black;");
            restartButton.setVisible(false);
            Scene scene = new Scene(root);
            gamestage.setScene(scene);
            gamestage.show();
            gc = canvas.getGraphicsContext2D();

            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    // gọi biến khi ấn tạo sự kiện cho rắn là code
                    KeyCode code = event.getCode();
                    // set điều kiệu cho Code bằng Nút D hoặc Right dấu mũi tên
                    if (code == KeyCode.RIGHT) {
                        // Để cho người dùng hiểu cơ chế nhìn từ góc nhìn của người dùng
                        // ví dụ đang quẹo phải bấm quẹo trái sẽ không được. Mún quẹo trái tính theo đầu
                        // rắn phải bấm lên
                        // Chạy từ góc nhìn của ng dùng
                        if (currentDirection != LEFT) {
                            currentDirection = RIGHT;
                        }
                    } else if (code == KeyCode.D) {
                        // Để cho người dùng hiểu cơ chế nhìn từ góc nhìn của người dùng
                        // ví dụ đang quẹo phải bấm quẹo trái sẽ không được. Mún quẹo trái tính theo đầu
                        // rắn phải bấm lên
                        // Chạy từ góc nhìn của ng dùng
                        if (currentDirection2 != LEFT2) {
                            currentDirection2 = RIGHT2;
                        }
                    } else if (code == KeyCode.LEFT) {
                        if (currentDirection != RIGHT) {
                            currentDirection = LEFT;
                        }
                    } else if (code == KeyCode.A) {
                        if (currentDirection2 != RIGHT2) {
                            currentDirection2 = LEFT2;
                        }
                    } else if (code == KeyCode.UP) {
                        if (currentDirection != DOWN) {
                            currentDirection = UP;
                        }
                    } else if (code == KeyCode.W) {
                        if (currentDirection2 != DOWN2) {
                            currentDirection2 = UP2;
                        }
                    } else if (code == KeyCode.DOWN) {
                        if (currentDirection != UP) {
                            currentDirection = DOWN;
                        }
                    } else if (code == KeyCode.S) {
                        if (currentDirection2 != UP2) {
                            currentDirection2 = DOWN2;
                        }
                    }
                }
            });

            for (int i = 0; i < bodysnakeleght; i++) {
                snakeBody.add(new Point2D(5, ROWS / 2));
                // snakeBody2.add(new Point2D(10, ROWS / 2));
            }
            snakeHead = snakeBody.get(0);
            mapsnake = new Maps();
            // lúc có giao diện sẽ cho người dùng chọn các img map để pust hình đó vào map
            // index đây là ví dụ lấy map số 2
            DatabaseConnection connection = new DatabaseConnection();
            mapsnake.setdatamap(connection.getMapData(mapindex));
            Health = 3;
            mapsnake.Drawmap();
            // pushmaplensever
            /*
             * DatabaseConnection connection = new DatabaseConnection();
             * mapsnake.Pullmap(3);
             * connection.saveMap(mapsnake.getdatamap());
             */
            if (hardmode == 1) {
                generateBoom();
            }
            generateFood();

            timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc)));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    /*
     * scene = new Scene(loadFXML("test"), 640, 480);
     * stage.setScene(scene);
     * stage.show();
     */
    // vẽ foodImage vào vị trí foodx foody với square là đơn vị hàng (Weight / rows
    // == đơn vị cho 1 ô , 2 cái cuối là kích thươc add kích thước nó bằng với 1 ô
    // map)
    private void drawFood(GraphicsContext gc) {

        gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    private void drawBoom(GraphicsContext gc) {

        gc.drawImage(boomImage, boomX * SQUARE_SIZE, boomY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    public void Restartgame() {
        currentDirection = RIGHT;
        snakeBody.clear();
        gameOver = false;
        bodysnakeleght = scorefollow / 2;
        scorefollow = bodysnakeleght;
        Point2D headPosition = new Point2D(5, ROWS / 2); // Lưu trữ tọa độ của snakeHead

        for (int i = 0; i < bodysnakeleght; i++) {
            snakeBody.add(headPosition); // Sử dụng headPosition để tạo các phần tử trong snakeBody
        }
        snakeHead = snakeBody.get(0);
        restartButton.setVisible(false);
        new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void Restartgamewhengameover() {
        currentDirection = RIGHT;
        bodysnakeleght = 3;
        gameOver = false;
        Health = 3;
        scorefollow = 3;
        score = 0;
        snakeBody.clear();
        for (int i = 0; i < bodysnakeleght; i++) {
            snakeBody.add(new Point2D(5, ROWS / 2));
            // snakeBody2.add(new Point2D(10, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);
        restartButton.setVisible(false);
        new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    private void run(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2);
            DatabaseConnection connection = new DatabaseConnection();
            connection.saveHistory(username, startTime, mapindex, score);
            restartButton.setVisible(true);
            timeline.stop();
            return;
        }

        drawBackground(gc);
        drawSnake(gc);
        if (hardmode == 1) {
            drawBoom(gc);
        }
        drawFood(gc);

        if (Health == 3) {
            drawHealth3();
        } else if (Health == 2) {
            drawHealth2();
        } else if (Health == 1) {
            drawHealth1();
        }
        drawScore();

        // tru 1 point trong snake để không xuất hiện màu của con rắn cũ hàm xóa vết nè
        // và cho còn rắn bò nè
        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            Point2D snakebodytemp = new Point2D(snakeBody.get(i - 1).getX(), snakeBody.get(i - 1).getY());
            snakeBody.set(i, snakebodytemp);
        } // move funtione of snake 2
        /*
         * for (int i = snakeBody2.size() - 1; i >= 1; i--) {
         * Point2D snakebodytemp2 = new Point2D(snakeBody2.get(i - 1).getX(),
         * snakeBody2.get(i - 1).getY());
         * snakeBody2.set(i, snakebodytemp2);
         * }
         */

        switch (currentDirection) {
            case RIGHT:
                moveRight();
                gc.drawImage(headright, snakeHead
                        .getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                break;
            case LEFT:
                moveLeft();
                gc.drawImage(headleft, snakeHead
                        .getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                break;
            case UP:
                moveUp();
                gc.drawImage(headup, snakeHead
                        .getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                break;
            case DOWN:
                moveDown();
                gc.drawImage(headown, snakeHead
                        .getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                break;
        }

        snakeBody.set(0, snakeHead);
        if(hardmode ==1)
        {
        countboom++;
        }
        eatFood();
        if (countboom == 70) {
            generateBoom();
        }

        gameOver();

    }

    public void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.web("4674E9"));// lay mau
        // 2 cái đuôi 35 dưới đẻ lấy bo tròn , kích thước -1 để đảm bảo rắng không vượt
        // quá 1 ô (đơn vị) ô = weight / row
        // khác với fillrect là chỉ fill full hình chữ nhật thì fillroundrect vẽ bo tròn
        // với setting được kích thước fill màu
        // nhân vào để tính đơn vị ô lúc này tính đang ở ô mấy
        // mảng bang đầu đã chia thành đơn vị ô để dễ fill dự và SQuare_Size
        // gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() *
        // SQUARE_SIZE, SQUARE_SIZE - 1,
        // SQUARE_SIZE - 1, 35, 35);
        // quét full body của rắn tình sao khi đã fill đầu răng thì tô từ đầu rắn về
        // đuôi chạy từ 1 lý do là đầu răng đã được fill đầu rắng là snakebody(0)
        for (int i = 0; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE,
                    SQUARE_SIZE - 1, SQUARE_SIZE - 1, 30, 30);
        }

    }

    public void drawBackground(GraphicsContext gc) {

        if (hardmode == 1) {
            for(int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    if (mapsnake.map[i][j] == 0) {
                        // Ô trống
                        if ((i + j) % 2 == 0) {
                            gc.setFill(Color.web("#380000"));
                            gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                        } else {
                            gc.setFill(Color.web("#380000"));
                            gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                        }
                    } else if (mapsnake.map[i][j] == 1) {
                        // Ô chứa vật cản
                        gc.drawImage(lavaImage, i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    }

                }
            }
        }
        else
        {
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
    private void generateFood() {
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

    private void generateBoom() {
        start: while (true) {
            // gán giá trị ngẫu nhiên từ 0 đến 20 rùi ép kiểu int
            boomX = (int) (Math.random() * ROWS);
            boomY = (int) (Math.random() * COLUMNS);
            // kiểm tra coi nó phải vật cảng không
            if (mapsnake.map[boomX][boomY] == 1) {
                continue start;
            }

            // check coi cái boom mới respone ra có trung với con rằng không
            // chạy hết người con rắn coi coi nó có boom tạo ở trong đó không
            for (Point2D snake : snakeBody) {
                if (snake.getX() == boomX && snake.getY() == boomY) {
                    continue start;
                }
            }
            countboom = 0;
            break;
        }
    }

    private void moveRight() {
        Point2D moverighthead = new Point2D(snakeHead.getX() + 1, snakeHead.getY());
        snakeHead = moverighthead;
    }

    private boolean checkCollisionWithWalls() {
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
        if (notificationCount >= 1000000) {
            collisionMessagewall = ""; // Đặt lại thông báo về giá trị rỗng
            collisionMessagebody = "";
            collisionMessageboom = "";
        }
        notificationCount++;
        // tong vào 4 vách tường
        if (snakeHead.getX() < 0 || snakeHead.getY() < 0 || snakeHead.getX() * SQUARE_SIZE >= WIDTH
                || snakeHead.getY() * SQUARE_SIZE >= HEIGHT) {
            Health--;

            if (Health < 0) {
                gameOver = true;
            } else {

                timeline.stop();

                Restartgame();
                return; // Kết thúc hàm gameOver() sau khi gọi Restartgame()
            }

        }
        // Dính Boom
        if (snakeHead.getX() == boomX && snakeHead.getY() == boomY) {
            gc.setFill(Color.GRAY);
            gc.setFont(new Font("Digital-7", 25));
            collisionMessageboom = "Bạn đã va vào boom";
            gc.fillText(collisionMessageboom, 250, 60); // Thay x và y bằng tọa độ hiển thị thông báo
            notificationCount = 0;
            gameOver = true;
            return;
        }

        // vat can
        if (snakeHead.getX() < 0 || snakeHead.getY() < 0 || snakeHead.getX() * SQUARE_SIZE >= WIDTH
                || snakeHead.getY() * SQUARE_SIZE >= HEIGHT || checkCollisionWithWalls()) {
            Health--;

            if (Health < 0) {
                gameOver = true;
            } else {
                gc.setFill(Color.GRAY);
                gc.setFont(new Font("Digital-7", 25));
                collisionMessagewall = "Bạn đã va vào tường";
                gc.fillText(collisionMessagewall, 250, 60); // Thay x và y bằng tọa độ hiển thị thông báo
                notificationCount = 0;
                timeline.stop();
                Restartgame();
                return; // Kết thúc hàm gameOver() sau khi gọi Restartgame()
            }
        }

        // destroy itself
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.getX() == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY()) {
                Health = Health - 2;

                if (Health < 0) {
                    gameOver = true;
                } else {
                    gc.setFill(Color.GRAY);
                    gc.setFont(new Font("Digital-7", 25));
                    collisionMessagebody = "Bạn đã cắn vào thân ";
                    gc.fillText(collisionMessagebody, 250, 60); // Thay x và y bằng tọa độ hiển thị thông báo
                    notificationCount = 0;
                    timeline.stop();
                    Restartgame();
                    return; // Kết thúc hàm gameOver() sau khi gọi Restartgame()
                }

                break;
            }
        }
    }

    private void eatFood() {
        if (snakeHead.getX() == foodX && snakeHead.getY() == foodY) {
            snakeBody.add(new Point2D(-1, -1));
            generateFood();
            score += 5;
            scorefollow++;
        }

    }

    private void drawScore() {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);

    }

    private void drawHealth1() {
        gc.drawImage(Healthimg, 140, 10, SQUARE_SIZE, SQUARE_SIZE);

    }

    private void drawHealth2() {
        gc.drawImage(Healthimg, 140, 10, SQUARE_SIZE, SQUARE_SIZE);
        gc.drawImage(Healthimg, 190, 10, SQUARE_SIZE, SQUARE_SIZE);

    }

    private void drawHealth3() {
        gc.drawImage(Healthimg, 140, 10, SQUARE_SIZE, SQUARE_SIZE);
        gc.drawImage(Healthimg, 190, 10, SQUARE_SIZE, SQUARE_SIZE);
        gc.drawImage(Healthimg, 240, 10, SQUARE_SIZE, SQUARE_SIZE);

    }

    // Hàm xử lí mỗi khung hình đồng bọ trên kia là hàm xử lí khi có sự kiện nhấn
    // nút

}
