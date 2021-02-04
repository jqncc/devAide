module devAide {

    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires org.controlsfx.controls;

    requires java.desktop;
    requires transitive java.sql;
    requires transitive java.xml;
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.web;
    requires commons.lang3;
    requires fastjson;
    requires jflame.commons;
    requires slf4j.api;
    requires cron.utils;
    requires com.jfoenix;

    exports org.jflame.devAide.component;
    exports org.jflame.devAide.model;
    exports org.jflame.devAide.plugin;

    exports org.jflame.devAide to javafx.graphics;
    exports org.jflame.devAide.controller to javafx.graphics, javafx.fxml;

    opens org.jflame.devAide.controller to javafx.fxml;
    opens org.jflame.devAide.component to javafx.fxml;

    uses org.jflame.devAide.plugin.ToolPlugin;
    uses org.jflame.devAide.util.format.CodeFormatter;

    provides org.jflame.devAide.plugin.ToolPlugin
            with org.jflame.devAide.plugin.BarcodeTool, org.jflame.devAide.plugin.CronTool,
            org.jflame.devAide.plugin.FormatTool, org.jflame.devAide.plugin.RegexTool;

    provides org.jflame.devAide.util.format.CodeFormatter
            with org.jflame.devAide.util.format.CssFormatter, org.jflame.devAide.util.format.JsonFormatter,
            org.jflame.devAide.util.format.SQLFormatter, org.jflame.devAide.util.format.XmlFormatter;

}