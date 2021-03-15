package org.jflame.devAide.plugin.codeFormatter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jflame.commons.exception.ConvertException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlFormatter implements CodeFormatter {

    public final static Pattern xmlPattern = Pattern.compile("^<\\s*(\\S+)(\\s[^>]*)?>[\\s\\S]*<\\s*\\/\\1\\s*>$",
            Pattern.CASE_INSENSITIVE);

    @Override
    public String convert(String xml) {
        xml = xml.replaceAll(">(\\s*|\n|\t|\r)<", "><");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();

            InputSource is = new InputSource(new StringReader(xml));
            Document xmlDoc = db.parse(is);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer tf = factory.newTransformer();
            // 输出内容是否使用换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());

            StringWriter out = new StringWriter();
            tf.transform(new DOMSource(xmlDoc), new StreamResult(out));
            return out.toString();
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            throw new ConvertException("格式化错误", e);
        }
    }

    @Override
    public boolean isSupported(String text) {
        return xmlPattern.matcher(text.strip())
                .matches();
    }

}
