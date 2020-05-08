package org.jflame.devAide.util;

import java.io.IOException;

import org.jflame.devAide.App;
import org.jflame.devAide.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * @author yucan.zhang
 */
public class FxUtils {

    private static final Logger logger = LoggerFactory.getLogger(FxUtils.class);

    /**
     * 加载一个fxml文档
     *
     * @param fxml fxml文件名
     * @return Optional&lt;Parent&gt; 加载异常时返回Optional.empty
     */
    public static Parent loadFXML(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
            if (AppContext.getInstance()
                    .getResourceBundle() != null) {
                fxmlLoader.setResources(AppContext.getInstance()
                        .getResourceBundle());
            }
            return fxmlLoader.load();
        } catch (IOException e) {
            UIComponents.createExDialog("加载界面异常,fxml: " + fxml, e)
                    .show();
            logger.error("加载fxml异常" + fxml, e);
            throw new RuntimeException(e);
        }
    }

    public static Parent loadFXML(String fxml, Node root, Object controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
            if (AppContext.getInstance()
                    .getResourceBundle() != null) {
                fxmlLoader.setResources(AppContext.getInstance()
                        .getResourceBundle());
            }
            fxmlLoader.setRoot(root);
            fxmlLoader.setController(controller);
            return fxmlLoader.load();
        } catch (IOException e) {
            UIComponents.createExDialog("加载界面异常,fxml: " + fxml, e)
                    .show();
            logger.error("加载fxml异常" + fxml, e);
            throw new RuntimeException(e);
        }
    }
}
