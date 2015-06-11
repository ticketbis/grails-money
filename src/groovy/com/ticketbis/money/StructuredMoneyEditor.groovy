package com.ticketbis.money

import org.grails.databinding.converters.AbstractStructuredBindingEditor

class StructuredMoneyEditor extends AbstractStructuredBindingEditor<Money> {

    public Money getPropertyValue(Map values) {

    	// retrieve the individual values from the Map
        def amount = values.amount
        def currency = values.currency

        // create and return a Money
        new Money(amount, currency)
    }
}