package Application;

import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.*;
import javafx.collections.*;
import javafx.stage.Modality;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.nio.file.*;
import java.net.URL;
import javafx.application.Application;
import javafx.animation.*;

import javafx.scene.layout.*;
import javafx.geometry.Bounds;
import javafx.geometry.Bounds;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;
import javafx.scene.transform.*;
import java.time.LocalTime;

public class App extends Application {

    public static Stage stage;
    public static Scene mainscene = null;
    public static Scene subscene = null;
    public static Text text;
    public static ContextMenu menu;

    private double dragStartX;
    private double dragStartY;

    private ArrayList<ImageView> Images;

    public static void main(String[] args) throws Exception {
        Application.launch(args);
    }

    public void init() {
        setPopupMenu();
    }

    private void setPopupMenu() {
        menu = new ContextMenu();
        MenuItem[] menui = new MenuItem[1];
        menui[0] = new MenuItem("終了");
        menu.getItems().addAll(menui);

        for (int i = 0; i < menui.length; i++) {
            menui[i].addEventHandler(ActionEvent.ACTION, (e) -> {
                if (((MenuItem) e.getSource()).getText() == "終了")
                    System.out.println(" 終了しました");
                stage.close();
            });
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        StackPane pane = mkPane();

        mainscene = new Scene(pane, 180, 180, Color.TRANSPARENT);
        mainscene.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                menu.show(pane, e.getScreenX(), e.getScreenY());
            } else if (e.getButton() == MouseButton.PRIMARY) {
                menu.hide();
                dragStartX = e.getSceneX();
                dragStartY = e.getSceneY();
            }
        });
        mainscene.setOnMouseDragged(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                stage.setX(e.getScreenX() - dragStartX);
                stage.setY(e.getScreenY() - dragStartY);
            }
        });
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(mainscene);
        stage.setTitle("Test");
        stage.show();
    }

    private StackPane mkPane() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: transparent;");
        root.setAlignment(Pos.CENTER);
        Images = new ArrayList<ImageView>();
        Images.add(new ImageView(new Image(getClass().getResourceAsStream("/Application/img/clockbase.png"))));
        Images.add(new ImageView(new Image(getClass().getResourceAsStream("/Application/img/hand_hour.png"))));
        Images.add(new ImageView(new Image(getClass().getResourceAsStream("/Application/img/hand_min.png"))));
        Images.add(new ImageView(new Image(getClass().getResourceAsStream("/Application/img/hand_sec.png"))));
        for (int i = 0; i < Images.size(); i++) {
            Images.get(i).fitWidthProperty().bind(stage.widthProperty());
            Images.get(i).fitHeightProperty().bind(stage.heightProperty());
        }

        LocalTime now = LocalTime.now();

        RotateTransition secondTransition = createRotateTransition(Duration.seconds(60), Images.get(3),
                getSecondAngle(now), 360d, 151, 151);
        secondTransition.play();

        RotateTransition minuteTransition = createRotateTransition(Duration.minutes(60), Images.get(2),
        getMinuteAngle(now), 360d, 151, 151);
        minuteTransition.play();

        RotateTransition hourTransition = createRotateTransition(Duration.hours(12), Images.get(1), getHourAngle(now),
                360d, 151, 151);
        hourTransition.play();


        root.getChildren().addAll(Images);
        return root;
    }

    private static double getSecondAngle(LocalTime time) {
        return time.getSecond() * 360 / 60;
    }

    private static double getMinuteAngle(LocalTime time) {
        return (time.getMinute() + time.getSecond() / 60d) * 360 / 60;
    }

    private static double getHourAngle(LocalTime time) {
        return (time.getHour() % 12 + time.getMinute() / 60d + time.getSecond() / (60d * 60d)) * 360 / 12;
    }

    private RotateTransition createRotateTransition(
            Duration duration, Node node, double fromAngle, double byAngle, double pivotX, double pivotY) {
        Bounds bounds = node.getBoundsInLocal(); // (1)
        double defaultPivotX = (bounds.getMinX() + bounds.getMaxX()) / 2d; // (1)
        double defaultPivotY = (bounds.getMinY() + bounds.getMaxY()) / 2d; // (1)
        double translateX = defaultPivotX - pivotX; // (2)
        double translateY = defaultPivotY - pivotY; // (2)
        node.getTransforms().add(new Translate(translateX, translateY)); // (3)
        node.setTranslateX(-translateX); // (4)
        node.setTranslateY(-translateY); // (4)
        RotateTransition rt = new RotateTransition(duration, node); // (5)
        rt.setFromAngle(fromAngle); // (5)
        rt.setByAngle(byAngle); // (5)
        rt.setCycleCount(Animation.INDEFINITE); // (5)
        rt.setInterpolator(Interpolator.LINEAR); // (5)
        return rt;
    }
}
