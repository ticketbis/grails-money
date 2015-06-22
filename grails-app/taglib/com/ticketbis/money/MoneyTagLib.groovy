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
        def parseFormat = attrs.remove('parseFormat')

        def attrsStr = attrs.
                collect { attr, val -> "$attr=\"$val\"" }.
                join(' ')

        out << """
            <input type="hidden" name="${ name }" value="struct">
            <input type="hidden" name="${ name }_currency" value="${ currency }">
            <input type="$type" name="${ name }_amount" value="${ amount }" $attrsStr>
        """
        if (parseFormat) {
            out << """
                <input type="hidden" name="${ name }_parseFormat" value="${ parseFormat }">
            """
        }
    }

    def format = { attrs ->
        Money value = new Money(attrs.value)

        NumberFormat formatter
        if (attrs.numberFormat) {
            formatter = attrs.numberFormat

        } else if (attrs.pattern) {
            formatter = new DecimalFormat(attrs.pattern)
            formatter.currency = value.currency
        } else {
            formatter = new DecimalFormat()
            formatter.currency = value.currency
        }

        out << formatter.format(value.amount)
    }
}
