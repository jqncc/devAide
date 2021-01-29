package org.jflame.devAide.controller;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jflame.commons.util.StringHelper;
import org.jflame.devAide.util.UIUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.alibaba.fastjson.JSONValidator.Type;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jfoenix.controls.JFXDialog;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * 格式化工具
 * 
 * @author yucan.zhang
 */
public class FormatToolController implements Initializable {

    @FXML
    private Button btnJsonFmt;
    @FXML
    private Button btnEscape;
    @FXML
    private Button btnCompressAndEscape;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnRefresh;
    @FXML
    private TextArea srcText;
    @FXML
    private TextArea targetText;
    @FXML
    private CheckBox cbxTree;
    @FXML
    private AnchorPane targetBox;

    // @FXML
    // private TextField searchText;
    @FXML
    private TreeView<String> treeJson;
    private TreeItem<String> rootNode = null;

    final Pattern xmlPattern = Pattern.compile("^<\\s*(\\S+)(\\s[^>]*)?>[\\s\\S]*<\\s*\\/\\1\\s*>$",
            Pattern.CASE_INSENSITIVE);

    /**
     * 格式化事件处理
     * 
     * @param event
     */
    @FXML
    private void formattHandle(ActionEvent event) {
        String srcStr = srcText.getText();
        String formattedStr = null;
        if (StringHelper.isNotEmpty(srcStr)) {
            srcStr = srcStr.strip();
            try {
                if (isXml(srcStr)) {
                    // 作为xml处理
                    formattedStr = formatXml(srcStr);
                    targetText.setText(formattedStr);
                } else if (JSONValidator.from(srcStr)
                        .validate()) {
                    // 作为json处理
                    if (cbxTree.isSelected()) {
                        createJsonTreeView(srcStr);
                    } else {
                        formattedStr = formatJson(srcStr);
                        targetText.setText(formattedStr);
                    }
                } else if (isCss(srcStr)) {
                    formattedStr = formatCss(srcStr);
                    targetText.setText(formattedStr);
                } else {

                }
            } catch (Exception e) {
                UIUtils.createExDialog("无法格式化,请确认格式是否正确", e)
                        .show();
            }
        }
    }

    private String formatCss(String srcStr) {
        return null;
    }

    /**
     * 转义事件处理
     * 
     * @param event
     */
    @FXML
    private void handleEscape(ActionEvent event) {
        String srcStr = srcText.getText();
        String escapeStr = null;
        if (StringHelper.isNotEmpty(srcStr)) {
            srcStr = srcStr.strip();
            if (isXml(srcStr)) {
                escapeStr = StringEscapeUtils.escapeXml10(srcStr);
            } else if (JSONValidator.from(srcStr)
                    .validate()) {
                escapeStr = StringEscapeUtils.escapeJson(srcStr);
            } else {
                // escapeStr = "不可识别格式";
                UIUtils.alert("提示", "不可识别格式")
                        .show();
                return;

            }
        }
        srcText.setText(escapeStr);

    }

    /**
     * 压缩并转意事件处理
     * 
     * @param event
     */
    @FXML
    private void handleCompressAndEscape(ActionEvent event) {
        String srcStr = srcText.getText();
        String escapeStr = null;
        if (StringHelper.isNotEmpty(srcStr)) {
            srcStr = srcStr.strip();
            if (isXml(srcStr)) {
                srcStr = srcStr.replaceAll(">(\\s*|\n|\t|\r)<", "><");
                escapeStr = StringEscapeUtils.escapeXml10(srcStr);
            } else if (JSONValidator.from(srcStr)
                    .validate()) {
                srcStr = JSON.toJSONString(
                        JSON.parse(srcStr, Feature.AllowComment, Feature.AllowSingleQuotes, Feature.AutoCloseSource),
                        SerializerFeature.WriteMapNullValue);
                escapeStr = StringEscapeUtils.escapeJson(srcStr);
            } else {
                escapeStr = "不可识别格式";
            }
        }
        srcText.setText(escapeStr);
    }

    String lastKeyword;

    /**
     * 文本查找高亮事件处理
     * 
     * @param event
     */
    /*@FXML
    private void handleFindText(ActionEvent event) {
        String keyword = searchText.getText();
        if (StringHelper.isEmpty(keyword)) {
            return;
        }
        keyword = keyword.trim();
        if (keyword.length() < 2) {
            return;
        }
        String srcStr = srcText.getText();
        if (StringHelper.isNotEmpty(srcStr)) {
            int startIndex = keyword.equals(lastKeyword) && srcText.getUserData() != null
                    ? (Integer) srcText.getUserData()
                    : 0;
            startIndex = srcStr.indexOf(keyword, startIndex);
            if (startIndex >= 0) {
                srcText.selectRange(startIndex, startIndex + keyword.length());
                btnSearch.setText("下一个");
                srcText.setUserData(startIndex);
                lastKeyword = keyword;
            } else {
                UIUtils.alert("提示", "查找无结果");
                btnSearch.setText("查找");
                lastKeyword = null;
                srcText.setUserData(null);
            }
        }
    }*/

    /**
     * 刷新JSON树节点事件处理
     * 
     * @param event
     */
    /* @FXML
    private void handleRefreshTree(ActionEvent event) {
        String srcStr = srcText.getText();
        if (StringHelper.isNotEmpty(srcStr)) {
            srcStr = srcStr.strip();
            createJsonTreeView(srcStr);
        }
    }*/

    /**
     * 检测是否是xml文本
     * 
     * @param text
     * @return
     */
    private boolean isXml(String text) {
        return xmlPattern.matcher(text)
                .matches();
    }

    /**
     * 检测是否是css文本
     * 
     * @param text
     * @return
     */
    private boolean isCss(String text) {
        return false;
    }

    /**
     * 格式化json
     * 
     * @param text
     * @return
     */
    private String formatJson(String text) {
        return JSON.toJSONString(
                JSON.parse(text, Feature.AllowComment, Feature.AllowSingleQuotes, Feature.AutoCloseSource), true);
    }

    /**
     * 格式化xml
     * 
     * @param text
     * @return
     * @throws Exception
     */
    private String formatXml(String text) throws Exception {
        text = text.replaceAll(">(\\s*|\n|\t|\r)<", "><");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(text));
        Document xmlDoc = db.parse(is);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer tf = factory.newTransformer();
        // 输出内容是否使用换行
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());

        StringWriter out = new StringWriter();
        tf.transform(new DOMSource(xmlDoc), new StreamResult(out));
        return out.toString();
    }

    /**
     * 创建json treeview
     * 
     * @param jsonText
     */
    private void createJsonTreeView(String jsonText) {
        JSONValidator validator = JSONValidator.from(jsonText);
        if (!validator.validate()) {
            return;
        }
        showTreeViewBox();
        if (rootNode == null) {
            rootNode = new TreeItem<String>("ROOT");
            treeJson.setRoot(rootNode);
        } else {
            rootNode.getChildren()
                    .clear();
        }
        if (validator.getType() == Type.Array) {
            JSONArray root = JSON.parseArray(jsonText);
            createJsonTreeNode(root, rootNode);
        } else {
            JSONObject root = JSON.parseObject(jsonText);
            createJsonTreeNode(root, rootNode);
        }

        // jsonTree.refresh();

    }

    static void createJsonTreeNode(JSON jnode, TreeItem<String> parent) {
        String nodeName;
        TreeItem<String> curItem;
        if (jnode instanceof JSONArray) {
            JSONArray root = (JSONArray) jnode;
            int size = root.size();
            for (int i = 0; i < size; i++) {
                Object node = root.get(i);
                nodeName = "[" + i + "]:";
                if (node instanceof JSONObject) {
                    nodeName = nodeName + "[Object]";
                    curItem = new TreeItem<String>(nodeName);
                    parent.getChildren()
                            .add(curItem);
                    // System.out.println(nodeName);
                    if (!((JSONObject) node).isEmpty()) {
                        createJsonTreeNode((JSONObject) node, curItem);
                    }
                } else if (node instanceof JSONArray) {
                    nodeName = nodeName + "[Array]";
                    curItem = new TreeItem<String>(nodeName);
                    parent.getChildren()
                            .add(curItem);
                    // System.out.println(nodeName);
                    if (!((JSONArray) node).isEmpty()) {
                        createJsonTreeNode((JSONArray) node, curItem);
                    }
                } else {
                    nodeName = nodeName + node;
                    curItem = new TreeItem<String>(nodeName);
                    parent.getChildren()
                            .add(curItem);
                }
            }
        } else if (jnode instanceof JSONObject) {
            JSONObject root = (JSONObject) jnode;
            for (Map.Entry<String,Object> kv : root.entrySet()) {
                nodeName = kv.getKey() + ":";
                if (kv.getValue() instanceof JSONObject) {
                    nodeName = nodeName + "[Object]";
                    curItem = new TreeItem<String>(nodeName);
                    parent.getChildren()
                            .add(curItem);
                    // System.out.println(nodeName);
                    if (!((JSONObject) kv.getValue()).isEmpty()) {
                        createJsonTreeNode((JSONObject) kv.getValue(), curItem);
                    }
                } else if (kv.getValue() instanceof JSONArray) {
                    nodeName = nodeName + "[Array]";
                    curItem = new TreeItem<String>(nodeName);
                    parent.getChildren()
                            .add(curItem);
                    // System.out.println(nodeName);
                    if (!((JSONArray) kv.getValue()).isEmpty()) {
                        createJsonTreeNode((JSONArray) kv.getValue(), curItem);
                    }
                } else {
                    nodeName = nodeName + kv.getValue();
                    curItem = new TreeItem<String>(nodeName);
                    parent.getChildren()
                            .add(curItem);
                    // System.out.println(nodeName);
                }
            }
        }
    }

    private void createMenuItem() {
        ContextMenu textAreaMenu = new ContextMenu();

        MenuItem mitemCopy = new MenuItem("复制");
        mitemCopy.setOnAction(e -> {
            srcText.copy();
        });
        MenuItem mitemPaste = new MenuItem("粘贴");
        mitemPaste.setOnAction(e -> {
            srcText.paste();
        });
        MenuItem mitemFind = new MenuItem("查找(F3)");
        mitemFind.setOnAction(e -> {
            JFXDialog dialog = new JFXDialog();
            HBox box = new HBox(10);
            box.setPrefSize(250, 50);
            box.setAlignment(Pos.CENTER_LEFT);
            TextField txtfSearch = new TextField();
            txtfSearch.setPrefSize(150, 30);
            Button btnFind = new Button("查找");
            Button btnFindNext = new Button("下一个");
            box.getChildren()
                    .addAll(txtfSearch, btnFind, btnFindNext);
            dialog.setContent(box);
            dialog.show();

        });
        textAreaMenu.setOnShowing(e -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            System.out.println("剪切版内容:" + clipboard.getString());
            if (!clipboard.hasString()) {
                mitemPaste.setDisable(true);
            }
        });
        textAreaMenu.getItems()
                .addAll(mitemCopy, mitemPaste, mitemFind);
        srcText.setContextMenu(textAreaMenu);
    }

    private void showTreeViewBox() {
        treeJson.setVisible(true);
        treeJson.setManaged(true);

        targetBox.setVisible(false);
        targetBox.setManaged(false);
    }

    // controller类实例化后执行方法,1.Initializable接口,2.定义个@FXML private void initialize(方法签名不能修改)
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        createMenuItem();
        srcText.textProperty()
                .addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue,
                            String newValue) {
                        btnSearch.setText("查找");
                        lastKeyword = null;
                        srcText.setUserData(null);
                    }
                });
        /*validationSupport.setValidationDecorator(new MyGraphicValidationDecoration());
        validationSupport.registerValidator(srcText, false, new Validator<String>() {
        
            @Override
            public ValidationResult apply(Control ctl, String value) {
                boolean condition = JSON.isValid(value);
                return ValidationResult.fromErrorIf(ctl, "JSON格式不正确", !condition);
            }
        });*/
    }
}
