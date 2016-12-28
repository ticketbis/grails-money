package com.ticketbis.money

import org.grails.web.databinding.converters.AbstractStructuredBindingEditor
import org.springframework.context.i18n.LocaleContextHolder as LCH
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.DecimalFormatSymbols
import groovy.transform.CompileStatic

@CompileStatic
class StructuredMoneyEditor extends AbstractStructuredBindingEditor<Money> {

    private static final String currencyPlaceholder = '¤'

    Money getPropertyValue(Map values) {

        DecimalFormat formatter = getCustomDecimalFormatter(values)?:getLocaleAwareDecimalFormatter()
        BigDecimal parsedAmount = getParsedAmount(formatter, (String) values.amount)

        new Money(parsedAmount, (String) values.currency)
    }

    protected DecimalFormat getLocaleAwareDecimalFormatter() {
        NumberFormat formatter = NumberFormat.getInstance(LCH.getLocale())
        if (!(formatter instanceof DecimalFormat)) {
            throw new IllegalStateException("Cannot support non-DecimalFormat: " + formatter)
        }
        (DecimalFormat) formatter
    }

    private DecimalFormat getCustomDecimalFormatter(Map values) {

        String amount = (String) values.amount
        DecimalFormat formatter

        if (values.numberFormat) {
            Map numberFormat = (Map) values.numberFormat
            String customPattern = (String) numberFormat.pattern

            DecimalFormatSymbols dfs = new DecimalFormatSymbols()
            dfs.monetaryDecimalSeparator = (char) numberFormat.decimalSeparator
            dfs.decimalSeparator = (char) numberFormat.decimalSeparator
            dfs.groupingSeparator = (char) numberFormat.groupingSeparator
            dfs.currencySymbol = numberFormat.currencySymbol

            String currencySymbol = numberFormat.currencySymbol
            String pattern = amount.contains(currencySymbol) ?
                customPattern : customPattern.replace(currencyPlaceholder, '').trim()

            formatter = new DecimalFormat(pattern, dfs)
        }

    formatter
    }

    private  BigDecimal getParsedAmount( DecimalFormat formatter, String amount) {
        formatter.parseBigDecimal = true

        formatter.parse(amount)
    }
}
