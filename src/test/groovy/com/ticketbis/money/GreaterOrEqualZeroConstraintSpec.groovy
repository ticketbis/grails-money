package com.ticketbis.money

import com.ticketbis.money.validation.GreaterOrEqualZeroConstraint
import spock.lang.Specification

class GreaterOrEqualZeroConstraintSpec extends Specification {

    def constraint = new GreaterOrEqualZeroConstraint()

    void setup() {
        Number.mixin(NumberMoneyExtension)
    }

    void 'test gteZero value'() {
        given:
            def money = new Money(1, 'EUR')
        expect:
            constraint.validate(money)
    }

    void 'test zero value'() {
        given:
            def money = Money.ZERO
        expect:
            constraint.validate(money)
    }

    void 'test non gteZero value'() {
        given:
            def money = new Money(-1, 'EUR')
        expect:
            !constraint.validate(money)
    }

    void 'test non Money value'() {
        given:
            def money = new BigDecimal('100')
        expect:
            !constraint.validate(money)
    }
}
