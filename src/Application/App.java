package Application;

import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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

import javafx.scene.layout.*;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;

public class App extends Application {    
    
    public static Stage stage;
    public static Scene mainscene = null;
    public static Scene subscene = null;
    public static Text text;
    public static ContextMenu menu;

    private double dragStartX;
    private double dragStartY;

    public static void main(String[] args) throws Exception {
        Application.launch(args);
    }    
    public void init() {
        setPopupMenu();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    private void setPopupMenu() {
        menu = new ContextMenu();
        MenuItem[] menui = new MenuItem[1];
        menui[0] = new MenuItem("終了");
        menu.getItems().addAll(menui);

        for (int i = 0; i < menui.length; i++) {
            menui[i].addEventHandler(ActionEvent.ACTION, (e) -> {
                if(((MenuItem) e.getSource()).getText()=="終了")
                System.out.println(" 終了しました");
                stage.close();
            });
        }
    }

    private Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), // 時間経過をトリガにするのはTimelineクラスを使う
            new EventHandler<ActionEvent>() {
                public void handle(ActionEvent actionEvent) {
                    LocalDateTime date1 = LocalDateTime.now();
                    DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    String fdate1 = dtformat.format(date1); // 表示形式+Stringに変換
                    text.setText("<現在時刻>\n" + fdate1); // 表示の更新
                }
            }));
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        text = new Text();
		text.setFont(Font.font("Serif", 24));

		FlowPane pane = new FlowPane();
		pane.setStyle("-fx-background-color: #b2dfdb;");
		pane.setAlignment(Pos.CENTER);
		pane.getChildren().add(text);
        stage = primaryStage;
        mainscene = new Scene(pane, 300, 150);
        mainscene.setOnMousePressed(e -> {
            if (e.getButton()==MouseButton.SECONDARY) {
                menu.show( pane , e.getScreenX() , e.getScreenY() );
            }
            else if(e.getButton()==MouseButton.PRIMARY){
                menu.hide();
                dragStartX = e.getSceneX();
                dragStartY = e.getSceneY();
            }
        });
        mainscene.setOnMouseDragged(e -> {
            if(e.getButton()==MouseButton.PRIMARY){
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
}
