package org.jflame.devAide.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class QrcodeRecord implements Serializable {

    private static final long serialVersionUID = 5545504415023324684L;
    private String content;
    private String path;
    private LocalDateTime createTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

}
