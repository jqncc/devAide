package devAide;

import java.util.regex.Pattern;

import org.jflame.devAide.plugin.codeFormatter.CssFormatter;
import org.jflame.devAide.plugin.codeFormatter.SQLFormatter;
import org.junit.Test;

public class FormatTest {

    @Test
    public void testCss() {
        String srcStr = "\n p{ padding:1px 2px;\n\n margin:2px}  \n /* asdfas */ #id {\n font-size:1em;border:1px solid #ccc;  } \n \n";
        CssFormatter formatter = new CssFormatter();
        System.out.println(formatter.isSupported(srcStr));

        Pattern cssPattern = Pattern.compile(
                "[\\.#@]?\\S+\\s*\\{\\s*(\\*?[a-zA-Z\\-]+\\s*:\\s*[\\w#,%\\`\\-\"\\(\\)\\s]+;?\\s*)+\\s*\\}",
                Pattern.CASE_INSENSITIVE);
        System.out.println(cssPattern.matcher(srcStr)
                .find());
    }

    @Test
    public void testSQL() {
        SQLFormatter formatter = new SQLFormatter();
        // System.out.println(formatter.convert("select user ,pass from use_info where a=1"));
        // System.out.println(formatter.convert("update use_info set user = 'test' where a=1"));
        System.out.println(formatter.convert("select user ,pass from use_info where a=1 \n and b='cdsdfs'"));
        System.out.println(formatter.convert("alter table a;add column a int;"));
    }

}
