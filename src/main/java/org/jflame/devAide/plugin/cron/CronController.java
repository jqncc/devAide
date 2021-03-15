package org.jflame.devAide.plugin.cron;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.jflame.commons.util.StringHelper;
import org.jflame.devAide.component.CheckBoxFlowPane;
import org.jflame.devAide.util.UIUtils;

import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronConstraintsFactory;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.CronField;
import com.cronutils.model.field.CronFieldName;
import com.cronutils.model.field.constraint.FieldConstraints;
import com.cronutils.model.field.expression.And;
import com.cronutils.model.field.expression.Between;
import com.cronutils.model.field.expression.Every;
import com.cronutils.model.field.expression.FieldExpression;
import com.cronutils.model.field.expression.FieldExpressionFactory;
import com.cronutils.model.field.expression.On;
import com.cronutils.model.field.value.IntegerFieldValue;
import com.cronutils.model.field.value.SpecialChar;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class CronController {

    @FXML
    private CheckBoxFlowPane secondFlowPane;
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
    private CheckBoxFlowPane minuteFlowPane;
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
    private CheckBoxFlowPane hourFlowPane;
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
    private CheckBoxFlowPane dayFlowPane;
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
    private CheckBoxFlowPane monthFlowPane;
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
    private CheckBoxFlowPane weekFlowPane;
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
    private Spinner<Integer> weekLastSpinner;
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
    private CheckBoxFlowPane yearFlowPane;
    @FXML
    private ToggleGroup yearGrp;
    @FXML
    private Spinner<Integer> yearPeriodSpinnerStart;
    @FXML
    private Spinner<Integer> yearPeriodSpinnerEnd;
    @FXML
    private RadioButton yearPeriodRadioBtn;
    @FXML
    private RadioButton yearPerRadioBtn;
    @FXML
    private RadioButton yearOnRadioBtn;

    @FXML
    private TextField cronTextField;
    @FXML
    private TextArea nextTimes;
    @FXML
    private Button btnParse;
    @FXML
    private Button btnGen;

    private ValidationSupport validationSupport = new ValidationSupport();

    private Map<CronFieldName,FieldExpression> cronFieldContainer = new EnumMap<>(CronFieldName.class);
    // private Map<CronFieldName,FieldPaneGroup> fieldGroup = new HashMap<>();
    private Map<CronFieldName,CronFieldValue> fieldValueGroup = new HashMap<>();

    private final CronDefinition initCronDef;

    private final String FIELD_EXPRESSION_ON = "m,n";
    private final String FIELD_EXPRESSION_ONLAST = "L";
    private final String FIELD_EXPRESSION_ONLASTFEW = "nL";
    private final String FIELD_EXPRESSION_BETWEEN = "m-n";
    private final String FIELD_EXPRESSION_EVERY = "m/n";
    private final String FIELD_EXPRESSION_INDEX_WEEK = "m#n";
    private final String FIELD_EXPRESSION_ALWAYS = "*";
    private final String FIELD_EXPRESSION_NOTSPECIFIED = "?";

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
        initYearPane();

        btnGen.setOnAction(event -> {
            generateCron();
        });

        btnParse.setOnAction(event -> {
            parseCronToUI();
        });
    }

    /**
     * '秒'面板初始
     */
    private void initSecondPane() {
        initCommonField(CronFieldName.SECOND, secondFlowPane, secondGrp, secPeriodSpinnerStart, secPeriodSpinnerEnd,
                secIntervalSpinnerStart, secIntervalSpinner);
    }

    /**
     * '分钟'面板初始
     */
    private void initMinutePane() {
        initCommonField(CronFieldName.MINUTE, minuteFlowPane, minuteGrp, minPeriodSpinnerStart, minPeriodSpinnerEnd,
                minIntervalSpinnerStart, minIntervalSpinner);
    }

    /**
     * '小时'面板初始
     */
    private void initHourPane() {
        initCommonField(CronFieldName.HOUR, hourFlowPane, hourGrp, hourPeriodSpinnerStart, hourPeriodSpinnerEnd,
                hourIntervalSpinnerStart, hourIntervalSpinner);
    }

    /**
     * '日'面板初始
     */
    private void initDayPane() {
        initCommonField(CronFieldName.DAY_OF_MONTH, dayFlowPane, dayGrp, dayPeriodSpinnerStart, dayPeriodSpinnerEnd,
                dayIntervalSpinnerStart, dayIntervalSpinner);
    }

    /**
     * '月份'面板初始
     */
    private void initMonthPane() {
        initCommonField(CronFieldName.MONTH, monthFlowPane, monthGrp, monthPeriodSpinnerStart, monthPeriodSpinnerEnd,
                monthIntervalSpinnerStart, monthIntervalSpinner);
    }

    /**
     * '周'面板初始
     */
    private void initDayOfWeekPane() {
        CronFieldValue bindProperty = initCommonField(CronFieldName.DAY_OF_WEEK, weekFlowPane, weekGrp,
                weekPeriodSpinnerStart, weekPeriodSpinnerEnd, weekIntervalSpinnerStart, weekIntervalSpinner);

        UIUtils.setSpinnerForInteger(weekLastSpinner, 1, 7);
        bindProperty.lastDayOfWeekProperty()
                .bind(weekLastSpinner.valueProperty());
    }

    /**
     * '年'面板初始
     */
    private void initYearPane() {
        CronFieldValue bindProperty = new CronFieldValue();

        int curYear = LocalDate.now()
                .getYear();
        createCheckboxByRange(yearFlowPane, yearOnRadioBtn, bindProperty, curYear - 5, curYear + 15);

        // 设置spinner为数值类型
        UIUtils.setSpinnerForInteger(yearPeriodSpinnerStart, 1970, 2099);
        UIUtils.setSpinnerForInteger(yearPeriodSpinnerEnd, 1970, 2099);

        bindProperty.selectedFieldExpression()
                .bind(yearGrp.selectedToggleProperty());
        bindProperty.periodStartProperty()
                .bind(yearPeriodSpinnerStart.valueProperty());
        bindProperty.periodEndProperty()
                .bind(yearPeriodSpinnerEnd.valueProperty());

        fieldValueGroup.put(CronFieldName.YEAR, bindProperty);
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
    private CronFieldValue initCommonField(CronFieldName field, CheckBoxFlowPane flowPane, ToggleGroup toggleGrp,
            Spinner<Integer> periodSpinnerStart, Spinner<Integer> periodSpinnerEnd,
            Spinner<Integer> intervalSpinnerStart, Spinner<Integer> intervalSpinner) {
        CronFieldValue bindProperty = new CronFieldValue();

        initCheckBoxOfFieldRange(flowPane, field, bindProperty);

        int maxRange = initCronDef.getFieldDefinition(field)
                .getConstraints()
                .getEndRange();
        // 设置spinner为数值类型
        UIUtils.setSpinnerForInteger(periodSpinnerStart, 0, maxRange);
        UIUtils.setSpinnerForInteger(periodSpinnerEnd, 1, maxRange);
        UIUtils.setSpinnerForInteger(intervalSpinnerStart, 0, maxRange);
        UIUtils.setSpinnerForInteger(intervalSpinner, 1, maxRange);

        bindProperty.selectedFieldExpression()
                .bind(toggleGrp.selectedToggleProperty());

        bindProperty.periodStartProperty()
                .bind(periodSpinnerStart.valueProperty());
        bindProperty.periodEndProperty()
                .bind(periodSpinnerEnd.valueProperty());

        bindProperty.intervalStartProperty()
                .bind(intervalSpinnerStart.valueProperty());
        bindProperty.intervalProperty()
                .bind(intervalSpinner.valueProperty());

        fieldValueGroup.put(field, bindProperty);
        return bindProperty;
    }

    /**
     * 根据CronFieldName值范围生成复选框
     * 
     * @param pane
     * @param fieldName
     */
    private void initCheckBoxOfFieldRange(CheckBoxFlowPane pane, CronFieldName fieldName, CronFieldValue bindProperty) {
        FieldConstraints fieldConstraints = initCronDef.getFieldDefinition(fieldName)
                .getConstraints();
        RadioButton rb = null;
        switch (fieldName) {
            case SECOND:
                rb = secOnRadioBtn;
                break;
            case MINUTE:
                rb = minOnRadioBtn;
                break;
            case HOUR:
                rb = hourOnRadioBtn;
                break;
            case DAY_OF_MONTH:
                rb = dayOnRadioBtn;
                break;
            case MONTH:
                rb = monthOnRadioBtn;
                break;
            case DAY_OF_WEEK:
                rb = weekOnRadioBtn;
                break;
            default:
                break;
        }
        createCheckboxByRange(pane, rb, bindProperty, fieldConstraints.getStartRange(), fieldConstraints.getEndRange());
    }

    private void createCheckboxByRange(CheckBoxFlowPane pane, RadioButton onRadioButton, CronFieldValue bindProperty,
            int start, int end) {
        ChangeListener<Boolean> rbListener = new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue && !onRadioButton.isSelected()) {
                    onRadioButton.setSelected(true);
                }
            }

        };
        for (int i = start; i <= end; i++) {
            CheckBox checkBox = new CheckBox(String.valueOf(i));
            pane.add(checkBox);
            checkBox.selectedProperty()
                    .addListener(rbListener);
        }
        bindProperty.onValuesProperty()
                .bind(pane.selectedValuesProperty());
    }

    private void buildFieldExpression(CronFieldName fieldName) {
        CronFieldValue fieldValue = fieldValueGroup.get(fieldName);
        FieldExpression expression = null;
        String selectedFieldExp = fieldValue.getSelectedFieldExpression();
        // System.out.println(fieldName.name() + "= " + selectedFieldExp);
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
                expression = fieldValue.buildFieldExpressionFromIntervalCtl();
                break;
            case FIELD_EXPRESSION_BETWEEN:
                expression = fieldValue.buildFieldExpressionFromPeriodCtl();
                break;
            case FIELD_EXPRESSION_ON:
                expression = fieldValue.buildFieldExpressionFromOnCtl();
                break;
            case FIELD_EXPRESSION_ONLAST:
                expression = FieldExpressionFactory.on(SpecialChar.L);
                break;
            case FIELD_EXPRESSION_ONLASTFEW:
                expression = fieldValue.buildFieldExpressionLastDayOfWeek();
                break;
            case FIELD_EXPRESSION_INDEX_WEEK:
                expression = fieldValue.buildFieldExpressionIndexDayOfWeek();
                break;
            default:
                break;
        }

        if (expression != null) {
            cronFieldContainer.put(fieldName, expression);
        }
    }

    /**
     * 生成cron表达式
     */
    private void generateCron() {
        cronFieldContainer.clear();
        for (CronFieldName cfn : CronFieldName.values()) {
            if (cfn != CronFieldName.DAY_OF_YEAR) {
                buildFieldExpression(cfn);
            }
        }

        if (cronFieldContainer.size() < 6) {
            cronTextField.setText(StringUtils.EMPTY);
            UIUtils.errorAlert("设置不完整");
            return;
        }
        if (cronFieldContainer.get(CronFieldName.DAY_OF_MONTH) != FieldExpression.questionMark()) {
            if (cronFieldContainer.get(CronFieldName.DAY_OF_WEEK) != FieldExpression.questionMark()) {
                UIUtils.errorAlert("星期和日不能同时指定");
                return;
            }
        }

        CronBuilder builder = CronBuilder.cron(initCronDef);
        if (cronFieldContainer.containsKey(CronFieldName.YEAR)) {
            builder = builder.withYear(cronFieldContainer.get(CronFieldName.YEAR));
        }
        Cron cron = null;
        try {
            cron = builder.withDoM(cronFieldContainer.get(CronFieldName.DAY_OF_MONTH))
                    .withMonth(cronFieldContainer.get(CronFieldName.MONTH))
                    .withDoW(cronFieldContainer.get(CronFieldName.DAY_OF_WEEK))
                    .withHour(cronFieldContainer.get(CronFieldName.HOUR))
                    .withMinute(cronFieldContainer.get(CronFieldName.MINUTE))
                    .withSecond(cronFieldContainer.get(CronFieldName.SECOND))
                    .instance();
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.errorAlert(e.getMessage());
            return;
        }
        cronTextField.setText(cron.asString());
        // 生成最近运行时间
        ExecutionTime executionTime = ExecutionTime.forCron(cron);
        ZonedDateTime now = ZonedDateTime.now();
        int i = 0;
        StringBuilder nextCronText = new StringBuilder(170);
        while (i < 8) {
            Optional<ZonedDateTime> zdt = executionTime.nextExecution(now);
            if (zdt.isPresent()) {
                nextCronText.append(DateHelper.format(zdt.get(), DateHelper.YYYY_MM_DD_HH_mm_ss))
                        .append(Chars.LF);
                now = zdt.get();
            }
            i++;
        }
        nextTimes.setText(nextCronText.toString());
    }

    /**
     * 解析cron表达式设置到UI控件
     */
    private void parseCronToUI() {
        String cronStr = cronTextField.getText();
        if (StringHelper.isNotEmpty(cronStr)) {
            CronParser parser = new CronParser(initCronDef);
            try {
                Cron cron = parser.parse(cronStr.trim());
                Map<CronFieldName,CronField> cronMap = cron.retrieveFieldsAsMap();
                cronMap.forEach((k, v) -> {
                    if (k == CronFieldName.SECOND) {
                        commonCronFieldAssignToUI(k, v, secondFlowPane, secondGrp, secPeriodSpinnerStart,
                                secPeriodSpinnerEnd, secIntervalSpinnerStart, secIntervalSpinner);
                    } else if (k == CronFieldName.MINUTE) {
                        commonCronFieldAssignToUI(k, v, minuteFlowPane, minuteGrp, minPeriodSpinnerStart,
                                minPeriodSpinnerEnd, minIntervalSpinnerStart, minIntervalSpinner);
                    } else if (k == CronFieldName.HOUR) {
                        commonCronFieldAssignToUI(k, v, hourFlowPane, hourGrp, hourPeriodSpinnerStart,
                                hourPeriodSpinnerEnd, hourIntervalSpinnerStart, hourIntervalSpinner);
                    } else if (k == CronFieldName.DAY_OF_MONTH) {
                        commonCronFieldAssignToUI(k, v, dayFlowPane, dayGrp, dayPeriodSpinnerStart, dayPeriodSpinnerEnd,
                                dayIntervalSpinnerStart, dayIntervalSpinner);
                    } else if (k == CronFieldName.MONTH) {
                        commonCronFieldAssignToUI(k, v, monthFlowPane, monthGrp, monthPeriodSpinnerStart,
                                monthPeriodSpinnerEnd, monthIntervalSpinnerStart, monthIntervalSpinner);
                    } else if (k == CronFieldName.DAY_OF_WEEK) {
                        commonCronFieldAssignToUI(k, v, weekFlowPane, weekGrp, weekPeriodSpinnerStart,
                                weekPeriodSpinnerEnd, weekIntervalSpinnerStart, weekIntervalSpinner);
                    } else if (k == CronFieldName.YEAR) {
                        commonCronFieldAssignToUI(k, v, yearFlowPane, yearGrp, yearPeriodSpinnerStart,
                                yearPeriodSpinnerEnd, null, null);
                    }
                });
            } catch (IllegalArgumentException e) {
                UIUtils.errorAlert("cron表达式解析错误:" + e.getMessage());
            }
        }
    }

    private void commonCronFieldAssignToUI(CronFieldName fieldName, CronField field, CheckBoxFlowPane checkBoxPane,
            ToggleGroup toggleGrp, Spinner<Integer> periodSpinnerStart, Spinner<Integer> periodSpinnerEnd,
            Spinner<Integer> intervalSpinnerStart, Spinner<Integer> intervalSpinner) {
        if (field.getExpression() == FieldExpression.always()) {
            selectRadioButtonByFieldExp(toggleGrp, FIELD_EXPRESSION_ALWAYS);
        } else if (field.getExpression() == FieldExpression.questionMark()) {
            if (fieldName != CronFieldName.YEAR) {
                selectRadioButtonByFieldExp(toggleGrp, FIELD_EXPRESSION_NOTSPECIFIED);
            }
        } else if (field.getExpression() instanceof Between) {
            selectRadioButtonByFieldExp(toggleGrp, FIELD_EXPRESSION_BETWEEN);
            Between bwExp = (Between) field.getExpression();
            periodSpinnerStart.getValueFactory()
                    .setValue(((IntegerFieldValue) bwExp.getFrom()).getValue());
            periodSpinnerEnd.getValueFactory()
                    .setValue(((IntegerFieldValue) bwExp.getTo()).getValue());
        } else if (field.getExpression() instanceof Every) {
            selectRadioButtonByFieldExp(toggleGrp, FIELD_EXPRESSION_EVERY);
            Every everyExp = (Every) field.getExpression();
            UIUtils.setValue(intervalSpinner, everyExp.getPeriod()
                    .getValue());
            UIUtils.setValue(intervalSpinnerStart, ((On) everyExp.getExpression()).getTime()
                    .getValue());
        } else if (field.getExpression() instanceof And) {
            selectRadioButtonByFieldExp(toggleGrp, FIELD_EXPRESSION_ON);
            And andExp = (And) field.getExpression();
            List<Object> onValues = new ArrayList<>();
            andExp.getExpressions()
                    .forEach(s ->
                    {
                        onValues.add(((On) s).getTime()
                                .getValue());
                    });
            checkBoxPane.setSelectedValues(onValues);
        } else if (field.getExpression() instanceof On) {
            On onExp = (On) field.getExpression();
            if (onExp.getSpecialChar()
                    .getValue() == SpecialChar.L) {
                if (onExp.getTime()
                        .getValue() == -1) {
                    selectRadioButtonByFieldExp(toggleGrp, FIELD_EXPRESSION_ONLAST);
                } else {
                    // nL只在'周'可用
                    if (fieldName == CronFieldName.DAY_OF_WEEK) {
                        weekLastRadioBtn.setSelected(true);
                        UIUtils.setValue(weekLastSpinner, onExp.getTime()
                                .getValue());
                    }
                }
            } else if (onExp.getSpecialChar()
                    .getValue() == SpecialChar.HASH) {
                // m#n只在'周'可用
                if (fieldName == CronFieldName.DAY_OF_WEEK) {
                    weekIntervalRadioBtn.setSelected(true);
                    UIUtils.setValue(intervalSpinnerStart, onExp.getTime()
                            .getValue());
                    UIUtils.setValue(intervalSpinner, onExp.getNth()
                            .getValue());
                }
            } else if (onExp.getSpecialChar()
                    .getValue() == SpecialChar.NONE) {
                selectRadioButtonByFieldExp(toggleGrp, FIELD_EXPRESSION_ON);
                checkBoxPane.setSelectedValues(Arrays.asList(onExp.getTime()
                        .getValue()));
            }
        }
    }

    private String getRadioButtonFieldExp(RadioButton rb) {
        String rbText = rb.getText();
        return StringUtils.substringBetween(rbText, "(", ")");
    }

    private void selectRadioButtonByFieldExp(ToggleGroup toggleGrp, String fieldExpression) {
        toggleGrp.getToggles()
                .forEach(t ->
                {
                    if (fieldExpression.equals(getRadioButtonFieldExp((RadioButton) t))) {
                        t.setSelected(true);
                    }
                });
    }

    private class CronFieldValue {

        private ObjectProperty<Toggle> selectedFieldExpression = new SimpleObjectProperty<>();
        private IntegerProperty periodStart = new SimpleIntegerProperty();
        private IntegerProperty periodEnd = new SimpleIntegerProperty();
        private IntegerProperty intervalStart = new SimpleIntegerProperty();
        private IntegerProperty interval = new SimpleIntegerProperty();
        private IntegerProperty lastDayOfWeek = new SimpleIntegerProperty();
        private ListProperty<Object> onValues = new SimpleListProperty<>();

        public String getSelectedFieldExpression() {
            RadioButton rb = (RadioButton) selectedFieldExpression.get();
            if (rb != null) {
                return getRadioButtonFieldExp(rb);
            }
            return null;
        }

        public FieldExpression buildFieldExpressionFromIntervalCtl() {
            int start = getIntervalStart();
            int interval = getInterval();
            if (interval > 0) {
                return FieldExpressionFactory.every(FieldExpressionFactory.on(start), interval);
            }
            return null;
        }

        public FieldExpression buildFieldExpressionFromPeriodCtl() {
            int from = getPeriodStart();
            int to = getPeriodEnd();
            return FieldExpressionFactory.between(from, to);
        }

        public FieldExpression buildFieldExpressionFromOnCtl() {
            List<Integer> fieldOnNums = getOnIntValues();
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

        public FieldExpression buildFieldExpressionLastDayOfWeek() {
            int lastDayOfWeek = getLastDayOfWeek();
            if (lastDayOfWeek > 0) {
                FieldExpressionFactory.on(lastDayOfWeek, SpecialChar.L);
            }
            return null;
        }

        public FieldExpression buildFieldExpressionIndexDayOfWeek() {
            int start = getIntervalStart();
            int end = getInterval();
            if (start > 0 && end > 0) {
                return FieldExpressionFactory.on(start, SpecialChar.HASH, end);
            }
            return null;
        }

        public ObjectProperty<Toggle> selectedFieldExpression() {
            return selectedFieldExpression;
        }

        public int getPeriodStart() {
            return periodStart.get();
        }

        public IntegerProperty periodStartProperty() {
            return periodStart;
        }

        public int getPeriodEnd() {
            return periodEnd.get();
        }

        public IntegerProperty periodEndProperty() {
            return periodEnd;
        }

        public int getIntervalStart() {
            return intervalStart.get();
        }

        public IntegerProperty intervalStartProperty() {
            return intervalStart;
        }

        public int getInterval() {
            return interval.get();
        }

        public IntegerProperty intervalProperty() {
            return interval;
        }

        public int getLastDayOfWeek() {
            return lastDayOfWeek.get();
        }

        public IntegerProperty lastDayOfWeekProperty() {
            return lastDayOfWeek;
        }

        public List<Integer> getOnIntValues() {
            List<Object> objs = onValues.get();
            if (objs != null) {
                List<Integer> intValues = new ArrayList<>(objs.size());
                objs.forEach(o -> intValues.add(Integer.valueOf(o.toString())));
                return intValues;
            }
            return null;
        }

        public ListProperty<Object> onValuesProperty() {
            return onValues;
        }
    }

}
