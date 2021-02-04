package devAide;

import java.util.regex.Pattern;

import org.jflame.devAide.util.format.CssFormatter;
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

}
