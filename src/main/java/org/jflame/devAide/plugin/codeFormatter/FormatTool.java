package org.jflame.devAide.plugin.codeFormatter;

import org.jflame.devAide.AppSetting;
import org.jflame.devAide.plugin.ToolPlugin;

public class FormatTool implements ToolPlugin {

    @Override
    public String getName() {
        return AppSetting.getString("plugin.codeformat.name");
    }

    @Override
    public String getFxml() {
        return "/org/jflame/devAide/plugin/codeFormatter/formatTool";
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
