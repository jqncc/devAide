package org.jflame.devAide.controller;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
import org.jflame.devAide.util.UIComponents;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

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
    private Button btnEscape;
    @FXML
    private Button btnCompressAndEscape;
    @FXML
    private TextArea srcText;
    @FXML
    private TextArea targetText;
    // private ValidationSupport validationSupport = new ValidationSupport();
    final Pattern xmlPattern = Pattern.compile("^<\\s*(\\S+)(\\s[^>]*)?>[\\s\\S]*<\\s*\\/\\1\\s*>$",
            Pattern.CASE_INSENSITIVE);

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
                } else if (JSONValidator.from(srcStr)
                        .validate()) {
                    // 作为json处理
                    formattedStr = formatJson(srcStr);
                } else {
                    formattedStr = srcStr;
                }
            } catch (Exception e) {
                UIComponents.createExDialog("无法格式化,请确认格式是否正确", e)
                        .show();
            }
        }
        targetText.setText(formattedStr);
        targetText.requestFocus();
    }

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
                escapeStr = "不可识别格式";
            }
        }
        targetText.setText(escapeStr);
        targetText.requestFocus();
    }

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
        targetText.setText(escapeStr);
        targetText.requestFocus();
    }

    private boolean isXml(String text) {
        return xmlPattern.matcher(text)
                .matches();
    }

    private String formatJson(String text) {
        return JSON.toJSONString(
                JSON.parse(text, Feature.AllowComment, Feature.AllowSingleQuotes, Feature.AutoCloseSource), true);
    }

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
