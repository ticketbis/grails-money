package com.ticketbis.money.test

import spock.lang.Specification

import com.ticketbis.money.*

class MoneyExchangeSpec extends Specification {
    class TestExchange implements Exchange {
        BigDecimal fixedRate

        TestExchange(rate) {
            fixedRate = rate
        }

        BigDecimal getRate(Currency from, Currency to) {
            fixedRate
        }
    }

    final eur = Currency.getInstance('EUR')
    final cad = Currency.getInstance('CAD')

    final exchange_2_0 = new TestExchange(2.0G)
    final exchange_1_0 = new TestExchange(1.0G)
    final exchange_0_5 = new TestExchange(0.5G)

    void "test basic money exchange"() {
    given:
        Money.defaultExchange = exchange_2_0

        def usd20 = new Money(20, 'USD')
    expect:
        Money.defaultExchange == exchange_2_0
        Money.currentExchange == exchange_2_0

        usd20.exchangeTo(eur) == new Money(40, 'EUR')
        usd20.exchangeTo(eur, exchange_0_5) == new Money(10, 'EUR')
    }

    void "test nested money exchange"() {
    given:
        Money.defaultExchange = exchange_2_0

        def usd20 = new Money(20, 'USD')
    expect:
        Money.withExchange(exchange_1_0) {
            assert usd20.exchangeTo(eur) == new Money(20, 'EUR')
            assert usd20.exchangeTo(cad) == new Money(20, 'CAD')

            Money.withExchange(exchange_0_5) {
                assert usd20.exchangeTo(eur) == new Money(10, 'EUR')
                assert usd20.exchangeTo(cad) == new Money(10, 'CAD')

                 Money.withExchange(exchange_2_0) {
                    assert usd20.exchangeTo(eur) == new Money(40, 'EUR')
                    assert usd20.exchangeTo(cad) == new Money(40, 'CAD')
                }
            }
        }
    }

}
