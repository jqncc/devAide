package org.jflame.devAide.controller;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.jflame.commons.model.Chars;
import org.jflame.commons.util.DateHelper;
import org.jflame.devAide.util.UIComponents;

import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronConstraintsFactory;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.CronFieldName;
import com.cronutils.model.field.constraint.FieldConstraints;
import com.cronutils.model.field.expression.FieldExpression;
import com.cronutils.model.field.expression.FieldExpressionFactory;
import com.cronutils.model.field.value.SpecialChar;
import com.cronutils.model.time.ExecutionTime;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class CronController {

    @FXML
    private FlowPane secondFlowPane;
    @FXML
    private ToggleGroup secondGrp;
    @FXML
    private Spinner<Integer> secPeriodSpinnerStart;
    @FXML
    private Spinner<Integer> secPeriodSpinnerEnd;
    @FXML
    private Spinner<Integer> secIntervalSpinnerStart;
    @FXML
    private Spinner<Integer> secIntervalSpinner;
    @FXML
    private RadioButton secPeriodRadioBtn;
    @FXML
    private RadioButton secPerRadioBtn;
    @FXML
    private RadioButton secIntervalRadioBtn;
    @FXML
    private RadioButton secOnRadioBtn;

    @FXML
    private FlowPane minuteFlowPane;
    @FXML
    private ToggleGroup minuteGrp;
    @FXML
    private Spinner<Integer> minPeriodSpinnerStart;
    @FXML
    private Spinner<Integer> minPeriodSpinnerEnd;
    @FXML
    private Spinner<Integer> minIntervalSpinnerStart;
    @FXML
    private Spinner<Integer> minIntervalSpinner;
    @FXML
    private RadioButton minPeriodRadioBtn;
    @FXML
    private RadioButton minPerRadioBtn;
    @FXML
    private RadioButton minIntervalRadioBtn;
    @FXML
    private RadioButton minOnRadioBtn;

    @FXML
    private FlowPane hourFlowPane;
    @FXML
    private ToggleGroup hourGrp;
    @FXML
    private Spinner<Integer> hourPeriodSpinnerStart;
    @FXML
    private Spinner<Integer> hourPeriodSpinnerEnd;
    @FXML
    private Spinner<Integer> hourIntervalSpinnerStart;
    @FXML
    private Spinner<Integer> hourIntervalSpinner;
    @FXML
    private RadioButton hourPeriodRadioBtn;
    @FXML
    private RadioButton hourPerRadioBtn;
    @FXML
    private RadioButton hourIntervalRadioBtn;
    @FXML
    private RadioButton hourOnRadioBtn;

    @FXML
    private FlowPane dayFlowPane;
    @FXML
    private ToggleGroup dayGrp;
    @FXML
    private Spinner<Integer> dayPeriodSpinnerStart;
    @FXML
    private Spinner<Integer> dayPeriodSpinnerEnd;
    @FXML
    private Spinner<Integer> dayIntervalSpinnerStart;
    @FXML
    private Spinner<Integer> dayIntervalSpinner;
    @FXML
    private RadioButton dayPeriodRadioBtn;
    @FXML
    private RadioButton dayPerRadioBtn;
    @FXML
    private RadioButton dayIntervalRadioBtn;
    @FXML
    private RadioButton dayOnRadioBtn;
    @FXML
    private RadioButton dayLastRadioBtn;
    @FXML
    private RadioButton dayNoSpecifiedRadioBtn;

    @FXML
    private FlowPane monthFlowPane;
    @FXML
    private ToggleGroup monthGrp;
    @FXML
    private Spinner<Integer> monthPeriodSpinnerStart;
    @FXML
    private Spinner<Integer> monthPeriodSpinnerEnd;
    @FXML
    private Spinner<Integer> monthIntervalSpinnerStart;
    @FXML
    private Spinner<Integer> monthIntervalSpinner;
    @FXML
    private RadioButton monthPeriodRadioBtn;
    @FXML
    private RadioButton monthPerRadioBtn;
    @FXML
    private RadioButton monthIntervalRadioBtn;
    @FXML
    private RadioButton monthOnRadioBtn;

    @FXML
    private FlowPane weekFlowPane;
    @FXML
    private ToggleGroup weekGrp;
    @FXML
    private Spinner<Integer> weekPeriodSpinnerStart;
    @FXML
    private Spinner<Integer> weekPeriodSpinnerEnd;
    @FXML
    private Spinner<Integer> weekIntervalSpinnerStart;
    @FXML
    private Spinner<Integer> weekIntervalSpinner;
    @FXML
    private Spinner<Integer> weekLastSpinnerStart;
    @FXML
    private RadioButton weekPeriodRadioBtn;
    @FXML
    private RadioButton weekPerRadioBtn;
    @FXML
    private RadioButton weekIntervalRadioBtn;
    @FXML
    private RadioButton weekOnRadioBtn;
    @FXML
    private RadioButton weekLastRadioBtn;
    @FXML
    private RadioButton weekNoSpecifiedRadioBtn;

    @FXML
    private TextField cronTextField;
    @FXML
    private TextArea nextTimes;

    private ValidationSupport validationSupport = new ValidationSupport();

    private Map<CronFieldName,FieldExpression> cronFieldContainer = new EnumMap<>(CronFieldName.class);
    private Map<CronFieldName,FieldPaneGroup> fieldGroup = new HashMap<>();

    private final CronDefinition initCronDef;

    private final String FIELD_EXPRESSION_ON = "on";
    private final String FIELD_EXPRESSION_ONLAST = "onlast";
    private final String FIELD_EXPRESSION_BETWEEN = "between";
    private final String FIELD_EXPRESSION_EVERY = "every";
    private final String FIELD_EXPRESSION_ALWAYS = "always";
    private final String FIELD_EXPRESSION_NOTSPECIFIED = "not_specified";

    public CronController() {
        initCronDef = CronDefinitionBuilder.defineCron()
                .withSeconds()
                .withValidRange(0, 59)
                .and()
                .withMinutes()
                .withValidRange(0, 59)
                .and()
                .withHours()
                .withValidRange(0, 23)
                .and()
                .withDayOfMonth()
                .withValidRange(1, 31)
                .supportsL()
                .supportsQuestionMark()
                .and()
                .withMonth()
                .withValidRange(1, 12)
                .and()
                .withDayOfWeek()
                .withValidRange(1, 7)
                .withMondayDoWValue(2)
                .supportsL()
                .supportsQuestionMark()
                .and()
                .withYear()
                .withValidRange(1970, 2099)
                .withStrictRange()
                .optional()
                .and()
                .withCronValidation(CronConstraintsFactory.ensureEitherDayOfWeekOrDayOfMonth())
                .instance();

        validationSupport.setValidationDecorator(new StyleClassValidationDecoration());
    }

    @FXML
    protected void initialize() {
        initSecondPane();
        initMinutePane();
        initHourPane();
        initDayPane();
        initMonthPane();
        initDayOfWeekPane();
    }

    /**
     * '秒'面板初始
     */
    private void initSecondPane() {
        initCommonField(CronFieldName.SECOND, secondFlowPane, secondGrp, secPeriodSpinnerStart, secPeriodSpinnerEnd,
                secIntervalSpinnerStart, secIntervalSpinner, secOnRadioBtn);

        FieldPaneGroup paneGroup = new FieldPaneGroup(secondFlowPane, secondGrp, secPeriodSpinnerStart,
                secPeriodSpinnerEnd, secIntervalSpinnerStart, secIntervalSpinner);
        fieldGroup.put(CronFieldName.SECOND, paneGroup);

        secIntervalRadioBtn.setUserData(FIELD_EXPRESSION_EVERY);
        secPeriodRadioBtn.setUserData(FIELD_EXPRESSION_BETWEEN);
        secOnRadioBtn.setUserData(FIELD_EXPRESSION_ON);
        secPerRadioBtn.setUserData(FIELD_EXPRESSION_ALWAYS);
    }

    /**
     * '分钟'面板初始
     */
    private void initMinutePane() {
        initCommonField(CronFieldName.MINUTE, minuteFlowPane, minuteGrp, minPeriodSpinnerStart, minPeriodSpinnerEnd,
                minIntervalSpinnerStart, minIntervalSpinner, minOnRadioBtn);

        FieldPaneGroup paneGroup = new FieldPaneGroup(minuteFlowPane, minuteGrp, minPeriodSpinnerStart,
                minPeriodSpinnerEnd, minIntervalSpinnerStart, minIntervalSpinner);
        fieldGroup.put(CronFieldName.MINUTE, paneGroup);

        minIntervalRadioBtn.setUserData(FIELD_EXPRESSION_EVERY);
        minPeriodRadioBtn.setUserData(FIELD_EXPRESSION_BETWEEN);
        minOnRadioBtn.setUserData(FIELD_EXPRESSION_ON);
        minPerRadioBtn.setUserData(FIELD_EXPRESSION_ALWAYS);
    }

    /**
     * '小时'面板初始
     */
    private void initHourPane() {
        initCommonField(CronFieldName.HOUR, hourFlowPane, hourGrp, hourPeriodSpinnerStart, hourPeriodSpinnerEnd,
                hourIntervalSpinnerStart, hourIntervalSpinner, hourOnRadioBtn);

        FieldPaneGroup paneGroup = new FieldPaneGroup(hourFlowPane, hourGrp, hourPeriodSpinnerStart,
                hourPeriodSpinnerEnd, hourIntervalSpinnerStart, hourIntervalSpinner);
        fieldGroup.put(CronFieldName.HOUR, paneGroup);

        hourIntervalRadioBtn.setUserData(FIELD_EXPRESSION_EVERY);
        hourPeriodRadioBtn.setUserData(FIELD_EXPRESSION_BETWEEN);
        hourOnRadioBtn.setUserData(FIELD_EXPRESSION_ON);
        hourPerRadioBtn.setUserData(FIELD_EXPRESSION_ALWAYS);
    }

    /**
     * '日'面板初始
     */
    private void initDayPane() {
        initCommonField(CronFieldName.DAY_OF_MONTH, dayFlowPane, dayGrp, dayPeriodSpinnerStart, dayPeriodSpinnerEnd,
                dayIntervalSpinnerStart, dayIntervalSpinner, dayOnRadioBtn);

        FieldPaneGroup paneGroup = new FieldPaneGroup(dayFlowPane, dayGrp, dayPeriodSpinnerStart, dayPeriodSpinnerEnd,
                dayIntervalSpinnerStart, dayIntervalSpinner);
        fieldGroup.put(CronFieldName.DAY_OF_MONTH, paneGroup);

        dayIntervalRadioBtn.setUserData(FIELD_EXPRESSION_EVERY);
        dayPeriodRadioBtn.setUserData(FIELD_EXPRESSION_BETWEEN);
        dayOnRadioBtn.setUserData(FIELD_EXPRESSION_ON);
        dayPerRadioBtn.setUserData(FIELD_EXPRESSION_ALWAYS);
        dayNoSpecifiedRadioBtn.setUserData(FIELD_EXPRESSION_NOTSPECIFIED);
        dayLastRadioBtn.setUserData(FIELD_EXPRESSION_ONLAST);
    }

    /**
     * '月份'面板初始
     */
    private void initMonthPane() {
        initCommonField(CronFieldName.MONTH, monthFlowPane, monthGrp, monthPeriodSpinnerStart, monthPeriodSpinnerEnd,
                monthIntervalSpinnerStart, monthIntervalSpinner, monthOnRadioBtn);

        FieldPaneGroup paneGroup = new FieldPaneGroup(monthFlowPane, monthGrp, monthPeriodSpinnerStart,
                monthPeriodSpinnerEnd, monthIntervalSpinnerStart, monthIntervalSpinner);
        fieldGroup.put(CronFieldName.MONTH, paneGroup);

        monthIntervalRadioBtn.setUserData(FIELD_EXPRESSION_EVERY);
        monthPeriodRadioBtn.setUserData(FIELD_EXPRESSION_BETWEEN);
        monthOnRadioBtn.setUserData(FIELD_EXPRESSION_ON);
        monthPerRadioBtn.setUserData(FIELD_EXPRESSION_ALWAYS);
    }

    /**
     * '周'面板初始
     */
    private void initDayOfWeekPane() {
        initCommonField(CronFieldName.DAY_OF_WEEK, weekFlowPane, weekGrp, weekPeriodSpinnerStart, weekPeriodSpinnerEnd,
                weekIntervalSpinnerStart, weekIntervalSpinner, weekOnRadioBtn);

        UIComponents.setSpinnerForInteger(weekLastSpinnerStart, 1, 7);

        FieldPaneGroup paneGroup = new FieldPaneGroup(weekFlowPane, weekGrp, weekPeriodSpinnerStart,
                weekPeriodSpinnerEnd, weekIntervalSpinnerStart, weekIntervalSpinner);
        fieldGroup.put(CronFieldName.DAY_OF_WEEK, paneGroup);

        weekIntervalRadioBtn.setUserData(FIELD_EXPRESSION_EVERY);
        weekPeriodRadioBtn.setUserData(FIELD_EXPRESSION_BETWEEN);
        weekOnRadioBtn.setUserData(FIELD_EXPRESSION_ON);
        weekPerRadioBtn.setUserData(FIELD_EXPRESSION_ALWAYS);
        weekNoSpecifiedRadioBtn.setUserData(FIELD_EXPRESSION_NOTSPECIFIED);
        weekLastRadioBtn.setUserData(FIELD_EXPRESSION_ONLAST);
    }

    /**
     * 面板中部分相同控件初始
     * 
     * @param field
     * @param flowPane
     * @param toggleGrp
     * @param periodSpinnerStart
     * @param periodSpinnerEnd
     * @param intervalSpinnerStart
     * @param intervalSpinner
     */
    private void initCommonField(CronFieldName field, FlowPane flowPane, ToggleGroup toggleGrp,
            Spinner<Integer> periodSpinnerStart, Spinner<Integer> periodSpinnerEnd,
            Spinner<Integer> intervalSpinnerStart, Spinner<Integer> intervalSpinner, RadioButton fieldOn) {
        initCheckBoxOfFieldRange(flowPane, field, fieldOn);
        int maxRange = initCronDef.getFieldDefinition(field)
                .getConstraints()
                .getEndRange();
        // 设置spinner为数值类型
        UIComponents.setSpinnerForInteger(periodSpinnerStart, 0, maxRange);
        UIComponents.setSpinnerForInteger(periodSpinnerEnd, 1, maxRange);
        UIComponents.setSpinnerForInteger(intervalSpinnerStart, 0, maxRange);
        UIComponents.setSpinnerForInteger(intervalSpinner, 1, maxRange);

        // 事件绑定
        toggleGrp.selectedToggleProperty()
                .addListener(new ChangedEventHander<Toggle>());

        ChangedEventHander<Integer> spnnerValueListener = new ChangedEventHander<>();

        periodSpinnerStart.valueProperty()
                .addListener(spnnerValueListener);
        periodSpinnerEnd.valueProperty()
                .addListener(spnnerValueListener);
        intervalSpinnerStart.valueProperty()
                .addListener(spnnerValueListener);
        intervalSpinner.valueProperty()
                .addListener(spnnerValueListener);
    }

    /**
     * 根据CronFieldName值范围生成复选框
     * 
     * @param pane
     * @param fieldName
     */
    private void initCheckBoxOfFieldRange(Pane pane, CronFieldName fieldName, RadioButton abouButton) {
        FieldConstraints fieldConstraints = initCronDef.getFieldDefinition(fieldName)
                .getConstraints();
        List<CheckBox> childs = new ArrayList<>();
        CheckedEventHander eventHander = new CheckedEventHander(abouButton);
        for (int i = fieldConstraints.getStartRange(); i <= fieldConstraints.getEndRange(); i++) {
            CheckBox checkBox = new CheckBox(String.valueOf(i));
            checkBox.selectedProperty()
                    .addListener(eventHander);
            childs.add(checkBox);
        }
        pane.getChildren()
                .addAll(childs);
    }

    private void buildFieldExpressionFromFieldPane(CronFieldName fieldName) {
        FieldPaneGroup paneGroup = fieldGroup.get(fieldName);
        RadioButton selectedMinuteRadio = (RadioButton) paneGroup.getFieldExpToggleGrp()
                .getSelectedToggle();
        FieldExpression expression = null;
        String selectedFieldExp = (String) selectedMinuteRadio.getUserData();
        if (selectedFieldExp == null) {
            return;
        }
        switch (selectedFieldExp) {
            case FIELD_EXPRESSION_ALWAYS:
                expression = FieldExpression.always();
                break;
            case FIELD_EXPRESSION_NOTSPECIFIED:
                expression = FieldExpression.questionMark();
                break;
            case FIELD_EXPRESSION_EVERY:
                expression = paneGroup.buildFieldExpressionFromIntervalCtl();
                break;
            case FIELD_EXPRESSION_BETWEEN:
                expression = paneGroup.buildFieldExpressionFromPeriodCtl();
                break;
            case FIELD_EXPRESSION_ON:
                expression = paneGroup.buildFieldExpressionFromOnCtl();
                break;
            case FIELD_EXPRESSION_ONLAST:
                expression = FieldExpressionFactory.on(SpecialChar.L);
                break;
            default:
                break;
        }

        if (expression != null) {
            cronFieldContainer.put(fieldName, expression);
        }
    }

    private List<Integer> getCheckboxValuesFromPane(Pane pane) {
        ObservableList<Node> checkboxs = pane.getChildren();
        List<Integer> values = new ArrayList<>();
        for (Node node : checkboxs) {
            if (node instanceof CheckBox) {
                CheckBox cbox = (CheckBox) node;
                if (cbox.isSelected()) {
                    values.add(Integer.valueOf(cbox.getText()));
                }
            }
        }
        return values;
    }

    private void generateCron() {
        buildFieldExpressionFromFieldPane(CronFieldName.SECOND);
        buildFieldExpressionFromFieldPane(CronFieldName.MINUTE);
        buildFieldExpressionFromFieldPane(CronFieldName.HOUR);
        buildFieldExpressionFromFieldPane(CronFieldName.DAY_OF_MONTH);
        buildFieldExpressionFromFieldPane(CronFieldName.MONTH);

        if (cronFieldContainer.size() < 6) {
            cronTextField.setText(StringUtils.EMPTY);
            return;
        }
        Cron cron = CronBuilder.cron(initCronDef)
                .withYear(cronFieldContainer.get(CronFieldName.YEAR))
                .withDoM(cronFieldContainer.get(CronFieldName.DAY_OF_MONTH))
                .withMonth(cronFieldContainer.get(CronFieldName.MONTH))
                .withDoW(cronFieldContainer.get(CronFieldName.DAY_OF_WEEK))
                .withHour(cronFieldContainer.get(CronFieldName.HOUR))
                .withMinute(cronFieldContainer.get(CronFieldName.MINUTE))
                .withSecond(cronFieldContainer.get(CronFieldName.SECOND))
                .instance();
        cronTextField.setText(cron.asString());
        // 生成最近运行时间
        ExecutionTime executionTime = ExecutionTime.forCron(cron);
        ZonedDateTime now = ZonedDateTime.now();
        int i = 0;
        StringBuilder nextCronText = new StringBuilder(210);
        while (i < 10) {
            Optional<ZonedDateTime> zdt = executionTime.nextExecution(now);
            if (zdt.isPresent()) {
                nextCronText.append(DateHelper.format(zdt.get(), DateHelper.YYYY_MM_DD_HH_mm_ss))
                        .append(Chars.LF);
                now = zdt.get();
            }
        }
        nextTimes.setText(nextCronText.toString());
    }

    /**
     * 修改事件处理器.重新生成cron
     *
     * @param <T>
     */
    private class ChangedEventHander<T> implements ChangeListener<T> {

        @Override
        public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
            generateCron();
        }
    }

    private class CheckedEventHander implements ChangeListener<Boolean> {

        private RadioButton aboutRadioButton;

        public CheckedEventHander(RadioButton radioButton) {
            aboutRadioButton = radioButton;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (aboutRadioButton.isSelected()) {
                generateCron();
            }
        }

    }

    /**
     * 表达式面板关联控件组,方便取关联的控件
     */
    private class FieldPaneGroup {

        private FlowPane onExpCheckboxPane;// '表达式范围'checbox所在的pane
        private ToggleGroup fieldExpToggleGrp;// 表达式选项按钮组
        private Spinner<Integer> periodSpinnerStart;
        private Spinner<Integer> periodSpinnerEnd;
        private Spinner<Integer> intervalSpinnerStart;
        private Spinner<Integer> intervalSpinner;

        public FieldPaneGroup() {
        }

        public FieldPaneGroup(FlowPane onExpCheckboxPane, ToggleGroup fieldExpToggleGrp,
                Spinner<Integer> periodSpinnerStart, Spinner<Integer> periodSpinnerEnd,
                Spinner<Integer> intervalSpinnerStart, Spinner<Integer> intervalSpinner) {
            this.onExpCheckboxPane = onExpCheckboxPane;
            this.fieldExpToggleGrp = fieldExpToggleGrp;
            this.periodSpinnerStart = periodSpinnerStart;
            this.periodSpinnerEnd = periodSpinnerEnd;
            this.intervalSpinnerStart = intervalSpinnerStart;
            this.intervalSpinner = intervalSpinner;
        }

        /**
         * 返回CronField数值范围的checkbox已选择的值
         * 
         * @return
         */
        public List<Integer> getCheckedOfOnExp() {
            return getCheckboxValuesFromPane(onExpCheckboxPane);
        }

        public FieldExpression buildFieldExpressionFromIntervalCtl() {
            Integer start = intervalSpinnerStart.getValue();
            Integer interval = intervalSpinner.getValue();
            if (start != null && interval != null && interval.intValue() > 0) {
                return FieldExpressionFactory.every(FieldExpressionFactory.on(start), interval);
            } else {
                intervalSpinner.requestFocus();
            }
            return null;
        }

        public FieldExpression buildFieldExpressionFromPeriodCtl() {
            Integer from = periodSpinnerStart.getValue();
            Integer to = periodSpinnerEnd.getValue();
            if (from != null && to != null && from.intValue() < to.intValue()) {
                return FieldExpressionFactory.between(from, to);
            } else {
                periodSpinnerEnd.requestFocus();
            }
            return null;
        }

        public FieldExpression buildFieldExpressionFromOnCtl() {
            List<Integer> fieldOnNums = getCheckedOfOnExp();
            if (!fieldOnNums.isEmpty()) {
                Collections.sort(fieldOnNums);
                List<FieldExpression> expressions = new ArrayList<>(fieldOnNums.size());
                fieldOnNums.forEach(c -> {
                    expressions.add(FieldExpressionFactory.on(c));
                });
                return FieldExpressionFactory.and(expressions);
            }
            return null;
        }

        public FlowPane getOnExpCheckboxPane() {
            return onExpCheckboxPane;
        }

        public void setOnExpCheckboxPane(FlowPane onExpCheckboxPane) {
            this.onExpCheckboxPane = onExpCheckboxPane;
        }

        public ToggleGroup getFieldExpToggleGrp() {
            return fieldExpToggleGrp;
        }

        public void setFieldExpToggleGrp(ToggleGroup fieldExpToggleGrp) {
            this.fieldExpToggleGrp = fieldExpToggleGrp;
        }

        public Spinner<Integer> getPeriodSpinnerStart() {
            return periodSpinnerStart;
        }

        public void setPeriodSpinnerStart(Spinner<Integer> periodSpinnerStart) {
            this.periodSpinnerStart = periodSpinnerStart;
        }

        public Spinner<Integer> getPeriodSpinnerEnd() {
            return periodSpinnerEnd;
        }

        public void setPeriodSpinnerEnd(Spinner<Integer> periodSpinnerEnd) {
            this.periodSpinnerEnd = periodSpinnerEnd;
        }

        public Spinner<Integer> getIntervalSpinnerStart() {
            return intervalSpinnerStart;
        }

        public void setIntervalSpinnerStart(Spinner<Integer> intervalSpinnerStart) {
            this.intervalSpinnerStart = intervalSpinnerStart;
        }

        public Spinner<Integer> getIntervalSpinner() {
            return intervalSpinner;
        }

        public void setIntervalSpinner(Spinner<Integer> intervalSpinner) {
            this.intervalSpinner = intervalSpinner;
        }

    }
}
