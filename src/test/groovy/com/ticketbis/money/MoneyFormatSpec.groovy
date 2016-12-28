package com.ticketbis.money

import spock.lang.Specification
import spock.lang.Unroll

import java.text.ChoiceFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

@Unroll
class MoneyFormatSpec extends Specification {

    def setupSpec() {
        Locale.setDefault(new Locale('es', 'ES'))
    }

    def "should format  #money using the default locale"() {
        expect:
        moneyFormatted == money.format()

        where:
        amount       | currency | moneyFormatted
        '123456.789' | 'EUR'    | '123.456,79 €'
        '123456.719' | 'USD'    | '123.456,72 USD'
        '1234567.89' | 'JPY'    | '1.234.568 JPY'

        money = getMoneyInstance(currency: currency, amount: amount)
    }

    def "should format  #money using a locale different than default one"() {
        expect:
        moneyFormatted == money.format(Locale.US)

        where:
        amount       | currency | moneyFormatted
        '123456.789' | 'EUR'    | 'EUR123,456.79'
        '123456.719' | 'USD'    | '\$123,456.72'
        '123456.719' | 'JPY'    | 'JPY123,457'

        money = getMoneyInstance(currency: currency, amount: amount)
    }

    def "should format  #money using a locale with NO decimals"() {
        expect:
        moneyFormatted == money.format(Locale.JAPAN)

        where:
        amount       | currency | moneyFormatted
        '123456.789' | 'EUR'    | 'EUR123,456.79'
        '123456.719' | 'USD'    | 'USD123,456.72'
        '123456.001' | 'JPY'    | '￥123,456'

        money = getMoneyInstance(currency: currency, amount: amount)
    }

    def "should format a #money using the custom formatter"() {
        given: "a custom Formatter"
        DecimalFormatSymbols symbols = new DecimalFormatSymbols()
        symbols.currencySymbol = '€'
        symbols.groupingSeparator = ']'
        symbols.monetaryDecimalSeparator = ','

        String pattern = '¤##,##0.00'
        DecimalFormat formatter = new DecimalFormat(pattern, symbols);

        expect:
        moneyformatted == money.format(formatter)

        where:
        amount       | currency | moneyformatted
        '123456.789' | 'EUR'    | '€123]456,79'
        '123456.719' | 'USD'    | '€123]456,72'

        money = getMoneyInstance(currency: currency, amount: amount)
    }

    def "should thrown a exception when something different from a DecimalFormat is send"() {
        given: "a money Object"
        Money money = getMoneyInstance(currency: 'EUR', amount: 123456.789G)

        and: "something different than a DecimalFormat"
        ChoiceFormat choiceFormat = new ChoiceFormat('')

        when: "format money is call"
        money.format(choiceFormat)

        then:
        thrown(IllegalStateException)
    }

    def "should format #money as a number using the default locale"() {
        expect:
        moneyFormattedAsNumber == money.formatNumber()

        where:
        amount       | currency | moneyFormattedAsNumber
        '123456'     | 'EUR'    | '123.456'
        '123456.789' | 'EUR'    | '123.456,79'
        '123456.719' | 'USD'    | '123.456,72'
        '123456.719' | 'JPY'    | '123.457'

        money = getMoneyInstance(currency: currency, amount: amount)
    }

    def "should format #money as a number using the custom locale"() {
        expect:
        moneyFormattedAsNumber == money.formatNumber(Locale.US)

        where:
        amount       | currency | moneyFormattedAsNumber
        '123456.789' | 'EUR'    | '123,456.79'
        '123456.719' | 'USD'    | '123,456.72'
        '123456.719' | 'JPY'    | '123,457'

        money = getMoneyInstance(currency: currency, amount: amount)
    }

    def "should return rounded Money: #expected"() {
        expect:
        money.round() == getMoneyInstance(currency: currency, amount: expected)

        where:
        amount       | currency | expected
        '123456.789' | 'EUR'    | '123456.79'
        '123456.711' | 'USD'    | '123456.71'
        '123456.719' | 'JPY'    | '123457'

        money = getMoneyInstance(currency: currency, amount: amount)
    }

    def "should format #money using the pattern: #pattern"() {
        expect:
        moneyFormatted == money.format(pattern)

        where:
        amount      | currency | pattern     | moneyFormatted
        123456.789G | 'EUR'    | '##,##0.00' | '123.456,79'
        123456.712G | 'EUR'    | '##,##0.00' | '123.456,71'
        123456.712G | 'USD'    | '##,##0.00' | '123.456,71'
        123456.712G | 'EUR'    | '##,##0.0'  | '123.456,7'
        123456.792G | 'EUR'    | '##,##0.0'  | '123.456,8'
        123456.712G | 'EUR'    | '##,##0.0'  | '123.456,7'
        123456.712G | 'JPY'    | '##,##0'    | '123.457'

        money = getMoneyInstance(currency: currency, amount: amount)
    }


    def "should thrown a exception pattern is not correct"() {
        given: "a money Object"
        Money money = getMoneyInstance(currency: "EUR", amount: 123456.789G)

        and: "an incorrect pattern"
        String incorrectPattern = '##.##0.00'

        when: "format money is call"
        money.format(incorrectPattern)

        then:
        thrown(IllegalArgumentException)
    }

    private static Money getMoneyInstance(Map attrs) {
        new Money(attrs.amount, attrs.currency)
    }
}
