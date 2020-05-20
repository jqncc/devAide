package org.jflame.devAide.plugin;

public class RegexTool implements ToolPlugin {

    @Override
    public String getName() {
        return "正则表达式";
    }

    @Override
    public String getFxml() {
        return "regexTester";
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getIcon() {
        return "@SORT_ALPHA_ASC";
    }

}
