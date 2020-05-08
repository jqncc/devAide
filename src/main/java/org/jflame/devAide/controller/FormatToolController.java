package org.jflame.devAide.controller;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jflame.commons.util.StringHelper;
import org.jflame.commons.valid.ValidatorHelper;
import org.jflame.devAide.util.UIComponents;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * 格式化工具
 * 
 * @author yucan.zhang
 */
public class FormatToolController implements Initializable {

    @FXML
    private Button btnJsonFmt;
    @FXML
    private TextArea srcText;
    @FXML
    private TextArea targetText;
    // private ValidationSupport validationSupport = new ValidationSupport();

    @FXML
    private void formattHandle(ActionEvent event) {
        String srcStr = srcText.getText();
        String formattedStr = null;
        if (StringHelper.isNotEmpty(srcStr)) {
            srcStr = srcStr.strip();
            try {
                if (ValidatorHelper.regex(srcStr, "<.*>")) {
                    // 作为xml处理
                    formattedStr = formatXml(srcStr);
                } else if (JSON.isValid(srcStr)) {
                    // 作为json处理
                    formattedStr = formatJson(srcStr);
                } else {
                    formattedStr = srcStr;
                }
            } catch (Exception e) {
                UIComponents.createExDialog("格式有错,无法格式化", e)
                        .show();
            }
        }
        targetText.setText(formattedStr);
    }

    private String formatJson(String text) {
        return JSON.toJSONString(
                JSON.parse(text, Feature.AllowComment, Feature.AllowSingleQuotes, Feature.AutoCloseSource), true);

    }

    private String formatXml(String text) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(text));
        Document xmlDoc = db.parse(is);
        /* OutputFormat format = new OutputFormat(xmlDoc);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        Writer out = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(document);
        return out.toString();*/
        // 创建TransformerFactory对象
        TransformerFactory tff = TransformerFactory.newInstance();
        // 创建 Transformer对象
        Transformer tf = tff.newTransformer();

        // 输出内容是否使用换行
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty(OutputKeys.ENCODING, "utf-8");

        StringWriter out = new StringWriter();
        // 创建xml文件并写入内容
        tf.transform(new DOMSource(xmlDoc), new StreamResult(out));
        return out.toString();
    }
    // controller类实例化后执行方法,1.Initializable接口,2.定义个@FXML private void initialize(方法签名不能修改)

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
