package org.jflame.devAide.util.format;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.jflame.commons.convert.Converter;
import org.jflame.commons.exception.ConvertException;

public interface CodeFormatter extends Converter<String,String> {

    boolean isSupported(String text);

    public static String format(String src) {
        ServiceLoader<CodeFormatter> sloader = ServiceLoader.load(CodeFormatter.class);
        Iterator<CodeFormatter> it = sloader.iterator();
        CodeFormatter formatter;
        while (it.hasNext()) {
            formatter = it.next();
            if (formatter.isSupported(src)) {
                return formatter.convert(src);
            }
        }
        throw new ConvertException("不支持的代码语言");
    }
}
