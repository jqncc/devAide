package devAide;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

class Test {

    final static Pattern cssPattern = Pattern.compile(
            "[\\.#@]?\\S+\\s*\\{\\s*(\\*?[a-zA-Z\\-]+\\s*:\\s*[\\w#,%\\`\\-\"\\(\\)\\s]+;?\\s*)+\\s*\\}",
            Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) {
        String srcStr = "\n p{ padding:1px 2px;\n\n margin:2px}  \n /* asdfas */ #id {\n font-size:1em;border:1px solid #ccc;  } \n \n";
        srcStr = srcStr.strip();
        // srcStr = StringUtils.replacePattern(srcStr, "\r\n", "\n");
        srcStr = StringUtils.removePattern(srcStr, "[\\r\\n\\f\\v]");
        // System.out.println(srcStr);
        // String cp = srcStr;
        Matcher matcher = cssPattern.matcher(srcStr);
        StringBuilder str = new StringBuilder(Math.round(srcStr.length() * 1.5f));
        int start = 0,end = 0;
        while (matcher.find()) {
            String g = matcher.group();
            // System.out.println(g);
            g = StringUtils.replacePattern(g, "\\{\\s*", " {\n    ");
            // System.out.println(g);
            g = StringUtils.replacePattern(g, ";\\s*(?!\\})", ";\n    ");
            // System.out.println(g);
            g = StringUtils.replacePattern(g, "\\s*:\\s{2,}", ": ");
            // System.out.println(g);
            g = StringUtils.replacePattern(g, "\\s*\\}", "\n}\n");
            // System.out.println("====");
            // System.out.println(matcher.start() + " , " + matcher.end());
            // cp = StringUtils.overlay(cp, g, matcher.start(), matcher.end());
            if (matcher.start() == 0) {
                str.append(g);
                end = matcher.end();
            } else {
                start = end;
                end = matcher.start();
                str.append(srcStr.substring(start, end)
                        .strip())
                        .append(g);
            }
        }
        srcStr = str.toString();
        srcStr = StringUtils.replace(srcStr, "/*", "\n/*");
        srcStr = StringUtils.replace(srcStr, "*/", "*/\n");
        System.out.println(srcStr);
    }
}
