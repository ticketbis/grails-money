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

    void setup() {
        Money.defaultExchange = exchange_2_0
    }

    void "test basic money exchange"() {
    given:
        def usd20 = new Money(20, 'USD')
    expect:
        Money.defaultExchange == exchange_2_0
        Money.currentExchange == exchange_2_0

        usd20.exchangeTo(eur) == new Money(40, 'EUR')
        usd20.exchangeTo(eur, exchange_0_5) == new Money(10, 'EUR')
    }

    void "test nested money exchange"() {
    given:
        def usd20 = new Money(20, 'USD')
    expect:
        Money.withExchange(exchange_1_0) {
            assert usd20.exchangeTo(eur) == new Money(20, 'EUR')
            Money.withExchange(exchange_0_5) {
                assert usd20.exchangeTo(eur) == new Money(10, 'EUR')
                assert usd20.exchangeTo(cad) == new Money(10, 'CAD')
            }
            assert usd20.exchangeTo(cad) == new Money(20, 'CAD')
        }
    }

    void "test exceptions in nested money exchange"() {
    given:
        def usd20 = new Money(20, 'USD')
        def exception = new Exception("Test exception")
    expect:
        Money.withExchange(exchange_0_5) {
            assert usd20.exchangeTo(eur) == new Money(10, 'EUR')
            try {
                Money.withExchange(exchange_2_0) {
                    assert usd20.exchangeTo(eur) == new Money(40, 'EUR')
                    throw exception
                }
            } catch (ex) {
                assert ex.is(exception)
            }
            assert usd20.exchangeTo(cad) == new Money(10, 'CAD')
        }
    }

    void "test compare with exchange"() {
    given:
        def usd20 = new Money(20, 'USD')
    expect:
        // with exchange_2_0 (EURUSD=2.0)
        usd20 == new Money(10, 'EUR')
        usd20 > new Money(8, 'EUR')
        usd20 <= new Money(12, 'EUR')
        Money.withExchange(exchange_1_0) {
            usd20 >= new Money(19, 'EUR')
            usd20 < new Money(21, 'EUR')
        }
    }

}
