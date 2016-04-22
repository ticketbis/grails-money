package com.ticketbis.money

import java.math.MathContext
import java.math.RoundingMode

@groovy.transform.CompileStatic
final class Money implements Serializable, Comparable<Money>, MoneyExchange, MoneyFormat {

    private static final long serialVersionUID = 7781578698983233143L;

    final BigDecimal amount
    final Currency currency

    private final static MathContext MONETARY_CONTEXT = MathContext.DECIMAL128

    final static Money ZERO = new Money()

    final static class CurrencyMismatchException extends RuntimeException {
        CurrencyMismatchException(String aMessage) {
            super(aMessage)
        }
    }

    Money() {
        this.amount = BigDecimal.ZERO
        this.currency = Currency.getInstance('EUR')
    }

    Money(Money other) {
        this.amount = other.amount
        this.currency = other.currency
    }

    Money(String money) {
        String[] parts = money.split(/\s+/)
        this.amount = new BigDecimal(parts[0])
        this.currency = Currency.getInstance(parts[1])
    }

    Money(Number amount, Currency currency) {
        this.amount = (BigDecimal) amount
        this.currency = currency
    }

    Money(Number amount, String currencyCode) {
        this.amount = (BigDecimal) amount
        this.currency = Currency.getInstance(currencyCode)
    }

    Money(String amount, String currencyCode) {
        this.amount = new BigDecimal(amount)
        this.currency = Currency.getInstance(currencyCode)
    }

    Money(String amount, Currency currency) {
        this.amount = new BigDecimal(amount)
        this.currency = currency
    }

    int hashCode() {
        amount.hashCode() ^ currency.hashCode()
    }

    boolean equals(Object other) {
        other instanceof Money && this == (Money) other
    }

    boolean equals(Money other) {
        (amount == 0 && other.amount == 0) || (
            currency == other.currency &&
            amount == other.amount
        )
    }

    String toString() {
        "${ amount } ${ currency }"
    }

    int compareTo(Money other) {
        if (amount == 0 && other.amount == 0)
            return 0 // No money :[

        if (currency == other.currency)
            return amount <=> other.amount

        if (Money.getCurrentExchange())
            return amount <=> other.exchangeTo(currency).amount

        currency.currencyCode <=> other.currency.currencyCode
    }

    /**
      * Clone Money instance.
      * Return new Money instance with same amount and currency of this Money.
      */
    Money clone() {
        this // Money is inmutable class
    }

    /**
      * Add other Money to this Money.
      * Currencies must match.
      */
    Money plus(Money other) {
        assert other
        checkCurrenciesMatch(other)
        new Money(amount.add(other.amount, MONETARY_CONTEXT), currency)
    }

    /**
      * Subtract other Money to this Money.
      * Currencies must match.
      */
    Money minus(Money other) {
        assert other
        checkCurrenciesMatch(other)
        new Money(amount.subtract(other.amount, MONETARY_CONTEXT), currency)
    }

    /**
      * Add other Money to this Money.
      * Currencies must match.
      */
    Money add(Money other) {
        plus(other)
    }

    /**
      * Subtract other Money to this Money.
      * Currencies must match.
      */
    Money subtract(Money other) {
        minus(other)
    }

    /**
      * Returns new Money with (-amount) value.
      */
    Money negative() {
        new Money(amount.negate(MONETARY_CONTEXT), currency)
    }

    /**
      * Add a Number to this Money.
      * Currencies must match.
      */
    Money plus(Number n) {
        new Money(amount.add((BigDecimal) n, MONETARY_CONTEXT), currency)
    }

    /**
      * Subtract a Number to this Money.
      * Currencies must match.
      */
    Money minus(Number n) {
        new Money(amount.subtract((BigDecimal) n, MONETARY_CONTEXT), currency)
    }

    /**
      * Add a Number to this Money.
      * Currencies must match.
      */
    Money add(Number n) {
        plus(n)
    }

    /**
      * Subtract a Number to this Money.
      * Currencies must match.
      */
    Money subtract(Number n) {
        minus(n)
    }

    /**
    * Multiply this Money by an number factor.
    *
    * The scale of the returned Money is equal to the scale of
    * 'this' Money.
    */
    Money multiply(Number n) {
        new Money(amount.multiply((BigDecimal) n, MONETARY_CONTEXT), currency)
    }

    /**
    * Divide this Money by an number divisor.
    *
    * The scale of the returned Money is equal to the scale of
    * 'this' Money.
    */
    Money div(Number n) {
        new Money(amount.divide((BigDecimal) n, MONETARY_CONTEXT), currency)
    }

    /**
     * Returns new Money scaled to specified value
     */
    Money setScale(int newScale, RoundingMode rounding) {
        new Money(amount.setScale(newScale, rounding), currency)
    }

    /**
      * Return true only if other Money has the same currency
      * as this Money.
      */
    public boolean isSameCurrencyAs(Money other) {
        currency == other?.currency
    }

    /**
      * Thrown when a set of Money objects do not have matching currencies.
      */
    private void checkCurrenciesMatch(Money other) {
        if (!isSameCurrencyAs(other)) {
            throw new CurrencyMismatchException(
                "${ other.currency } doesn't match the expected currency : ${ currency }"
            )
        }
    }
}
