package org.jflame.devAide.controller;

import java.awt.BasicStroke;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.jflame.commons.common.bean.pair.NameValuePair;
import org.jflame.commons.util.DateHelper;
import org.jflame.commons.util.NumberHelper;
import org.jflame.commons.util.StringHelper;
import org.jflame.commons.util.json.JsonHelper;
import org.jflame.commons.valid.ValidatorHelper;
import org.jflame.devAide.AppContext;
import org.jflame.devAide.component.FileField;
import org.jflame.devAide.component.MyGraphicValidationDecoration;
import org.jflame.devAide.component.convertor.IntFieldFormatter;
import org.jflame.devAide.component.convertor.NVPairConverter;
import org.jflame.devAide.model.BarcodeInfo;
import org.jflame.devAide.util.UIComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

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
    private Hyperlink lblParser;
    @FXML
    private Label lblNoCodeImageMsg;
    @FXML
    private Button btnOpenBarcode;
    @FXML
    private CheckBox chbxLogoBorder;
    @FXML
    private ListView<BarcodeInfo> historyView;

    private Tooltip imgViewTip;

    private final Path qrcodeCacheDir;
    private final Path recordPath;

    private final String IMG_FORMAT_JPG = "jpg";
    private final int QRCODE_DEFAULT_WIDTH = 250;// 二维码默认尺寸
    private final int LOGO_DEFAULT_WIDTH = QRCODE_DEFAULT_WIDTH / 5; // logo默认尺寸
    private final float BARCODE_ASPECT_RATIO = 0.5f;// 条形码宽高比2:1

    private NVPairConverter cbxNVPairConverter = new NVPairConverter();
    private ValidationSupport validationSupport = new ValidationSupport();

    private List<NameValuePair> ecLevels;
    private List<NameValuePair> barcodeTypes;
    private ObservableList<BarcodeInfo> barcodeRecords = FXCollections.observableArrayList();

    public QrcodeController() {
        qrcodeCacheDir = Paths.get(AppContext.getInstance()
                .projDataDir(), "barcode");
        recordPath = Paths.get(qrcodeCacheDir.toString(), "record.json");

        ecLevels = NameValuePair.newPairs()
                .add("L(7%)", "L")
                .add("M(15%)", "M")
                .add("Q(25%)", "Q")
                .add("H(30%)", "H")
                .toList();
        barcodeTypes = NameValuePair.newPairs()
                .add("Qrcode(二维码)", "QR_CODE")
                .add("DataMatrix(二维码)", "DATA_MATRIX")
                .add("MaxiCode(二维码)", "DATA_MATRIX")
                .add("PDF417(二维码)", "PDF_417")
                .add("Code39(条形码)", "CODE_39")
                .add("Code128(条形码)", "CODE_128")
                .add("Code93(条形码)", "CODE_93")
                .toList();
    }

    @FXML
    protected void initialize() {
        initCodeContentValiation();
        logoField.addExtensionFilter("图片", "*.png", "*.jpg", "*.gif", "*.bmp");
        /* logoField.selectedFileProperty()
                .addListener(new ChangeListener<List<File>>() {
        
                    @Override
                    public void changed(ObservableValue<? extends List<File>> observable, List<File> oldValue,
                            List<File> newValue) {
                        System.out.println(newValue.get(0)
                                .getPath());
                
                    }
                });*/

        cpickerBarcodeBg.setValue(Color.WHITE);
        cpickerBarcodeFg.setValue(Color.BLACK);

        // 容错等级下拉框数据初始
        Tooltip faultTip = new Tooltip("二维码容错等级分为：L(7%)、M(15%)、Q(25%)、H(30%)四级,容错率越高则二维码图片能被遮挡的部分越多");
        cbxFault.setTooltip(faultTip);
        bindNVPairToCombox(cbxFault, ecLevels, 2, 4);

        // 条码码制下拉框数据初始
        bindNVPairToCombox(cbxBarcodeType, barcodeTypes, 0, 7);
        cbxBarcodeType.getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<NameValuePair>() {

                    @Override
                    public void changed(ObservableValue<? extends NameValuePair> observable, NameValuePair oldValue,
                            NameValuePair newValue) {
                        cbxFault.setDisable(!is2DBarcode(BarcodeFormat.valueOf(newValue.getValue())));
                    }
                });

        codeImageViwer.setVisible(false);
        // spinner
        spnBarcodeSize
                .setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(80, 1000, QRCODE_DEFAULT_WIDTH));
        spnBarcodeSize.getEditor()
                .setTextFormatter(new IntFieldFormatter());// 限制只能输入数字
        spnLogoSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 400, LOGO_DEFAULT_WIDTH));
        spnLogoSize.getEditor()
                .setTextFormatter(new IntFieldFormatter());

        historyView.setItems(barcodeRecords);
        Platform.runLater(() -> {
            if (!Files.exists(recordPath)) {
                try {
                    Files.createFile(recordPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String recordStr;
                try {
                    recordStr = Files.readString(recordPath);
                    if (StringHelper.isNotEmpty(recordStr)) {
                        List<BarcodeInfo> lst = JsonHelper.parseList(recordStr, BarcodeInfo.class);
                        barcodeRecords.addAll(lst);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initCodeContentValiation() {
        validationSupport.setValidationDecorator(new MyGraphicValidationDecoration());
        validationSupport.registerValidator(barcodeText, true, new Validator<String>() {

            @Override
            public ValidationResult apply(Control ctl, String value) {
                boolean condition = true;
                if (!is2DBarcode(BarcodeFormat.valueOf(cbxBarcodeType.getValue()
                        .getValue()))) {
                    condition = ValidatorHelper.regex(value, "[0-9a-zA-Z]{1,80}");
                }
                return ValidationResult.fromErrorIf(ctl, "条形码内容只能是字母或数字,且长度小于80", !condition);
            }
        });
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
    protected void handleCreateBarcode(ActionEvent event) {
        if (StringHelper.isEmpty(barcodeText.getText())) {
            barcodeText.requestFocus();
            return;
        }
        if (!initCacheDir()) {
            return;
        }
        validationSupport.redecorate();
        if (validationSupport.isInvalid()) {
            return;
        }
        BarcodeInfo codeInfo = new BarcodeInfo();
        codeInfo.setFormat(IMG_FORMAT_JPG);
        String content = barcodeText.getText()
                .strip();
        codeInfo.setContent(content);
        BarcodeFormat barcodeType = BarcodeFormat.valueOf(cbxBarcodeType.getValue()
                .getValue());
        File logoPath = logoField.getFirstSelectedFile();
        boolean is2Dbarcode = is2DBarcode(barcodeType) && logoPath != null;

        Integer barcodeWidth = spnBarcodeSize.getValue(); // 图像宽度
        int barcodeHegiht;
        if (NumberHelper.isNullOrZero(barcodeWidth)) {
            barcodeWidth = QRCODE_DEFAULT_WIDTH;
        }
        Integer logoSize = spnLogoSize.getValue();
        Path newBarcodePath = Paths.get(qrcodeCacheDir.toString(), createBarcodeName());
        Map<EncodeHintType,Object> hints = null;
        if (is2Dbarcode) {
            barcodeHegiht = barcodeWidth;
            if (NumberHelper.isNullOrZero(logoSize)) {
                logoSize = LOGO_DEFAULT_WIDTH;
            }
            if (logoSize >= barcodeWidth * 0.5) {
                UIComponents.showWarnAlert("Logo尺寸过大影响识别率真,请重新调整大小");
                return;
            }
            hints = new HashMap<>();
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
            codeInfo.setErrorCorrectionLevel(ecLevel.name());
        } else {
            barcodeHegiht = (int) (barcodeWidth * BARCODE_ASPECT_RATIO);
        }

        Color fgColor = cpickerBarcodeFg.getValue();
        int offColor = UIComponents.colorToArgb(cpickerBarcodeBg.getValue());// 背景色
        int onColor;

        codeInfo.setWidth(barcodeWidth);
        codeInfo.setHeight(barcodeHegiht);
        codeInfo.setBgColor(cpickerBarcodeBg.getValue()
                .toString());
        codeInfo.setFgColor(cpickerBarcodeFg.getValue()
                .toString());
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, barcodeType, barcodeWidth, barcodeHegiht, hints);
            if (is2Dbarcode) {
                // 在要设置logo时,前景色纯黑色时微调一点,避免zxing会把logo颜色变为黑白的bug
                if (fgColor == Color.BLACK) {
                    onColor = UIComponents.colorToArgb(Color.valueOf("0x000001FF"));
                } else {
                    onColor = UIComponents.colorToArgb(cpickerBarcodeFg.getValue());
                }
                MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(onColor, offColor);
                BufferedImage barcodeBufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix,
                        matrixToImageConfig);
                BufferedImage bufferedImage = drawLogoMatrix(barcodeBufferedImage, logoPath, logoSize,
                        chbxLogoBorder.isSelected());
                codeInfo.setLogoPath(logoPath.toString());
                codeInfo.setLogoSize(logoSize);
                ImageIO.write(bufferedImage, IMG_FORMAT_JPG, newBarcodePath.toFile());
            } else {
                onColor = UIComponents.colorToArgb(cpickerBarcodeFg.getValue());
                MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(onColor, offColor);
                MatrixToImageWriter.writeToPath(bitMatrix, IMG_FORMAT_JPG, newBarcodePath, matrixToImageConfig);
            }

            showBarcodeImage(newBarcodePath, barcodeWidth, barcodeHegiht);
            codeInfo.setBarcodePath(newBarcodePath.toString());
            codeInfo.setCreateTime(LocalDateTime.now());
            barcodeRecords.add(codeInfo);
        } catch (WriterException | IOException | IllegalArgumentException e) {
            UIComponents.createExDialog("生成条码异常", e)
                    .show();
            logger.error("生成条码异常,内容:{},ex:{}", content, e.getMessage());
        }
    }

    /**
     * 在系统中直接打开生成的条码图片
     * 
     * @param event
     */
    @FXML
    protected void handleOpenBarcodeInExplorer(ActionEvent event) {
        Path currentBarPath = (Path) codeImageViwer.getUserData();
        if (currentBarPath != null) {
            try {
                Desktop.getDesktop()
                        .open(currentBarPath.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private FileChooser parseFileChooser;

    /**
     * 条码解码事件
     * 
     * @param event
     */
    @FXML
    protected void handleParseBarcode(ActionEvent event) {
        if (parseFileChooser == null) {
            parseFileChooser = new FileChooser();
            parseFileChooser.getExtensionFilters()
                    .add(new ExtensionFilter("图片", "*.png", "*.jpg", "*.gif"));
        }
        File selectedBarcodeFile = parseFileChooser.showOpenDialog(lblParser.getScene()
                .getWindow());
        if (selectedBarcodeFile != null) {
            MultiFormatReader formatReader = new MultiFormatReader();
            // 读取指定的二维码文件
            BufferedImage bufferedImage;
            try {
                bufferedImage = ImageIO.read(selectedBarcodeFile);
                BinaryBitmap binaryBitmap = new BinaryBitmap(
                        new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
                com.google.zxing.Result result = formatReader.decode(binaryBitmap);

                barcodeText.setText(result.getText());
                barcodeText.selectAll();
                bufferedImage.flush();
                parseFileChooser.setInitialDirectory(selectedBarcodeFile.getParentFile());
            } catch (IOException | NotFoundException e) {
                UIComponents.createExDialog("解码失败", e)
                        .show();
            }
        }

    }

    @FXML
    protected void handleOpenBarcodeDir(ActionEvent event) {
        Path currentBarPath = (Path) codeImageViwer.getUserData();
        if (currentBarPath != null) {
            try {
                Desktop.getDesktop()
                        .browseFileDirectory(currentBarPath.toFile());
            } catch (UnsupportedOperationException e) {
                String osName = System.getProperty("os.name");
                if (osName.startsWith("Windows")) {
                    try {
                        Runtime.getRuntime()
                                .exec("explorer /select, " + currentBarPath.toUri()
                                        .toURL()
                                        .toExternalForm());
                    } catch (IOException e1) {
                        UIComponents.showErrorAlert("当前操作系统不支持该操作,请复制路径打开");
                    }
                }
            } catch (Exception e) {
                logger.error("打开条码目录失败" + currentBarPath, e);
            }
        }
    }

    private void showBarcodeImage(Path barcodePath, int barcodeWidth, int barcodeHegiht) throws MalformedURLException {
        lblNoCodeImageMsg.setVisible(false);

        codeImageViwer.setFitWidth(barcodeWidth);
        codeImageViwer.setFitHeight(barcodeHegiht);
        codeImageViwer.setImage(new Image(barcodePath.toUri()
                .toURL()
                .toExternalForm()));
        codeImageViwer.setUserData(barcodePath);
        if (imgViewTip == null) {
            imgViewTip = new Tooltip();
            imgViewTip.setShowDelay(Duration.millis(100d));
            Tooltip.install(codeImageViwer, imgViewTip);
        }
        imgViewTip.setText(codeImageViwer.getUserData()
                .toString());
        codeImageViwer.setVisible(true);
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
     * @param isDrawBroder logo周边是否绘制一个边框
     * @return 返回带有logo的BufferedImage
     */
    private BufferedImage drawLogoMatrix(BufferedImage matrixImage, File logoFile, int logoSize, boolean isDrawBroder)
            throws IOException {
        Graphics2D g2 = matrixImage.createGraphics();

        int matrixWidth = matrixImage.getWidth();
        int matrixHeigh = matrixImage.getHeight();
        int logoX = (matrixWidth - logoSize) / 2;
        int logoY = (matrixHeigh - logoSize) / 2;

        // 绘制logo
        BufferedImage logo = ImageIO.read(logoFile);
        g2.drawImage(logo, logoX, logoY, logoSize, logoSize, null);// 绘制logo

        // 外圈绘制个白底圆角矩形
        BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke);
        RoundRectangle2D.Float round = new RoundRectangle2D.Float(logoX, logoY, logoSize, logoSize, 5, 5);
        g2.setColor(java.awt.Color.white);
        g2.draw(round);
        if (isDrawBroder) {
            // 内圈绘制灰色边框
            BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            g2.setStroke(stroke2);
            RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(logoX + 2, logoX + 2, logoSize - 4, logoSize - 4,
                    5, 5);
            g2.setColor(new java.awt.Color(128, 128, 128));
            g2.draw(round2);
        }

        g2.dispose();
        matrixImage.flush();
        return matrixImage;
    }

    private String createBarcodeName() {
        return DateHelper.format(LocalDateTime.now(), DateHelper.yyyyMMddHHmmssSSS)
                + RandomStringUtils.randomAlphanumeric(3) + '.' + IMG_FORMAT_JPG;
    }

}
