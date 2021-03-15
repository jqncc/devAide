package org.jflame.devAide.plugin;

import org.jflame.commons.util.StringHelper;
import org.jflame.devAide.AppSetting;
import org.jflame.devAide.util.FxUtils;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * 工具型插件父类
 * 
 * @author yucan.zhang
 */
public interface ToolPlugin extends Comparable<ToolPlugin> {

    /**
     * 插件名
     * 
     * @return
     */
    public String getName();

    /**
     * 插件的主fxml文件,如果不指定fxml文件请重写getContent()方法返回
     * 
     * @return
     */
    public String getFxml();

    /**
     * 插件显示顺序
     * 
     * @return
     */
    public int getOrder();

    /**
     * 返回插件的根节点,默认实现为从fxml文件加载
     * 
     * @return
     */
    default public Node getContent() {
        if (StringHelper.isNotEmpty(getFxml())) {
            return FxUtils.loadFXML(getFxml());
        }
        return new Label("未指定插件主文件");
    }

    /**
     * 插件描述
     * 
     * @return
     */
    default public String getDesc() {
        return null;
    }

    /**
     * 插件图标
     * 
     * @return
     */
    default public String getIcon() {
        return null;
    }

    default public Node icon(double size) {
        // @开头为内置FontAwesome字体图标
        if (getIcon().charAt(0) == '@') {
            return AppSetting.FONT_AWESOME.create(getIcon().substring(1))
                    .size(size);
        } else {
            ImageView imageView = new ImageView(getIcon());
            imageView.setFitHeight(size);
            imageView.setFitWidth(size);
            return imageView;
        }
    }

    @Override
    default int compareTo(ToolPlugin o) {
        if (this.getOrder() > o.getOrder())
            return 1;
        else if (this.getOrder() < o.getOrder())
            return -1;
        else
            return this.getName()
                    .compareTo(o.getName());
    }
}
