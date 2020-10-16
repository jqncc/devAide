package org.jflame.devAide.controller;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.jflame.commons.util.MapHelper;
import org.jflame.devAide.component.MyGraphicValidationDecoration;
import org.jflame.devAide.util.ResourceUtils;
import org.jflame.devAide.util.SegmentXmlUtils;
import org.jflame.devAide.util.UIComponents;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class RegexController {

    @FXML
    private TextArea srcText;
    @FXML
    private TextArea resultText;
    @FXML
    private TextArea regexText;
    @FXML
    private Button btnHandle;
    @FXML
    private CheckBox chxCase;
    @FXML
    private CheckBox chxMultiMode;
    @FXML
    private CheckBox chxReplace;
    @FXML
    private CheckBox chxDotall;
    @FXML
    private TextArea replText;
    @FXML
    private VBox usuallyRegexBox;
    /*    @FXML
    private Tab usuallyRegexTab;*/

    @FXML
    private WebView syntaxView;

    private PattenOption pattenOption = new PattenOption();
    private ValidationSupport validationSupport = new ValidationSupport();

    public RegexController() {

    }

    @FXML
    private void initialize() {
        replText.setManaged(false);
        // 绑定属性
        chxReplace.selectedProperty()
                .addListener(new ChangeListener<Boolean>() {

                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                            Boolean newValue) {
                        if (newValue) {
                            UIComponents.show(replText);
                            replText.setText(null);
                        } else {
                            UIComponents.hide(replText);
                        }
                    }
                });
        chxCase.selectedProperty()
                .bindBidirectional(pattenOption.caseInsensitive());
        chxMultiMode.selectedProperty()
                .bindBidirectional(pattenOption.multiline());
        chxDotall.selectedProperty()
                .bindBidirectional(pattenOption.dotall());

        initRgihtTabpane();

        validationSupport.setValidationDecorator(new MyGraphicValidationDecoration());
        validationSupport.registerValidator(srcText, Validator.createEmptyValidator("请输入源文本"));
        validationSupport.registerValidator(regexText, Validator.createEmptyValidator("请输入正则表达式"));
        validationSupport.registerValidator(replText, new Validator<String>() {

            @Override
            public ValidationResult apply(Control t, String u) {
                if (chxReplace.isSelected()) {
                    return ValidationResult.fromErrorIf(t, "请输入替换文本", StringUtils.isBlank(replText.getText()));
                }
                return null;
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

    }

    /**
     * 初始右边'正则语法'和'常用正则'面板
     */
    private void initRgihtTabpane() {
        Map<String,String> usuallyRegexMap = SegmentXmlUtils.getUsuallyRegex();// 从xml加载文档内容
        // 常用正则
        if (MapHelper.isNotEmpty(usuallyRegexMap)) {
            UsuallyRegexLabelClickHandler clickHandler = new UsuallyRegexLabelClickHandler();
            usuallyRegexMap.forEach((k, v) -> {
                Label label = new Label(k);
                label.setUserData(v);
                label.setOnMouseClicked(clickHandler);
                usuallyRegexBox.getChildren()
                        .add(label);
            });
        }
        // 使用webview展示正则语法
        syntaxView.getEngine()
                .setUserStyleSheetLocation(ResourceUtils.cssUrl("webview"));
        syntaxView.setContextMenuEnabled(false);
        syntaxView.getEngine()
                .loadContent(SegmentXmlUtils.getRegexSyntax());
    }

    @FXML
    protected void handleRegex(ActionEvent event) {
        if (!validationSupport.isInvalid()) {
            Pattern regex = Pattern.compile(regexText.getText(), pattenOption.or());
            Matcher matcher = regex.matcher(srcText.getText());
            if (chxReplace.isSelected()) {
                String replResult = matcher.replaceAll(replText.getText());
                resultText.setText("替换结果: " + replResult);
            } else {
                StringBuilder matchResult = new StringBuilder();
                int i = 1;
                while (matcher.find()) {
                    matchResult.append("匹配结果" + i + ": ");
                    matchResult.append(matcher.group());
                    matchResult.append("\n");
                    i++;
                }
                if (matchResult.length() == 0) {
                    resultText.setText("无匹配结果");
                } else {
                    resultText.setText(matchResult.toString());
                }
            }
        }
    }

    private class UsuallyRegexLabelClickHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Label l = (Label) event.getSource();
                regexText.setText((String) l.getUserData());
            }
        }
    }

    private class PattenOption {

        private BooleanProperty caseInsensitive = new SimpleBooleanProperty(false);
        private BooleanProperty multiline = new SimpleBooleanProperty(false);
        private BooleanProperty dotall = new SimpleBooleanProperty(false);

        public boolean getCaseInsensitive() {
            return caseInsensitive.get();
        }

        public BooleanProperty caseInsensitive() {
            return caseInsensitive;
        }

        public void setCaseInsensitive(boolean _caseInsensitive) {
            caseInsensitive.set(_caseInsensitive);
        }

        public boolean getMultiline() {
            return multiline.get();
        }

        public BooleanProperty multiline() {
            return multiline;
        }

        public void setMultiline(boolean _multiline) {
            this.multiline.set(_multiline);
        }

        public boolean getDotall() {
            return dotall.get();
        }

        public BooleanProperty dotall() {
            return dotall;
        }

        public void setDotall(boolean _dotall) {
            this.dotall.set(_dotall);
        }

        /**
         * 多个正则选项作|操作
         * 
         * @return
         */
        public int or() {
            int i = 0;
            if (getCaseInsensitive()) {
                i = i | Pattern.CASE_INSENSITIVE;
            }
            if (getDotall()) {
                i = i | Pattern.DOTALL;
            }
            if (getMultiline()) {
                i = i | Pattern.MULTILINE;
            }
            return i;
        }

    }

    /*    public static void main(String[] args) {
        PattenOption ops = new PattenOption();
        System.out.println(ops.or());
        ops.setCaseInsensitive(true);
        System.out.println(ops.or());
        ops.setMultiline(true);
        System.out.println(ops.or());
    }*/
}
