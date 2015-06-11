package com.ticketbis.money

import org.grails.databinding.converters.AbstractStructuredBindingEditor

import org.springframework.context.i18n.LocaleContextHolder as LCH

import java.text.DecimalFormat
import java.text.NumberFormat

class StructuredMoneyEditor extends AbstractStructuredBindingEditor<Money> {

    Money getPropertyValue(Map values) {

        // retrieve the individual values from the Map
        def amount = values.amount
        def currency = values.currency

        // build decimal formatter
        DecimalFormat formatter
        if (values.parseFormat) {
            formatter = new DecimalFormat(values.parseFormat)
        } else {
            formatter = getLocaleAwareDecimalFormatter()
        }
        formatter.setParseBigDecimal(true)

        BigDecimal parsedAmount = formatter.parse(amount)

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
