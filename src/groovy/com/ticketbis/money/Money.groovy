package com.ticketbis.money

import java.math.MathContext
import java.math.BigDecimal
import java.text.DecimalFormat

@groovy.transform.CompileStatic
final class Money implements Serializable, Comparable, MoneyExchange {
    final BigDecimal amount
    final Currency currency

    private final static DecimalFormat DECIMAL_FORMATTER =
        new DecimalFormat( "###,##0.00" )

    private final static MathContext MONETARY_CONTEXT = MathContext.DECIMAL32

    final static Money ZERO = new Money(BigDecimal.ZERO, Currency.getInstance('EUR'))

    final static class CurrencyMismatchException extends RuntimeException {
        CurrencyMismatchException(String aMessage) {
            super(aMessage)
        }
    }

    Money(Money other) {
        this.amount = other.amount
        this.currency = other.currency
    }

    Money(Number amount, Currency currency) {
        this.amount = (BigDecimal) amount
        this.currency = currency
    }

    Money(String value) {
        String[] parts = value.split(/\s+/)
        this.amount = new BigDecimal(parts[0])
        this.currency = Currency.getInstance(parts[1])
    }

    Money(Number amount, String currencyCode) {
        this.amount = (BigDecimal) amount
        this.currency = Currency.getInstance(currencyCode)
    }

    Money(String value, String currencyCode) {
        this.amount = new BigDecimal(value)
        this.currency = Currency.getInstance(currencyCode)
    }

    int hashCode() {
        amount.hashCode() ^ currency.hashCode()
    }

    boolean equals(Object other) {
        other instanceof Money &&
            currency == ((Money) other).currency &&
            amount == ((Money) other).amount
    }

    String toString() {
        String formatted = DECIMAL_FORMATTER.format(amount)
        "${ formatted } ${ currency?.currencyCode }"
    }

    int compareTo(Object other) {
        compareTo((Money) other)
    }

    int compareTo(Money other) {
        checkCurrenciesMatch(other)
        amount <=> other.amount
    }

    /**
      * Clone Money instance.
      * Return new Money instance with same amount and currency of this Money.
      */
    Money clone() {
        this // Money is inmutable class
    }

      /** Return the negative amount value. */
    Money negate() {
        new Money(-amount, currency)
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
      * Return true only if other Money has the same currency
      * as this Money.
      */
    public boolean isSameCurrencyAs(Money other){
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

