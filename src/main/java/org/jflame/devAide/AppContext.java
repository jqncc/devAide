package org.jflame.devAide;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Set;

import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.jflame.commons.util.CollectionHelper;
import org.jflame.devAide.util.ResourceUtils;

import javafx.stage.Stage;

public final class AppContext {

    private static AppContext instance = new AppContext();
    private ResourceBundle i18n;
    /**
     * 主界面场景
     */
    private Stage mainStage;
    /**
     * fontAwesome对象
     */
    public static GlyphFont FONT_AWESOME = GlyphFontRegistry.font("FontAwesome");
    /**
     * 通用样式文件路径
     */
    public final static String BASE_CSS_FILES = ResourceUtils.absUrl("/css/base.css");
    /**
     * 项目根目录地址
     */
    private final String projRootDir;

    /**
     * 项目数据存放路径
     */
    private final String projDataDir;

    /**
     * 所有要加载的样式文件
     */
    private static Set<String> styleFiles = CollectionHelper.newSet(BASE_CSS_FILES);

    private AppContext() {
        try {
            projRootDir = Paths.get(Thread.currentThread()
                    .getContextClassLoader()
                    .getResource("")
                    .toURI())
                    .toString();
            projDataDir = Paths.get(projRootDir, "data")
                    .toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static AppContext getInstance() {
        return instance;
    }

    /* public static void main(String[] args) throws URISyntaxException {
        String a = Paths.get(Thread.currentThread()
                .getContextClassLoader()
                .getResource("")
                .toURI())
                .toString();
        System.out.println(a);
        System.out.println(Paths.get(a, "/data"));
    }*/

    public static void addStyleFile(String styleFile) {
        styleFiles.add(styleFile);
    }

    public static Set<String> getStyleFiles() {
        return Collections.unmodifiableSet(styleFiles);
    }

    public static String getMessage(String key) {
        if (instance.i18n == null) {
            return key;
        } else {
            return instance.i18n.getString(key);
        }
    }

    public void setResourceBundle(ResourceBundle bundle) {
        i18n = bundle;
    }

    public ResourceBundle getResourceBundle() {
        return i18n;
    }

    public App getApplication() {
        return App.instance;
    }

    public String projRootDir() {
        return projRootDir;
    }

    public String projDataDir() {
        return projDataDir;
    }

    public Stage mainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        if (this.mainStage == null) {
            this.mainStage = mainStage;
        }
    }

}
