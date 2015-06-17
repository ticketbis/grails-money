package com.ticketbis.money

/**
  * Extensions for Number
  */
@groovy.transform.CompileStatic
final class NumberMoneyExtension {

    static Money plus(Number n, Money money) {
        money + n
    }

    static Money minus(Number n, Money money) {
        -money + n
    }

    static Money multiply(Number n, Money money) {
        money * n
    }

    static Money toMoney(Number n, Currency currency) {
        new Money(n, currency)
    }
}
