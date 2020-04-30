package org.jflame.devAide.util;

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
        return ResourceUtils.class.getResource(relativePath)
                .toExternalForm();
    }

    private static final String CSS_DIR = "/css";

    public static String cssUrl(String cssName) {
        String relPath = UrlHelper.mergeUrl(CSS_DIR, cssName, ".css");
        return ResourceUtils.absUrl(relPath);
    }
}
