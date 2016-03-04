package com.ticketbis.money

import grails.test.mixin.TestFor
import spock.lang.Specification
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(MoneyTagLib)
class MoneyTagLibSpec extends Specification {

    def setup() {
        Locale.setDefault(new Locale('en', 'US'))
    }

    def cleanup() {
    }

    void "test money inputField for decimal values"() {
    given:
        def html = applyTemplate(
                '<money:inputField name="testMoney" value="123.45" currency="EUR"/>'
        )
    expect:
        html =~ /value="struct"/
        html =~ /name="testMoney_amount".*value="123.45"/
        html =~ /name="testMoney_currency".*value="EUR"/
    }

    void "test money inputField for money values"() {
    given:
        def money = new Money(123.45G, Currency.getInstance('USD'))

        def html = applyTemplate(
                '<money:inputField name="testMoney" value="${ money }"/>',
                [money: money]
        )
    expect:
        html =~ /value="struct"/
        html =~ /name="testMoney_amount".*value="123.45"/
        html =~ /name="testMoney_currency".*value="USD"/
    }

    void "test money formatting"() {
    given:
        def money = new Money(1234.5G, 'RUB')

    expect:
        'RUB 1,234.50' == applyTemplate(
            '<money:format value="${ money }" pattern="¤ ##,##0.00"/>', [money: money])
    }

    void "test money advanced formatting"() {
    given:
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.currencySymbol = 'руб.'
        dfs.groupingSeparator = ' '
        dfs.decimalSeparator = ','

        def formatter = new DecimalFormat("##,##0 ¤")
        formatter.decimalFormatSymbols = dfs

        def money = new Money(1234.0G, 'RUB')

    expect:
        '1 234 руб.' == applyTemplate(
            '<money:format value="${ money }" numberFormat="${ formatter }"/>',
            [money: money, formatter: formatter])

    }

}
