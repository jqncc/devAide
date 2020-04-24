package org.jflame.devAide;

import java.util.Set;

import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.jflame.commons.util.CollectionHelper;
import org.jflame.devAide.util.ResourceUtils;

import javafx.stage.Stage;

public final class Globals {

    /**
     * 主界面场景
     */
    public static Stage MAIN_STAGE;
    /**
     * fontAwesome对象
     */
    public static GlyphFont FONT_AWESOME = GlyphFontRegistry.font("FontAwesome");
    /**
     * 通用样式文件路径
     */
    public static String BASE_CSS_FILES = ResourceUtils.absUrl("/css/base.css");

    /**
     * 所有要加载的样式文件
     */
    private static Set<String> cssFiles = CollectionHelper.newSet(BASE_CSS_FILES);

    public static void addCss(String cssFile) {
        cssFiles.add(cssFile);
    }

    public static Set<String> getCssFiles() {
        return cssFiles;
    }
}
