package org.jflame.devAide.model.cron;

import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.expression.FieldExpression;
import com.cronutils.model.field.expression.FieldExpressionFactory;

public class CronField {

    public static final char[] wildcards = new char[] { '/','-',',','*' };

    public static void main(String[] args) {
        Cron quartzBuiltCronExpression = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
                .withYear(FieldExpression.always())
                .withDoM(FieldExpressionFactory.between(1, 3))
                .withMonth(FieldExpression.always())
                .withDoW(FieldExpression.questionMark())
                .withHour(FieldExpression.always())
                .withMinute(FieldExpression.always())
                .withSecond(FieldExpressionFactory.on(9)
                        .and(FieldExpressionFactory.on(6)))
                .instance();
        String quartzBuiltCronExpressionString = quartzBuiltCronExpression.asString();
        System.out.println(quartzBuiltCronExpressionString);
    }
}
