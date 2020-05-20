package org.jflame.devAide.plugin;

public class FormatTool implements ToolPlugin {

    @Override
    public String getName() {
        return "JSON工具";
    }

    @Override
    public String getFxml() {
        return "formatTool";
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getIcon() {
        return "@FILE_TEXT";
    }

}
