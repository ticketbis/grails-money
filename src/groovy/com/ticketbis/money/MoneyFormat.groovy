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

    DecimalFormat getFormatter(Locale locale = Locale.default) {
        DecimalFormat currencyFormat = NumberFormat.getCurrencyInstance(resolveLocale(locale)) as DecimalFormat
        currencyFormat.currency = currency
        currencyFormat.maximumFractionDigits = currency.defaultFractionDigits

        return currencyFormat
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
        DecimalFormat decimalFormat = NumberFormat.getNumberInstance(resolveLocale(locale)) as DecimalFormat
        decimalFormat.maximumFractionDigits = currency.defaultFractionDigits
        decimalFormat.format(amount);
    }

    Money round(RoundingMode rounding = RoundingMode.HALF_EVEN) {
        setScale(currency.defaultFractionDigits, rounding)
    }

    @CompileStatic
    static Locale resolveLocale(Object locale) {
        Locale myLocale
        if (locale instanceof Locale) {
            myLocale = (Locale) locale
        } else if (locale != null) {
            myLocale = StringUtils.parseLocaleString(locale.toString())
        }

        return myLocale
    }
}
