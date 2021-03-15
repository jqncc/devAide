package org.jflame.devAide;

import javafx.stage.Stage;

public final class AppContext {

    private static AppContext instance = new AppContext();

    /**
     * 主界面场景
     */
    private Stage mainStage;

    public static AppContext getInstance() {
        return instance;
    }

    public App getApplication() {
        return App.instance;
    }

    public Stage mainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        if (this.mainStage == null) {
            this.mainStage = mainStage;
        }
    }

}
