package com.ticketbis.money

import java.text.DecimalFormat
import java.text.NumberFormat

class MoneyTagLib {
    static namespace = 'money'

    def inputField = { attrs ->
        def name = attrs.remove('name')
        def value = attrs.remove('value')

        def currency, amount
        if (value instanceof Money) {
            amount = value.amount
            currency = value.currency
        } else {
            amount = new BigDecimal(value ?: 0)
            currency = Currency.getInstance(attrs.remove('currency'))
        }

        def type = attrs.remove('type') ?: 'number'
        def numberFormat = attrs.remove('numberFormat')

        String amountStr
        if (numberFormat) {
            amountStr = numberFormat.format(amount)
        } else {
            def currencySymbol = attrs.remove('currencySymbol') ?: ''
            amountStr = formatNumber(number: amount, type: 'currency',
                        currencyCode: currency, currencySymbol: currencySymbol)
        }

        def attrsStr = attrs.
                collect { attr, val -> "$attr=\"$val\"" }.
                join(' ')

        out << """
            <input type="hidden" name="${ name }" value="struct">
            <input type="hidden" name="${ name }_currency" value="${ currency }">
            <input type="$type" name="${ name }_amount" value="${ amountStr }" $attrsStr>
        """
        if (numberFormat) {
            out << """
                <input type="hidden" name="${ name }_numberFormat.pattern" value="${ numberFormat.toPattern() }">

                <input type="hidden" name="${ name }_numberFormat.decimalSeparator"
                                    value="${ numberFormat.decimalFormatSymbols.decimalSeparator }">
                <input type="hidden" name="${ name }_numberFormat.groupingSeparator"
                                    value="${ numberFormat.decimalFormatSymbols.groupingSeparator }">
                <input type="hidden" name="${ name }_numberFormat.currencySymbol"
                                    value="${ numberFormat.decimalFormatSymbols.currencySymbol }">
            """
        }
    }
}
