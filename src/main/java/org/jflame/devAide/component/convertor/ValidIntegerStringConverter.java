package org.jflame.devAide.component.convertor;

import org.jflame.commons.valid.ValidatorHelper;

import javafx.util.converter.IntegerStringConverter;

public class ValidIntegerStringConverter extends IntegerStringConverter {

    @Override
    public Integer fromString(String value) {
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (ValidatorHelper.isInteger(value)) {
            return Integer.valueOf(value);
        }
        return null;
    }

}
