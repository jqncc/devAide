package org.jflame.devAide.controller;

import org.controlsfx.control.StatusBar;
import org.jflame.commons.util.StringHelper;
import org.jflame.devAide.AppContext;
import org.jflame.devAide.model.UIModel;
import org.jflame.devAide.util.FxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * 主界面
 *
 * @author yucan.zhang
 */
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML
    private TabPane mainTab;
    @FXML
    private StatusBar statusBar;
    @FXML
    private WebView mainWebView;
    @FXML
    private ListView<UIModel> lstViewTool;

    public MainController() {

    }

    @FXML
    protected void initialize() {
        initToolListView();
        // initKnowHomePage();
        initStatusBar();
    }

    /**
     * 初始导航主页
     */
    private void initKnowHomePage() {

        WebEngine webEngine = mainWebView.getEngine();
        webEngine.getLoadWorker()
                .stateProperty()
                .addListener(new ChangeListener<State>() {

                    @Override
                    public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                            Text statusText = new Text(webEngine.getTitle());
                            statusBar.getLeftItems()
                                    .add(statusText);

                        }
                    }
                });
        webEngine.load("http://www.hao123.com");

    }

    /**
     * 初始开发工具列表
     */
    private void initToolListView() {
        UIModel qrCodeToolModel = new UIModel("条码生成器", "QRCODE", "qrcodeTool");
        UIModel regToolModel = new UIModel("正则表达式", "EURO");
        UIModel jsonToolModel = new UIModel("代码格式化", "FILE_TEXT", "formatTool");
        UIModel cronToolModel = new UIModel("CRON表达式", "COPYRIGHT");
        ObservableList<UIModel> lstViewToolData = FXCollections.observableArrayList(qrCodeToolModel, regToolModel,
                jsonToolModel, cronToolModel);
        lstViewTool.setItems(lstViewToolData);
        lstViewTool.setCellFactory((ListView<UIModel> l) -> new ToolListViewCell());
        lstViewTool.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue<? extends UIModel> ov, UIModel old_val, UIModel new_val) ->
                {
                    boolean isExist = false;
                    ObservableList<Tab> tabs = mainTab.getTabs();
                    for (Tab tab : tabs) {
                        if (new_val.getText()
                                .equals(tab.getUserData())) {
                            isExist = true;
                            mainTab.getSelectionModel()
                                    .select(tab);
                        }
                    }
                    if (!isExist && StringHelper.isNotEmpty(new_val.getAction())) {
                        Parent toolPaneOpt = FxUtils.loadFXML(new_val.getAction());
                        Tab toolTab = new Tab(new_val.getText());
                        toolTab.setUserData(new_val.getText());
                        toolTab.setGraphic(AppContext.FONT_AWESOME.create(new_val.getIcon())
                                .size(9));
                        toolTab.setClosable(true);
                        toolTab.setContent(toolPaneOpt);
                        mainTab.getTabs()
                                .add(toolTab);
                        mainTab.getSelectionModel()
                                .select(toolTab);
                    }

                });
    }

    private void initStatusBar() {
        Hyperlink linkGit = new Hyperlink("Github");
        linkGit.getStyleClass()
                .add("gitlink");
        linkGit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                AppContext.getInstance()
                        .getApplication()
                        .getHostServices()
                        .showDocument("https://github.com/jqncc/devAide");
            }
        });

        statusBar.getRightItems()
                .add(linkGit);
    }

    static class ToolListViewCell extends ListCell<UIModel> {

        @Override
        protected void updateItem(UIModel item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty && item != null) {
                setText(item.getText());
                if (item.getIcon() != null) {
                    setGraphic(AppContext.FONT_AWESOME.create(item.getIcon())
                            .size(14));
                } else {
                    setGraphic(AppContext.FONT_AWESOME.create("WRENCH"));
                }
            }

        }
    }
}
