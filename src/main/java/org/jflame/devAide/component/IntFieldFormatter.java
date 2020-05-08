package org.jflame.devAide.component;

import org.jflame.commons.valid.ValidatorHelper;

import javafx.scene.control.TextFormatter;

public class IntFieldFormatter extends TextFormatter<Number> {

    public IntFieldFormatter() {
        super(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }
            if (ValidatorHelper.isInteger(newText)) {
                return change;
            } else {
                return null;
            }
        });
    }
}
