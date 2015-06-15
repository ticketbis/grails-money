package com.ticketbis.money

interface Exchange {
    BigDecimal getRate(Currency from, Currency to)
}
