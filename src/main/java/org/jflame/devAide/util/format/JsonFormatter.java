package org.jflame.devAide.util.format;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.alibaba.fastjson.parser.Feature;

public class JsonFormatter implements CodeFormatter {

    @Override
    public String convert(String source) {
        return JSON.toJSONString(
                JSON.parse(source, Feature.AllowComment, Feature.AllowSingleQuotes, Feature.AutoCloseSource), true);
    }

    @Override
    public boolean isSupported(String text) {
        return JSONValidator.from(text)
                .validate();
    }

}
