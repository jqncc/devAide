module devAide {

    requires java.desktop;
    requires transitive java.sql;
    requires transitive java.xml;
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.web;
    requires transitive commons.lang3;
    requires transitive jflame.commons;
    requires transitive slf4j.api;
    requires org.controlsfx.controls;
    requires com.jfoenix;
    requires fastjson;
    requires static cron.utils;
    requires static com.google.zxing;
    requires static com.google.zxing.javase;

    exports org.jflame.devAide.component;
    exports org.jflame.devAide.model;
    exports org.jflame.devAide.plugin;
    exports org.jflame.devAide.util;

    exports org.jflame.devAide to javafx.graphics;
    exports org.jflame.devAide.controller to javafx.graphics, javafx.fxml;

    opens org.jflame.devAide.controller to javafx.fxml;
    opens org.jflame.devAide.component to javafx.fxml;

    uses org.jflame.devAide.plugin.ToolPlugin;
    uses org.jflame.devAide.plugin.codeFormatter.CodeFormatter;

    provides org.jflame.devAide.plugin.ToolPlugin
            with org.jflame.devAide.plugin.barcode.BarcodeTool, org.jflame.devAide.plugin.cron.CronTool,
            org.jflame.devAide.plugin.codeFormatter.FormatTool, org.jflame.devAide.plugin.regex.RegexTool;

    provides org.jflame.devAide.plugin.codeFormatter.CodeFormatter with
            org.jflame.devAide.plugin.codeFormatter.CssFormatter, org.jflame.devAide.plugin.codeFormatter.JsonFormatter,
            org.jflame.devAide.plugin.codeFormatter.SQLFormatter, org.jflame.devAide.plugin.codeFormatter.XmlFormatter;

}