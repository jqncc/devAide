package org.jflame.devAide.component;

import java.util.Arrays;
import java.util.Collection;

import org.controlsfx.control.decoration.Decoration;
import org.controlsfx.control.decoration.GraphicDecoration;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;

import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class MyGraphicValidationDecoration extends GraphicValidationDecoration {

    @Override
    protected Collection<Decoration> createValidationDecorations(ValidationMessage message) {
        return Arrays.asList(new GraphicDecoration(createDecorationNode(message), Pos.TOP_RIGHT));
    }

    protected Tooltip createTooltip(ValidationMessage message) {
        Tooltip tooltip = new Tooltip(message.getText());
        tooltip.setOpacity(.9);
        tooltip.setAutoFix(true);
        tooltip.setShowDelay(Duration.millis(100d));
        tooltip.setStyle(getStyleBySeverity(message.getSeverity()));
        return tooltip;
    }
}
