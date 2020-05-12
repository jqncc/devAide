package org.jflame.devAide.component;

import java.util.Arrays;
import java.util.Collection;

import org.controlsfx.control.decoration.Decoration;
import org.controlsfx.control.decoration.GraphicDecoration;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class MyGraphicValidationDecoration extends GraphicValidationDecoration {

    private static final String SHADOW_EFFECT = "-fx-effect: dropshadow(three-pass-box, rgba(120,120,120,0.8), 6, 0, 0, 0);";

    private static final String ERROR_TOOLTIP_STYLECLASS = "valid_error_tip";
    private static final String WARNING_TOOLTIP_STYLECLASS = "valid_warning_tip";
    private static final String INFO_TOOLTIP_STYLECLASS = "valid_info_tip";

    @Override
    protected Collection<Decoration> createValidationDecorations(ValidationMessage message) {
        return Arrays.asList(new GraphicDecoration(createDecorationNode(message), Pos.TOP_RIGHT));
    }

    protected Node createDecorationNode(ValidationMessage message) {
        Node graphic = getGraphicBySeverity(message.getSeverity());
        graphic.setStyle(SHADOW_EFFECT);
        Label label = new Label();
        label.setGraphic(graphic);
        label.setTooltip(createTooltip(message));
        label.setAlignment(Pos.CENTER);
        return label;
    }

    protected Tooltip createTooltip(ValidationMessage message) {
        Tooltip tooltip = new Tooltip(message.getText());
        tooltip.setOpacity(.9);
        tooltip.setAutoFix(true);
        tooltip.setShowDelay(Duration.millis(100d));
        tooltip.getStyleClass()
                .add(getStyleClassBySeverity(message.getSeverity()));
        return tooltip;
    }

    protected String getStyleClassBySeverity(Severity severity) {
        switch (severity) {
            case ERROR:
                return ERROR_TOOLTIP_STYLECLASS;
            case WARNING:
                return WARNING_TOOLTIP_STYLECLASS;
            default:
                return INFO_TOOLTIP_STYLECLASS;
        }
    }
}
