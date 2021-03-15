package org.jflame.devAide.plugin.regex;

import org.jflame.devAide.AppSetting;
import org.jflame.devAide.plugin.ToolPlugin;

public class RegexTool implements ToolPlugin {

    @Override
    public String getName() {
        return AppSetting.getString("plugin.regex.name");
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
