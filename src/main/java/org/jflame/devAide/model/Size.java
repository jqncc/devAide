package org.jflame.devAide.model;

import java.io.Serializable;

public class Size implements Serializable {

    private static final long serialVersionUID = -7990855621413262890L;
    private int width;
    private int height;

    public Size() {
    }

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
