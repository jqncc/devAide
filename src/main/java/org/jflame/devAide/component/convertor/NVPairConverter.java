package org.jflame.devAide.component.convertor;

import org.jflame.commons.common.bean.pair.NameValuePair;

import javafx.util.StringConverter;

public class NVPairConverter extends StringConverter<NameValuePair> {

    @Override
    public String toString(NameValuePair object) {
        return object.getKey();
    }

    @Override
    public NameValuePair fromString(String string) {
        return null;
    }
}
