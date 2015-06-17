package com.ticketbis.money

@groovy.transform.PackageScope
trait MoneyExchange {
    final static class MissingExchangeException extends RuntimeException { }

    static ThreadLocal<Stack<Exchange>> localExchangeStack =
        new ThreadLocal<Stack<Exchange>>()

    static Exchange defaultExchange = null

    abstract Currency getCurrency()
    abstract BigDecimal getAmount()

    Money exchangeTo(Currency to, Exchange exchange = getCurrentExchange()) {
        if (!exchange)
            throw new MissingExchangeException()

        Currency from = getCurrency()

        BigDecimal rate = exchange.getRate(from, to)
        BigDecimal convertedAmount = getAmount() * rate
        new Money(convertedAmount, to)
    }

    static void withExchange(Exchange exchange, Closure block) {
        // Initialize localExchangeStack with an empty stack for current thread
        if (!localExchangeStack.get())
            localExchangeStack.set(new Stack<Exchange>())

        localExchangeStack.get().push(exchange)
        try {
            block()
        } catch (ex) {
            throw ex
        } finally {
            assert exchange == localExchangeStack.get().pop(),
                "Local exchange stack was unexpectedly updated"
        }
    }

    static Exchange getCurrentExchange() {
        localExchangeStack.get()?.size() ?
            localExchangeStack.get()?.peek() :
            defaultExchange
    }
}
