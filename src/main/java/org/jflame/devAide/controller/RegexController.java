package org.jflame.devAide.controller;

import org.jflame.devAide.util.UIComponents;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;

public class RegexController {

    @FXML
    private TextArea srcText;
    @FXML
    private TextArea resultText;
    @FXML
    private TextArea regexText;
    @FXML
    private Button btnHandle;

    /*  @FXML
    private ToggleButton toggleBtnCase;
    
    @FXML
    private GridPane leftgrid;*/
    @FXML
    private CheckBox chxCase;
    @FXML
    private CheckBox chxMultiMode;
    @FXML
    private CheckBox chxReplace;

    @FXML
    private TextArea replText;

    public RegexController() {

    }

    @FXML
    private void initialize() {
        // 替换模式
        chxReplace.selectedProperty()
                .addListener(new ChangeListener<Boolean>() {

                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                            Boolean newValue) {
                        if (newValue) {
                            UIComponents.hide(replText);
                            replText.setText(null);
                        } else {
                            UIComponents.show(replText);
                        }
                    }
                });
        /* ToggleGroup optToggleGrp = new ToggleGroup();
        ToggleButton searchToggleBtn = new ToggleButton("查找");
        searchToggleBtn.setSelected(true);
        ToggleButton replToggleBtn = new ToggleButton("替换");
        SegmentedButton optSegmentedBtn = new SegmentedButton(searchToggleBtn, replToggleBtn);
        optSegmentedBtn.setToggleGroup(optToggleGrp);
        optToggleGrp.selectedToggleProperty()
                .addListener(new ChangeListener<Toggle>() {
        
                    @Override
                    public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue,
                            Toggle newValue) {
                        if (newValue == replToggleBtn) {
        
                        }
                    }
                });
        
        ObservableList<Node> hboxOptBarChilds = hboxOptBar.getChildren();
        
        hboxOptBarChilds.add(2, optSegmentedBtn);
        
        srcText = new TextArea();
        
        Node srcBorder = Borders.wrap(srcText)
                .lineBorder()
                .title("源文本")
                .thickness(1)
                .color(Color.GAINSBORO)
                .buildAll();
        leftgrid.add(srcBorder, 0, 2);
        
        resultText = new TextArea();
        Node resultBorder = Borders.wrap(resultText)
                .lineBorder()
                .title("结果")
                .thickness(1)
                .color(Color.GAINSBORO)
                .buildAll();
        leftgrid.add(resultBorder, 0, 3);*/

        replText.setManaged(false);

    }

    @FXML
    protected void handleRegex(ActionEvent event) {

    }

}
