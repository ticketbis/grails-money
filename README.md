# grails-money

[![Build
Status](https://travis-ci.org/ticketbis/grails-money.png?branch=master)](https://travis-ci.org/ticketbis/grails-money)

Grails plugin for money and currency exchange management

## Installation

Add dependency to your BuildConfig;

```groovy
compile "com.ticketbis:money:0.1.21"
```

## Usage

```groovy
import com.ticketbis.money.*

def money = new Money(100, 'EUR')

money.amount == 100.00G
money.currency == Currency.getInstance('EUR')

// Comparing money
money == new Money('100 EUR') // true
money == new Money('100 USD') // false
money != new Money('250 EUR') // true

// Arithmetic
new Money('100 EUR') / 2 == new Money('50 EUR')
new Money('100 EUR') * 2 == new Money('200 EUR')
new Money('100 EUR') + new Money('20 EUR') == new Money('120 EUR')
```

## Working with grails 2.x

To work with grails version 2.x check <https://github.com/ticketbis/grails-money/tree/money-grails-2x/>.