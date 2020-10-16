package org.jflame.devAide.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jflame.commons.exception.BusinessException;
import org.jflame.commons.util.file.FileHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SegmentXmlUtils {

    private static Map<String,Object> segmentMap = null;

    private static void loadSegment() throws SAXException, IOException, ParserConfigurationException {
        /* 
         * sax解析
         * try (InputStream inputFile = FileHelper.readFileFromClassPath("segment.xml")) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            SegmentXmlHandler se = new SegmentXmlHandler();
            saxParser.parse(inputFile, se);
            segmentMap = se.getSegments();
            }*/
        // dom解析
        segmentMap = new HashMap<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(FileHelper.toAbsolutePath("segment.xml")
                .toFile());
        NodeList segmentNodes = document.getElementsByTagName("segment");

        Node segmentNode;
        Node regexNode;
        String tmpRegexName,tmpRegex;
        for (int i = 0; i < segmentNodes.getLength(); i++) {
            segmentNode = segmentNodes.item(i);
            String segmentId = segmentNode.getAttributes()
                    .getNamedItem("id")
                    .getNodeValue();
            // System.out.println(segmentId);
            if ("usuallyRegex".equals(segmentId)) {
                Map<String,String> usuallyRegexMap = new HashMap<>();
                NodeList regexNodes = segmentNode.getChildNodes();
                for (int k = 0; k < regexNodes.getLength(); k++) {
                    regexNode = regexNodes.item(k);
                    if ("regex".equals(regexNode.getNodeName())) {
                        tmpRegexName = regexNode.getAttributes()
                                .getNamedItem("name")
                                .getNodeValue();
                        tmpRegex = regexNode.getTextContent();
                        usuallyRegexMap.put(tmpRegexName, tmpRegex.strip());
                    }
                }
                segmentMap.put(segmentId, usuallyRegexMap);
            } else if ("syntaxRegex".equals(segmentId)) {
                segmentMap.put(segmentId, segmentNode.getTextContent());
            }
        }

    }

    /*  public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        loadSegment();
    }*/

    public static Object getSegmentById(String id) {
        if (segmentMap == null) {
            try {
                loadSegment();
            } catch (ParserConfigurationException | SAXException | IOException e) {
                throw new BusinessException("读取segment.xml文件异常");
            }
        }
        return segmentMap.get(id);
    }

    @SuppressWarnings("unchecked")
    public static Map<String,String> getUsuallyRegex() {
        return (Map<String,String>) getSegmentById("usuallyRegex");
    }

    public static String getRegexSyntax() {
        return (String) getSegmentById("syntaxRegex");
    }

    /*private static class SegmentXmlHandler extends DefaultHandler {
    
        private Map<String,Object> segmentMap = new HashMap<>();
        private Object curObj;
        private String curId;
        private String curName;
        private String curTag;
        StringBuilder sb = new StringBuilder();
    
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            curTag = qName;
            sb.setLength(0);
            if ("segment".equals(curTag)) {
                curId = attributes.getValue("id");
                if ("usuallyRegex".equals(curId)) {
                    curObj = new LinkedHashMap<String,String>();
                }
            } else if ("regex".equals(qName)) {
                curName = attributes.getValue("name");
            }
        }
    
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            sb.append(ch, start, length);
        }
    
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("regex".equals(qName)) {
                ((Map<String,String>) curObj).put(curName, sb.toString()
                        .strip());
                System.out.println(curName + "=" + sb.toString());
            } else if ("segment".equals(qName)) {
                segmentMap.put(curId, curObj);
                System.out.println("segmentMap:" + curId);
            }
        }
    
        public Map<String,Object> getSegments() {
            return segmentMap;
        }
    
    }
    */
}
