package org.jflame.devAide;

import java.io.IOException;

import org.jflame.devAide.component.MyDecorator;
import org.jflame.devAide.util.FxUtils;

// import com.jfoenix.assets.JFoenixResources;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * devAide main app
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent parent = FxUtils.loadFXML("main")
                .get();
        MyDecorator decorator = MyDecorator.create(stage, parent, "DevAide - 开发辅助工具", "/images/logo16X16.png");
        Scene scene = new Scene(decorator, 1024, 768);

        // JFoenixResources
        // String css_jfoenix_font = ResourceUtils.absUrl("/css/jfoenix-fonts.css");
        // String css_jfoenix_design = ResourceUtils.absUrl("/css/jfoenix-design.css");
        // String css_main = ResourceUtils.absUrl("/css/jfmain.css");
        scene.getStylesheets()
                .addAll(Globals.getCssFiles());
        stage.setScene(scene);
        stage.show();
        Globals.MAIN_STAGE = stage;
    }

    public static void main(String[] args) {
        launch();
    }

}
