package org.jflame.devAide;

import java.io.IOException;

import org.jflame.devAide.component.MyDecorator;
import org.jflame.devAide.util.FxUtils;

// import com.jfoenix.assets.JFoenixResources;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * devAide main app
 */
public class App extends Application {

    static App instance;

    @Override
    public void start(Stage stage) throws IOException {
        Parent parent = FxUtils.loadFXML("main");
        MyDecorator decorator = MyDecorator.create(stage, parent, "DevAide - 开发辅助工具", "/images/logo16X16.png");

        Scene scene = new Scene(decorator, 1366, 800);
        // JFoenixResources
        // String css_jfoenix_font = ResourceUtils.absUrl("/css/jfoenix-fonts.css");
        // String css_jfoenix_design = ResourceUtils.absUrl("/css/jfoenix-design.css");
        // String css_main = ResourceUtils.absUrl("/css/jfmain.css");
        scene.getStylesheets()
                .addAll(AppContext.getStyleFiles());
        stage.setScene(scene);
        stage.setResizable(true);
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
