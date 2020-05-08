package org.jflame.devAide.controller;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
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
import org.jflame.commons.common.bean.pair.NameValuePair;
import org.jflame.commons.util.DateHelper;
import org.jflame.commons.util.NumberHelper;
import org.jflame.commons.util.StringHelper;
import org.jflame.devAide.AppContext;
import org.jflame.devAide.component.FileField;
import org.jflame.devAide.component.IntFieldFormatter;
import org.jflame.devAide.util.UIComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

public class QrcodeController {

    private final Logger logger = LoggerFactory.getLogger(QrcodeController.class);
    @FXML
    private FileField logoField;
    @FXML
    private TextArea barcodeText;
    @FXML
    private ImageView codeImageViwer;
    @FXML
    private Button btnCreate;
    @FXML
    private ComboBox<NameValuePair> cbxFault;
    @FXML
    private ComboBox<NameValuePair> cbxBarcodeType;
    @FXML
    private ColorPicker cpickerBarcodeBg;
    @FXML
    private ColorPicker cpickerBarcodeFg;
    @FXML
    private StackPane barcodeBox;
    @FXML
    private Spinner<Integer> spnBarcodeSize;
    @FXML
    private Spinner<Integer> spnLogoSize;
    @FXML
    private Label lblParser;
    @FXML
    private Label lblNoCodeImageMsg;

    private final Path qrcodeCacheDir;
    private final String IMG_FORMAT_JPG = "jpg";
    private final int BARCODE_DEFAULT_SIZE = 200;
    private final int LOGO_DEFAULT_SIZE = 30;

    private CbxNVPairConverter cbxNVPairConverter = new CbxNVPairConverter();

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
                        System.out.println(newValue.get(0)
                                .getPath());
                    }
                });

        cpickerBarcodeBg.setValue(Color.WHITE);
        cpickerBarcodeFg.setValue(Color.BLACK);

        // 容错等级下拉框数据初始
        Tooltip faultTip = new Tooltip("二维码容错等级分为：L(7%)、M(15%)、Q(25%)、H(30%)四级,容错率越高则二维码图片能被遮挡的部分越多");
        cbxFault.setTooltip(faultTip);
        List<NameValuePair> ecLevels = NameValuePair.newPairs()
                .add("L(7%)", "L")
                .add("M(15%)", "M")
                .add("Q(25%)", "Q")
                .add("H(30%)", "H")
                .toList();
        bindNVPairToCombox(cbxFault, ecLevels, 2, 4);

        // 条码码制下拉框数据初始
        List<NameValuePair> barcodeTypes = NameValuePair.newPairs()
                .add("Qrcode(二维码)", "QR_CODE")
                .add("DataMatrix(二维码)", "DATA_MATRIX")
                .add("MaxiCode(二维码)", "DATA_MATRIX")
                .add("PDF417(二维码)", "PDF_417")
                .add("Code39(条形码)", "CODE_39")
                .add("Code128(条形码)", "CODE_128")
                .add("Code93(条形码)", "CODE_93")
                .toList();
        bindNVPairToCombox(cbxBarcodeType, barcodeTypes, 0, 7);

        codeImageViwer.setVisible(false);

        // spinner
        spnBarcodeSize
                .setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(80, 1000, BARCODE_DEFAULT_SIZE));
        spnBarcodeSize.getEditor()
                .setTextFormatter(new IntFieldFormatter());// 限制只能输入数字
        spnLogoSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(LOGO_DEFAULT_SIZE, 300));
        spnLogoSize.getEditor()
                .setTextFormatter(new IntFieldFormatter());
    }

    private void bindNVPairToCombox(ComboBox<NameValuePair> cbx, List<NameValuePair> dataItems, Integer selectedIndex,
            Integer visibleRowCount) {
        ObservableList<NameValuePair> items = FXCollections.observableArrayList(dataItems);
        cbx.setItems(items);
        cbx.setConverter(cbxNVPairConverter);
        if (selectedIndex != null) {
            cbx.setValue(dataItems.get(selectedIndex));
        }
        if (visibleRowCount != null) {
            cbx.setVisibleRowCount(visibleRowCount);
        }
    }

    private boolean is2DBarcode(BarcodeFormat barcodeType) {
        return barcodeType == BarcodeFormat.QR_CODE || barcodeType == BarcodeFormat.AZTEC
                || barcodeType == BarcodeFormat.DATA_MATRIX || barcodeType == BarcodeFormat.MAXICODE
                || barcodeType == BarcodeFormat.PDF_417;
    }

    /**
     * 生成条码事件
     * 
     * @param event
     */
    @FXML
    protected void handleGenerateQRcode(ActionEvent event) {
        if (StringHelper.isEmpty(barcodeText.getText())) {
            barcodeText.requestFocus();
            return;
        }
        if (!initCacheDir()) {
            return;
        }
        String content = barcodeText.getText()
                .strip();
        BarcodeFormat barcodeType = BarcodeFormat.valueOf(cbxBarcodeType.getValue()
                .getValue());
        boolean is2Dbarcode = is2DBarcode(barcodeType);

        File logoPath = logoField.getFirstSelectedFile();

        Integer barcodeSize = spnBarcodeSize.getValue(); // 图像宽度
        if (NumberHelper.isNullOrZero(barcodeSize)) {
            barcodeSize = BARCODE_DEFAULT_SIZE;
        }
        double maxShowWidth = barcodeBox.getWidth() - barcodeBox.getHeight() > 0 ? barcodeBox.getHeight()
                : barcodeBox.getWidth();
        if (is2Dbarcode && logoPath != null) {
            Integer logoSize = spnLogoSize.getValue();
            if (NumberHelper.isNullOrZero(logoSize)) {
                logoSize = LOGO_DEFAULT_SIZE;
            }
            if (logoSize >= barcodeSize * 0.6) {
                UIComponents.createAlert("Logo尺寸过大影响识别率真,请重新调整大小");
                return;
            }
        }
        Path newBarcodePath = Paths.get(qrcodeCacheDir.toString(), createBarcodeName());
        Map<EncodeHintType,Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        // 容错等级
        ErrorCorrectionLevel ecLevel = ErrorCorrectionLevel.Q;
        if (!cbxFault.getSelectionModel()
                .isEmpty()) {
            ecLevel = ErrorCorrectionLevel.valueOf(cbxFault.getSelectionModel()
                    .getSelectedItem()
                    .getValue());
        }
        hints.put(EncodeHintType.ERROR_CORRECTION, ecLevel);
        hints.put(EncodeHintType.MARGIN, 1);

        Color fgColor = cpickerBarcodeFg.getValue();
        int offColor = UIComponents.colorToArgb(cpickerBarcodeBg.getValue());// 背景色
        // int onColor = UIComponents.colorToArgb(cpickerBarcodeFg.getValue());// 前景色
        int onColor;
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, barcodeType, barcodeSize, barcodeSize, hints);
            if (is2Dbarcode && logoPath != null) {
                // 在要设置logo时,前景色纯黑色时微调一点,避免zxing会把logo颜色变为黑白的bug
                if (fgColor == Color.BLACK) {
                    onColor = UIComponents.colorToArgb(Color.valueOf("0x000001FF"));
                } else {
                    onColor = UIComponents.colorToArgb(cpickerBarcodeFg.getValue());
                }
                MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(onColor, offColor);
                BufferedImage bufferedImage = LogoMatrix(
                        MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig), logoPath);
                ImageIO.write(bufferedImage, IMG_FORMAT_JPG, newBarcodePath.toFile());// 输出带log
            } else {
                onColor = UIComponents.colorToArgb(cpickerBarcodeFg.getValue());
                MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(onColor, offColor);
                MatrixToImageWriter.writeToPath(bitMatrix, IMG_FORMAT_JPG, newBarcodePath, matrixToImageConfig);// 输出原图片
            }
            System.out.println(newBarcodePath);
            showBarcodeImage(new Image(newBarcodePath.toUri()
                    .toURL()
                    .toExternalForm()), barcodeSize > maxShowWidth ? maxShowWidth : barcodeSize);
        } catch (WriterException | IOException e) {
            UIComponents.createExDialog("生成条码异常", e);
            logger.error("生成条码异常,内容:{},ex:{}", content, e.getMessage());
        }

    }

    private void showBarcodeImage(Image image, double showWidth) {
        lblNoCodeImageMsg.setVisible(false);

        codeImageViwer.setFitWidth(showWidth);
        codeImageViwer.setFitHeight(showWidth);
        codeImageViwer.setVisible(true);
        codeImageViwer.setImage(image);
    }

    /**
     * 初始条码文件缓存目录
     */
    private boolean initCacheDir() {
        if (Files.notExists(qrcodeCacheDir)) {
            try {
                Files.createDirectories(qrcodeCacheDir);
                qrcodeCacheDir.toFile()
                        .setWritable(true);
            } catch (IOException e) {
                UIComponents.createExDialog("无法创建二维码缓存目录" + qrcodeCacheDir, e)
                        .show();
                return false;
            }
        }
        return true;
    }

    /**
     * 二维码添加logo
     * 
     * @param matrixImage 源二维码图片
     * @param logoFile logo图片
     * @return 返回带有logo的二维码图片 参考：https://blog.csdn.net/weixin_39494923/article/details/79058799
     */
    public static BufferedImage LogoMatrix(BufferedImage matrixImage, File logoFile) throws IOException {
        Graphics2D g2 = matrixImage.createGraphics();

        int matrixWidth = matrixImage.getWidth();
        int matrixHeigh = matrixImage.getHeight();

        BufferedImage logo = ImageIO.read(logoFile);

        g2.drawImage(logo, matrixWidth / 5 * 2, matrixHeigh / 5 * 2, matrixWidth / 5, matrixHeigh / 5, null);// 绘制logo
        BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke);// 设置笔画对象
        // 绘制圆角矩形
        RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth / 5 * 2, matrixHeigh / 5 * 2,
                matrixWidth / 5, matrixHeigh / 5, 5, 5);
        g2.setColor(java.awt.Color.white);
        g2.draw(round);// 绘制圆弧矩形

        // 绘制灰色边框
        BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke2);
        RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth / 5 * 2 + 2, matrixHeigh / 5 * 2 + 2,
                matrixWidth / 5 - 4, matrixHeigh / 5 - 4, 5, 5);
        g2.setColor(new java.awt.Color(128, 128, 128));
        g2.draw(round2);// 绘制圆弧矩形

        g2.dispose();
        matrixImage.flush();
        return matrixImage;
    }

    private String createBarcodeName() {
        return DateHelper.format(LocalDateTime.now(), DateHelper.yyyyMMddHHmmssSSS)
                + RandomStringUtils.random(3, true, true) + '.' + IMG_FORMAT_JPG;
    }

    static class CbxNVPairConverter extends StringConverter<NameValuePair> {

        @Override
        public String toString(NameValuePair object) {
            return object.getKey();
        }

        @Override
        public NameValuePair fromString(String string) {
            return null;
        }
    }
}
