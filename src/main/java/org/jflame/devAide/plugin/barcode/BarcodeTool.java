package org.jflame.devAide.plugin.barcode;

import org.jflame.devAide.AppSetting;
import org.jflame.devAide.plugin.ToolPlugin;

public class BarcodeTool implements ToolPlugin {

    @Override
    public String getName() {
        return AppSetting.getString("plugin.bc.name");
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
