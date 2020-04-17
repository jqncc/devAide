package org.jflame.devnote.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * 主界面
 *
 * @author yucan.zhang
 */
public class MainView {

    @FXML
    TabPane mainTab;
    @FXML
    Tab tabKown;
    @FXML
    Tab tabToolbox;

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

    public MainView() {
    }
}
