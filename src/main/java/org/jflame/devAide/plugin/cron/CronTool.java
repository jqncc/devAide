package org.jflame.devAide.plugin.cron;

import org.jflame.devAide.AppSetting;
import org.jflame.devAide.plugin.ToolPlugin;

public class CronTool implements ToolPlugin {

    @Override
    public String getName() {
        return AppSetting.getString("plugin.cron.name");
    }

    @Override
    public String getFxml() {
        return "/org/jflame/devAide/plugin/cron/cronTool";
    }

    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    public String getIcon() {
        return "@CLOCK_ALT";
    }

}
