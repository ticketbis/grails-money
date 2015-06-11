package com.ticketbis.money

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(MoneyTagLib)
class MoneyTagLibSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test money inputField for decimal values"() {
    given:
        def html = applyTemplate(
                '<money:inputField name="testMoney" value="123.45" currency="EUR"/>'
        )
    expect:
        html =~ /value="money.struct"/
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
        html =~ /value="money.struct"/
        html =~ /name="testMoney_amount".*value="123.45"/
        html =~ /name="testMoney_currency".*value="USD"/
    }
}
