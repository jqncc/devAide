package org.jflame.devAide.component;

import org.jflame.devAide.component.convertor.ValidIntegerStringConverter;

import javafx.beans.NamedArg;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.SpinnerValueFactory;

public class MyIntegerSpinnerValueFactory extends SpinnerValueFactory<Integer> {

    /**
     * Constructs a new IntegerSpinnerValueFactory that sets the initial value to be equal to the min value, and a
     * default {@code amountToStepBy} of one.
     *
     * @param min The minimum allowed integer value for the Spinner.
     * @param max The maximum allowed integer value for the Spinner.
     */
    public MyIntegerSpinnerValueFactory(@NamedArg("min") int min, @NamedArg("max") int max) {
        this(min, max, min);
    }

    /**
     * Constructs a new IntegerSpinnerValueFactory with a default {@code amountToStepBy} of one.
     *
     * @param min The minimum allowed integer value for the Spinner.
     * @param max The maximum allowed integer value for the Spinner.
     * @param initialValue The value of the Spinner when first instantiated, must be within the bounds of the min and
     *            max arguments, or else the min value will be used.
     */
    public MyIntegerSpinnerValueFactory(@NamedArg("min") int min, @NamedArg("max") int max,
            @NamedArg("initialValue") int initialValue) {
        this(min, max, initialValue, 1);
    }

    /**
     * Constructs a new IntegerSpinnerValueFactory.
     *
     * @param min The minimum allowed integer value for the Spinner.
     * @param max The maximum allowed integer value for the Spinner.
     * @param initialValue The value of the Spinner when first instantiated, must be within the bounds of the min and
     *            max arguments, or else the min value will be used.
     * @param amountToStepBy The amount to increment or decrement by, per step.
     */
    public MyIntegerSpinnerValueFactory(@NamedArg("min") int min, @NamedArg("max") int max,
            @NamedArg("initialValue") int initialValue, @NamedArg("amountToStepBy") int amountToStepBy) {
        setMin(min);
        setMax(max);
        setAmountToStepBy(amountToStepBy);
        setConverter(new ValidIntegerStringConverter());

        valueProperty().addListener((o, oldValue, newValue) -> {
            if (newValue == null) {
                setValue(null);
                return;
            }
            if (newValue < getMin()) {
                setValue(getMin());
            } else if (newValue > getMax()) {
                setValue(getMax());
            }
        });
        setValue(initialValue >= min && initialValue <= max ? initialValue : min);
    }

    /***********************************************************************
     * * Properties * *
     **********************************************************************/

    // --- min
    private IntegerProperty min = new SimpleIntegerProperty(this, "min") {

        @Override
        protected void invalidated() {
            Integer currentValue = MyIntegerSpinnerValueFactory.this.getValue();
            if (currentValue == null) {
                return;
            }

            int newMin = get();
            if (newMin > getMax()) {
                setMin(getMax());
                return;
            }

            if (currentValue < newMin) {
                MyIntegerSpinnerValueFactory.this.setValue(newMin);
            }
        }
    };

    public final void setMin(int value) {
        min.set(value);
    }

    public final int getMin() {
        return min.get();
    }

    /**
     * Sets the minimum allowable value for this value factory
     * 
     * @return the minimum allowable value for this value factory
     */
    public final IntegerProperty minProperty() {
        return min;
    }

    // --- max
    private IntegerProperty max = new SimpleIntegerProperty(this, "max") {

        @Override
        protected void invalidated() {
            Integer currentValue = MyIntegerSpinnerValueFactory.this.getValue();
            if (currentValue == null) {
                return;
            }

            int newMax = get();
            if (newMax < getMin()) {
                setMax(getMin());
                return;
            }

            if (currentValue > newMax) {
                MyIntegerSpinnerValueFactory.this.setValue(newMax);
            }
        }
    };

    public final void setMax(int value) {
        max.set(value);
    }

    public final int getMax() {
        return max.get();
    }

    /**
     * Sets the maximum allowable value for this value factory
     * 
     * @return the maximum allowable value for this value factory
     */
    public final IntegerProperty maxProperty() {
        return max;
    }

    // --- amountToStepBy
    private IntegerProperty amountToStepBy = new SimpleIntegerProperty(this, "amountToStepBy");

    public final void setAmountToStepBy(int value) {
        amountToStepBy.set(value);
    }

    public final int getAmountToStepBy() {
        return amountToStepBy.get();
    }

    /**
     * Sets the amount to increment or decrement by, per step.
     * 
     * @return the amount to increment or decrement by, per step
     */
    public final IntegerProperty amountToStepByProperty() {
        return amountToStepBy;
    }

    /***********************************************************************
     * * Overridden methods * *
     **********************************************************************/

    /** {@inheritDoc} */
    @Override
    public void decrement(int steps) {
        final int min = getMin();
        final int max = getMax();
        final int newIndex = getValue() - steps * getAmountToStepBy();
        setValue(newIndex >= min ? newIndex : (isWrapAround() ? wrapValue(newIndex, min, max) + 1 : min));
    }

    /** {@inheritDoc} */
    @Override
    public void increment(int steps) {
        final int min = getMin();
        final int max = getMax();
        final int currentValue = getValue();
        final int newIndex = currentValue + steps * getAmountToStepBy();
        setValue(newIndex <= max ? newIndex : (isWrapAround() ? wrapValue(newIndex, min, max) - 1 : max));
    }

    static int wrapValue(int value, int min, int max) {
        if (max == 0) {
            throw new RuntimeException();
        }

        int r = value % max;
        if (r > min && max < min) {
            r = r + max - min;
        } else if (r < min && max > min) {
            r = r + max - min;
        }
        return r;
    }
}
