package com.ticketbis.money

import org.grails.databinding.converters.AbstractStructuredBindingEditor
import org.springframework.context.i18n.LocaleContextHolder as LCH
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.DecimalFormatSymbols
import groovy.transform.CompileStatic

@CompileStatic
class StructuredMoneyEditor extends AbstractStructuredBindingEditor<Money> {

    Money getPropertyValue(Map values) {
        // retrieve the individual values from the Map
        String amount = values.amount
        String currency = values.currency

        // build decimal formatter
        DecimalFormat formatter
        if (values.numberFormatter) {
            Map numberFormat = (Map) values.numberFormat
            formatter = new DecimalFormat((String) numberFormat.pattern)

            DecimalFormatSymbols dfs = new DecimalFormatSymbols()
            dfs.decimalSeparator = (char) numberFormat.decimalSeparator
            dfs.groupingSeparator = (char) numberFormat.groupingSeparator
            dfs.currencySymbol = numberFormat.currencySymbol
            formatter.decimalFormatSymbols = dfs
        } else {
            formatter = getLocaleAwareDecimalFormatter()
        }
        formatter.parseBigDecimal = true

        BigDecimal parsedAmount = (BigDecimal) formatter.parse(amount)

        // create and return a Money
        new Money(parsedAmount, currency)
    }

    protected DecimalFormat getLocaleAwareDecimalFormatter() {
        NumberFormat formatter = NumberFormat.getInstance(LCH.getLocale())
        if (!(formatter instanceof DecimalFormat)) {
            throw new IllegalStateException("Cannot support non-DecimalFormat: " + formatter)
        }
        (DecimalFormat) formatter
    }
}
