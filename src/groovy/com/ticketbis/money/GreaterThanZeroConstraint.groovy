package com.ticketbis.money

import org.codehaus.groovy.grails.validation.AbstractConstraint
import org.springframework.validation.Errors

class GreaterThanZeroConstraint extends AbstractConstraint {

    private static final String DEFAULT_INVALID_MESSAGE_CODE = 'default.gtZero.invalid'
    public static final String CONSTRAINT_NAME = 'gtZero'

    private boolean gtZero

    public void setParameter(Object constraintParameter) {
        if (!(constraintParameter instanceof Boolean)) {
            throw new IllegalArgumentException('Parameter for constraint ['
                + CONSTRAINT_NAME + '] of property ['
                + constraintPropertyName + '] of class ['
                + constraintOwningClass + '] must be a boolean value')
        }
        this.gtZero = ((Boolean) constraintParameter).booleanValue()
        super.setParameter(constraintParameter)
    }

    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        if (!validate(propertyValue)) {
            def args = (Object[]) [constraintPropertyName, constraintOwningClass, propertyValue]
            super.rejectValue(target, errors, DEFAULT_INVALID_MESSAGE_CODE, 'not.' + CONSTRAINT_NAME, args)
        }
    }

    boolean supports(Class type) {
        return type != null && Money.class.isAssignableFrom(type)
    }

    String getName() {
        return CONSTRAINT_NAME
    }

    boolean validate(value) {
        if (value instanceof Money) {
            return value.amount > BigDecimal.ZERO
        }
        return false

    }
}