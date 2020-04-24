package org.jflame.devAide.model;

public class UIModel {

    private String text;
    private String icon;
    private String action;

    public UIModel() {
    }

    public UIModel(String _text) {
        text = _text;
    }

    public UIModel(String _text, String _icon) {
        text = _text;
        icon = _icon;
    }

    public UIModel(String _text, String _icon, String _action) {
        text = _text;
        icon = _icon;
        action = _action;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "UIModel [text=" + text + ", icon=" + icon + ", action=" + action + "]";
    }

}
