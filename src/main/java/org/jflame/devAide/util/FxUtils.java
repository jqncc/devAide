package org.jflame.devAide.util;

import java.io.IOException;
import java.util.Optional;

import org.jflame.devAide.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
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
    public static Optional<Parent> loadFXML(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
            return Optional.ofNullable(fxmlLoader.load());
        } catch (IOException | IllegalStateException e) {
            UIComponentCreater.createExDialog("加载界面异常,fxml: " + fxml, e)
                    .show();
            logger.error("加载fxml异常" + fxml, e);
        }
        return Optional.empty();
    }
}
