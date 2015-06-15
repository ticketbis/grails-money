package com.ticketbis.money

trait MoneyExchange {
    static Exchange defaultExchange = null

    abstract Currency getCurrency()
    abstract BigDecimal getAmount()

    Money exchangeTo(Currency to, Exchange exchange = getCurrentExchange()) {
        def from = getCurrency()

        def rate = exchange.getRate(from, to)
        def convertedAmount = getAmount() * rate
        new Money(convertedAmount, to)
    }

    Exchange getCurrentExchange() {
        defaultExchange
    }
}
