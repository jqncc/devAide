package org.jflame.devAide.component;

import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;

public class CheckBoxFlowPane extends FlowPane {

    private boolean isUseText;// 从checbox的text取数据,默认使用userData
    private ListProperty<Object> selectedValues;

    public CheckBoxFlowPane() {
        selectedValues = new SimpleListProperty<>(FXCollections.observableArrayList());
        /*selectedValues.addListener(new ListChangeListener<Object>() {
        
            @Override
            public void onChanged(Change<? extends Object> c) {
                while (c.next()) {
                    List<CheckBox> boxs = getCheckBoxs();
                    if (c.wasAdded()) {
                        List<? extends Object> addObjs = c.getAddedSubList();
                        addObjs.forEach(o -> {
                            for (CheckBox box : boxs) {
                                if (o.equals(getData(box))) {
                                    box.setSelected(true);
                                }
                            }
                        });
                    } else if (c.wasRemoved()) {
                        List<? extends Object> removedObjs = c.getRemoved();
                        removedObjs.forEach(o -> {
                            boxs.stream()
                                    .filter(b -> o.equals(getData(b)))
                                    .forEach(x -> x.setSelected(false));
                        });
                    }
        
                }
            }
        });*/
    }

    public void add(CheckBox checkBox) {
        super.getChildren().add(checkBox);
        checkBox.selectedProperty()
                .addListener(new ChangeListener<Boolean>() {

                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                            Boolean newValue) {
                        Object o = getData(checkBox);
                        // System.out.println("cbox,v=" + o + " is:" + newValue);
                        if (o != null) {
                            if (newValue) {
                                // System.out.println("add:" + o);
                                selectedValues.add(o);
                            } else {
                                selectedValues.remove(o);
                            }
                        }

                    }
                });
    }

    /**
     * 返回所有已选择的复选框的值集合
     * 
     * @return
     */
    public List<Object> getSelectedValues() {
        return selectedValues.get();
    }

    public void setSelectedValues(final List<Object> _selectedValues) {
        this.selectedValues.clear();
        ObservableList<Node> childrens = super.getChildren();
        for (Node node : childrens) {
            if (node instanceof CheckBox) {
                CheckBox box = (CheckBox) node;
                box.setSelected(false);
                Object o = getData(box);
                for (Object obj : _selectedValues) {
                    if (isUseText && !(obj instanceof String)) {
                        obj = obj.toString();
                    }
                    if (o.equals(obj)) {
                        box.setSelected(true);
                    }
                }
            }
        }
    }

    /**
     * 设置所有复选框为选中状态
     */
    public void selectAll() {
        ObservableList<Node> childrens = super.getChildren();
        for (Node box : childrens) {
            if (box instanceof CheckBox) {
                ((CheckBox) box).setSelected(true);
            }
        }
    }

    /**
     * 取消所有复选框的选中状态
     */
    public void unselectAll() {
        ObservableList<Node> childrens = super.getChildren();
        for (Node box : childrens) {
            if (box instanceof CheckBox) {
                ((CheckBox) box).setSelected(true);
            }
        }
    }

    private Object getData(CheckBox box) {
        return isUseText ? box.getText() : box.getUserData();
    }

    public boolean isUseText() {
        return isUseText;
    }

    /**
     * 检查至少有一个checbox被选择了
     * 
     * @return true=至少有一个checbox被选
     */
    public boolean hasAnyChecked() {
        return !selectedValues.isEmpty();
    }

    public void setUseText(boolean isUseText) {
        this.isUseText = isUseText;
    }

    public ListProperty<Object> selectedValuesProperty() {
        return selectedValues;
    }

    public List<CheckBox> getCheckBoxs() {
        ObservableList<Node> childrens = super.getChildren();
        return childrens.filtered(c -> c instanceof CheckBox)
                .stream()
                .map(x -> (CheckBox) x)
                .collect(Collectors.toUnmodifiableList());
    }
}
