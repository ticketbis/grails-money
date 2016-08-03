package com.ticketbis.money

import groovy.transform.CompileStatic
import org.springframework.util.StringUtils

import java.math.RoundingMode

import java.text.DecimalFormat
import java.text.NumberFormat

@groovy.transform.PackageScope
trait MoneyFormat {

    abstract Currency getCurrency()
    abstract BigDecimal getAmount()

    DecimalFormat getFormatter(Locale locale = Locale.default, boolean includeCurrency = true) {
        DecimalFormat formatter
        if (includeCurrency) {
            formatter = NumberFormat.getCurrencyInstance(locale) as DecimalFormat
        } else {
            formatter = NumberFormat.getNumberInstance(locale) as DecimalFormat
        }

        formatter.currency = currency
        formatter.maximumFractionDigits = currency.defaultFractionDigits

        return formatter
    }

    String format(Locale locale = Locale.default) {
        DecimalFormat currencyFormat = getFormatter(locale)
        format(currencyFormat)
    }

    String format(NumberFormat numberFormat) {
        if (!(numberFormat instanceof DecimalFormat)) {
            throw new IllegalStateException("Cannot support non-DecimalFormat: ${numberFormat}")
        }

        numberFormat.format(amount)
    }

    String format(String pattern) {
        DecimalFormat decimalFormat = NumberFormat.getNumberInstance() as DecimalFormat
        decimalFormat.applyPattern(pattern)

        decimalFormat.format(amount)
    }

    String formatNumber(Locale locale = Locale.default) {
        DecimalFormat decimalFormat = getFormatter(locale, false)
        format(decimalFormat);
    }

    Money round(RoundingMode rounding = RoundingMode.HALF_EVEN) {
        setScale(currency.defaultFractionDigits, rounding)
    }
}
