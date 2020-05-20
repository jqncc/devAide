package org.jflame.devAide.plugin;

public class CronTool implements ToolPlugin {

    @Override
    public String getName() {
        return "CRON表达式";
    }

    @Override
    public String getFxml() {
        return "cronTool";
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
