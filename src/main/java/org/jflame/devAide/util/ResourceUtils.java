package org.jflame.devAide.util;

import org.jflame.commons.exception.BusinessException;
import org.jflame.commons.util.UrlHelper;

/**
 * 资源文件工具类
 *
 * @author yucan.zhang
 */
public class ResourceUtils {

    /**
     * 返回资源文件的物理路径
     *
     * @param relativePath 文件相对路径
     * @return
     */
    public static String absUrl(String relativePath) {
        if (relativePath == null) {
            throw new IllegalArgumentException("relativePath not be null");
        }
        try {
            return ResourceUtils.class.getResource(relativePath)
                    .toExternalForm();
        } catch (NullPointerException e) {
            throw new BusinessException(relativePath + "不存在");
        }

    }

    private static final String CSS_DIR = "/css";

    public static String cssUrl(String cssName) {
        if (!cssName.endsWith(".css")) {
            cssName = cssName + ".css";
        }
        String relPath = UrlHelper.mergeUrl(CSS_DIR, cssName);
        return ResourceUtils.absUrl(relPath);
    }
}
