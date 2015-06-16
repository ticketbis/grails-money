# grails-money

Grails plugin for manage money and currency exchange

## Installation

Add dependency to your BuildConfig;

```groovy
compile "com.ticketbis.money:money:0.0.1"
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
