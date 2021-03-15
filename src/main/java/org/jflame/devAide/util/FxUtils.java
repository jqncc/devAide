package org.jflame.devAide.util;

import java.io.IOException;
import java.util.ResourceBundle;

import org.jflame.devAide.App;
import org.jflame.devAide.AppSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * fxml相关工具方法
 * 
 * @author charles.zhang
 */
public class FxUtils {

    private static final Logger logger = LoggerFactory.getLogger(FxUtils.class);

    /**
     * 方法注释参考{@linkplain #loadFXML(String, Node, Object, ResourceBundle)}
     * 
     * @param fxml
     * @return
     */
    public static Parent loadFXML(String fxml) {
        return loadFXML(fxml, null);
    }

    /**
     * 方法注释参考{@linkplain #loadFXML(String, Node, Object, ResourceBundle)}
     * 
     * @param fxml
     * @param resourceBundle
     * @return
     */
    public static Parent loadFXML(String fxml, ResourceBundle resourceBundle) {
        return loadFXML(fxml, null, null, resourceBundle);
    }

    /**
     * 方法注释参考{@linkplain #loadFXML(String, Node, Object, ResourceBundle)}
     * 
     * @param fxml
     * @param controller
     * @return
     */
    public static Parent loadFXML(String fxml, Object controller) {
        return loadFXML(fxml, null, controller, null);
    }

    /**
     * 加载一个fxml文件
     * 
     * @param fxml fxml fxml文件名，加载方式App.class.getResource(fxml + ".fxml")
     * @param root 根节点控件
     * @param controller 关联ui controller
     * @param resourceBundle 指定多语言资源文件,不指定时使用全局资源文件
     * @return 返回生成的Parent
     */
    public static Parent loadFXML(String fxml, Node root, Object controller, ResourceBundle resourceBundle) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
            fxmlLoader.setResources(resourceBundle == null ? AppSetting.getResourceBundle() : resourceBundle);
            if (root != null) {
                fxmlLoader.setRoot(root);
            }
            if (controller != null) {
                fxmlLoader.setController(controller);
            }
            return fxmlLoader.load();
        } catch (IOException e) {
            UIUtils.createExDialog(AppSetting.getString("ex.loadfxml", fxml), e)
                    .show();
            logger.error("加载fxml异常" + fxml, e);
            throw new RuntimeException(e);
        }
    }
}
