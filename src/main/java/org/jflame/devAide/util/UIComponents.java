package org.jflame.devAide.util;

import org.controlsfx.dialog.ExceptionDialog;
import org.jflame.devAide.AppContext;
import org.jflame.devAide.component.MyIntegerSpinnerValueFactory;
import org.jflame.devAide.component.convertor.IntFieldFormatter;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * 提供一些控件的便捷方法
 *
 * @author yucan.zhang
 */
public final class UIComponents {

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
        dlg.setTitle("异常提示");
        // 对话框的Scene是独立的,样式没有共用主界面,所以需要单独引用
        dlg.getDialogPane()
                .getStylesheets()
                .addAll(AppContext.getStyleFiles());
        return dlg;
    }

    public static void showExDialog(String errorMsg, Exception e) {
        createExDialog(errorMsg, e).show();
    }

    public static Alert createAlert(String errorMsg) {
        Alert alert = new Alert(AlertType.INFORMATION, errorMsg, ButtonType.OK);
        /* alert.getDialogPane()
                .setStyle("-fx-background-color:#fff;-fx-border-width:1px;-fx-border-color:#ccc");
        alert.setHeaderText(null);
        alert.initStyle(StageStyle.UNDECORATED);
        double x,y;
        alert.getDialogPane()
                .getContent()
                .setOnMousePressed(new EventHandler<MouseEvent>() {
        
                    @Override
                    public void handle(MouseEvent event) {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    }
        
                });
        alert.setGraphic(Globals.FONT_AWESOME.create(FontAwesome.Glyph.EXCLAMATION_CIRCLE)
                .color(Color.WHITE)
                .size(18));*/
        return alert;
    }

    public static void showWarnAlert(String warnMsg) {
        new Alert(AlertType.WARNING, warnMsg, ButtonType.OK).show();
    }

    public static void showErrorAlert(String errorMsg) {
        new Alert(AlertType.ERROR, errorMsg, ButtonType.OK).show();
    }

    /**
     * 返回Color所表示颜色ARGB模式的int值
     * 
     * @param color Color
     * @return
     */
    public static int colorToArgb(Color color) {
        int r = (int) Math.round(color.getRed() * 255.0);
        int g = (int) Math.round(color.getGreen() * 255.0);
        int b = (int) Math.round(color.getBlue() * 255.0);
        int o = (int) Math.round(color.getOpacity() * 255.0);
        String hexColor = String.format("%02x%02x%02x%02x", o, r, g, b);
        return Integer.parseUnsignedInt(hexColor, 16);
    }

    public static void hide(Node node) {
        node.setVisible(false);
        node.setManaged(false);
    }

    public static void show(Node node) {
        node.setVisible(true);
        node.setManaged(true);
    }

    /**
     * 设置spinner为只可输入整数.初始值为最小值
     * 
     * @param spinner
     * @param min
     * @param max
     */
    public static void setSpinnerForInteger(Spinner<Integer> spinner, int min, int max) {
        setSpinnerForInteger(spinner, min, max, min);
    }

    /**
     * 设置spinner为只可输入整数.
     * 
     * @param spinner
     * @param min
     * @param max
     * @param initialValue 初始值
     */
    public static void setSpinnerForInteger(Spinner<Integer> spinner, int min, int max, int initialValue) {
        setSpinnerForInteger(spinner, min, max, initialValue, 1);
    }

    /**
     * 设置spinner为只可输入整数
     * 
     * @param spinner
     * @param min
     * @param max
     * @param initialValue 初始值
     * @param amountToStepBy 增长步长
     */
    public static void setSpinnerForInteger(Spinner<Integer> spinner, int min, int max, int initialValue,
            int amountToStepBy) {
        spinner.setValueFactory(new MyIntegerSpinnerValueFactory(min, max, initialValue, amountToStepBy));
        spinner.getEditor()
                .setTextFormatter(new IntFieldFormatter());
    }
}
