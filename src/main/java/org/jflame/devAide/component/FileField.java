package org.jflame.devAide.component;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.jflame.commons.util.CollectionHelper;
import org.jflame.commons.util.StringHelper;
import org.jflame.devAide.util.FxUtils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * 自定义文件选择控件
 * 
 * @author yucan.zhang
 */
public class FileField extends StackPane implements Initializable {

    @FXML
    private TextField fileText;
    @FXML
    private Button fileButton;

    private FileChooser fileChooser = new FileChooser();
    // private BooleanProperty multipleFile = new SimpleBooleanProperty(this, "multipleFile", false);
    private boolean multipleFile = false;// 是否可多选择文件

    public FileField() {
        getStyleClass().add("file-field");
        FxUtils.loadFXML("fileField", this, this);
    }

    @FXML
    protected void handleFileChoose(ActionEvent event) {
        List<File> selectedFiles = null;
        if (isMultipleFile()) {
            selectedFiles = fileChooser.showOpenMultipleDialog(super.getScene().getWindow());
        } else {
            File selectedFile = fileChooser.showOpenDialog(super.getScene().getWindow());
            if (selectedFile != null) {
                selectedFiles = List.of(selectedFile);
            }
        }
        List<String> selectedFilePath = null;
        List<String> oldselectedFilePath = getSelectedFilePaths();
        if (CollectionHelper.isNotEmpty(selectedFiles)) {
            selectedFilePath = extractFilePaths(selectedFiles);
        }
        if (!CollectionHelper.elementEquals(oldselectedFilePath, selectedFilePath)) {
            selectedFileProperty().set(selectedFiles);
        }
        fileText.setText(StringHelper.join(selectedFilePath));
    }

    private ObjectProperty<List<File>> selectedFileProperty;

    public final ObjectProperty<List<File>> selectedFileProperty() {
        if (selectedFileProperty == null) {
            selectedFileProperty = new ReadOnlyObjectWrapper<>(this, "selectedFile");
        }
        return selectedFileProperty;
    }

    /**
     * 返回首个选择文件的路径
     * 
     * @return
     */
    public String getFirstSelectedFilePath() {
        File f = getFirstSelectedFile();
        return f == null ? null : f.getPath();
    }

    /**
     * 返回首个选择的文件
     * 
     * @return File
     */
    public File getFirstSelectedFile() {
        List<File> selectedFiles = selectedFileProperty().getValue();
        return CollectionHelper.isEmpty(selectedFiles) ? null : selectedFiles.get(0);
    }

    /**
     * 返回所有选择的文件
     * 
     * @return
     */
    public List<File> getSelectedFiles() {
        return selectedFileProperty().getValue();
    }

    private List<String> getSelectedFilePaths() {
        return extractFilePaths(selectedFileProperty().getValue());
    }

    private List<String> extractFilePaths(List<File> selectedFiles) {
        List<String> paths = new ArrayList<>();
        if (CollectionHelper.isNotEmpty(selectedFiles)) {
            selectedFiles.forEach(f -> {
                paths.add(f.getPath());
            });
        }
        return paths;
    }

    public final boolean isMultipleFile() {
        return multipleFile;
    }

    public final void setMultipleFile(boolean multipleFile) {
        this.multipleFile = multipleFile;
    }

    public final String getText() {
        return fileButton.getText();
    }

    /**
     * 设置选择按钮的文本,默认是"选择文件"
     * 
     * @param text
     */
    public final void setText(String text) {
        fileButton.setText(text);
    }

    /**
     * 增加要过滤掉的文件类型. 示例:addExtensionFilter("图片","*.jpg","*.png")
     * 
     * @param desc 显示在文件类型选择框里的名称名称
     * @param extensions 要过滤掉的文件类型,格式:*.ext
     */
    public void addExtensionFilter(String desc, String... extensions) {
        fileChooser.getExtensionFilters()
                .add(new ExtensionFilter(desc, extensions));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
