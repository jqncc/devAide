package org.jflame.devAide.util.format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class CssFormatter implements CodeFormatter {

    public final static Pattern cssPattern = Pattern.compile(
            "[\\.#@]?\\S+\\s*\\{\\s*(\\*?[a-zA-Z\\-]+\\s*:\\s*[\\w#,%\\`\\-\"\\(\\)\\s]+;?\\s*)+\\s*\\}",
            Pattern.CASE_INSENSITIVE);

    @Override
    public String convert(String _css) {
        String fmtStr = _css.strip();
        fmtStr = StringUtils.removePattern(fmtStr, "[\\r\\n\\f\\v]");// 清理掉换行
        // 匹配出每一段样式定义 格式化(如:div{padding:1px;margin:2px;})
        Matcher matcher = cssPattern.matcher(fmtStr);
        StringBuilder str = new StringBuilder(Math.round(fmtStr.length() * 1.5f));
        int start = 0,end = 0;
        while (matcher.find()) {
            String g = matcher.group();
            g = StringUtils.replacePattern(g, "\\{\\s*", " {\n    ");
            g = StringUtils.replacePattern(g, ";\\s*(?!\\})", ";\n    ");
            g = StringUtils.replacePattern(g, "\\s*:\\s{2,}", ": ");
            g = StringUtils.replacePattern(g, "\\s*\\}", "\n}\n");
            if (matcher.start() == 0) {
                str.append(g);
                end = matcher.end();
            } else {
                start = end;
                end = matcher.start();
                str.append(fmtStr.substring(start, end)
                        .strip())
                        .append(g);
            }
        }
        fmtStr = str.toString();
        // 注释换个行
        fmtStr = StringUtils.replace(fmtStr, "/*", "\n/* ");
        fmtStr = StringUtils.replace(fmtStr, "*/", " */\n");
        return fmtStr;
    }

    @Override
    public boolean isSupported(String text) {
        String css = StringUtils.removePattern(text, "/\\*.+\\*/");// 删除css注释
        return cssPattern.matcher(css)
                .find();
    }

}
