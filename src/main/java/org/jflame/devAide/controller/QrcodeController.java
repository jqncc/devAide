package org.jflame.devAide.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.jflame.commons.util.DateHelper;
import org.jflame.commons.util.EnumHelper;
import org.jflame.commons.util.StringHelper;
import org.jflame.devAide.AppContext;
import org.jflame.devAide.component.FileField;
import org.jflame.devAide.util.UIComponentCreater;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class QrcodeController {

    @FXML
    private FileField logoField;
    @FXML
    private TextArea codeText;
    @FXML
    private ImageView codeImage;
    @FXML
    private Button btnCreate;
    @FXML
    private ComboBox<String> cbxFault;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private VBox qrcodeBox;

    private final Path qrcodeCacheDir;
    private final String barcodeFormat = "jpg";// 图像类型

    public QrcodeController() {
        qrcodeCacheDir = Paths.get(AppContext.getInstance()
                .projDataDir(), "qrcode");
    }

    @FXML
    protected void initialize() {
        logoField.addExtensionFilter("图片", "*.png", "*.jpg", "*.gif", "*.bmp");
        logoField.selectedFileProperty()
                .addListener(new ChangeListener<List<File>>() {

                    @Override
                    public void changed(ObservableValue<? extends List<File>> observable, List<File> oldValue,
                            List<File> newValue) {
                        System.out.println(newValue);
                    }
                });

        colorPicker.setValue(Color.BLACK);
        Tooltip faultTip = new Tooltip("二维码容错等级分为：L(7%)、M(15%)、Q(25%)、H(30%)四级,容错率越高则二维码图片能被遮挡的部分越多");
        ObservableList<String> faultOptions = FXCollections
                .observableArrayList(EnumHelper.enumNames(ErrorCorrectionLevel.class));
        cbxFault.setItems(faultOptions);
        cbxFault.setTooltip(faultTip);

        Label label = new Label("暂未生成二维码");
        qrcodeBox.getChildren()
                .add(label);

    }

    @FXML
    protected void handleGenerateQRcode(ActionEvent event) {
        if (StringHelper.isEmpty(codeText.getText())) {
            return;
        }

        if (Files.notExists(qrcodeCacheDir)) {
            try {
                Files.createDirectories(qrcodeCacheDir);
            } catch (IOException e) {
                UIComponentCreater.createExDialog("无法创建二维码缓存目录" + qrcodeCacheDir, e)
                        .show();
                return;
            }
        }
        String content = codeText.getText()
                .strip();
        int width = 200; // 图像宽度
        int height = 200; // 图像高度

        Path newBarcodePath = Paths.get(qrcodeCacheDir.toString(), createBarcodeName());

        Map<EncodeHintType,Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        // 容错等级
        ErrorCorrectionLevel ecLevel = ErrorCorrectionLevel.Q;
        if (!cbxFault.getSelectionModel()
                .isEmpty()) {
            ecLevel = ErrorCorrectionLevel.valueOf(cbxFault.getSelectionModel()
                    .getSelectedItem());
        }
        hints.put(EncodeHintType.ERROR_CORRECTION, ecLevel);
        hints.put(EncodeHintType.MARGIN, 1);
        // 背景色
        Color color = colorPicker.getValue();
        int onColor = Integer.parseInt(color.toString(), 16);
        System.out.println("on color:" + color.toString());

        File logoPath = logoField.getFirstSelectedFile();

        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(onColor, 0xFFFFFFFF);
            MatrixToImageWriter.writeToPath(bitMatrix, barcodeFormat, newBarcodePath, matrixToImageConfig);// 输出原图片

            /* 
             * 问题：生成二维码正常,生成带logo的二维码logo变成黑白
             原因：MatrixToImageConfig默认黑白，需要设置BLACK、WHITE
             解决：https://ququjioulai.iteye.com/blog/2254382
            */
            if (logoPath != null) {
                BufferedImage bufferedImage = LogoMatrix(
                        MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig), logoPath);
                // BufferedImage bufferedImage = LogoMatrix(toBufferedImage(bitMatrix), new File("D:\\logo.png"));
                ImageIO.write(bufferedImage, "gif", new File("D:\\zxing1.gif"));// 输出带log
            }
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 二维码添加logo
     * 
     * @param matrixImage 源二维码图片
     * @param logoFile logo图片
     * @return 返回带有logo的二维码图片 参考：https://blog.csdn.net/weixin_39494923/article/details/79058799
     */
    public static BufferedImage LogoMatrix(BufferedImage matrixImage, File logoFile) throws IOException {
        /**
         * 读取二维码图片，并构建绘图对象
         */
        Graphics2D g2 = matrixImage.createGraphics();

        int matrixWidth = matrixImage.getWidth();
        int matrixHeigh = matrixImage.getHeight();

        /**
         * 读取Logo图片
         */
        BufferedImage logo = ImageIO.read(logoFile);

        // 开始绘制图片
        /* g2.drawImage(logo, matrixWidth / 5 * 2, matrixHeigh / 5 * 2, matrixWidth / 5, matrixHeigh / 5, null);// 绘制
        BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke);// 设置笔画对象
        // 指定弧度的圆角矩形
        RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth / 5 * 2, matrixHeigh / 5 * 2,
                matrixWidth / 5, matrixHeigh / 5, 20, 20);
        g2.setColor(Color.WHITE);
        g2.draw(round);// 绘制圆弧矩形
        
        // 设置logo 有一道灰色边框
        BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke2);// 设置笔画对象
        RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth / 5 * 2 + 2, matrixHeigh / 5 * 2 + 2,
                matrixWidth / 5 - 4, matrixHeigh / 5 - 4, 20, 20);
        g2.setColor(Color.color(128, 128, 128));
        g2.draw(round2);// 绘制圆弧矩形
        */
        g2.dispose();
        matrixImage.flush();
        return matrixImage;
    }

    private String createBarcodeName() {
        return DateHelper.format(LocalDateTime.now(), DateHelper.yyyyMMddHHmmssSSS)
                + RandomStringUtils.random(3, true, true) + '.' + barcodeFormat;
    }
}
