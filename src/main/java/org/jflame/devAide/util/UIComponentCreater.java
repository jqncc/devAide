package org.jflame.devAide.util;

import org.controlsfx.dialog.ExceptionDialog;
import org.jflame.devAide.Globals;

import javafx.scene.image.ImageView;

/**
 * 提供一些控件的创建方法
 *
 * @author yucan.zhang
 */
public final class UIComponentCreater {

    /**
     * 用给定的图片路径创建一个ImageView
     *
     * @param imagePath 图片路径
     * @return
     */
    public static ImageView createImageView(String imagePath) {
        return new ImageView(ResourceUtils.absUrl(imagePath));
    }

    /**
     * 用给定的图片路径创建一个ImageView,并指定图片宽高
     *
     * @param imagePath 图片路径
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static ImageView createImageView(String imagePath, double width, double height) {
        ImageView image = new ImageView(ResourceUtils.absUrl(imagePath));
        image.setFitWidth(width);
        image.setFitHeight(height);
        return image;
    }

    /**
     * 创建一个异常信息框
     * 
     * @param errorMsg
     * @param e
     * @return
     */
    public static ExceptionDialog createExDialog(String errorMsg, Exception e) {
        ExceptionDialog dlg = new ExceptionDialog(e);
        dlg.setHeaderText(errorMsg);
        // 对话框的Scene是独立的,样式没有共用主界面,所以需要单独引用
        dlg.getDialogPane()
                .getStylesheets()
                .addAll(Globals.getCssFiles());
        return dlg;
    }

}
