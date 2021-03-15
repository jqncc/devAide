package org.jflame.devAide;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Set;

import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.jflame.commons.util.CollectionHelper;
import org.jflame.devAide.util.ResourceUtils;

public final class AppSetting {

    private static final String BUNDLE_NAME = "lang";
    private static ResourceBundle i18n;
    /**
     * fontAwesome对象
     */
    public static GlyphFont FONT_AWESOME = GlyphFontRegistry.font("FontAwesome");
    /**
     * 通用样式文件路径
     */
    private static final String BASE_CSS_FILES = ResourceUtils.absUrl("/css/base.css");
    /**
     * 项目根目录地址
     */
    private static final String PROJ_ROOT_DIR;

    /**
     * 项目数据存放路径
     */
    private static final String PROJ_DATA_DIR;

    /**
     * 所有要加载的样式文件
     */
    private static final Set<String> styleFiles;

    static {
        String baseDir = System.getProperty("baseDir");
        if (baseDir == null) {
            try {
                File root;
                String runningJarPath = App.class.getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
                        .getPath()
                        .replaceAll("\\\\", "/");// 获取jar包所有目录
                int lastIndexOf = runningJarPath.lastIndexOf("/target/");
                System.out.println("====" + runningJarPath);
                if (lastIndexOf < 0) {
                    root = new File("");
                } else {
                    root = new File(runningJarPath.substring(0, lastIndexOf));
                }
                System.out.println("application resolved root folder: " + root.getAbsolutePath());
                baseDir = root.getAbsolutePath();
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        PROJ_ROOT_DIR = baseDir;
        PROJ_DATA_DIR = Paths.get(baseDir, "data")
                .toString();

        // i18n = AppSetting.getResourceBundle();
        styleFiles = CollectionHelper.newSet(BASE_CSS_FILES);
    }

    private AppSetting() {

    }

    public static ResourceBundle getResourceBundle() {
        if (i18n == null) {
            i18n = ResourceBundle.getBundle(BUNDLE_NAME);
        }
        return i18n;
    }

    public static String getString(String key) {
        return getResourceBundle().getString(key);
    }

    public static String getString(String key, Object... args) {
        String fmt = getString(key);
        if (fmt != null) {
            return MessageFormat.format(fmt, args);
        } else {
            return key;
        }
    }

    public static String getAppTitle() {
        return getString("app.title");
    }

    public static Set<String> getStyleFiles() {
        return styleFiles;
    }

    public static String projRootDir() {
        return PROJ_ROOT_DIR;
    }

    public static String projDataDir() {
        return PROJ_DATA_DIR;
    }
}
