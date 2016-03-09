package com.ticketbis.money

import com.ticketbis.money.validation.GreaterThanZeroConstraint
import spock.lang.Specification

class GreaterThanZeroConstraintSpec extends Specification {

    def constraint = new GreaterThanZeroConstraint()

    void setup() {
        Number.mixin(NumberMoneyExtension)
    }

    void 'test gtZero value'() {
        given:
            def money = new Money(1, 'EUR')
        expect:
            constraint.validate(money)
    }    

    void 'test non gtZero value'() {
        given:
            def money = new Money()
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
