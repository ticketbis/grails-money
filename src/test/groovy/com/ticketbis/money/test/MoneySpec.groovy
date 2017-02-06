package com.ticketbis.money.test

import spock.lang.Specification
import java.math.RoundingMode
import com.ticketbis.money.*

class MoneySpec extends Specification {

    void setup() {
        Number.mixin(NumberMoneyExtension)
    }

    void "test money constructors"() {
    given:
        def eur12 = new Money(12.0G, 'EUR')
    expect:
        Money.ZERO == new Money()
        eur12 == new Money('12 EUR')
        eur12 == new Money('12.0 EUR')
        eur12 == new Money(12, 'EUR')
        eur12 == new Money('12', 'EUR')
    }

    void "test money basic arithmetic"() {
    given:
        def eur12 = new Money(12, 'EUR')
        def eur20 = new Money(20, 'EUR')
        def eur32 = new Money(32.0G, 'EUR')

    expect:
        eur12 + eur20 == eur32
        eur32 - eur20 == eur12
        eur12 - eur32 == -eur20
        -eur20 - eur12 == -eur32
        eur20 + 12 == eur32
        eur32 - 12.0G == eur20
        eur12.add(eur20) == eur32
        eur32.subtract(eur20) == eur12
        eur12.subtract(eur32) == -eur20
        (-eur20).subtract(eur12) == -eur32
        eur20.add(12) == eur32
        eur32.subtract(12.0G) == eur20
    }

    void "test money compare"() {
        def eur12 = new Money(12.0G, 'EUR')

    expect:
        eur12 != new Money(12, 'USD')
        eur12 != new Money(20, 'EUR')
        new Money(0, 'EUR') == new Money(0, 'USD')
        eur12 > new Money(5, 'EUR')
        eur12 <= new Money(12, 'EUR')
    }

    void "test currency mistmatch"() {
    given:
        def eur12 = new Money(12, 'EUR')
        def usd20 = new Money(20, 'USD')

    when:
        eur12 + usd20
    then:
        thrown(Money.CurrencyMismatchException)
    when:
        usd20 - eur12
    then:
        thrown(Money.CurrencyMismatchException)
    when:
        eur12.add(usd20)
    then:
        thrown(Money.CurrencyMismatchException)
    when:
        usd20.subtract(eur12)
    then:
        thrown(Money.CurrencyMismatchException)
    }


    void "test operations with values"() {
    given:
        def eur20 = new Money(20, 'EUR')
        def eur40 = new Money(40.0G, 'EUR')

    expect:
        eur40 == eur20 * 2
        eur20 == eur40 / 2.0G
    }

    void "test number extension"() {
    given:
        def eur20 = new Money(20, 'EUR')

    expect:
        20 + eur20 == new Money(40, 'EUR')
        40 - eur20 == new Money(20, 'EUR')
        2 * eur20 == new Money(40, 'EUR')
        eur20 == 20.toMoney(Currency.getInstance('EUR'))
    }

    void "test rounding moneys"() {
    given:
        def eur20 = new Money(20.1G, 'EUR')

    expect:
        eur20.setScale(0, RoundingMode.CEILING) == new Money('21 EUR')
        eur20.setScale(0, RoundingMode.HALF_DOWN) == new Money('20 EUR')
    }

    def "test fromMinorUnits"() {
    given:
        def eur220cents = Money.fromMinorUnits(220, 'EUR')
        def eur200cents = Money.fromMinorUnits(200, 'EUR')
    expect:
        eur220cents == new Money(2.20, 'EUR')
        eur200cents == new Money(2, 'EUR')
    }

    def "test majorUnits"() {
    given:
        def eurCents = new Money(2.35, 'EUR')
        def bdhCents = new Money(-1.845, 'BHD')
        def noCents = new Money(4, 'EUR')
    expect:
        eurCents.majorUnits() == 200
        bdhCents.majorUnits() == -1000
        noCents.majorUnits() == 400
    }

    def "test minorUnits"() {
    given:
        def eurCents = new Money(2.35, 'EUR')
        def bdhCents = new Money(-1.345, 'BHD')
        def noCents = new Money(4, 'EUR')
    expect:
        eurCents.minorUnits() == 235
        bdhCents.minorUnits() == -1345
        noCents.minorUnits() == 400
    }

    def "test majorPart"() {
    given:
        def eurCents = new Money(2.35, 'EUR')
        def bdhCents = new Money(-1.345, 'BHD')
        def noCents = new Money(4, 'EUR')
    expect:
        eurCents.majorPart() == 2
        bdhCents.majorPart() == -1
        noCents.majorPart() == 4
    }

    def "test minorPart"() {
    given:
        def eurCents = new Money(2.35, 'EUR')
        def bdhCents = new Money(-1.345, 'BHD')
        def noCents = new Money(4, 'EUR')
    expect:
        eurCents.minorPart() == 35
        bdhCents.minorPart() == -345
        noCents.minorPart() == 0
    }
}
