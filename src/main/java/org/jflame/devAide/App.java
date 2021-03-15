package org.jflame.devAide;

import java.io.IOException;

import org.jflame.devAide.component.MyDecorator;
import org.jflame.devAide.util.FxUtils;
import org.jflame.devAide.util.UIUtils;

// import com.jfoenix.assets.JFoenixResources;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * devAide main app
 */
public class App extends Application {

    static App instance;

    @Override
    public void start(Stage stage) throws IOException {

        stage.setResizable(false);
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        Parent parent = FxUtils.loadFXML("main");
        MyDecorator decorator = UIUtils.decorator(stage, parent, AppSetting.getAppTitle(), "/images/logo16X16.png");

        Scene scene = new Scene(decorator, 1366, 800);
        // scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets()
                .addAll(AppSetting.getStyleFiles());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("DevAide");
        stage.getIcons()
                .add(new Image(App.class.getResourceAsStream("/images/logo32X32.png")));
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        instance = this;
        AppContext.getInstance()
                .setMainStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
