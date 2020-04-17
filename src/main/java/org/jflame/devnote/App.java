package org.jflame.devnote;

import com.jfoenix.assets.JFoenixResources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import org.jflame.devnote.component.MyDecorator;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        //loginForm(stage);
        Parent parent = loadFXML("main");
        MyDecorator decorator = new MyDecorator(stage, parent, false, true, true);
        decorator.setCustomMaximize(true);
        decorator.setTitle("DevNote - 专业程序员笔记");

        ImageView imageView = new ImageView(getClass().getResource("/images/logo16X16.png").toExternalForm());
        imageView.setFitHeight(16);
        imageView.setFitWidth(16);
        decorator.setGraphic(imageView);
        scene = new Scene(decorator, 1024, 768);

        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                getClass().getResource("/css/jfmain.css").toExternalForm(),
                getClass().getResource("/css/base.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.show();
    }

    private void loginForm(Stage stage) {

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        pane.add(scenetitle, 0, 0, 2, 1);

//创建Label对象，放到第0列，第1行
        Label userName = new Label("user Name:");
        pane.add(userName, 0, 1);

//创建文本输入框，放到第1列，第1行
        TextField userTextField = new TextField();
        pane.add(userTextField, 1, 1);

        Label pw = new Label("password:");
        pane.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        pane.add(pwBox, 1, 2);

        final Text actiontarget = new Text();//增加用于显示信息的文本
        pane.add(actiontarget, 1, 6);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);//将按钮控件作为子节点
        pane.add(hbBtn, 1, 4);//将HBox pane放到grid中的第1列，第4行

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                actiontarget.setText("登录中...");
                actiontarget.setFill(Color.RED);
            }

        });
        scene = new Scene(pane, 640, 480);
        scene.getStylesheets().add(getClass().getResource("/css/base.css").toExternalForm());
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
