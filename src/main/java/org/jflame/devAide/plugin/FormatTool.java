package org.jflame.devAide.plugin;

public class FormatTool implements ToolPlugin {

    @Override
    public String getName() {
        return "代码格式化";
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
