package com.ticketbis.money.validation

import com.ticketbis.money.Money
import org.codehaus.groovy.grails.validation.AbstractConstraint
import org.springframework.validation.Errors

class GreaterThanZeroConstraint extends AbstractConstraint {

    private static final String DEFAULT_INVALID_MESSAGE_CODE = 'default.gtZero.invalid'
    static final String CONSTRAINT_NAME = 'gtZero'

    private boolean gtZero

    void setParameter(Object constraintParameter) {
        if (!(constraintParameter instanceof Boolean)) {
            throw new IllegalArgumentException("Parameter for constraint [${CONSTRAINT_NAME}]"
                + " of property [${constraintPropertyName}]"
                + " of class [${constraintOwningClass}] must be a boolean value")
        }
        gtZero = ((Boolean) constraintParameter).booleanValue()
        super.setParameter(constraintParameter)
    }

    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        if (!validate(propertyValue)) {
            def args = (Object[]) [constraintPropertyName, constraintOwningClass, propertyValue]
            rejectValue(target, errors, DEFAULT_INVALID_MESSAGE_CODE, "not.${CONSTRAINT_NAME}", args)
        }
    }

    boolean supports(Class type) {
        type != null && Money.isAssignableFrom(type)
    }

    String getName() {
        CONSTRAINT_NAME
    }

    boolean validate(value) {
        value instanceof Money && value.amount > BigDecimal.ZERO
    }
}
