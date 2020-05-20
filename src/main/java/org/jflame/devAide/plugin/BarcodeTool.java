package org.jflame.devAide.plugin;

public class BarcodeTool implements ToolPlugin {

    @Override
    public String getName() {
        return "条码生成器";
    }

    @Override
    public String getFxml() {
        return "qrcodeTool";
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getIcon() {
        return "@QRCODE";
    }

}
