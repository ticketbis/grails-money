package com.ticketbis.money

/**
  * Extensions for Number
  */
class NumberMoneyExtension {

    static Money plus(Number self, Money money) {
        money + self
    }

    static Money minus(Number self, Money money) {
        -money + self
    }

    static Money multiply(Number self, Money money) {
        money * self
    }

    static Money toMoney(Number self, Currency currency) {
        new Money(self, currency)
    }
}
