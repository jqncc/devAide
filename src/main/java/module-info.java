module org.jflame.devnote {
    requires javafx.controls;
    requires javafx.fxml;

    //requires javafx.base;
    //requires javafx.graphics;
    opens org.jflame.devnote to javafx.fxml;
    opens org.jflame.devnote.controller to javafx.fxml;
    exports org.jflame.devnote;
    exports org.jflame.devnote.controller;

    requires com.jfoenix;
    requires org.controlsfx.controls;
}
